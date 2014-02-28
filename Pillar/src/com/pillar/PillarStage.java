package com.pillar;

import java.text.DecimalFormat;
import java.util.ArrayList;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.primitive.Gradient;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.EntityBackground;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;

import android.graphics.Typeface;
import android.util.Log;

public class PillarStage implements  IBarTimerOwner, IBrickManagerOwner {

	public static final float SCENE_WIDTH = 480, SCENE_HEIGHT = 3000;
	// Camera
	public static final float CAM_WIDTH = 480, CAM_HEIGHT = 800;
	private final float CAM_VEL_X = 1f, CAM_VEL_Y = 3000f, CAM_VEL_Z = 1f;
	// BarTimer
	private final float BT_X = 18f, BT_Y = 375f;
	private final float BT_W = 25f, BT_H = 740f;
	private final float BT_BORDER_W = 5f;
	private final Color BT_BAR_C = new Color(1f, 0.8f, 0.3f);
	private final Color BT_BORDER_C = Color.WHITE;
	private final float TIMER_DURATION = 60f;
	private final float TIMER_INTERVAL = 0.1f;
	// PillarBrick
	private final int PB_NUMS = 17; 
	private final int PB_LINKED_NUMS_THR = 5;

	public static ITextureRegion BACKBROUND;
	
	private Engine _engine;
	private VertexBufferObjectManager _vertexBufferObjectManager;
	private FontManager _fontManager;
	private TextureManager _textureManager;
	private Scene _scene;
	private MySmoothCamera _camera;
	private HUD _hud;
	
	private BrickManager _brickManager;
	
	private ArrayList<Brick> _bricks;
	private int _bottomBrickIndex;
	// HUD
	private Text _completeText;
	private BarTimer _barTimer;
	
	private boolean _isComplete;
	private int _unHandled = 0;
	
	private Scene _Scene2;
	
	private float _begineCamY;
	private float _endCamY;
	
	public PillarStage() {
		_isComplete = false;
		
		_bricks = new ArrayList<Brick>();
		_camera = new MySmoothCamera(0, 0, CAM_WIDTH, CAM_HEIGHT, CAM_VEL_X, CAM_VEL_Y, CAM_VEL_Z);
		_begineCamY = _camera.getCenterY();
	}
	
	public void initScene() {
		_scene = new Scene();
		_hud = new HUD();
		// background
		{
			float w = CAM_WIDTH, h = CAM_HEIGHT;
			float x = w / 2, y = h / 2;
			Gradient gradient = new Gradient(x, y, w, h, _vertexBufferObjectManager);
			gradient.setGradient(new Color(0.34f, 0.76f, 0.82f), Color.WHITE, 0f, 1f);
			_scene.setBackground(new EntityBackground(gradient));
		}
//		initBrick();
		_brickManager = new BrickManager(_engine, _scene, _vertexBufferObjectManager, this);
		_brickManager.init();
//		String s = "";
//		for (Brick b : _bricks) {
//			if (b instanceof PillarBrick)
//				s += "p";
//			else
//				s += "N";
//		}
//		Log.d("Wei", s);

//		chaseBrickAtBottom(_bricks.get(_bottomBrickIndex));
		
//		// text
//		{
//			Font font = FontFactory.create(_fontManager, _textureManager, 256, 256, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
//			font.load();
//			_completeText = new Text(0, 0, font, "01234567890123456789", _vertexBufferObjectManager);
//			_completeText.setText(new DecimalFormat("#.##").format(calcCompleteRate()) + "%");
//			float x = _completeText.getWidth() / 2, y = _camera.getHeight() - _completeText.getHeight() / 2;
//			_completeText.setPosition(x, y);
//			_hud.attachChild(_completeText);
//		}
//		// bartimer
//		{
//			_barTimer = new BarTimer(BT_X, BT_Y, BT_W, BT_H, BT_BORDER_W, BT_BAR_C, BT_BORDER_C, TIMER_DURATION, TIMER_INTERVAL, _vertexBufferObjectManager, this);
//			_hud.attachChild(_barTimer);
//			_scene.registerUpdateHandler(_barTimer.getTimerHandler());
//		}
//		// test button
//		{
//			Rectangle r; 
//			r = new Rectangle(50f, CAM_HEIGHT - 50f, 50f, 50f, _vertexBufferObjectManager) {
//				@Override
//				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
//			    {
//					if (pSceneTouchEvent.isActionDown()) {
//						chaseBrickAtBottom(_bricks.get(0));
//					} else if (pSceneTouchEvent.isActionUp()) {
//						chaseBrickAtBottom(_bricks.get(_bottomBrickIndex));
//					}
//			        return true;
//			    };
//			};
//			r.setColor(Color.RED);
//			_hud.registerTouchArea(r);
//			_hud.attachChild(r); 
//			r = new Rectangle(50f, CAM_HEIGHT - 120f, 50f, 50f, _vertexBufferObjectManager) {
//				@Override
//				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
//			    {
//					if (pSceneTouchEvent.isActionMove())
//						_camera.setCenter(_camera.getCenterX(), _camera.getCenterY() + 30);
//					if (pSceneTouchEvent.isActionUp())
//						_engine.setScene(_scene);
//			        return true;
//			    };
//			};
//			r.setColor(Color.GREEN);
//			_hud.registerTouchArea(r);
//			_hud.attachChild(r); 
//			r = new Rectangle(50f, CAM_HEIGHT - 190f, 50f, 50f, _vertexBufferObjectManager) {
//				@Override
//				public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float X, float Y) 
//			    {
//					if (pSceneTouchEvent.isActionMove())
//						_camera.setCenter(_camera.getCenterX(), _camera.getCenterY() - 30);
//					if (pSceneTouchEvent.isActionUp())
//						_engine.setScene(_Scene2);
//			        return true;
//			    };
//			};
//			r.setColor(Color.GREEN);
//			_hud.registerTouchArea(r);
//			_hud.attachChild(r);
//		}
		_camera.setHUD(_hud);
	}
	
//	public void initBrick() {
//		for (int i = 0, pCount = 0; pCount < PB_NUMS; i++) {
//			float pX = CAM_WIDTH / 2;
//			float pY = SCENE_HEIGHT - (i + 1) * Brick.DEFAULT_HEIGHT + Brick.DEFAULT_HEIGHT / 2;
//			pY = (i + 0.5f) * Brick.DEFAULT_HEIGHT;
//			Brick brick;
//			if (MathUtils.random(0, 2) == 0) {
//				brick = new PillarBrick(pX, pY, _vertexBufferObjectManager, this, PB_NUMS - pCount - 1);
//				pCount++;
//			} else
//				brick = new NoiseBrick(pX, pY, _vertexBufferObjectManager, this);
//			addBrick(brick);
//		}
//		_bottomBrickIndex = 0;
//	}
	
//	private void addBrick(Brick brick) {
//		_scene.attachChild(brick);
//		_scene.registerTouchArea(brick);
//		_bricks.add(brick);
//	}
	
	///////////////////////////////////////////////////////////////////////////
	// get
	///////////////////////////////////////////////////////////////////////////
	
	public SmoothCamera getSmoothCamera() {
		return _camera;
	}
	
	public Scene getScene() {
		return _scene;
	}
	
	public void setEngine(Engine engine) {
		_engine = engine;
	}
	
	///////////////////////////////////////////////////////////////////////////
	// set
	///////////////////////////////////////////////////////////////////////////
	
	public void setVertexBufferObjectManager(VertexBufferObjectManager vertexBufferObjectManager) {
		_vertexBufferObjectManager = vertexBufferObjectManager;
	}
	
	public void setTextureManager(TextureManager textureManager) {
		_textureManager = textureManager;
	}
	
	public void setFontManager(FontManager fontManager) {
		_fontManager = fontManager;
	}
	

	///////////////////////////////////////////////////////////////////////////
	// IBarTimerOwner
	///////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onTimesUp(TimerHandler timerHandler) {
//		freezeBricks();
//		setBricksIsTouchable(0, _bricks.size(), false);
		_completeText.setText("FAIL");
	}

	///////////////////////////////////////////////////////////////////////////
	// IBrickOwner
	///////////////////////////////////////////////////////////////////////////

//	@Override
//	public boolean isBrickTouchable(Brick brick) {
//		return brick.getIsTouchable() & !_camera.getIsMoving();
//	}
//	
//	@Override
//	public void onBrickMovedDown(Brick brick) {
//		if (_unHandled > 0) {
//			_unHandled--;
//			{
//				if (hasNoiseBrick())
//					_completeText.setText(new DecimalFormat("#.##").format(calcCompleteRate()) + "%");
//				else
//					_completeText.setText("Complete " + new DecimalFormat("#.##").format(calcCompleteRate()) + "% !!");
//				float x = _completeText.getWidth() / 2, y = _camera.getHeight() - _completeText.getHeight() / 2;
//				_completeText.setPosition(x, y);
//			}
//			if (_unHandled == 0) {
//				while (isPillarBrick(_bottomBrickIndex, PB_LINKED_NUMS_THR)) {
//					final int old = _bottomBrickIndex;	
//					_bottomBrickIndex += PB_LINKED_NUMS_THR;
//					_bottomBrickIndex = _bottomBrickIndex < _bricks.size() ? _bottomBrickIndex : _bricks.size() - 1;
//					setBricksIsTouchable(old, _bottomBrickIndex - old, false);
//				}
//				chaseBrickAtBottom(_bricks.get(_bottomBrickIndex));
//				// �����ʵe(�C�t�������Y������W)
//				if (_isComplete) {
//					final float bottomY = _bricks.get(0).getDestinationY() + (_camera.getHeight() - brick.getHeight()) / 2;
//					_camera.setCenter(_camera.getCenterX(), bottomY, _camera.getMaxVelocityY(), new IEntityModifierListener() {
//						@Override
//						public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
//						@Override
//						public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
//							final float topY = _bricks.get(_bricks.size() - 1).getDestinationY();
//							_camera.setCenter(_camera.getCenterX(), topY, _camera.getMaxVelocityY() / 10);
//						}
//					});
//				}
//			}
//		}
//	}
//	
//	@Override
//	public void onBrickMovedOut(final Brick brick) {
//		int i = _bricks.indexOf(brick);
//		deleteBrick(brick);
//		moveDownBricks(i);
//		if (!hasNoiseBrick()) {
//			_isComplete = true;
//			_barTimer.pause();
//			setBricksIsTouchable(_bottomBrickIndex, _bricks.size(), false);
//		}
//	}
//
//	@Override
//	public void onBrickTouchedUp(final Brick brick) {
//		_unHandled++;
//	}

	///////////////////////////////////////////////////////////////////////////
	// IBrickManagerOwner
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void onBrickLinked(BrickManager brickManager) {
		// TODO Auto-generated method stub
		MyLog.d(Thread.currentThread().getStackTrace()[2].toString());
		_brickManager.focusNext();
		chaseBrickAtBottom(_brickManager.getFocusBrick());
	}
	
	@Override
	public void onCompleted(BrickManager brickManager) {
		MyLog.d("completed");
		_endCamY = _camera.getCenterY();
		_camera.setCenterDirect(_camera.getCenterX(), _begineCamY);
		_camera.setCenter(_camera.getCenterX(), _endCamY, _camera.getMaxVelocityY() / 30f);
	}
	
	///////////////////////////////////////////////////////////////////////////
	// others
	///////////////////////////////////////////////////////////////////////////
	
	private void moveDownBricks(final int start) {
		for (int i = start; i < _bricks.size(); i++)
			_bricks.get(i).moveDown();
	}
	
//	private void freezeBricks() {
//		for (int i = _bottomBrickIndex; i < _bricks.size(); i++)
//			_bricks.get(i).setIsTouchable(false);
//	}
	
//	private boolean isPillarBrickLinked(int n) {
//		int index;
//		for (int i = 0; i < n; i++) {
//			index = _bottomBrickIndex + i;
//			if (index >= _bricks.size() || !(_bricks.get(index) instanceof PillarBrick))
//				return false;
//		}
//		return true;
//	}

	private boolean isPillarBrick(final int begine, final int n) {
		int count = 0;
		for (int i = begine; i < _bricks.size() && count < n; i++, count++)
			if (!(_bricks.get(i) instanceof PillarBrick))
				return false;
		return count < 5 ? false : true;
	}
	
//	private boolean isPillarBrick(final int begine, final int n, final int pass) {
//		for (int i = begine, count = 0; i < _bricks.size() && count < n; i++, count++)
//			if (i == pass)
//				count--;
//			else if (!(_bricks.get(i) instanceof PillarBrick)) {
//				return false;
//			}
//		return true;
//	}
	
	private void setBricksIsTouchable(final int begine, final int n, final boolean b) {
		for (int i = begine; i < begine + n && i < _bricks.size(); i++) {
			_bricks.get(i).setIsTouchable(b);
		}
	}
	
//	private void tryChaseBrickAtBottom() {
//		int targetIndex;
//		while (isPillarBrickLinked(PB_LINKED_NUMS_THR)) {
//			targetIndex = _bottomBrickIndex + PB_LINKED_NUMS_THR;
//			if (targetIndex < _bricks.size()) {
//				Brick targetBrick = _bricks.get(targetIndex);
//				chaseBrickAtBottom(targetBrick);
//				_bottomBrickIndex = _bricks.indexOf(targetBrick);
//			} else 
//				break;
//		}
//	}
	
	private void chaseBrickAtBottom(Brick brick) {
		float cY = brick.getDestinationY() + (_camera.getHeight() - brick.getHeight()) / 2;
		_camera.setCenter(_camera.getCenterX(), cY);
	}

	private boolean hasNoiseBrick() {
		for (Brick b : _bricks)
			if (b instanceof NoiseBrick && !b.getIsMovingOut())
				return true;
		return false;
	}
	
	private float calcCompleteRate() {
		return (float)countLinkedBrick() / PB_NUMS * 100f;
	}
	
	private int countLinkedBrick() {
		int c = 0;
		for (Brick b : _bricks)
			if (b instanceof PillarBrick)
				c++;
			else
				break;
		return c;
	}
	
	private void deleteBrick(final Brick brick) {
		_engine.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				_scene.unregisterTouchArea(brick);
				_scene.detachChild(brick);
				brick.detachSelf();
				brick.dispose();
			}
		});
		_bricks.remove(brick);
	}

}