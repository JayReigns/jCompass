package com.jr.jcompass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.graphics.Typeface;

public class CompassInfoView extends View
{
	public static final String[] DIRECTIONS = {"N", "NE", "E", "ES", "S", "SW", "W", "NW"};
	
	private float[] mOrientation = {0, 0, 0};

	private Paint mPaint;
	
	
	public CompassInfoView(Context c)
	{
		super(c);
		mPaint = new Paint();
		mPaint.setColor(Color.WHITE);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		//setBackgroundColor(Color.GREEN);
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
		mPaint.setTextSize(getHeight()*0.4f);
	}

	public void setOrientation(float[] orientation)
	{
		this.mOrientation = orientation;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		final float degree = this.mOrientation[0];
		String text = (int) degree + "° " + DIRECTIONS[(int)(degree/45)];
		canvas.drawText(text, getWidth()*0.5f - mPaint.measureText(text)*0.5f, getHeight()*0.5f, mPaint);
	}
}
