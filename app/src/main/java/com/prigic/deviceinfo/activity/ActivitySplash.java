package com.prigic.deviceinfo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prigic.deviceinfo.R;
import com.prigic.deviceinfo.mainfunction.LoaderData;

public class ActivitySplash extends Activity {

    LoaderData cpu = null;
    LinearLayout lyt_progress;
    TextView tv_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        tv_message = (TextView) findViewById(R.id.tv_message);
        lyt_progress = (LinearLayout) findViewById(R.id.lyt_progress);
        cpu = new LoaderData(this);

        new LoaderInfo().execute("");
    }

    public class LoaderInfo extends AsyncTask<String, String, String> {

        public LoaderInfo() {
            tv_message.setText("Starting...");
        }

        @Override
        protected String doInBackground(String... params) {
            try {

                publishProgress("Load CPU info");
                Thread.sleep(300);
                cpu.loadCpuInfo();

                publishProgress("Load Battery info");
                Thread.sleep(300);
                cpu.loadBateryInfo();

                publishProgress("Load Device info");
                Thread.sleep(300);
                cpu.loadDeviceInfo();

                publishProgress("Load System info");
                Thread.sleep(300);
                cpu.loadSystemInfo();

                publishProgress("Load Sensor info");
                Thread.sleep(300);
                cpu.loadSupportInfo();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            tv_message.setText(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {
            lyt_progress.setVisibility(View.GONE);
            Intent i = new Intent(getApplicationContext(), ActivityMain.class);
            startActivity(i);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
            super.onPostExecute(result);
        }


    }

}
