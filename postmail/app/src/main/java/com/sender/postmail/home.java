package com.sender.postmail;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.view.View;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Hashtable;
import java.util.Map;
import java.util.StringTokenizer;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    String uid,pass;
    RecyclerView rv;
    TextView showmode,user;
    String mode="inbox";
    GridLayoutManager glm;
    AdptRv adp;
    AdptRv.clk ck;
    SwipeRefreshLayout srl;
    Vibrator vb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        vb=(Vibrator)getSystemService(Context.VIBRATOR_SERVICE);
        ActivityCompat.requestPermissions(home.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.MANAGE_EXTERNAL_STORAGE},1);
        sp= PreferenceManager.getDefaultSharedPreferences(home.this);

        Control.ctrl='o';
        rv=findViewById(R.id.homeshowall);
        srl=findViewById(R.id.homeswp);

        showmode=findViewById(R.id.homemode);
        showmode.setText("Receive");
        glm=new GridLayoutManager(home.this,1);
        rv.setLayoutManager(glm);

        uid=sp.getString("post-mail-userid","");
        pass=sp.getString("post-mail-password","");


        ck=new AdptRv.clk() {
            @Override
            public void click(String msgid,String seen) {
                Intent ii=new Intent(home.this,sendmail.class);
                ii.putExtra("msid",msgid);
                ii.putExtra("form_id",uid);
                ii.putExtra("mode",mode);
                ii.putExtra("seen",seen);
                startActivity(ii);
            }

            @Override
            public void longClick(final String msg_id) {
                vb.vibrate(70);
                AlertDialog.Builder bld=new AlertDialog.Builder(home.this);
                bld.create();
                bld.setCancelable(false);
                bld.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        StringRequest sr=new StringRequest(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/imgs/delete_msg.php", new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                msg("Done");
                                getData();
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                msg("Check Your Internet Connection..!");
                            }
                        }){
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String,String>pems=new Hashtable<>();
                                pems.put("msgid",msg_id);
                                pems.put("user_id",uid);
                                return pems;
                            }
                        };
                        sr.setShouldCache(false);
                        RequestQueue q= Volley.newRequestQueue(home.this);
                        q.add(sr);
                        q.getCache().clear();
                    }
                });
                bld.setNegativeButton("No",null);
                bld.setTitle("Delete!");
                bld.setMessage("Are You Sure To Delete This Msg");
                bld.show();
            }
        };

        adp=new AdptRv(home.this,ck);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(home.this,sendmail.class);
                i.putExtra("form_id",uid);
                i.putExtra("msid","0");
                i.putExtra("mode","send");
                startActivity(i);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View v=navigationView.getHeaderView(0);
        user=v.findViewById(R.id.showhomeuserid);
        user.setText(uid);

        StringRequest sr=new StringRequest(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/login.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response=response.trim();
                if (response.equals("Error Password"))
                {
                    AlertDialog.Builder bld=new AlertDialog.Builder(home.this);
                    bld.create();
                    bld.setCancelable(false);
                    bld.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            spe=sp.edit();
                            spe.putString("post-mail-userid","");
                            spe.putString("post-mail-password","");
                            spe.apply();
                            finish();
                        }
                    });
                    bld.setTitle("Error");
                    bld.setMessage("Password Has Been Change");
                    bld.show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                msg("Check Your Internet Connection..!");
                finish();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String>pems=new Hashtable<>();
                pems.put("uid",uid);
                pems.put("pass",pass);
                return pems;
            }
        };
        sr.setShouldCache(false);
        RequestQueue q= Volley.newRequestQueue(home.this);
        q.add(sr);
        q.getCache().clear();
        srl.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
            }
        });
    }

    @Override
    protected void onResume() {
        getData();
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.inbox) {
            adp.clear();
            rv.setAdapter(adp);
            mode="inbox";
            showmode.setText("Loading...");
            getData();
        } else if (id == R.id.outbox) {
            adp.clear();
            rv.setAdapter(adp);
            mode="outbox";
            showmode.setText("Loading...");
            getData();
        } else if (id == R.id.logout) {
            spe=sp.edit();
            spe.putString("post-mail-userid","");
            spe.putString("post-mail-password","");
            spe.apply();
            startActivity(new Intent(home.this,loginpage.class));
            finish();
        }
        else if (id==R.id.changepass)
        {
            Intent ii=new Intent(home.this,changepass.class);
            ii.putExtra("post_mail_userid",uid);
            startActivity(ii);
        }
        else if (id==R.id.appinfo)
        {
            startActivity(new Intent(home.this,appinfopage.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    void getData() {

        if(Control.ctrl=='o'){
        StringRequest sr = new StringRequest(Request.Method.POST, "https://souravsaha1234.000webhostapp.com/post-mail/fetch.php", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (mode.equals("inbox")) {
                    showmode.setText("Receive");
                } else if (mode.equals("outbox")) {
                    showmode.setText("Sended");
                }
                response = response.trim();
                adp.clear();
                rv.setAdapter(adp);
                srl.setRefreshing(false);
                if (response.length() > 0) {
                    StringTokenizer st = new StringTokenizer(response, "`");
                    while (st.hasMoreTokens()) {
                        adp.getData(new CaptureDatas(st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken(), st.nextToken()));
                    }
                    response = "";
                    rv.setAdapter(adp);
                } else {
                    adp.clear();
                    rv.setAdapter(adp);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                msg("Error connection");
                srl.setRefreshing(false);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> pems = new Hashtable<>();
                pems.put("val", mode);
                pems.put("idv", uid);
                return pems;
            }
        };
        sr.setShouldCache(false);
        RequestQueue q = Volley.newRequestQueue(home.this);
        q.add(sr);
        q.getCache().clear();
    }
    else {
        Control.ctrl='o';
        }
    }
    void msg(String s)
    {
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
