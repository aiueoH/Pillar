package com.pillar;

import java.util.ArrayList;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.color.Color;

import android.R.integer;
import android.util.Log;

public class BarTimer extends Rectangle {

	
	private float _x, _y, _width, _height;
	private float _duration, _time;
	private float _interval;
	
	private boolean _isPause;
	
	private Line[] _borderLines = new Line[4];
	private Rectangle _bar;
	private TimerHandler _timerHandler;
	private IBarTimerOwner _barTimerOwner;
	
	public BarTimer(float x, float y, float width, float height, 
			float boderWidth, 
			Color barColor, Color borderColor, 
			float duration,
			float interval,
			VertexBufferObjectManager vertexBufferObjectManager, 
			IBarTimerOwner barTimerOwner) {
		super(x, y, width, height, vertexBufferObjectManager);
		_isPause = false;
		this.setColor(Color.TRANSPARENT);
		_x = x;
		_y = y;
		_width = width;
		_height = height;
		_time = _duration = duration;
		_interval = interval;
		_barTimerOwner = barTimerOwner;
		_bar = new Rectangle(x, y, width, height, vertexBufferObjectManager);
		_bar.setColor(barColor);
		this.attachChild(_bar);
		{
			float cX[] = {x - width / 2, x + width / 2};
			float cY[] = {y + height / 2, y - height / 2};
			_borderLines[0] = new Line(cX[0], cY[0], cX[1], cY[0], boderWidth, vertexBufferObjectManager);
			_borderLines[1] = new Line(cX[1], cY[0], cX[1], cY[1], boderWidth, vertexBufferObjectManager);
			_borderLines[2] = new Line(cX[1], cY[1], cX[0], cY[1], boderWidth, vertexBufferObjectManager);
			_borderLines[3] = new Line(cX[0], cY[1], cX[0], cY[0], boderWidth, vertexBufferObjectManager);
		}
		for (Line line : _borderLines) {
			line.setColor(borderColor);
			this.attachChild(line);
		}
		final BarTimer thisBarTimer = this;
		_timerHandler = new TimerHandler(interval, true, new ITimerCallback() {
			@Override
			public void onTimePassed(TimerHandler timerHandler) {
				if (!_isPause) {
					if (thisBarTimer._time > 0) {
						thisBarTimer._time -= thisBarTimer._interval;
						float p = thisBarTimer._time / thisBarTimer._duration;
						thisBarTimer._bar.setHeight(thisBarTimer._height * p);
						thisBarTimer._bar.setY(thisBarTimer._y - thisBarTimer._height / 2 + thisBarTimer._bar.getHeight() / 2);
					} else {
						timerHandler.setAutoReset(false);
						thisBarTimer._barTimerOwner.onTimesUp(timerHandler);
					}
				}
			}
		});
	}
	
	public void pause() {
		_isPause = true;
	}
	
	public void setPercent(float n) {
		_bar.setY(_x + (1f - n) * _height);
		_bar.setHeight(n * _height);
	}

	public TimerHandler getTimerHandler() {
		return _timerHandler;
	}
	
}