package slideshow.slides.Items;

import java.awt.Color;

public abstract class SlideItem {

	public static final int LINE      = 1;
	public static final int RECTANGLE = 2;
	public static final int IMAGE     = 3;
	public static final int TEXT      = 4;
	public static final int CIRCLE    = 5;
	public static final int POLYGON   = 6;

	public int type;
	
	public float x, y, w, h;
	public float rotation;
	public Color fill_color;// NOTE: Must be always defined
	public Color outline;// NOTE: Must be always defined
	
	
	public SlideItem() {
		x = 0.5f;
		y = 0.5f;
		w = .2f;
		h = .2f;
	}
	
	public SlideItem(float _x, float _y) {
		x = _x;
		y = _y;
		w = .2f;
		h = .2f;
	}
	
	public SlideItem(float _x, float _y,float _w,float _h, int _type) {
		x = _x;
		y = _y;
		w = _w;
		h = _h;
		type = _type;
	}
	
	public void update(double dt) {
		
	}
}
