package slideshow.slides.Items;

import java.awt.Color;

public class SlideLine extends SlideItem {
	
	public float thickness;
	
	public SlideLine() {
		fill_color = new Color(0, 0, 0, 0);
		outline    = new Color(0, 0, 0, 0);
	}
	
	public SlideLine(float x, float y, float w, float h, Color fill, Color out) {
		super(x, y, w, h, LINE);
		fill_color = fill;
		outline    = out;
		
		if (fill_color == null) fill_color = new Color(0, 0, 0, 0);
		if (outline == null)    outline    = new Color(0, 0, 0, 0);
	}

	public SlideLine(float x, float y, float w, float h) {
		// #CopyPaste
		super(x, y, w, h, LINE);
		
		if (fill_color == null) fill_color = new Color(0, 0, 0, 0);
		if (outline == null)    outline    = new Color(0, 0, 0, 0);
	}
}
