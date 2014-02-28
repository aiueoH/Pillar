package std.pillar.stage;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.hud.HUD;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.UpdateHandlerList;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ColorModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.font.FontManager;
import org.andengine.opengl.texture.TextureManager;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;

import android.R.integer;

import std.pillar.MyLog;
import std.pillar.MySmoothCamera;

public abstract class Stage {

	private final float FADE_DURATION = 0.3f;
	
	private IStageSwitcher _stageSwitcher;
	private VertexBufferObjectManager _vertexBufferObjectManager;
	private TextureManager _textureManager;
	private FontManager _fontManager;
	private MySmoothCamera _camera;
	private Scene _scene;
	private HUD _hud;
	
	public Stage(MySmoothCamera camera, IStageSwitcher stageSwitcher) {
		_stageSwitcher = stageSwitcher;
		_camera = camera;
	}
	
	public VertexBufferObjectManager getVertexBufferObjectManager() {
		return _vertexBufferObjectManager;
	}
	
	public MySmoothCamera getCamera() {
		return _camera;
	}
	
	public Scene getScene() {
		return _scene;
	}
	
	public HUD getHUD() {
		return _hud;
	}
	
	public TextureManager getTextureManager() {
		return _textureManager;
	}
	
	public FontManager getFontManager() {
		return _fontManager;
	}
	
	public IStageSwitcher getStageSwitcher() {
		return _stageSwitcher;
	}
	
	public void setVertexBufferObjectManager(VertexBufferObjectManager vertexBufferObjectManager) {
		_vertexBufferObjectManager = vertexBufferObjectManager;
	}
	
	public void setTextureManager(TextureManager textureManager) {
		_textureManager = textureManager;
	}
	
	public void setFontManager(FontManager fontManager) {
		_fontManager = fontManager;
	}
	
	public void init() {
		_scene = new Scene();
		_hud = new HUD();
		initScene();
		initHUD();
		mask = new Rectangle(_camera.getCenterX(), _camera.getCenterY(), _camera.getWidth(), _camera.getHeight(), getVertexBufferObjectManager());
		mask.setColor(Color.BLACK);
		mask.setVisible(false);
		_hud.attachChild(mask);
	}

	protected abstract void initScene();
	
	protected abstract void initHUD();
	
	public void fadeIn(final IEntityModifierListener entityModifierListener) {
		fadeIn(FADE_DURATION, entityModifierListener);
	}
	
	public void fadeIn(final float duration, final IEntityModifierListener entityModifierListener) {
		fade(duration, 1f, 0f, entityModifierListener);
	}
	
	public void fadeOut(final IEntityModifierListener entityModifierListener) {
		fadeOut(FADE_DURATION, entityModifierListener);
	}
	
	public void fadeOut(final float duration, final IEntityModifierListener entityModifierListener) {
		fade(duration, 0f, 1f, entityModifierListener);
	}
	
	private Rectangle mask;
	private void fade(final float duration, final float begine, final float end, final IEntityModifierListener entityModifierListener) {
		mask.setVisible(true);
		mask.registerEntityModifier(new AlphaModifier(duration, begine, end, new IEntityModifierListener() {
			@Override
			public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {
				if (entityModifierListener != null)
					entityModifierListener.onModifierStarted(pModifier, pItem);
			}
			@Override
			public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
				mask.setVisible(false);
				if (entityModifierListener != null)
					entityModifierListener.onModifierFinished(pModifier, pItem);
			}
		}));
	}

}
