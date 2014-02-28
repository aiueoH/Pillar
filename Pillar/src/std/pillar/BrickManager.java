///////////////////////////////////////////////////////////////////////////////
// 新增, 移除, 移動 brick
// 處理 brick 事件
// 發生 pillar 完成事件
///////////////////////////////////////////////////////////////////////////////

package std.pillar;

import java.util.ArrayList;

import org.andengine.engine.Engine;
import org.andengine.entity.scene.Scene;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.math.MathUtils;

import std.pillar.brick.Brick;
import std.pillar.brick.IBrickOwner;
import std.pillar.brick.NoiseBrick;
import std.pillar.brick.PillarBrick;

public class BrickManager implements IBrickOwner {

	private Scene _scene;
	private VertexBufferObjectManager _vertexBufferObjectManager;
	private IBrickManagerOwner _brickManagerOwner;
	private ArrayList<Brick> _bricks;
	
	private int _pillarNum;
	private int _touchedCount;
	private int _focus;
	
	public BrickManager(final Scene scene, final VertexBufferObjectManager vertexBufferObjectManager, final IBrickManagerOwner brickManagerOwner) {
		_scene = scene;
		_vertexBufferObjectManager = vertexBufferObjectManager;
		_brickManagerOwner = brickManagerOwner;
		
		_bricks = new ArrayList<Brick>();
		_touchedCount = 0;
		
		_pillarNum = 17;
	}
	
	public void dispose() {
		for (Brick brick : _bricks)
			deleteBrick(brick);
		_bricks = null;
		_brickManagerOwner = null;
		_vertexBufferObjectManager = null;
		_scene = null;
	}
	
	public int getFocusIndex() {
		return _focus;
	}
	
	public Brick getFocusBrick() {
		return _bricks.get(_focus);
	}
	
	public int getLinkedCount() {
		int count = 0;
		for (Brick brick : _bricks)
			if (brick instanceof PillarBrick)
				count++;
		return count;
	}
	
	public void focusNext() {
		if (_focus < _bricks.size() - 5 && _focus < _pillarNum - 8) {
			_focus += 5;
		}
	}
	
	public void init() {
		float x = 480f / 2f, y;
		Brick brick;
		for (int i = 0, count = 0; count < _pillarNum; i++) {
			if (MathUtils.random(0, 5) != 0) {
				brick = new PillarBrick(x, 0, _vertexBufferObjectManager, this, _pillarNum - count - 1);
				count++;
			} else
				brick = new NoiseBrick(x, 0, _vertexBufferObjectManager, this);
			y = (i + 0.5f) * brick.getHeight();
			brick.setY(y);
			brick.setDstY(y);
			addBrick(brick);
		}
		_focus = 0;
	}
	
	private void addBrick(final Brick brick) {
		_scene.attachChild(brick);
		_scene.registerTouchArea(brick);
		_bricks.add(brick);
	}
	
	private void deleteBrick(final Brick brick) {
		GameActivity.ENGINE.runOnUpdateThread(new Runnable() {
			@Override
			public void run() {
				_scene.unregisterTouchArea(brick);
				_scene.detachChild(brick);
				brick.dispose();
			}
		});
		_bricks.remove(brick);
	}
	
	private void moveDownBricks(final int begine) {
		for (int i = begine; i < _bricks.size(); i++)
			_bricks.get(i).moveDown();
	}

	@Override
	public boolean isBrickTouchable(Brick brick) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void onBrickTouchedUp(Brick brick) {
		_touchedCount++;
	}

	@Override
	public void onBrickMovedOut(Brick brick) {
		int i = _bricks.indexOf(brick);
		deleteBrick(brick);
		moveDownBricks(i);
	}

	@Override
	public void onBrickMovedDown(Brick brick) {
		if (_touchedCount > 0) {
			_touchedCount--;
			if (_touchedCount == 0) {
				// 檢查 brick 鏈結
				{
					int count = 0;
					for (int i = _focus; i < _focus + 5 && i < _bricks.size(); i++)
						if ((_bricks.get(i) instanceof PillarBrick) && !_bricks.get(i).getIsMovingOut())
							count++;
					if (count >= 5)
						_brickManagerOwner.onBrickLinked(this);
				}
				// 檢查完成柱子
				{
					boolean isComplete = true;
					for (int i = _focus; i < _bricks.size(); i++)
						if (!(_bricks.get(i) instanceof PillarBrick) || _bricks.get(i).getIsMovingOut()) {
							isComplete = false;
							break;
						}
					if (isComplete)
						_brickManagerOwner.onCompleted(this);
				}
			}
		}
	}

}
