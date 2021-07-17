package com.jr.jcompass;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.graphics.Typeface;

public class CompassRingView extends View
{
	private float COMPASS_RING_RADIUS = 0.28f;
	private float COMPASS_RING_RIM_SIZE = 0.04f;
	private float COMPASS_AXIS_SIZE = 0.15f;
	
	private float COMPASS_DEGREE_TEXT_DISTANCE = 0.4f;
	private float COMPASS_DEGREE_TEXT_SIZE = 0.04f;
	
	private float COMPASS_DIRECTION_TEXT_DISTANCE = 0.23f;
	private float COMPASS_DIRECTION_TEXT_SIZE = 0.06f;
	
	private float COMPASS_MINI_CIRCLE_RADIUS = 0.1f;
	private float COMPASS_MINI_AXIS_SIZE = 0.05f;
	
	private float COMPASS_XY_ROTATION_FACTOR = 2f;
	
	private int COMPASS_RING_STROKE_THIN = 3;
	private int COMPASS_RING_STROKE_THICK = 6;
	private int COMPASS_POINTER_STROKE = 8;
	private int COMPASS_AXIS_STROKE_HORIZONTAL = 4;
	private int COMPASS_AXIS_STROKE_VERTICAL = 3;
	
	private int COMPASS_RING_STROKE_COUNT = 15;
	
	private float[] mOrientation = {0, 0, 0};
	
	private float mRingRadius;
	private float mRingRimSize;
	private float mAxisSize;
	private float mDegreeTextDistance;
	private float mDegreeTextSize;
	private float mDirectionTextDistance;
	private float mDirectionTextSize;
	
	private float mMiniCircleRadius;
	private float mMiniAxisSize;
	
	private int mSize;
	private int mHalfSize;

	private Paint mPaint;
	private Path mPointerTrianglePath;

	private float[] compassRingStrokes = new float[4*COMPASS_RING_STROKE_COUNT];
	
	public CompassRingView(Context c)
	{
		super(c);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setTypeface(Typeface.DEFAULT_BOLD);
		//this.setBackgroundColor(Color.RED);
	}
	
	protected void init()
	{
		mSize = getWidth();
		mHalfSize = mSize/2;
		
		mRingRadius = COMPASS_RING_RADIUS * mSize;
		mRingRimSize = COMPASS_RING_RIM_SIZE * mSize;
		mAxisSize = COMPASS_AXIS_SIZE * mSize;
		mDegreeTextDistance = COMPASS_DEGREE_TEXT_DISTANCE * mSize;
		mDegreeTextSize = COMPASS_DEGREE_TEXT_SIZE * mSize;
		mDirectionTextDistance = COMPASS_DIRECTION_TEXT_DISTANCE * mSize;
		mDirectionTextSize = COMPASS_DIRECTION_TEXT_SIZE * mSize;
		
		mMiniCircleRadius = COMPASS_MINI_CIRCLE_RADIUS * mSize;
		mMiniAxisSize = COMPASS_MINI_AXIS_SIZE * mSize;
		
		generateCompassRingStrokes();
		generatePointerTriangle();
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
		init();
	}
	
	protected void generatePointerTriangle()
	{
		Path path = new Path();
		
		float y = mRingRadius + mRingRimSize + 20;
		float base = 10, height = 20;
		
		path.moveTo(-base, -y);
		path.lineTo(0, -y - height);
		path.lineTo(base, -y);
		path.lineTo(-base, -y);
		path.close();
		
		mPointerTrianglePath = path;
	}

	protected void generateCompassRingStrokes()
	{
		final float innerRadius = mRingRadius;
		final float outerRadius = innerRadius + mRingRimSize;

		for(int i=0; i<COMPASS_RING_STROKE_COUNT; i++) {
			double angle = (Math.PI * i * 2 / 180.0);

			compassRingStrokes[i*4 + 0] = (float) (innerRadius * Math.cos(angle));
			compassRingStrokes[i*4 + 1] = (float) (innerRadius * Math.sin(angle));

			compassRingStrokes[i*4 + 2] = (float) (outerRadius * Math.cos(angle));
			compassRingStrokes[i*4 + 3] = (float) (outerRadius * Math.sin(angle));
		}
	}
	
	public void setOrientation(float[] orientation)
	{
		this.mOrientation = orientation;
		invalidate();
	}

	@Override
	protected void onDraw(Canvas canvas)
	{
		drawXYRotation(canvas);
		
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.WHITE);
		
		drawRingStrokes(canvas);
		drawPointerLine(canvas);
		drawAxis(canvas);
		
		mPaint.setStyle(Paint.Style.FILL);
		
		drawDegreeText(canvas);
		drawDirectionText(canvas);
		
		mPaint.setColor(Color.rgb(255, 165, 0));
		drawPointerTriangle(canvas);
	}
	
	private void drawPointerTriangle(Canvas canvas)
	{
		final float degree = -this.mOrientation[0];

		int restore = canvas.save();
		canvas.translate(mHalfSize, mHalfSize);
		canvas.rotate(degree);

		canvas.drawPath(mPointerTrianglePath, mPaint);

		canvas.restoreToCount(restore);
	}
	
	private void drawPointerLine(Canvas canvas)
	{
		mPaint.setStrokeWidth(COMPASS_POINTER_STROKE);
		canvas.drawLine(mHalfSize, mHalfSize - mRingRadius, mHalfSize, mHalfSize - mDegreeTextDistance + mDegreeTextSize, mPaint);
	}
	
	private void drawAxis(Canvas canvas)
	{
		mPaint.setStrokeWidth(COMPASS_AXIS_STROKE_HORIZONTAL);
		canvas.drawLine(mHalfSize - mAxisSize, mHalfSize, mHalfSize + mAxisSize, mHalfSize, mPaint);
		
		mPaint.setStrokeWidth(COMPASS_AXIS_STROKE_VERTICAL);
		canvas.drawLine(mHalfSize, mHalfSize - mAxisSize, mHalfSize, mHalfSize + mAxisSize, mPaint);
	}
	
	private void drawRingStrokes(Canvas canvas)
	{
		final float degree = -this.mOrientation[0];

		int restore = canvas.save();
		canvas.translate(mHalfSize, mHalfSize);
		canvas.rotate(degree);

		for(int i=0; i<180/COMPASS_RING_STROKE_COUNT; i++) {
			mPaint.setStrokeWidth(COMPASS_RING_STROKE_THICK);
			canvas.drawLines(compassRingStrokes, 0, 4, mPaint);

			mPaint.setStrokeWidth(COMPASS_RING_STROKE_THIN);
			canvas.drawLines(compassRingStrokes, 4, 4*(COMPASS_RING_STROKE_COUNT-1), mPaint);

			canvas.rotate(2*COMPASS_RING_STROKE_COUNT);
		}
		
		canvas.restoreToCount(restore);
	}
	
	private void drawDegreeText(Canvas canvas)
	{
		final float degree = -this.mOrientation[0];
		
		int restore = canvas.save();
		canvas.translate(mHalfSize, mHalfSize);
		
		mPaint.setTextSize(mDegreeTextSize);
		
		for(int i=0; i<12; i++) {
			double angle = (i*30 + degree + 270) * Math.PI / 180.0;
			String text = String.valueOf(i*30);
			canvas.drawText(text, (int) (mDegreeTextDistance * Math.cos(angle) - mPaint.measureText(text)*0.5f), (int) (mDegreeTextDistance * Math.sin(angle) + mDegreeTextSize*0.35f), mPaint);
		}
		
		canvas.restoreToCount(restore);
	}
	
	private void drawDirectionText(Canvas canvas)
	{
		final float degree = -this.mOrientation[0];

		int restore = canvas.save();
		canvas.translate(mHalfSize, mHalfSize);
		
		mPaint.setTextSize(mDirectionTextSize);

		for(int i=0; i<4; i++) {
			double angle = (i*90 + degree - 90) * Math.PI / 180.0;
			String text = CompassInfoView.DIRECTIONS[i*2];
			canvas.drawText(text, (int) (mDirectionTextDistance * Math.cos(angle) - mPaint.measureText(text)*0.5f), (int) (mDirectionTextDistance * Math.sin(angle) + mDirectionTextSize*0.35f), mPaint);
		}

		canvas.restoreToCount(restore);
	}
	
	private void drawXYRotation(Canvas canvas)
	{
		float tx = mOrientation[2] * COMPASS_XY_ROTATION_FACTOR;
		float ty = mOrientation[1] * COMPASS_XY_ROTATION_FACTOR;
		
		int restore = canvas.save();
		canvas.translate(mHalfSize + tx, mHalfSize + ty);
		
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setColor(Color.argb(128, 255, 255, 255));
		
		canvas.drawCircle(0, 0, mMiniCircleRadius, mPaint);
		
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setColor(Color.WHITE);
		
		mPaint.setStrokeWidth(COMPASS_AXIS_STROKE_HORIZONTAL);
		canvas.drawLine(-mMiniAxisSize, 0, mMiniAxisSize, 0, mPaint);

		mPaint.setStrokeWidth(COMPASS_AXIS_STROKE_VERTICAL);
		canvas.drawLine(0, -mMiniAxisSize, 0, mMiniAxisSize, mPaint);

		canvas.restoreToCount(restore);
	}
}
