package std.pillar;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

public class BarTimer extends Rectangle {

	
	private float _x, _y, _width, _height, _borderWidth;
	private float _duration, _time;
	private float _interval;
	private Color _barColor, _borderColor;
	
	private boolean _isPause;
	
	private Line[] _borderLines = new Line[4];
	private Rectangle _bar;
	private TimerHandler _timerHandler;
	private IBarTimerOwner _barTimerOwner;
	
	public BarTimer(final float x, final float y, final float width, final float height, 
			final float borderWidth, 
			final Color barColor, final Color borderColor, 
			final float duration,
			final float interval,
			final VertexBufferObjectManager vertexBufferObjectManager, 
			final IBarTimerOwner barTimerOwner) {
		super(x, y, width, height, vertexBufferObjectManager);
		_isPause = false;
		this.setColor(Color.TRANSPARENT);
		_x = x;
		_y = y;
		_width = width;
		_height = height;
		_borderWidth = borderWidth;
		_barColor = barColor;
		_borderColor = borderColor;
		_time = _duration = duration;
		_interval = interval;
		_barTimerOwner = barTimerOwner;
		_bar = new Rectangle(_x, _y, _width, _height, vertexBufferObjectManager);
		_bar.setColor(_barColor);
		attachChild(_bar);
		{
			float cX[] = {_x - _width / 2, _x + _width / 2};
			float cY[] = {_y + _height / 2, _y - _height / 2};
			_borderLines[0] = new Line(cX[0], cY[0], cX[1], cY[0], _borderWidth, vertexBufferObjectManager);
			_borderLines[1] = new Line(cX[1], cY[0], cX[1], cY[1], _borderWidth, vertexBufferObjectManager);
			_borderLines[2] = new Line(cX[1], cY[1], cX[0], cY[1], _borderWidth, vertexBufferObjectManager);
			_borderLines[3] = new Line(cX[0], cY[1], cX[0], cY[0], _borderWidth, vertexBufferObjectManager);
			for (Line line : _borderLines) {
				line.setColor(_borderColor);
				attachChild(line);
			}
		}
		_timerHandler = new TimerHandler(_interval, true, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler timerHandler) {
				if (!_isPause) {
					if (_time > 0) {
						_time -= _interval;
						float p = _time / _duration;
						_bar.setHeight(_height * p);
						_bar.setY(_y - _height / 2 + _bar.getHeight() / 2);
					} else {
						timerHandler.setAutoReset(false);
						_barTimerOwner.onTimesUp(timerHandler);
					}
				}
			}
		});
	}
	
	public void pause() {
		_isPause = true;
	}
	
	public void setPercent(final float n) {
		_bar.setY(_x + (1f - n) * _height);
		_bar.setHeight(n * _height);
	}

	public TimerHandler getTimerHandler() {
		return _timerHandler;
	}
	
}
