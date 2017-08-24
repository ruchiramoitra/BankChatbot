package com.example.ruchira.tcsproject;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
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

/**
 * Created by USER1 on 07-07-2017.
 */

public class Display extends Activity {
    String username;
    String chatbalance;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        Button but1 =(Button)findViewById(R.id.button1);
        Button but2 =(Button)findViewById(R.id.button2);
        Button but3 =(Button)findViewById(R.id.button3);
        Button but4 =(Button)findViewById(R.id.button4);
        Button but5 =(Button)findViewById(R.id.button5);
        Button but6 =(Button)findViewById(R.id.button6);
        TextView welcome=(TextView)findViewById(R.id.textView6);
        username = getIntent().getExtras().getString("username");
        //chatbalance = getIntent().getExtras().getString("chatbalance");
        welcome.setText("Welcome to mobile banking   "+ username);
        but4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent int4 = new Intent(Display.this,chatbot_page.class);
                int4.putExtra("username",username);
                startActivity(int4);
                StringRequest stringRequest = null;
                stringRequest = new StringRequest(Request.Method.POST, Constants.BALANCE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jresponse = new JSONObject(response);
                            String respo1 = jresponse.getString("repo");
                            if (respo1.equalsIgnoreCase("success")) {
                                String balance = jresponse.getString("balance");

                               // int4.putExtra("username",username);
                                int4.putExtra("Balance", balance);
                                startActivity(int4);
                            } else
                                Toast.makeText(getApplicationContext(), respo1, Toast.LENGTH_SHORT).show();

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
                        params.put(Constants.KEY_USERNAME, username);
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
        });
        but1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent int1=new Intent(Display.this,paynow.class);
                int1.putExtra("username",username);
                startActivity(int1);
            }
        });
        but2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent int2=new Intent(Display.this,Applynow.class);
                startActivity(int2);

            }
        });
        but3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent int3=new Intent(Display.this,investment.class);
                startActivity(int3);

            }
        });
      /*  but4.setOnClickListener(new View.OnClickListener() {       //chatbot
            @Override
            public void onClick(View v) {

                Intent int4=new Intent(Display.this,Applynow.class);
                startActivity(int4);

            }
        }); */
        but5.setOnClickListener(new View.OnClickListener() {      //logout
            @Override
            public void onClick(View v) {

                Intent int5=new Intent(Display.this, com.example.ruchira.tcsproject.thankyou.class);
                finish();
                startActivity(int5);

            }
        });
        but6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StringRequest stringRequest = null;
                stringRequest = new StringRequest(Request.Method.POST, Constants.BALANCE_URL, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jresponse = new JSONObject(response);
                            String respo1 = jresponse.getString("repo");
                            if (respo1.equalsIgnoreCase("success")) {
                                String balance = jresponse.getString("balance");
                                Intent int5 = new Intent(Display.this, Balance.class);
                                int5.putExtra("username",username);
                                int5.putExtra("Balance", balance);
                                startActivity(int5);
                            } else
                                Toast.makeText(getApplicationContext(), respo1, Toast.LENGTH_SHORT).show();

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
                        params.put(Constants.KEY_USERNAME, username);
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
        });





    }
}
