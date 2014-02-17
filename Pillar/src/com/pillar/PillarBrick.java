package com.pillar;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

public class PillarBrick extends Brick {

	public static TiledTextureRegion TILEDTEXTURE;
	
	public PillarBrick(float x, float y,
			VertexBufferObjectManager vertexBufferObjectManager,
			IBrickOwner brickOwner) {
		super(x, y, TILEDTEXTURE, vertexBufferObjectManager, brickOwner);
	}

}