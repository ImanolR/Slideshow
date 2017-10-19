package slideshow.slides.Items;

import java.util.ArrayList;

public class Slideshow {

	public String name;
	private ArrayList<Slide> slides;
	private int index;
	
	public Slideshow(String title) {
		name = title;
		slides = new ArrayList<Slide>();
		index = 0;
	}

	public Slide currentSlide() {
		return slides.get(index);
	}
	
	public void empty() {
		slides = new ArrayList<Slide>();
	}
	
	public void clampIndex() {
		if (index > slides.size())
			index = slides.size()-1;
	}

	public void addSlide(Slide slide) {
		slides.add(slide);
	}
	
	public void prev() {
		if (index > 0) {
			index--;
		}
	}
	
	public void next() {
		if (index < slides.size()-1) {
			index++;
		}
	}
	
	public int slideCount() {
		return slides.size();
	}
	
	public Slide getSlide(int index) {
		// @Todo: check range
		return slides.get(index);
	}
	
	public String toString() {
		return name;
	}
}
