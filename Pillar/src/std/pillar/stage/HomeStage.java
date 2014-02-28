package std.pillar.stage;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.FadeInModifier;
import org.andengine.entity.modifier.FadeOutModifier;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.modifier.IEntityModifier.IEntityModifierListener;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.adt.color.Color;
import org.andengine.util.modifier.IModifier;

import std.pillar.MyLog;
import std.pillar.MySmoothCamera;
import std.pillar.TPacker;
import std.pillar.TPacker.TKey;

public class HomeStage extends Stage {
	
	public HomeStage(MySmoothCamera camera, IStageSwitcher stageSwitcher) {
		super(camera, stageSwitcher);
	}
	
	@Override
	protected void initScene() {
		getScene().setBackground(new Background(new Color(0.2f, 0.5f, 0.6f)));
	}
	
	@Override
	protected void initHUD() {
		{
			AnimatedSprite title = new AnimatedSprite(0, 0, TPacker.getInstance().getTTR(TPacker.TKey.TITLE), getVertexBufferObjectManager());
			title.setPosition(getCamera().getCenterX(), getCamera().getCenterY() + 100f);
			getHUD().attachChild(title);
		}
		{
			final HomeStage thisHomeStage = this;
			AnimatedSprite start = new AnimatedSprite(0, 0, TPacker.getInstance().getTTR(TPacker.TKey.START), getVertexBufferObjectManager()) {
				@Override
				public boolean onAreaTouched(final TouchEvent touchEvent, final float x, final float y) {
					if (touchEvent.isActionUp()) {
						fadeOut(new IEntityModifierListener() {
							@Override
							public void onModifierStarted(IModifier<IEntity> pModifier, IEntity pItem) {}
							@Override
							public void onModifierFinished(IModifier<IEntity> pModifier, IEntity pItem) {
								getStageSwitcher().switchTo(thisHomeStage, IStageSwitcher.StageEnum.PILLAR, 0);
							}
						});
					}
					return true;
				}
			};
			start.setPosition(getCamera().getCenterX(), getCamera().getCenterY());
			start.registerEntityModifier(new LoopEntityModifier(
					new SequenceEntityModifier(
						new DelayModifier(0.8f),
						new FadeOutModifier(0.2f),
						new DelayModifier(0.1f),
						new FadeInModifier(0.5f))
					)
			);
			getHUD().attachChild(start);
			getHUD().registerTouchArea(start);
		}
	}
	
	

}
