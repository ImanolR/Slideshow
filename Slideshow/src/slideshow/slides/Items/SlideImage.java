package slideshow.slides.Items;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class SlideImage extends SlideItem {

	public File img_file;
	public BufferedImage image;
	
	public SlideImage(File file) {
		img_file = file;
		if (img_file == null) return;
		try {
			image = ImageIO.read(img_file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		type = IMAGE;
	}

//	@Override
//	public float getMinW() {
//		return 0.02f;
//	}
//
//	@Override
//	public float getMinH() {
//		return 0.02f;
//	}

}
