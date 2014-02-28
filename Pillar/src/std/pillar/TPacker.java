package std.pillar;

import java.util.HashMap;
import java.util.Map;

import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.BaseGameActivity;

public class TPacker {
	
	public enum TKey {
		PB,
		PB_BORDER,
		NB,
		TITLE,
		START,
		C_G_FISH,
	};
	
	private static TPacker _instance;
	private Map<TKey, TiledTextureRegion> _textures;
	
	private BaseGameActivity _activity; 
	
	private TPacker() {
		_textures = new HashMap<TKey, TiledTextureRegion>();
	}
	
	public static TPacker getInstance() {
		if (_instance == null)
			synchronized (TPacker.class) {
				if (_instance == null)
					_instance = new TPacker();
			}
		return _instance;
	}
	
	public void load(final BaseGameActivity baseGameActivity) {
		_activity = baseGameActivity;
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		
		load(TKey.PB, "pb.png", 300, 100, 0, 0, 1, 1);
		load(TKey.PB_BORDER, "pbb.png", 300, 100, 0, 0, 1, 1);
		load(TKey.NB, "nb.png", 300, 100, 0, 0, 1, 1);

		load(TKey.TITLE, "title.png", 400, 100, 0, 0, 1, 1);
		load(TKey.START, "start.png", 200, 50, 0, 0, 1, 1);
		
		load(TKey.C_G_FISH, "fish_g.png", 300, 1700, 0, 0, 1, 17);
	}
	
	private void load(final TKey key, final String file, final int w, final int h, final int x, final int y, final int c, final int r) {
		BitmapTextureAtlas bta = new BitmapTextureAtlas(_activity.getTextureManager(), w, h);
		TiledTextureRegion ttr = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(bta, _activity, file, x, y, c, r);
		bta.load();
		_textures.put(key, ttr);
	}
	
	public TiledTextureRegion getTTR(final TKey key) {
		return _textures.get(key);
	}

}
