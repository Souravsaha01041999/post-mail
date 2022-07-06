package com.sender.postmail;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
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

import java.util.Hashtable;
import java.util.Map;

public class loginpage extends AppCompatActivity {
    Button login;
    EditText userid,pass;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    ProgressDialog pd;
    TextView fp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp= PreferenceManager.getDefaultSharedPreferences(loginpage.this);


        if (sp.getString("permission","").length()==0) {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
            spe=sp.edit();
            spe.putString("permission","Done");
            spe.apply();

        }

        setContentView(R.layout.activity_loginpage2);
        findViewById(R.id.opensignupfor).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(loginpage.this,signuppage.class));
            }
        });
        userid=findViewById(R.id.loginuserid);
        pass=findViewById(R.id.loginpassword);
        login=findViewById(R.id.loginbutton);
        fp=findViewById(R.id.fogpass);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((userid.getText().toString().length()>0)&&(pass.getText().toString().length()>0))
                {
                    pd=ProgressDialog.show(loginpage.this,"Login","Please wait");
                    StringRequest sr=new StringRequest(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/login.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            response=response.trim();
                            pd.dismiss();
                            if (response.equals("Success"))
                            {
                                spe=sp.edit();
                                spe.putString("post-mail-userid",userid.getText().toString());
                                spe.putString("post-mail-password",pass.getText().toString());
                                spe.apply();
                                startActivity(new Intent(loginpage.this,home.class));
                                finish();
                            }
                            else {
                                msg(response);
                                pass.setText("");
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            msg("Error connection");
                            pd.dismiss();
                        }
                    })
                    {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String>pems=new Hashtable<>();
                            pems.put("uid",userid.getText().toString());
                            pems.put("pass",pass.getText().toString());
                            return pems;
                        }
                    };
                    sr.setShouldCache(false);
                    RequestQueue q= Volley.newRequestQueue(loginpage.this);
                    q.add(sr);
                    q.getCache().clear();
                }
                else {
                    msg("Enter proper details");
                }
            }
        });
        fp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userid.getText().toString().length()>0)
                {
                    msg("Sending password in your email");
                    StringRequest srr=new StringRequest(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/forgatepassword.php", new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            msg(response);
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
                            pems.put("uid",userid.getText().toString());
                            return pems;
                        }
                    };
                    srr.setShouldCache(false);
                    RequestQueue qq=Volley.newRequestQueue(loginpage.this);
                    qq.add(srr);
                    qq.getCache().clear();
                }
                else {
                    msg("Please Enter User Id");
                }
            }
        });
    }
    void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
