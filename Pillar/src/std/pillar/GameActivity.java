package std.pillar;
import java.util.HashMap;

import org.andengine.engine.Engine;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import std.pillar.stage.HomeStage;
import std.pillar.stage.IStageSwitcher;
import std.pillar.stage.PillarStage;
import std.pillar.stage.Stage;


import android.util.DisplayMetrics;


public class GameActivity extends SimpleBaseGameActivity implements IStageSwitcher{
	// Camera
	public static final float CAM_WIDTH = 480, CAM_HEIGHT = 800;
	private final float CAM_VEL_X = 1f, CAM_VEL_Y = 3000f, CAM_VEL_Z = 1f;
	
	public static Engine ENGINE;
	private HashMap<IStageSwitcher.StageEnum, Stage> _stages;
	private MySmoothCamera _camera;
	
	public GameActivity() {
		super();
		_stages = new HashMap<IStageSwitcher.StageEnum, Stage>();
		_camera = new MySmoothCamera(0, 0, CAM_WIDTH, CAM_HEIGHT, CAM_VEL_X, CAM_VEL_Y, CAM_VEL_Z);
		_stages.put(StageEnum.PILLAR, new PillarStage(_camera, this));
		_stages.put(StageEnum.HOME, new HomeStage(_camera, this));
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		// 依螢幕縮放至最大, 不變形
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float windowW = dm.widthPixels;
		float windowH = dm.heightPixels;
		if (windowW / CAM_WIDTH > windowH / CAM_HEIGHT)
			windowW = CAM_WIDTH * windowH / CAM_HEIGHT;
		else
			windowH = CAM_HEIGHT * windowW / CAM_WIDTH;
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR, new RatioResolutionPolicy(windowW, windowH), _camera);
		// 增強梯度效果
//		engineOptions.getRenderOptions().setDithering(true);
		return engineOptions;
	}
	
	@Override
	protected void onCreateResources() {
		TPacker.getInstance().load(this);
	}

	@Override
	protected Scene onCreateScene() {
		ENGINE = mEngine;
		mEngine.registerUpdateHandler(new FPSLogger());
		
		try {
			for (Stage stage : _stages.values()) {
				stage.setVertexBufferObjectManager(getVertexBufferObjectManager());
				stage.setTextureManager(getTextureManager());
				stage.setFontManager(getFontManager());
				stage.init();
			}
		} catch (Exception e) {
			MyLog.d(e);
		}
		
		IStageSwitcher.StageEnum s = IStageSwitcher.StageEnum.HOME;
		_camera.setHUD(_stages.get(s).getHUD());
		return _stages.get(s).getScene();
	}

	@Override
	public void switchTo(Stage sender, StageEnum stage, int arg) {
		Stage s = _stages.get(stage);
		s.fadeIn(1f, null);
		_camera.setHUD(s.getHUD());
		mEngine.setScene(s.getScene());
	}

	
	
}
