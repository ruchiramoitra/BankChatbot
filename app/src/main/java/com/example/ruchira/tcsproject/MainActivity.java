package com.example.ruchira.tcsproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.view.View;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button_openchatbot = (Button)findViewById(R.id.button3);
        Button button_openlogin = (Button)findViewById(R.id.button2);

        /**   button_openchatbot.setOnClickListener(new View.OnClickListener()
         {

         public void onClick(View v){
         Intent intent = new Intent(MainActivity.this,chatbot_page.class);
         startActivity(intent);
         }

         });**/
        button_openlogin.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v){
                Intent intent = new Intent(MainActivity.this,login_page.class);
                startActivity(intent);
            }
        });


    }





}
