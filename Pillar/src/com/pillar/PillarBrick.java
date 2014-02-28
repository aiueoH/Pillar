package com.pillar;

import org.andengine.entity.sprite.TiledSprite;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class PillarBrick extends Brick {
	
	public PillarBrick(final float x, final float y,
			final VertexBufferObjectManager vertexBufferObjectManager,
			final IBrickOwner brickOwner, final int part) {
		// brick
		super(x, y, TPacker.getInstance().getTTR(TPacker.TKey.PB), vertexBufferObjectManager, brickOwner);
		// character
		TiledSprite c = new TiledSprite(getWidth() / 2, getHeight() / 2, TPacker.getInstance().getTTR(TPacker.TKey.C_G_FISH), vertexBufferObjectManager);
		c.setCurrentTileIndex(part);
		attachChild(c);
		// border
		TiledSprite b = new TiledSprite(getWidth() / 2, getHeight() / 2, TPacker.getInstance().getTTR(TPacker.TKey.PB_BORDER), vertexBufferObjectManager);
		attachChild(b);
	}

}
