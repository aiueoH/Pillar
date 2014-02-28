package com.pillar;

import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;

public class MySmoothCamera extends SmoothCamera {

	private final float COOR_EQ_ERR = 0.01f;
	
	private float _dstX, _dstY;
	private float _defaultMaxVelocityY;
	private boolean _isMoving;
	private IEntityModifierListener _moveListener;
	
	public MySmoothCamera(float pX, float pY, float pWidth, float pHeight,
			float pMaxVelocityX, float pMaxVelocityY, float pMaxZoomFactorChange) {
		super(pX, pY, pWidth, pHeight, pMaxVelocityX, pMaxVelocityY,
				pMaxZoomFactorChange);
		_isMoving = false;
		_moveListener = null;
		_dstX = getCenterX();
		_dstY = getCenterY();
		_defaultMaxVelocityY = getMaxVelocityY();
		registerUpdateHandler(new IUpdateHandler() {
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (_isMoving)
					if (equal(getCenterX(), _dstX, COOR_EQ_ERR) && equal(getCenterY(), _dstY, COOR_EQ_ERR)) {
						_isMoving = false;
						onMovingFinished();
					}
			}
			@Override
			public void reset() {}
		});
	}
	
	public boolean getIsMoving() {
		return _isMoving;
	}
	
	@Override
	public void setCenter(float x, float y) {
		_moveListener = null;
		setMaxVelocityY(_defaultMaxVelocityY);
		setCenterImp(x, y);
	}
	
	public void setCenter(final float x, final float y, final float maxVelocityY) {
		_moveListener = null;
		setMaxVelocityY(maxVelocityY);
		setCenterImp(x, y);
	}
	
	public void setCenter(final float x, final float y, IEntityModifierListener listener) {
		_moveListener = listener;
		setMaxVelocityY(_defaultMaxVelocityY);
		setCenterImp(x, y);
		onMovingStarted();
	}
	
	public void setCenter(final float x, final float y, final float maxVelocityY, IEntityModifierListener listener) {
		_moveListener = listener;
		setMaxVelocityY(maxVelocityY);
		setCenterImp(x, y);
		onMovingStarted();
	}
	
	private void setCenterImp(final float x, final float y) {
		if (!equal(x, _dstX, COOR_EQ_ERR) || !equal(y, _dstY, COOR_EQ_ERR))
			_isMoving = true;
		_dstX = x;
		_dstY = y;
		super.setCenter(x, y);
	}
	
	private boolean equal(float a, float b, float err) {
		return Math.abs(a - b) < err ? true : false;
	}

	private void onMovingStarted() {
		if (_moveListener != null)
			_moveListener.onModifierStarted(null, null); 
	}
	
	private void onMovingFinished() {
		setMaxVelocityY(_defaultMaxVelocityY);
		if (_moveListener != null)
			_moveListener.onModifierFinished(null, null);
	}
}
