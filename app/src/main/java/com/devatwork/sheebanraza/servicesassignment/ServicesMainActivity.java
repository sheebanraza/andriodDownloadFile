package com.devatwork.sheebanraza.servicesassignment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.net.MalformedURLException;
import java.net.URL;

public class ServicesMainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_main);
    }

    public void downloadDirectly(View view) throws MalformedURLException {
        if (view.getId() == R.id.downloadPDF) {
            Intent intent = new Intent(ServicesMainActivity.this, DownloadService.class);
            URL[] urls = new URL[]{
                    new URL("http://www.cisco.com/web/about/ac79/docs/innov/IoE_Economy.pdf")};
            intent.putExtra("URLs", urls);
            startService(intent);
        }
    }

    public void downloadEnteredURL(View view) throws MalformedURLException {
        if (view.getId() == R.id.downloadManually) {
            EditText url1 = (EditText) findViewById(R.id.getURL1);
            EditText url2 = (EditText) findViewById(R.id.getURL2);
            Intent intent = new Intent(ServicesMainActivity.this, DownloadService.class);

            URL[] urls = new URL[]{
                    new URL(url1.getText().toString()),
                    new URL(url2.getText().toString())};
            intent.putExtra("URLs", urls);
            startService(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_services_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
