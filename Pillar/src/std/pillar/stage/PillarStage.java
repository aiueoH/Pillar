package std.pillar.stage;

import java.text.DecimalFormat;
import java.util.ArrayList;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.SmoothCamera;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
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
import org.andengine.util.adt.data.operator.IntOperator;
import org.andengine.util.math.MathUtils;
import org.andengine.util.modifier.IModifier;

import std.pillar.BarTimer;
import std.pillar.BrickManager;
import std.pillar.IBarTimerOwner;
import std.pillar.IBrickManagerOwner;
import std.pillar.MyLog;
import std.pillar.MySmoothCamera;
import std.pillar.brick.Brick;
import std.pillar.stage.IStageSwitcher.StageEnum;


import android.graphics.Typeface;
import android.util.Log;

public class PillarStage extends Stage implements  IBarTimerOwner, IBrickManagerOwner {

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
	
	private BrickManager _brickManager;
	
	// HUD
	private Text _completeText;
	private BarTimer _barTimer;
	
	private float _begineCamY;
	private float _endCamY;
	
	public PillarStage(MySmoothCamera camera, IStageSwitcher stageSwitcher) {
		super(camera, stageSwitcher);
	}
	
	@Override
	protected void initScene() {
		_begineCamY = getCamera().getCenterY();
		// background
		{
			float w = CAM_WIDTH, h = CAM_HEIGHT;
			float x = w / 2, y = h / 2;
			Gradient gradient = new Gradient(x, y, w, h, getVertexBufferObjectManager());
			gradient.setGradient(new Color(0.34f, 0.76f, 0.82f), Color.WHITE, 0f, 1f);
			getScene().setBackground(new EntityBackground(gradient));
		}
		_brickManager = new BrickManager(getScene(), getVertexBufferObjectManager(), this);
		_brickManager.init();
	}
	
	@Override
	protected void initHUD() {
		
	}
	
	///////////////////////////////////////////////////////////////////////////
	// IBarTimerOwner
	///////////////////////////////////////////////////////////////////////////
	
	@Override
	public void onTimesUp(TimerHandler timerHandler) {
		_completeText.setText("FAIL");
	}

	///////////////////////////////////////////////////////////////////////////
	// IBrickManagerOwner
	///////////////////////////////////////////////////////////////////////////

	@Override
	public void onBrickLinked(BrickManager brickManager) {
		_brickManager.focusNext();
		chaseBrickAtBottom(_brickManager.getFocusBrick());
	}
	
	@Override
	public void onCompleted(BrickManager brickManager) {
		MyLog.d("completed");
		final PillarStage thispiPillarStage = this;
		
		getScene().registerEntityModifier(new DelayModifier(1f, new IEntityModifierListener() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				_endCamY = getCamera().getCenterY();
				fadeOut(1f, new IEntityModifierListener() {
					@Override
					public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
					@Override
					public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
						getCamera().setCenterDirect(getCamera().getCenterX(), _begineCamY);
						fadeIn(1f, new IEntityModifierListener() {
							@Override
							public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
							@Override
							public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
								getCamera().setCenter(getCamera().getCenterX(), _endCamY, getCamera().getMaxVelocityY() / 30f, new IEntityModifierListener() {
									@Override
									public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
									@Override
									public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
										MyLog.d("over");
										getScene().registerEntityModifier(new DelayModifier(1f, new IEntityModifierListener() {
											@Override
											public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
											@Override
											public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
												getStageSwitcher().switchTo(thispiPillarStage, IStageSwitcher.StageEnum.HOME, 0);
											}
										}));
									}
								});
							}
						});
					}
				});
			}
		}));
	}
	
	///////////////////////////////////////////////////////////////////////////
	// others
	///////////////////////////////////////////////////////////////////////////
	
	private void chaseBrickAtBottom(Brick brick) {
		float cY = brick.getDestinationY() + (getCamera().getHeight() - brick.getHeight()) / 2;
		getCamera().setCenter(getCamera().getCenterX(), cY);
	}
}
