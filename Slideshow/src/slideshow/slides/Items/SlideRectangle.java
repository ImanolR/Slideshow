package slideshow.slides.Items;

import java.awt.Color;

public class SlideRectangle extends SlideItem {

	
	public Color color;
	
	public SlideRectangle() {
		super();
		color = Color.BLACK;
	}
	
	public SlideRectangle(float x, float y, float w, float h) {
		super(x, y, w, h);
		color = Color.BLACK;
	}
	
	public void setColor(Color c) {
		color = c;
	}
	
	@Override
	public void update(double dt) {
		
	}

	public String toString() {
		return "Rectangle ("+x+", "+y+", "+w+", "+h+")";
	}
}
