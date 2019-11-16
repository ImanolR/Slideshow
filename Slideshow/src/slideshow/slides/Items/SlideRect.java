package slideshow.slides.Items;

import java.awt.Color;

public class SlideRect extends SlideItem {

	public SlideRect() {
		fill_color = new Color(0, 0, 0, 0);
		outline    = new Color(0, 0, 0, 0);
	}
	
	public SlideRect(float x, float y, float w, float h, Color fill, Color out) {
		super(x, y, w, h, RECTANGLE);
		fill_color = fill;
		outline    = out;
		
		if (fill_color == null) fill_color = new Color(0, 0, 0, 0);
		if (outline == null)    outline    = new Color(0, 0, 0, 0);
	}

	public SlideRect(float x, float y, float w, float h) {
		// #CopyPaste
		super(x, y, w, h, RECTANGLE);
		
		if (fill_color == null) fill_color = new Color(0, 0, 0, 0);
		if (outline == null)    outline    = new Color(0, 0, 0, 0);
	}

//	@Override
//	public float getMinW() {
//		return 0.01f;
//	}
//
//	@Override
//	public float getMinH() {
//		return 0.01f;
//	}
	
}
