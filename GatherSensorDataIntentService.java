package ca.birthalert.aronne.birthalert;

import android.app.IntentService;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

import nl.littlerobots.bean.Bean;
import nl.littlerobots.bean.BeanDiscoveryListener;
import nl.littlerobots.bean.BeanListener;
import nl.littlerobots.bean.BeanManager;
import nl.littlerobots.bean.message.Callback;

/**
 * Created by Aronne on 12/07/2015.
 */
public class GatherSensorDataIntentService extends IntentService {

    private static String TAG = "ServiceTracker";
    private static String TAG2 = "ScratchTempData";
    public static Bean myBean;
    BeanListener myBeanListener;

    private final double TEMPTHRESH = 20;


    public GatherSensorDataIntentService(){super("GatherSensorDataIntentService");}


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

        public GatherSensorDataIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d(TAG, "Intent Started");
        myBeanListener = new BeanListener() {

            @Override
            public void onConnected() {
                myBean.setLed(0, 100, 0);
                myBean.readTemperature(new Callback<Integer>() {
                    @Override
                    public void onResult(Integer integer) {
                        double temperature = integer.doubleValue();

                        ((DataArrayApplication)getApplicationContext()).myTempData.add(temperature);

                        if (temperature > TEMPTHRESH) {
                            myBean.setLed(150, 0, 0);
                        }

                        //long time = System.currentTimeMillis();
                        //String timeStamp = String.valueOf(time);

                        if(((DataArrayApplication)getApplicationContext()).myTempData.size()>25) {
                            File sdcard = Environment.getExternalStorageDirectory();
                            File dir = new File(sdcard.getAbsolutePath() + "/testfolder");
                            dir.mkdirs();
                            File file = new File(dir, "testdata.txt");
                            FileOutputStream fos = null;
                            Object[] tempData =((DataArrayApplication)getApplicationContext()).myTempData.toArray();

                            if (!file.exists()) {
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                            try {
                                //BufferedWriter for performance, true to set append to file flag
                                BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));

                                for(int i=0;i<15;i++){
                                    buf.append(String.valueOf(tempData[i]));
                                    buf.newLine();
                                }

                                buf.close();
                                ((DataArrayApplication)getApplicationContext()).myTempData.clear();
                            } catch (IOException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                        }

                        //Log.d("This is temp", "temp " + temperature);
                        Log.d("More Temp data", "global temp" +((DataArrayApplication)getApplicationContext()).myTempData );
                        //Log.d("Temp Data", "TemperatureData" + myTempData);
                        if (myBean != null) {
                            myBean.disconnect();
                        }
                    }
                });

            }

            @Override
            public void onConnectionFailed() {

            }

            @Override
            public void onDisconnected() {

            }

            @Override
            public void onSerialMessageReceived(byte[] bytes) {



            }

            @Override
            public void onScratchValueChanged(int i, byte[] bytes) {
                Log.d(TAG2, "SCRATCHDATA WAS CHANGED IN BANK: "+i+" TO "+bytes[0]);
            }
        };

    }

    @Override
    public void onCreate() {
        super.onCreate();
        BeanDiscoveryListener beanDiscoveryListener = new BeanDiscoveryListener() {
            @Override
            public void onBeanDiscovered(Bean bean) {
                myBean=bean;
                //Toast.makeText(getApplicationContext(),"Bean Discovered",Toast.LENGTH_SHORT).show();

                myBean.connect(getApplicationContext(),myBeanListener);
                BeanManager.getInstance().cancelDiscovery();
            }

            @Override
            public void onDiscoveryComplete() {

            }
        };

        BeanManager.getInstance().startDiscovery(beanDiscoveryListener);


        checkExternalMedia();


    }

    private void checkExternalMedia() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(TAG, "Service Started");

    }



    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(TAG, "Service Destroyed");
    }

}
