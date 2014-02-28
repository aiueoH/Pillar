package std.pillar.brick;

import org.andengine.opengl.vbo.VertexBufferObjectManager;

import std.pillar.TPacker;
import std.pillar.TPacker.TKey;

public class NoiseBrick extends Brick {
	
	public NoiseBrick(float x, float y,
			VertexBufferObjectManager vertexBufferObjectManager,
			IBrickOwner brickOwner) {
		super(x, y, TPacker.getInstance().getTTR(TPacker.TKey.NB), vertexBufferObjectManager, brickOwner);
	}
}