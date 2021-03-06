package com.example.android_carpool;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class DriveFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_drive, container, false);

        initFields();

        return view;
    }

    private void initFields() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Ticket");

        recyclerView = (RecyclerView) view.findViewById(R.id.drive_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void onStart() {
        super.onStart();

        Query query = reference.limitToLast(50);

        FirebaseRecyclerOptions options = new FirebaseRecyclerOptions.Builder<Ticket>()
                .setQuery(query, Ticket.class)
                .build();

        FirebaseRecyclerAdapter<Ticket, TicketViewHolder> firebaseRecyclerAdapter
                = new FirebaseRecyclerAdapter<Ticket, TicketViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull TicketViewHolder ticketViewHolder, int i, @NonNull Ticket ticketDrive) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            ticketViewHolder.origin.setText(ticketDrive.getOrigin());
                            ticketViewHolder.destination.setText(ticketDrive.getDestination());
                            ticketViewHolder.cost.setText(ticketDrive.getCost());
                            ticketViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String id = getRef(i).getKey();
                                    Intent intent = new Intent(getActivity(), ConfirmDriveActivity.class);
                                    intent.putExtra("id", id);
                                    intent.putExtra("ORIGIN_LOCATION_STRING_KEY", ticketDrive.getOrigin());
                                    intent.putExtra("DESTINATION_LOCATION_STRING_KEY", ticketDrive.getDestination());
                                    intent.putExtra("COST_STRING_KEY", ticketDrive.getCost());
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @NonNull
            @Override
            public TicketViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_drive, parent, false);
                TicketViewHolder viewHolder = new TicketViewHolder(view);
                return viewHolder;
            }
        };

        recyclerView.setAdapter(firebaseRecyclerAdapter);
        firebaseRecyclerAdapter.startListening();
    }

    public static class TicketViewHolder extends RecyclerView.ViewHolder {

        private TextView origin, destination, cost;

        public TicketViewHolder(@NonNull View itemView) {
            super(itemView);
            origin = itemView.findViewById(R.id.origin_cardview_drive_text);
            destination = itemView.findViewById(R.id.destination_cardview_drive_text);
            cost = itemView.findViewById(R.id.cost_text_view_cardview_drive);
        }
    }
}
