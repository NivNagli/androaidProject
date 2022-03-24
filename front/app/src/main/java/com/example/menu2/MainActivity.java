package com.example.menu2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import com.android.volley.VolleyError;

import java.util.Map;

import com.android.volley.Request;

public class MainActivity extends AppCompatActivity {

    public void Login(View view) {
        HideKeyboard();
        Button click = (Button) view;
        TextView email = (TextView) findViewById(R.id.email);
        TextView password = (TextView) findViewById(R.id.pwd);
        if(VerifyDetails(email.getText().toString(), password.getText().toString())){
            if(click.getText().toString().equals("LOGIN"))
                VolleyRequest(email.getText().toString(), password.getText().toString(), "Login");
            else
                VolleyRequest(email.getText().toString(), password.getText().toString(), "Sign up");
        }
    }

    public void HideKeyboard(){
        View hideKeyboard = this.getCurrentFocus();
        if (hideKeyboard != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(hideKeyboard.getWindowToken(), 0);
        }
    }

    public boolean VerifyDetails(String email, String password) {
        if (Patterns.EMAIL_ADDRESS.matcher(email).matches())
            if (password.length() > 4)
                return true;
        Toast.makeText(getApplicationContext(), "Invalid Email or Password", Toast.LENGTH_LONG).show();
        return false;
    }

    private void VolleyRequest(String email, String password, String action) {
        Map<String, String> params = new HashMap();
        params.put("email", email);
        params.put("password", password);
        JSONObject parameters = new JSONObject(params);
        String url;
        if (action.equals("Sign up"))
            url = "https://androidapitelhai.herokuapp.com/user/signup";
        else
            url = "https://androidapitelhai.herokuapp.com/user/login";
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                if (action.equals("Sign up")) {
                    Toast.makeText(getApplicationContext(), "Sign up successful", Toast.LENGTH_LONG).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "Login successful", Toast.LENGTH_LONG).show();
                SharedPreferences prefs = getSharedPreferences("refs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString("email", email);
                editor.apply();
                GoToMenu();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (action.equals("Sign up"))
                    Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_LONG).show();
                else
                    System.out.println(error.toString());
                    Toast.makeText(getApplicationContext(), "Wrong email or password", Toast.LENGTH_LONG).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonRequest);
    }

    public void GoToMenu() {
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
}