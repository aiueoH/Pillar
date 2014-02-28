package std.pillar.stage;

public interface IStageSwitcher {
	public enum StageEnum {
		HOME,
		PILLAR,
	}
	public void switchTo(Stage sender, StageEnum stage, int arg);
}
