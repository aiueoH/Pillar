package com.pillar;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.IUpdateHandler;

import android.util.Log;

public class MySmoothCamera extends SmoothCamera {

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
					if (getCenterX() == _dstX && getCenterY() == _dstY)
						_isMoving = false;
			}
			@Override
			public void reset() {}
		});
	}
	
	@Override
	public void setCenter(float x, float y) {
		if (x != _dstX || y != _dstY)
			_isMoving = true;
		_dstX = x;
		_dstY = y;
		super.setCenter(x, y);
	}
	
	public boolean getIsMoving() {
		return _isMoving;
	}

}
