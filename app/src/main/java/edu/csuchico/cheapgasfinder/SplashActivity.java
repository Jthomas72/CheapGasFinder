package edu.csuchico.cheapgasfinder;

import android.app.Activity;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.location.LocationClient;


public class SplashActivity extends Activity {

    private static String TAG = SplashActivity.class.getName();
    private static long SLEEP_TIME = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);    // Removes title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);    // Removes notification bar

        setContentView(R.layout.activity_splash);

        // Start timer and launch main activity

        IntentLauncher launcher = new IntentLauncher();
        launcher.start();
    }


    private class IntentLauncher extends Thread
    {
        public void run()
        {
            try
            {
                //Thread.sleep(SLEEP_TIME*1000);
                //LocationClient mLocationClient;
                //Location mCurrentLocation;
                //mCurrentLocation = mLocationClient.getLastLocation();
            }
            catch (Exception e)
            {
                Log.e(TAG, e.getMessage());
            }
            Intent intent = new Intent(SplashActivity.this, selectCarActivity.class);
            SplashActivity.this.startActivity(intent);
            SplashActivity.this.finish();
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.splash, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
