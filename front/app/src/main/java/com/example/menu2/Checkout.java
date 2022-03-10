package com.example.menu2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Checkout extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
    }

    public void OrderCompleted(View view) {
        TextView emailAddress = (TextView) findViewById(R.id.FullName);
        TextView PhoneNumber = (TextView) findViewById(R.id.PhoneNum);
        TextView PostAdd = (TextView) findViewById(R.id.PostalAddress);
        if(emailAddress.getText().toString().equals("") || PhoneNumber.getText().toString().equals("") || PostAdd.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Missing details!", Toast.LENGTH_LONG).show();
        }
        else
        {
            Intent intent = new Intent(this, OrderCompleted.class);
            startActivity(intent);
        }

    }
}