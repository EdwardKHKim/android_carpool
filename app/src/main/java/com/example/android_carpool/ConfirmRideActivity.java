package com.example.android_carpool;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ConfirmRideActivity extends AppCompatActivity {

    private TextView originText, destinationText, costText;
    private EditText phoneNumberText;

    private String originStr, destinationStr, costStr, phoneNumberStr, key;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference, keyReference;

    private RelativeLayout confirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_ride);
        firebaseDatabase = FirebaseDatabase.getInstance();
        reference = firebaseDatabase.getReference().child("Ticket");

        originText();
        destinationText();
        costText();

        initBack();
        confirmRide();
    }

    private void originText() {
        originText = (TextView) findViewById(R.id.origin_text_view_activity_confirm_ride);
        Bundle bundle = getIntent().getExtras();
        originStr = bundle.getString("ORIGIN_LOCATION_STRING_KEY");
        originText.setText(originStr);
    }

    private void destinationText() {
        destinationText = (TextView) findViewById(R.id.destination_text_view_activity_confirm_ride);
        Bundle bundle = getIntent().getExtras();
        destinationStr = bundle.getString("DESTINATION_LOCATION_STRING_KEY");
        destinationText.setText(destinationStr);
    }

    private void costText() {
        costText = (TextView) findViewById(R.id.cost_text_view);
        Bundle bundle = getIntent().getExtras();
        costStr = bundle.getString("COST_STRING_KEY");
        costText.setText(costStr);
    }

    private void saveData() {
        key = reference.push().getKey();
        HashMap<String, Object> ticketKey = new HashMap<>();
        reference.updateChildren(ticketKey);
        keyReference = reference.child(key);

        phoneNumberText = (EditText) findViewById(R.id.phone_number_activity_confirm_ride);
        phoneNumberStr = phoneNumberText.getText().toString();

        HashMap<String, Object> map = new HashMap<>();
        map.put("Origin", originStr);
        map.put("Destination", destinationStr);
        map.put("Cost", costStr);
        map.put("PhoneNumber", phoneNumberStr);

        keyReference.updateChildren(map);
    }

    private void initBack() {
        ImageView back = (ImageView) findViewById(R.id.back_activity_confirm_ride);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void confirmRide() {
        confirm = (RelativeLayout) findViewById(R.id.confirm_click);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();

                Intent intent = new Intent(ConfirmRideActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }
}
