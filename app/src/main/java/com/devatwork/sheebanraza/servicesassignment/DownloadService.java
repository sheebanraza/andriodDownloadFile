package com.devatwork.sheebanraza.servicesassignment;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import junit.framework.Assert;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;

/**
 * Created by Sheeban Raza on 12-Mar-16.
 */
public class DownloadService extends Service {

    private final IBinder binder = new MyBinder();

    public class MyBinder extends Binder {
        DownloadService getService() {
            return DownloadService.this;
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();

        Object[] objUrls = (Object[]) intent.getExtras().get("URLs");
        URL[] urls = new URL[10];
        int i = 0;
        Assert.assertNotNull(objUrls);
        for (Object object : objUrls) {
            urls[i] = (URL) object;
            i++;
        }
        new DownloadManager().execute(urls);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }

    public class DownloadManager extends AsyncTask<URL, Integer, Long> {
        @Override
        protected Long doInBackground(URL... urls) {
            long totalBytesDownloaded = 0;
            int i = 0;
            for (URL url : urls) {
                if(url!=null) {
                    totalBytesDownloaded += downloadFile(url);
                    publishProgress((int) (((i + 1) / urls.length) * 100));
                }
            }
            return totalBytesDownloaded;
        }

        protected void onProgressUpdate(Integer... progress) {
            Toast.makeText(getBaseContext(), String.valueOf(progress[0]) + "% downloaded", Toast.LENGTH_LONG).show();
        }

        protected void onPostExecute(Long result) {
            Toast.makeText(getBaseContext(), "Downloaded " + result + " %", Toast.LENGTH_LONG).show();
        }

        private int downloadFile(URL url) {
            try {
                if (url != null) {
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setDoOutput(true);
                    urlConnection.connect();
                    InputStream inputStream = new BufferedInputStream(urlConnection.getInputStream());
                    File sdcard = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                    File file = new File(sdcard, "/down.pdf");

                    byte[] buffer = new byte[1024];
                    int bufferLength = 0;
                    FileOutputStream fileOutput = new FileOutputStream(file);
                    while ((bufferLength = inputStream.read(buffer)) > 0) {
                        fileOutput.write(buffer, 0, bufferLength);
                    }
                    fileOutput.flush();
                    fileOutput.close();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return 100;
        }
    }

}
