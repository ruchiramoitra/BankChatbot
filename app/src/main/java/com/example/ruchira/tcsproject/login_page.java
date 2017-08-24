package com.example.ruchira.tcsproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class login_page extends AppCompatActivity implements View.OnClickListener{
    DatabaseHelper helper = new  DatabaseHelper(this);
    EditText a,b;
    TextView Bsignup;
    Button Blogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        a=(EditText)findViewById(R.id.TFUsername);
        b=(EditText)findViewById(R.id.TFPassword);
        Bsignup=(TextView) findViewById(R.id.Bsignup);
        Blogin=(Button)findViewById(R.id.Blogin);
        Blogin.setOnClickListener(this);
        Bsignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Blogin:
                final String str = a.getText().toString();
                final String pass = b.getText().toString();
                /*String password = helper.searchPass(str);*/
                StringRequest stringRequest = null;
                if (pass.equals("") || str.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please Fill the field!!", Toast.LENGTH_SHORT).show();
                } else {
                    stringRequest = new StringRequest(Request.Method.POST, Constants.LOGIN_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jresponse=new JSONObject(response);
                                String respo1=jresponse.getString("repo");
                                if(respo1.equalsIgnoreCase("success"))
                                {
                                    String string=jresponse.getString("uname");
                                    String bal=jresponse.getString("balance");
                                   /* String bal1=String.valueOf(bal);*/
                                    helper.getnewbalance(bal,str);
                                    finish();
                                    Intent i = new Intent(login_page.this, Display.class);
                                    i.putExtra("username", string);
                                    startActivity(i);
                                    Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_SHORT).show();
                                }
                                else  if(respo1.equalsIgnoreCase("INVALID PASSWORD")){
                                    Toast.makeText(getApplicationContext(),"INVALID PASSWORD", Toast.LENGTH_SHORT).show();
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
                            params.put(Constants.KEY_USERNAME, str);
                            params.put(Constants.KEY_PASSWORD, pass);
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
                break;
                /*else if (pass.equals(password)) {

                    Intent i = new Intent(login_page.this, Display.class);
                    i.putExtra("username", str);
                    startActivity(i);
                } else {
                    Toast temp = Toast.makeText(login_page.this, "Incorrect Username and Password", Toast.LENGTH_SHORT);
                    temp.show();
                }*/

            case R.id.Bsignup:
                Intent i = new Intent(login_page.this, SignUp.class);
                startActivity(i);
                break;
            default:
                break;

        }
    }
}
