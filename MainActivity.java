package ca.birthalert.aronne.birthalert;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Calendar;


public class MainActivity extends ActionBarActivity {

    AlarmManager alarmManager;
    PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //getMenuInflater().inflate(R.menu.main_activity_actions, menu);
       // return super.OnCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       // TextView tv = (TextView) findViewById(R.id.tvLength);

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

       // String length = "20 min";
        //tv.append(length);
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);

        }


        return super.onOptionsItemSelected(item);
    }

    public void actionConnectHandler(View view) {

        Intent alarmIntent = new Intent(this, GatherSensorDataIntentService.class);
        pendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, Calendar.getInstance().getTimeInMillis(), 1000, pendingIntent);
        startService(new Intent(getBaseContext(), GatherSensorDataIntentService.class));

    }

    @Override
    protected void onStop() {
        super.onStop();


    }

    public void actionHistoryClickHandler(MenuItem item) {
        Intent historyIntent = new Intent(this, History.class);
        startActivity(historyIntent);
    }

    public void actionLiveDisplayClickHandler(MenuItem item) {
        Intent liveIntent = new Intent(this, LiveDisplay.class);
        startActivity(liveIntent);
    }
}
