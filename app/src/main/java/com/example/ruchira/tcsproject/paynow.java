package com.example.ruchira.tcsproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class paynow extends AppCompatActivity {
    EditText transfer,receiver;
    final DatabaseHelper helper = new  DatabaseHelper(this);
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paynow);
        transfer = (EditText)findViewById(R.id.editText14);
        receiver=(EditText)findViewById(R.id.editText10);
        username = getIntent().getExtras().getString("username");

    }

    public void onButtonClick(View v) {
        if (v.getId() == R.id.confirmPay )
        {
            final String rec=receiver.getText().toString();
            final String amt=transfer.getText().toString();
            StringRequest stringRequest = null;
            if(rec.equals("")||amt.equals("")){
                Toast.makeText(getApplicationContext(), "Please Fill the field!!", Toast.LENGTH_SHORT).show();
            }else{
                stringRequest = new StringRequest(Request.Method.POST, Constants.PAYNOW_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jresponse=new JSONObject(response);
                            String respo1=jresponse.getString("repo");
                            if(respo1.equalsIgnoreCase("success"))
                            {
                                String balance=jresponse.getString("balance");//balance of the sender
                                String balance2=jresponse.getString("balance2");//balance of receiver
                                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                                receiver.setText("");
                                transfer.setText("");
                                helper.getnewbalance(balance, username);
                            }
                            else
                                Toast.makeText(getApplicationContext(),respo1, Toast.LENGTH_SHORT).show();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError) {
                            Toast.makeText(getApplicationContext(), "TimeOut Error!!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NoConnectionError) {
                            Toast.makeText(getApplicationContext(), "No Connection Error!!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(getApplicationContext(), "Authentication Failure Error!!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(getApplicationContext(), "Network Error!!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(getApplicationContext(), "Server Error!!", Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(getApplicationContext(), "JSON Parse Error!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put(Constants.KEY_USERNAME,username);
                        params.put(Constants.KEY_USERNAME2, rec);
                        params.put(Constants.KEY_AMOUNT, amt);
                        return params;
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("User-Agent", "ABC_Bank");
                        return headers;
                    }
                };

                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);

            }

           /*if(Integer.parseInt(transfer.getText().toString())<Integer.parseInt(helper.getDataForChat(username)))
           {


                transfer = (EditText) findViewById(R.id.editText14);
                String trans = transfer.getText().toString();

                String bal = helper.getDataForChat(username);
                int bal1 = Integer.parseInt(bal) - Integer.parseInt(trans);
                String balance = Integer.toString(bal1);
                helper.getnewbalance(balance, username);
               Toast temp = Toast.makeText(paynow.this, "Payment Successful" + balance  , Toast.LENGTH_SHORT);
               temp.show();


            else {

                Toast temp = Toast.makeText(paynow.this, "Low balance", Toast.LENGTH_SHORT);
                temp.show();

            }*/

        }

    }
}
