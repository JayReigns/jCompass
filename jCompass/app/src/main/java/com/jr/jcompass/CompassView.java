package com.jr.jcompass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.view.ViewGroup;
import android.widget.Toast;

public class CompassView extends ViewGroup
{
	private float PADDING_FACTOR_TOP = 0.06f;
	private float PADDING_FACTOR_MIDDLE = 0.04f;
	private float PADDING_FACTOR_BOTTOM = 0.1f;
	
	private CompassRingView mCompassRingView;
	private CompassInfoView mCompassInfoView;

	public CompassView(Context c)
	{
		super(c);
		mCompassRingView = new CompassRingView(c);
		mCompassInfoView = new CompassInfoView(c);
		
		this.setBackgroundColor(Color.BLACK);
		this.addView(mCompassRingView);
		this.addView(mCompassInfoView);
	}
	
	public void setOrientation(float[] orientation)
	{
		mCompassRingView.setOrientation(orientation);
		mCompassInfoView.setOrientation(orientation);
		//invalidate();
	}

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) 
	{
		//float aspect = (float) getHeight()/getWidth();
		//if(aspect > 1.5f) {
			int width = getWidth(), height = getHeight();
			int childTop = (int) (height * PADDING_FACTOR_TOP);
			
			mCompassRingView.layout(0, childTop, width, childTop + width);
			childTop += width + (int) (height * PADDING_FACTOR_MIDDLE);
			int paddingBottom = (int) (height * PADDING_FACTOR_BOTTOM);
			mCompassInfoView.layout(0, childTop, width, height - paddingBottom);
		//}
	}

}
        
