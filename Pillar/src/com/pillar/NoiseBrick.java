package com.pillar;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class NoiseBrick extends Brick {
	
	public NoiseBrick(float x, float y,
			VertexBufferObjectManager vertexBufferObjectManager,
			IBrickOwner brickOwner) {
		super(x, y, TPacker.getInstance().getTTR(TPacker.TKey.NB), vertexBufferObjectManager, brickOwner);
	}
}