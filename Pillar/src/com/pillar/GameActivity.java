package com.pillar;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.util.DisplayMetrics;


public class GameActivity extends SimpleBaseGameActivity {
	
	private PillarStage _PillarStage;
	
	public GameActivity() {
		super();
		_PillarStage = new PillarStage();
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		// 依螢幕縮放至最大, 不變形
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float windowW = dm.widthPixels;
		float windowH = dm.heightPixels;
		if (windowW / PillarStage.CAM_WIDTH > windowH / PillarStage.CAM_HEIGHT)
			windowW = PillarStage.CAM_WIDTH * windowH / PillarStage.CAM_HEIGHT;
		else
			windowH = PillarStage.CAM_HEIGHT * windowW / PillarStage.CAM_WIDTH;
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR, new RatioResolutionPolicy(windowW, windowH), _PillarStage.getSmoothCamera());
		// 增強梯度效果
		engineOptions.getRenderOptions().setDithering(true);
		return engineOptions;
	}

	public static TiledTextureRegion FISH_REGION, F1, F2;
	@Override
	protected void onCreateResources() {
		TPacker.getInstance().load(this);
	}

	@Override
	protected Scene onCreateScene() {
		_PillarStage.setEngine(mEngine);
		mEngine.registerUpdateHandler(new FPSLogger());
		_PillarStage.setVertexBufferObjectManager(getVertexBufferObjectManager());
		_PillarStage.setTextureManager(getTextureManager());
		_PillarStage.setFontManager(getFontManager());
		_PillarStage.initScene();
		return _PillarStage.getScene();
	}

}
