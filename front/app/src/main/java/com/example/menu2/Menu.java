package com.example.menu2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Menu extends AppCompatActivity {

    public HashMap<String, String> items = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SharedPreferences prefs = getSharedPreferences("refs", Context.MODE_PRIVATE);
        String email = prefs.getString("email", "default value");
        View logout_btn = findViewById(R.id.Logoutbtn);
        View login_btn = findViewById(R.id.Loginbtn);
        if((email.equals(""))) {
            login_btn.setVisibility(View.VISIBLE);
            logout_btn.setVisibility(View.GONE);
        }
        else
        {
            logout_btn.setVisibility(View.VISIBLE);
            login_btn.setVisibility(View.GONE);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void updateOrder(View view) {
        try {
            SharedPreferences prefs = getSharedPreferences("refs", Context.MODE_PRIVATE);
            String email = prefs.getString("email", "default value");
            if (email.equals("")) {
                Toast.makeText(getApplicationContext(), "Please log in to order", Toast.LENGTH_LONG).show();
                GoToLogin(view);
                return;
            }
            boolean orderOk = false;
            for (String v : items.values()) {
                int val = Integer.parseInt(v);
                if (val > 0) {
                    orderOk = true;
                    break;
                }
            }
            if (orderOk) {
                items.put("email", email);
                JSONObject parameters = new JSONObject(items);
                String url = "https://androidapitelhai.herokuapp.com/order/add-meals";
                JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean checkBonus = response.getBoolean("getBonus");
                            SharedPreferences prefs = getSharedPreferences("refs", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putString("bonus", String.valueOf(checkBonus));
                            editor.apply();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        GoToCheckout(view);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Server Error!", Toast.LENGTH_LONG).show();
                    }
                });
                Volley.newRequestQueue(this).add(jsonRequest);
            } else {
                Toast.makeText(getApplicationContext(), "cart Empty!", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "User Not Logged In!", Toast.LENGTH_LONG).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void GoToCheckout(View view) {
        Intent intent = new Intent(this, Checkout.class);
        startActivity(intent);
    }

    public void GoToLogin(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void amount(View view) {
        TableRow row = (TableRow) view.getParent();
        Button button = (Button) view;
        TextView counter = (TextView) row.getVirtualChildAt(4);
        int current_value = Integer.parseInt(counter.getText().toString());
        TextView price = (TextView) row.getVirtualChildAt(2);
        String price_str = price.getText().toString();
        int dish_price = Integer.parseInt(price_str.substring(0, price_str.length() - 1));
        TextView total_amount = (TextView) findViewById(R.id.Total_Amount);
        int current_amount = Integer.parseInt(total_amount.getText().toString().replace("$", ""));
        if (button.getText().toString().equals("+")) {
            current_value += 1;
            current_amount += dish_price;
        } else if (current_value > 0) {
            current_value -= 1;
            current_amount -= dish_price;
        }
        TextView mealName = (TextView) row.getVirtualChildAt(1);
        String mealNameString = mealName.getText().toString();
        items.put(mealNameString, String.valueOf(current_value));
        counter.setText(String.valueOf(current_value));
        total_amount.setText((String.valueOf(current_amount)) + "$");
    }

    public void LogOut(View view) {
        view.setVisibility(View.GONE);
        View login_btn = findViewById(R.id.Loginbtn);
        login_btn.setVisibility(View.VISIBLE);
        SharedPreferences prefs = getSharedPreferences("refs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", "");
        editor.apply();
    }
}