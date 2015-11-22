package com.example.app.jason.ragerelease.app.Framework;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

/**
 * Created by Win8 on 22/11/2015.
 */
public class SensorHandler implements SensorEventListener
{
    protected Activity activity = null;
    protected SensorManager sensorManager = null;
    protected Sensor accelerometer = null;
    protected boolean beingShaked = false;
    protected long lastUpdate = 0;
    protected float last_x, last_y, last_z;
    protected static final int SHAKE_THRESHOLD = 4000;

    public SensorHandler(final Activity currentActivity, final String sensorName)
    {
        // Initialising the sensor.
        activity = currentActivity;
        sensorManager = (SensorManager) currentActivity.getSystemService(sensorName);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public void registerListener()
    {
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    public void unregisterListener()
    {
        sensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    public void onSensorChanged(SensorEvent sensorEvent)
    {
        Sensor currentSensor = sensorEvent.sensor;

        // If the phone is being shaked.
        if(currentSensor.getType() == Sensor.TYPE_ACCELEROMETER)
        {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100)
            {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z)/ diffTime * 10000;

                if (speed > SHAKE_THRESHOLD)
                {
                    Toast.makeText(activity, "Have a nice day!", Toast.LENGTH_SHORT).show();
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    // Getters.
    // This will return the current accelerometer status of the phone.
    public boolean isPhoneBeingShaked() { return beingShaked; }
}
