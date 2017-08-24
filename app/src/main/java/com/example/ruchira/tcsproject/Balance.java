package com.example.ruchira.tcsproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Balance extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        String username,balance;
        username = getIntent().getExtras().getString("username");
        balance = getIntent().getExtras().getString("Balance");
        TextView tx1=(TextView)findViewById(R.id.txt1);
        TextView tx2=(TextView)findViewById(R.id.txt2);
        //chatbalance = getIntent().getExtras().getString("chatbalance");
        tx1.setText("USERNAME   "+ username);
        tx2.setText("Balance   "+ balance);
    }
}
