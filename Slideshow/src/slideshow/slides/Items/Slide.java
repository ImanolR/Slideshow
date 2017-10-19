package slideshow.slides.Items;
import java.awt.Color;
import java.util.ArrayList;

public class Slide {

	public ArrayList<SlideItem> items;
	
	public Color background_color;
	
	//SlideTransition transition; // Fade in method

	public Slide() {
		items = new ArrayList<SlideItem>();
		background_color = new Color(100, 100, 100, 255); // Default color
	}
	
	public void addItem(SlideItem item) {
		items.add(item);
	}

	public void setBackgroundColor(Color color) {
		background_color = color;
	}
	
	public void update() {
		
	}
	
	public String toString() {
		for (SlideItem item: items) {
			if (item instanceof SlideText) return ((SlideText)item).text;
		}
		return ((Object) this).toString();
	}
}
