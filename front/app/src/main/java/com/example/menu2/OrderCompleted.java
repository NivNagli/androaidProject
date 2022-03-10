package com.example.menu2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.Locale;

// TODO: 06/03/2022 -

//- בדיקה שהטקסט כתובת לא ריק
//- כתיבת מסמך README
//-סגירת כל שאר הדפים
//- הוספת פיצ'רים חדשים? 



public class OrderCompleted extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void orderTimeStamp()
    {
        SharedPreferences prefs = getSharedPreferences("refs", Context.MODE_PRIVATE);
        String check_bonus = prefs.getString("bonus", "default value");
        if(check_bonus.equals("true")){
            TextView msgb = (TextView) findViewById(R.id.bonusText);
            msgb.setText("This is your tenth order!\nYou will receive a free dessert on the house!");
        }
        String date = new SimpleDateFormat("dd-mm-yyyy hh:mm:ss", Locale.getDefault()).format(new Date());
        TextView msg = (TextView) findViewById(R.id.ordercomtext);
        msg.setText("Order Completed!\n"+date+"\n We will contact you shortly to confirm the order and payment!");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_completed);
        orderTimeStamp();
    }

    public void GoToMenu(View view) {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }
}