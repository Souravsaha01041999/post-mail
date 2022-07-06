package com.sender.postmail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class DownloadNow extends AsyncTask<String,String,String>
{
    Context c;
    String link;
    ProgressDialog pd;
    File file;
    DownloadNow(Context cc,String l)
    {
        this.c=cc;
        this.link=l;
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=ProgressDialog.show(c,"Downloading","please Wait");
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        pd.dismiss();
        addToGallery(c,file.getPath());
        Toast.makeText(c,"Finish",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected String doInBackground(String...strings) {
        try
        {
            int count;
            URL url=new URL(strings[0]);
            URLConnection connection=url.openConnection();
            connection.connect();
            InputStream is=new BufferedInputStream(url.openStream(),1024);
            File f= Environment.getExternalStorageDirectory();
            File dir=new File(f,"/post-mail/");
            dir.mkdir();
            file=new File(dir,link);
            OutputStream os=new FileOutputStream(file);
            byte[] data=new byte[1024];
            count=is.read(data);
            while (count>0)
            {
                os.write(data,0,count);
                count=is.read(data);
            }
            os.flush();
            os.close();
        }
        catch (Exception e)
        {
            Toast.makeText(c,"error",Toast.LENGTH_SHORT).show();
        }
        return null;
    }
    void addToGallery(Context context, String photoPath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(photoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }
}
