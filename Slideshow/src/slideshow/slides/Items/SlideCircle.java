package slideshow.slides.Items;

import java.awt.Color;

public class SlideCircle extends SlidePolygon {
	
	public SlideCircle(float x, float y, float w, float h, Color c, Color out) {
		super(x, y, w, h, c, out);
		type = CIRCLE;
	}

	public SlideCircle(float x, float y, float w, float h) {
		super(x, y, w, h, null, null);
		
		type = CIRCLE;
	}

	@Override
	public V2[] getVerteces() {
		assert false : "This should never be called";
		return null;
	}
}