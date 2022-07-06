package com.sender.postmail;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

public class sendmail extends AppCompatActivity {
    EditText to,subject,body;
    Button send;
    String date,subjectval="no subject",frmid,mode="",ms_id="",seen,time,filename;
    boolean ctrl=true;
    ProgressDialog pd;
    boolean selectImage=false;
    byte imgdata[];
    String imglink;
    ImageView tv,download;
    Vibrator vb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sendmail);
        to=findViewById(R.id.sendmailto);
        subject=findViewById(R.id.sendmailsubject);
        body=findViewById(R.id.sendmailbody);
        send=findViewById(R.id.sendmailsend);
        download=findViewById(R.id.download_img);
        tv=findViewById(R.id.showimg);

        vb=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);

        Control.ctrl='n';
        findViewById(R.id.imgset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(Intent.ACTION_OPEN_DOCUMENT).setType("image/*"),200);
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!imglink.equals("none"))
                {
                    startActivity(new Intent(sendmail.this,sendimage.class).putExtra("img_sorce",imglink));
                }
                else
                {
                    msg("No Image");
                }
            }
        });

        Intent ii=getIntent();
        frmid=ii.getStringExtra("form_id");
        mode=ii.getStringExtra("mode");
        ms_id=ii.getStringExtra("msid");
        seen=ii.getStringExtra("seen");


        if (mode.equals("send")) {
            imglink="none";
            send.setText("SEND");
        }
        else {
            send.setText("FORWARD");
            pd=ProgressDialog.show(sendmail.this,"Loading","Please Wait...");
            StringRequest srr=new StringRequest(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/fetch_total_msg.php", new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    pd.dismiss();
                    response=response.trim();
                    if(response.length()>0) {
                        StringTokenizer st = new StringTokenizer(response, "`");
                        to.setText(st.nextToken());
                        subject.setText(st.nextToken());
                        body.setText(st.nextToken());
                        imglink=st.nextToken();
                        if (!imglink.equals("none"))
                        {
                            Picasso.get().load(Uri.parse(imglink))
                                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                                    .networkPolicy(NetworkPolicy.NO_CACHE)
                                    .placeholder(R.drawable.loadingmove)
                                    .error(R.drawable.errorfor)
                                    .into(tv);
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    pd.dismiss();
                    msg("Check your internet...!");
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String>pes=new Hashtable<>();
                    pes.put("msgid",ms_id);
                    pes.put("cata",mode);
                    pes.put("seen",seen);
                    return pes;
                }
            };
            srr.setShouldCache(false);
            RequestQueue qq=Volley.newRequestQueue(sendmail.this);
            qq.add(srr);
            qq.getCache().clear();
        }

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Control.ctrl='o';
                if (to.getText().toString().length()==0)
                {
                    msg("Please Enter To id");
                    ctrl=false;
                }
                if (body.getText().toString().length()==0)
                {
                    msg("Please enter body");
                    ctrl=false;
                }
                if (ctrl)
                {
                    if (subject.getText().toString().length()>0) {
                        subjectval = subject.getText().toString();
                    }
                    else {
                        subjectval="no subject";
                    }
                    date=new SimpleDateFormat("dd/MM/yyyy").format(new Date());
                    time=new SimpleDateFormat("HH:mm").format(new Date());
                    pd=ProgressDialog.show(sendmail.this,"Sending","Please wait");
                    if(selectImage)
                    {
                        filename=frmid+"_"+date.replace("/",":")+":"+"_"+time+"_"+String.valueOf((int) (Math.random() * ((100 - 1) + 1) + 1))+".jpg";

                        VolleyMultipart vm=new VolleyMultipart(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/imgs/sendimmail.php", new Response.Listener<NetworkResponse>() {
                            @Override
                            public void onResponse(NetworkResponse response) {
                                pd.dismiss();
                                try {
                                    vb.vibrate(50);
                                    msg(new String(response.data, HttpHeaderParser.parseCharset(response.headers)));
                                }
                                catch (UnsupportedEncodingException e)
                                {

                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pd.dismiss();

                                msg("Error try again...!");
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String>pems=new Hashtable<>();
                                pems.put("toval",to.getText().toString());
                                pems.put("frmval",frmid);
                                pems.put("msgval",body.getText().toString());
                                pems.put("dateval",date);
                                pems.put("timeval",time);
                                pems.put("subval",subjectval);
                                return pems;
                            }
                            @Override
                            protected Map<String, VolleyMultipart.DataPart> getByteData() throws AuthFailureError {
                                Map<String, VolleyMultipart.DataPart> params = new HashMap<>();
                                params.put("file",new DataPart(filename,imgdata));
                                return params;
                            }
                        };
                        vm.setShouldCache(false);
                        RequestQueue vq=Volley.newRequestQueue(sendmail.this);
                        vq.add(vm);
                        vq.getCache().clear();

                    }
                    else {
                        StringRequest sr=new StringRequest(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/sendmail.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                pd.dismiss();
                                vb.vibrate(50);
                                msg(response);
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                pd.dismiss();

                                msg("Check your internet connection");
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String>pems=new Hashtable<>();
                                pems.put("toval",to.getText().toString());
                                pems.put("frmval",frmid);
                                pems.put("msgval",body.getText().toString());
                                pems.put("dateval",date);
                                pems.put("timeval",time);
                                pems.put("subval",subjectval);
                                return pems;
                            }
                        };
                        sr.setShouldCache(false);
                        RequestQueue q= Volley.newRequestQueue(sendmail.this);
                        q.add(sr);
                        q.getCache().clear();
                    }


                }
                else
                {
                    ctrl=true;
                }
            }
        });

        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!imglink.equals("none")) {
                    String datatemp[] = imglink.split("/");
                    String lnk = datatemp[datatemp.length - 1].replace(":", "");
                    new DownloadNow(sendmail.this, lnk).execute(imglink);
                }
                else
                {
                    msg("No Image");
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==200)&&(resultCode==RESULT_OK))
        {
            selectImage=true;
            try {
                Bitmap bp = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                imgdata=getByte(bp);

            }
            catch (FileNotFoundException e)
            {
                msg("Error for access file");
            }
            catch (IOException e)
            {
                msg("Error");
            }
        }
        else
        {
            msg("Please select a Image");
        }
    }

    void msg(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
    byte[] getByte(Bitmap bitmap)
    {
        ByteArrayOutputStream b=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,b);
        return b.toByteArray();
    }
}
