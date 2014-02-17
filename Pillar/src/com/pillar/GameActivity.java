package com.pillar;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.util.FPSLogger;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.bitmap.BitmapTexture;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;

import android.util.DisplayMetrics;
import android.util.Log;


public class GameActivity extends SimpleBaseGameActivity {
	
	private GameController _gameController;
	
	public GameActivity() {
		super();
		_gameController = new GameController();
	}

	@Override
	public EngineOptions onCreateEngineOptions() {
		// 依螢幕縮放至最大, 不變形
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		float windowW = dm.widthPixels;
		float windowH = dm.heightPixels;
		if (windowW / GameController.CAM_WIDTH > windowH / GameController.CAM_HEIGHT)
			windowW = GameController.CAM_WIDTH * windowH / GameController.CAM_HEIGHT;
		else
			windowH = GameController.CAM_HEIGHT * windowW / GameController.CAM_WIDTH;
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_SENSOR, new RatioResolutionPolicy(windowW, windowH), _gameController.getSmoothCamera());
		// 增強梯度效果
		engineOptions.getRenderOptions().setDithering(true);
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		BitmapTextureAtlas btaNB = new BitmapTextureAtlas(getTextureManager(), 200, 100, TextureOptions.BILINEAR);
		NoiseBrick.TILEDTEXTURE = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(btaNB, this, "noiseB.png", 0, 0, 1, 1);
		btaNB.load();
		BitmapTextureAtlas btaPB = new BitmapTextureAtlas(getTextureManager(), 200, 100, TextureOptions.BILINEAR);
		PillarBrick.TILEDTEXTURE = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(btaPB, this, "pillarB.png", 0, 0, 1, 1);
		btaPB.load();

		BitmapTextureAtlas btaBG = new BitmapTextureAtlas(getTextureManager(), 480, 800, TextureOptions.BILINEAR);
		GameController.BACKBROUND = BitmapTextureAtlasTextureRegionFactory.createFromAsset(btaBG, this, "background.png", 0, 0);
		btaBG.load();
	}

	@Override
	protected Scene onCreateScene() {
		_gameController.setEngine(mEngine);
		mEngine.registerUpdateHandler(new FPSLogger());
		_gameController.setVertexBufferObjectManager(getVertexBufferObjectManager());
		_gameController.setTextureManager(getTextureManager());
		_gameController.setFontManager(getFontManager());
		_gameController.initScene();
		return _gameController.getScene();
	}

}
