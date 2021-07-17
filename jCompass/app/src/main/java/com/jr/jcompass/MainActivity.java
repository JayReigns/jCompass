package com.jr.jcompass;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;

public class MainActivity extends Activity implements SensorEventListener
{
	private SensorManager mSensorManager;
	private CompassView mCompassView;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
		mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		mCompassView = new CompassView(this);
		
		setContentView(mCompassView);
    }
	
	@Override
    protected void onResume()
	{
        super.onResume();
        mSensorManager.registerListener(this,
										mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
										SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause()
	{
        super.onPause();
		mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event)
	{
		mCompassView.setOrientation(event.values);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // not in use
    }
}
