package std.pillar;

import org.andengine.util.debug.Debug;

public class MyLog {

	private static String _tag = "Wei";
	private MyLog() {}
	
	public static void d(String s) {
		Debug.d(_tag, s);
	}
	
	public static void d(Exception e) {
		d(e.getMessage());
	}

}
