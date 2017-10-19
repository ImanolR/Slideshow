package slideshow.slides.Items;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SlideImage extends SlideItem {

	public BufferedImage image;
	public File file; // We save the path of the file so that we can save and retrive the image after
	                  // We can also load the image when needed instead of having it in memory without using it
	
	public SlideImage() {
		image = null;
		file  = null;
	}
	
	public SlideImage(File img) {
		super();
		file = img;
		loadImage(img);
	}
	
	public SlideImage(float x, float y, File img) {
		super(x, y);
		file = img;
		loadImage(img);
	}
	
	public SlideImage(float x, float y, float w, float h, File img) {
		super(x, y, w, h);
		
		file = img;
		loadImage(img);
	}
	
	public void loadImage(File img) {
		try {
			image = ImageIO.read(img);
		} catch (IOException e) {
			System.out.println("ERROR: could not load image" + img);
		}
	}
	
	@Override
	public void update(double dt) {
		
	}

	public String toString() {
		return "Image \""+file.getName()+"\"";
	}
}
