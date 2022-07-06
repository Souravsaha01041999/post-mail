package com.sender.postmail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

public class changepass extends AppCompatActivity {
    TextView showemail;
    Button sendootp,trgchange;
    String email,otp="",userid;
    ProgressDialog pd;
    EditText otpval,cpass;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    boolean ctrl=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepass);
        sendootp=findViewById(R.id.changepassotp);
        showemail=findViewById(R.id.changeuid);
        trgchange=findViewById(R.id.changepassmain);
        otpval=findViewById(R.id.changepassotpval);
        cpass=findViewById(R.id.changepassword);

        sp= PreferenceManager.getDefaultSharedPreferences(changepass.this);

        userid=getIntent().getStringExtra("post_mail_userid");

        pd=ProgressDialog.show(changepass.this,"Loading","Please Wait...");
        StringRequest sr=new StringRequest(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/getemail_changepass.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response=response.trim();
                pd.dismiss();
                email=response;
                showemail.setText(email);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pd.dismiss();
                msg("No internet connection try again");
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>pems=new Hashtable<>();
                pems.put("uid",userid);
                return pems;
            }
        };
        sr.setShouldCache(false);
        RequestQueue q= Volley.newRequestQueue(changepass.this);
        q.add(sr);
        q.getCache().clear();

        sendootp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg("sending otp check the email...");
                Date d=new Date();
                DateFormat df=new SimpleDateFormat("mmss");
                otp=df.format(d);

                StringRequest srr=new StringRequest(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/signup_otp.php", new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        msg("otp has been send in your email");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                otp = "";
                            }
                        },60000);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        msg("Error internet connection");
                    }
                })
                {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String>pems=new Hashtable<>();
                        pems.put("email",email);
                        pems.put("rcvdata",otp);
                        return pems;
                    }
                };
                srr.setShouldCache(false);
                RequestQueue qq=Volley.newRequestQueue(changepass.this);
                qq.add(srr);
                qq.getCache().clear();
            }
        });
        trgchange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cpass.getText().toString().length() == 0) {
                    ctrl = false;
                    msg("please enter new password");
                }
                if (otpval.getText().toString().length() == 0) {
                    ctrl = false;
                    msg("please enetr otp");
                }

                if(ctrl)
                {
                if (otp.equals(otpval.getText().toString())) {
                    msg("Changing Password pleas wait...");
                    StringRequest srrr = new StringRequest(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/changepass.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            msg(response);
                            spe = sp.edit();
                            spe.putString("post-mail-password", cpass.getText().toString());
                            spe.apply();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            msg("error connection");
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> pems = new Hashtable<>();
                            pems.put("uid", userid);
                            pems.put("value", cpass.getText().toString());
                            return pems;
                        }
                    };
                    srrr.setShouldCache(false);
                    RequestQueue qqq = Volley.newRequestQueue(changepass.this);
                    qqq.add(srrr);
                    qqq.getCache().clear();
                } else {
                    msg("error otp");
                }
            }
            else {
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