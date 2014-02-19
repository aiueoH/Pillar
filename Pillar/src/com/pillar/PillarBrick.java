package com.pillar;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

public class PillarBrick extends Brick {

	public static TiledTextureRegion BRICK_TTR, BRICKBORDER_TTR;
	
	public PillarBrick(final float x, final float y,
			final VertexBufferObjectManager vertexBufferObjectManager,
			final IBrickOwner brickOwner, final int part) {
		super(x, y, BRICK_TTR, vertexBufferObjectManager, brickOwner);
//		super(x, y, TILEDTEXTURE, vertexBufferObjectManager, brickOwner);
		
		TiledSprite c = new TiledSprite(getWidth() / 2, getHeight() / 2, GameActivity.FISH_REGION, vertexBufferObjectManager);
		c.setCurrentTileIndex(part);
		attachChild(c);
		
		TiledSprite b = new TiledSprite(getWidth() / 2, getHeight() / 2, BRICKBORDER_TTR, vertexBufferObjectManager);
		attachChild(b);
	}

}
