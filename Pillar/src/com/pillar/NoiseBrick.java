package com.pillar;

import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

public class NoiseBrick extends Brick {

	public static TiledTextureRegion TILEDTEXTURE;
	
	public NoiseBrick(float x, float y,
			VertexBufferObjectManager vertexBufferObjectManager,
			IBrickOwner brickOwner) {
		super(x, y, TILEDTEXTURE, vertexBufferObjectManager, brickOwner);
	}
}