package slideshow.slides.Items;

import java.awt.Color;
import java.util.ArrayList;

public class Slide {

	public Color bg_color;
	public float aspect_ratio;
	
	public ArrayList<SlideItem> items;
	
	public Slide() {
		items = new ArrayList<SlideItem>();
		aspect_ratio = 16.f / 9.f;
		bg_color = Color.WHITE.darker();
//		aspect_ratio = 4.f / 3.f;
	}
	
	public Slide(float aspect) {
		items = new ArrayList<SlideItem>();
		bg_color = Color.WHITE.darker();
		aspect_ratio = aspect;
	}
	
}
