package com.sender.postmail;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

public class signuppage extends AppCompatActivity {
    EditText userid,pass,repass,otp,email;
    Button sendotp,signup;
    String otpval="";
    boolean ctrl=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signuppage);
        userid=findViewById(R.id.anduseridfor);
        pass=findViewById(R.id.passwordfor);
        repass=findViewById(R.id.repasswordfor);
        email=findViewById(R.id.emailfor);
        otp=findViewById(R.id.otpfor);
        sendotp=findViewById(R.id.sendotpfor);
        signup=findViewById(R.id.signupfor);

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((pass.getText().toString().equals(repass.getText().toString()))&&(pass.getText().toString().length()>0))
                {
                    msg("Sending otp in your mail");
                    Date d=new Date();
                    DateFormat df=new SimpleDateFormat("mmss");
                    otpval=df.format(d);
                    StringRequest sr=new StringRequest(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/signup_otp.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            msg("Otp has been send in your mail check the otp");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    otpval = "";
                                }
                            },60000);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            msg("Check your internet connection");
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String>pems=new Hashtable<>();
                            pems.put("rcvdata",otpval);
                            pems.put("email",email.getText().toString());
                            return pems;
                        }
                    };
                    sr.setShouldCache(false);
                    RequestQueue q= Volley.newRequestQueue(signuppage.this);
                    q.add(sr);
                    q.getCache().clear();
                }
                else
                {
                    msg("Please enter same password");
                    pass.setText("");
                    repass.setText("");
                }
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userid.getText().toString().length()==0)
                {
                    ctrl=false;
                    msg("please enter user id");
                }
                if (pass.getText().toString().length()==0)
                {
                    msg("please enter password");
                    ctrl=false;
                }
                if (otp.getText().toString().length()==0)
                {
                    msg("please enter otp");
                    ctrl=false;
                }
                if (ctrl)
                {
                    if (otpval.equals(otp.getText().toString()))
                    {
                        msg("Please wait...");
                        StringRequest srr=new StringRequest(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/signup.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                response=response.trim();
                                if (response.equals("1"))
                                {
                                    msg("Already exist this user try annother user id");
                                }
                                else if (response.equals("2"))
                                {
                                    msg("Success you can log in now");
                                    finish();
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                msg("Error connection");
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String>pems=new Hashtable<>();
                                pems.put("uid",userid.getText().toString());
                                pems.put("password",pass.getText().toString());
                                pems.put("mail",email.getText().toString());
                                return pems;
                            }
                        };
                        srr.setShouldCache(false);
                        RequestQueue qq= Volley.newRequestQueue(signuppage.this);
                        qq.add(srr);
                        qq.getCache().clear();
                    }
                    else
                    {
                        msg("Error Otp");
                    }
                }
                else
                {
                    ctrl=true;
                }
            }
        });
    }
    void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
