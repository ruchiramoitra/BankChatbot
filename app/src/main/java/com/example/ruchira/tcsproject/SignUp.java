package com.example.ruchira.tcsproject;



import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import java.util.HashMap;
import java.util.Map;


/**
 * Created by USER1 on 07-07-2017.
 */

public class SignUp extends Activity implements View.OnClickListener {

    EditText name, email, mob, uname, pass1, et_passwordver;
    Button Bsign;

    DatabaseHelper helper = new DatabaseHelper(this);

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name = (EditText) findViewById(R.id.TFname);
        email = (EditText) findViewById(R.id.TFemail);
        mob = (EditText) findViewById(R.id.TFmob);
        uname = (EditText) findViewById(R.id.TFuname);
        pass1 = (EditText) findViewById(R.id.TFpass1);
        et_passwordver = (EditText) findViewById(R.id.et_password_ver);
        Button Bsign = (Button) findViewById(R.id.BsignupButton);
        Bsign.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.BsignupButton:
                final String namestr = name.getText().toString();
                final String emailstr = email.getText().toString();
                final String mobstr =mob.getText().toString();
                final String unamestr = uname.getText().toString();
                final String pass1str = pass1.getText().toString().trim();
                String pass2str = et_passwordver.getText().toString().trim();
                /*String unamestr2 = helper.searchUser(unamestr);*/
                String regex = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,15}";
                if (namestr.equals("") || emailstr.equals("") || mobstr.equals("") || pass1str.equals("") || et_passwordver.equals("")) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields!!", Toast.LENGTH_SHORT).show();
                } else if (!pass1str.equals(pass2str)) {
                    Toast.makeText(getApplicationContext(), "Passwords do not match!!", Toast.LENGTH_SHORT).show();
                } else if (android.util.Patterns.EMAIL_ADDRESS.matcher(emailstr).matches() == false) {
                    Toast.makeText(getApplicationContext(), "E-mail address is not valid", Toast.LENGTH_SHORT).show();
                } else if (!pass1str.matches(regex)) {
                    Toast.makeText(getApplicationContext(), "Password invalid ! Please read criteria ", Toast.LENGTH_SHORT).show();
                } else if (mobstr.length()<10){
                    Toast.makeText(getApplicationContext(), "Mobile No. invalid!! ", Toast.LENGTH_SHORT).show();
                } /*else if(unamestr.equals(unamestr2)){
                    Toast.makeText(getApplicationContext(), "User Name already exist!!", Toast.LENGTH_SHORT).show();
                }*/
                else {
                    StringRequest stringRequest=new StringRequest(Request.Method.POST, Constants.REGISTER_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.contains("Successfully Registered!!!") ) {
                                Intent i = new Intent(SignUp.this, login_page.class);
                                Contact c = new Contact();
                                c.setName(namestr);
                                c.setEmail("20000");
                                c.setMob(mobstr);
                                c.setUname(unamestr);
                                c.setPass(pass1str);
                                helper.insertContact(c);
                                finish();
                                startActivity(i);
                            }else if(response.contains("User Name already exist!!")) {
                            }
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error instanceof TimeoutError){
                                Toast.makeText(getApplicationContext(),"TimeOut Error!!",Toast.LENGTH_SHORT).show();
                            } else if(error instanceof NoConnectionError){
                                Toast.makeText(getApplicationContext(),"No Connection Error!!",Toast.LENGTH_SHORT).show();
                            } else if(error instanceof AuthFailureError){
                                Toast.makeText(getApplicationContext(),"Authentication Failure Error!!",Toast.LENGTH_SHORT).show();
                            } else if(error instanceof NetworkError){
                                Toast.makeText(getApplicationContext(),"Network Error!!",Toast.LENGTH_SHORT).show();
                            } else if(error instanceof ServerError){
                                Toast.makeText(getApplicationContext(),"Server Error!!",Toast.LENGTH_SHORT).show();
                            } else if(error instanceof ParseError){
                                Toast.makeText(getApplicationContext(),"JSON Parse Error!!",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }){
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<String, String>();
                            params.put(Constants.KEY_NAME, namestr);
                            params.put(Constants.KEY_EMAIL, emailstr);
                            params.put(Constants.KEY_MOBILE, mobstr);
                            params.put(Constants.KEY_USERNAME, unamestr);
                            params.put(Constants.KEY_PASSWORD, pass1str);
                            params.put(Constants.KEY_BALANCE,"20000");
                            return params;
                        }
                        @Override
                        public Map<String, String> getHeaders () throws AuthFailureError {
                            Map<String, String> headers = new HashMap<String, String>();
                            headers.put("User-Agent", "ABC_Bank");
                            return headers;
                        }
                    };

                    MySingleton.getInstance(getApplicationContext()).addToRequestQueue(stringRequest);
                }
                break;
            default:
                break;
        }
    }
}



