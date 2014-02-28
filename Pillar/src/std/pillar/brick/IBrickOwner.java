package std.pillar.brick;


public interface IBrickOwner {
	public boolean isBrickTouchable(final Brick brick);
	public void onBrickTouchedUp(final Brick brick);
	public void onBrickMovedOut(final Brick brick);
	public void onBrickMovedDown(final Brick brick);
}
