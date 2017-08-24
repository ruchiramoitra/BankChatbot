package com.example.ruchira.tcsproject;



import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

public class Applynow extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applynow);



    }
    public void onButtonClick(View v) {
        if (v.getId() == R.id.saving) {
            Toast temp1 = Toast.makeText(Applynow.this, "Savings Account selected", Toast.LENGTH_SHORT);
            temp1.show();
        }

        if (v.getId()==R.id.creidt)
        {
            Toast temp2 = Toast.makeText(Applynow.this, "Credit Card selected", Toast.LENGTH_SHORT);
            temp2.show();
        }
        if (v.getId() == R.id.submitapply) {
            Toast temp3 = Toast.makeText(Applynow.this, "Response Submitted", Toast.LENGTH_SHORT);
            temp3.show();
        }
        /*if (v.getId()==R.id.H2)
        {
            Intent int1=new Intent(Applynow.this,Display.class);
            startActivity(int1);
        }*/
    }
}



