package slideshow.slides.Items;

public abstract class SlideItem {

	public float x, y;
	public float w, h; // @Note: if negative auto size
	
	public SlideItem() {
		x = y = w = h = 0;
	}
	
	public SlideItem(float _x, float _y) {
		x = _x;
		y = _y;
		w = -1;
		h = -1;
	}
	
	public SlideItem(float _x, float _y, float _w, float _h) {
		x = _x;
		y = _y;
		w = _w;
		h = _h;
	}
	
	public abstract void update(double dt);
	
}
