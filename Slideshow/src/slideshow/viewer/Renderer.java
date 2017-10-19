package slideshow.viewer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;

import slideshow.slides.Items.Slide;
import slideshow.slides.Items.SlideImage;
import slideshow.slides.Items.SlideItem;
import slideshow.slides.Items.SlideRectangle;
import slideshow.slides.Items.SlideText;

public class Renderer {

	// Flags
	public static int CURSOR_VISIBLE = 0x1;
	public static int SELECTED_RECT  = 0X2;
	
	
	private static boolean cursor_visible = false; // @Hardcoded
	
	// options or flags? which name?
	public static void renderSlideCentered(Slide slide, Graphics cg, int width, int height, int options) {
		// Clear canvas
		{ //@Todo: only do this if the canvas dimensions have change
			cg.setColor(new Color(50, 50, 50, 255)); // Default back fill color
			cg.fillRect(0, 0, width, height);
		}

		// If the window height gets too small it crashes
		if (height < 1 || width < 1) return;
		
		// @Incomplete: we may want to define the aspect ratio in the future
		final float target_ratio = 16.f / 9.f;
		
		
		Dimension dim = correctAspectRatio(target_ratio, width, height);
		
		final Image slide_buffer = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_4BYTE_ABGR);
		renderSlide(slide, slide_buffer, dim.width, dim.height, options);
		
		// Center the slide in the window
		// Size of the margins
		int x_diff = (width  - dim.width)  / 2;
		int y_diff = (height - dim.height) / 2;
		
		
		
		cg.drawImage(slide_buffer, x_diff, y_diff, null);
	}

	
	public static void render(Slide slide, JComponent canvas, int options) {
		Image back_buffer = canvas.createImage(canvas.getWidth(), canvas.getHeight());
		renderSlideCentered(slide, canvas.getGraphics(), canvas.getWidth(), canvas.getHeight(), options);
		
		// Draw cursor
		if ((options & CURSOR_VISIBLE) != 0) {
			Point cursor_pos = canvas.getMousePosition();
			if (cursor_pos != null) {
				final float cursor_radius = .02f;
				int radius_px = roundToInt(canvas.getHeight() * cursor_radius);
				back_buffer.getGraphics().setColor(Color.WHITE);
				back_buffer.getGraphics().fillOval(cursor_pos.x - radius_px, cursor_pos.y - radius_px,
						   2 * radius_px, 2 * radius_px);
			}
		}
		canvas.getGraphics().drawImage(back_buffer, 0, 0, null);

	}
	
	
	// Paints the slide in the given buffer
	public static void renderSlide(Slide slide, Image buffer, int width, int height, int options) {
		Graphics2D g = (Graphics2D) buffer.getGraphics();
		
		
		// Draw background
		g.setColor(slide.background_color);
		g.fillRect(0, 0, buffer.getWidth(null), height);
		g.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g.setRenderingHint(
		        RenderingHints.KEY_INTERPOLATION,
		        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		for (SlideItem item : slide.items) {
			int X, Y, W, H;
			X = Y = W = H = 0;
			
			// @Note @Important: there MUST be a case for every type of SlideItem children
			// @Note @Important: there MUST be a case for every type of SlideItem children
			// @Note @Important: there MUST be a case for every type of SlideItem children

			if (item instanceof SlideText) {
				
				SlideText text_item = (SlideText) item;
				
				g.setColor(text_item.color);
				
				// Calculate font size in pt
				float size_px = height * text_item.font_size;
				H = (int) size_px; // @Note: the rounding might be wrong
				int size_pt = roundToInt(size_px * SlideText.PX_TO_PT);
				
				Font font = new Font(text_item.font_name, text_item.font_style, size_pt);
				g.setFont(font);
				
				
				// Translate from [0, 1] range coords to absolute pixels
				
				X = (int) (width * text_item.x);
				W = g.getFontMetrics().stringWidth(text_item.text);

				// Alignment
				switch(text_item.just) {
				case LEFT:
					// Already calculated on X
					break;
				case CENTER:
					X -= W/2;
					break;
				case RIGHT:
					X -= W;
					break;
				default:
					assert false: "ERROR: The alingment of the text is not specified";// @Todo: remove this assert?
					X = (int) (width  * text_item.x);
				}
				
				Y = (int) (height * text_item.y);
				
				g.drawString(text_item.text, X, Y);
			}
			else if (item instanceof SlideImage) {
				SlideImage image_item = (SlideImage) item;
				X = (int) (width  * image_item.x);
				Y = (int) (height * image_item.y);
				
				W = (int) (width  * image_item.w);
				H = (int) (height * image_item.h);
				
				if (image_item.image != null) {
					// @Todo: change to g.drawImage(image_item.image, X, Y, W, H, null); ??
					g.drawImage(image_item.image, X, Y, null);
				}
				else {
					// @Todo: Temporarily paint a black square?
				}
			}
			else if (item instanceof SlideRectangle) {
				SlideRectangle rect_item = (SlideRectangle) item;
				X = (int) (width  * rect_item.x);
				Y = (int) (height * rect_item.y);
				W = (int) (width  * rect_item.w);
				H = (int) (height * rect_item.h);
				g.setColor(rect_item.color);
				
				g.fillRect(X, Y, W, H);
			}
			else {
				System.out.println("Undefined routine for the item passed");
			}
			
			if ((options & SELECTED_RECT) != 0) {
				// @Todo: choose a suitable color
				g.setColor(Color.BLACK);
				g.drawRect(X, Y, W, H);
			}
		}
		
		
	}
	
	
	
	/** Returns the dimensions for a correct aspect ratio with the bigeest area possible.
	 *  The result is always smaller or equal to the parameters passed
	 */
	private static Dimension correctAspectRatio(float target_ratio, int width, int height) {
		Dimension result = new Dimension(width, height);
		
		final float aspect_ratio = (float) width / (float) height;
		
		if (aspect_ratio > target_ratio) {
			// It is widther than taller so,
			// we have to proportion the width
			result.width = (int) ((float) height * target_ratio);
		}
		else {
			// It is taller than widther so,
			// we have to proportion the height
			result.height = (int) ((float) width / target_ratio);
		}
		assert Math.abs(target_ratio-((float)result.width / (float)result.height)) <= 0.01 : "The ratio has been miscalculated " + (result.width / result.height);
		
		return result;
	}
	
	private static int roundToInt(float n) {
		return (int) (n + .5);
	}
}

class RenderOptions {
	// Flags
	public static int CURSOR_VISIBLE = 0x1;
	public static int SELECTED_RECT  = 0X2;

	public int flags;
	
	public RenderOptions(int options) {
		flags = options;
	}
}
