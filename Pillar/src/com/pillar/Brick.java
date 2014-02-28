package com.pillar;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.modifier.IModifier;

public abstract class Brick extends AnimatedSprite {
	
	public static final float DEFAULT_WIDTH = 200, DEFAULT_HEIGHT = 100;
	public static final float MOVEDOWN_DURATION = 0.1f;
	public static final float MOVEOUT_DURATION = 0.07f;
	
	private IBrickOwner _brickOwner;
	private float _destinationY;
	private boolean _isTouchable;
	private boolean _isMovingOut;
	
	public Brick(final float x, final float y, 
			final TiledTextureRegion textureRegion,
			final VertexBufferObjectManager pVertexBufferObjectManager, 
			final IBrickOwner brickOwner) {
		super(x, y, textureRegion, pVertexBufferObjectManager);
		_isTouchable = true;
		_isMovingOut = false;
		_brickOwner = brickOwner;
		_destinationY = y;
		this.setCullingEnabled(true);
	}
	
	@Override
	public void dispose() {
		detachSelf();
		super.dispose();
	}
	
	public void moveDown() {
		_destinationY -= getHeight();
		this.clearEntityModifiers();
		final Brick b = this;
		this.registerEntityModifier(new MoveYModifier(MOVEDOWN_DURATION, this.getY(), _destinationY, new IEntityModifierListener() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				_brickOwner.onBrickMovedDown(b);
			}
		}));
	}
	
	public void moveOut() {
		_isMovingOut = true;
		this.clearEntityModifiers();
		this.registerEntityModifier(new MoveXModifier(MOVEOUT_DURATION, getX(), getX() + PillarStage.CAM_WIDTH / 2f + getWidth() / 2f, new IEntityModifierListener() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				onBrickMovedOut();
			}
		}));
	}
	
//	public boolean beContained(float xMin, float xMax, float yMin, float yMax) {
//		return isInRect(xMin, xMax, yMin, yMax, getX(), getY()) ||
//				isInRect(xMin, xMax, yMin, yMax, getX() + getWidth(), getY()) ||
//				isInRect(xMin, xMax, yMin, yMax, getX(), getY() + getHeight()) ||
//				isInRect(xMin, xMax, yMin, yMax, getX() + getWidth(), getY() + getHeight());
//	}
	
//	private boolean isInRect(float xMin, float xMax, float yMin, float yMax, float x, float y) {
//		return (x >= xMin && x <= xMax && y >= yMin && y <= yMax) ? true : false; 
//	}
	
	public float getDestinationY() {
		return _destinationY;
	}
	
	public boolean getIsMovingOut() {
		return _isMovingOut;
	}

	public void setDstY(final float y) {
		_destinationY = y;
	}
	
	public boolean getIsTouchable() {
		return _isTouchable;
	}
	
	public void setIsTouchable(final boolean b) {
		_isTouchable = b;
	}
	
	@Override
	public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float x, float y) 
    {
		if (_brickOwner.isBrickTouchable(this))
			if (pSceneTouchEvent.isActionUp()) {
				moveOut();
				_brickOwner.onBrickTouchedUp(this);
			}
        return true;
    };
	
	private void onBrickMovedOut() {
		_brickOwner.onBrickMovedOut(this);
	}
}
