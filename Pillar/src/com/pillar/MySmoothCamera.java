package com.pillar;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.util.math.MathUtils;

import android.util.Log;

public class MySmoothCamera extends SmoothCamera {

	private final float COOR_EQ_ERR = 0.01f;
	
	private float _dstX, _dstY;
	private boolean _isMoving;
	
	public MySmoothCamera(float pX, float pY, float pWidth, float pHeight,
			float pMaxVelocityX, float pMaxVelocityY, float pMaxZoomFactorChange) {
		super(pX, pY, pWidth, pHeight, pMaxVelocityX, pMaxVelocityY,
				pMaxZoomFactorChange);
		_dstX = getCenterX();
		_dstY = getCenterY();
		_isMoving = false;
		registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (_isMoving)
					if (equal(getCenterX(), _dstX, COOR_EQ_ERR) && equal(getCenterY(), _dstY, COOR_EQ_ERR))
						_isMoving = false;
				Log.d("Wei", "isMoving = " + _isMoving + " dX=" + _dstX + " dY=" + _dstY + " x=" + getCenterX() + " y=" + getCenterY());
			}
			@Override
			public void reset() {}
		});
	}
	
	@Override
	public void setCenter(float x, float y) {
		if (!equal(x, _dstX, COOR_EQ_ERR) || !equal(y, _dstY, COOR_EQ_ERR))
			_isMoving = true;
		_dstX = x;
		_dstY = y;
		super.setCenter(x, y);
	}
	
	public boolean getIsMoving() {
		return _isMoving;
	}
	
	private boolean equal(float a, float b, float err) {
		return Math.abs(a - b) < err ? true : false;
	}

}
