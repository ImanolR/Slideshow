package slideshow.viewer;

import java.awt.*;

import slideshow.slides.Items.*;

public class Renderer {
	public static String colorToHex(Color c) {
		// #NOTE: Java saves colors as RGBA
		String res = "";
	
		if (c != null) 
			res += toHex(c.getRed()) +
				toHex(c.getBlue()) +
				toHex(c.getGreen()) +
				toHex(c.getAlpha());
		
		return res;
	}
	
	/** Returns the number as a 2 digit long hex number
	 * 
	 * @param n Integer number
	 * @return 
	 */
	public static String toHex(int n) {
		String res = "";
		if (n > 0xf) {
			res += Integer.toHexString(n); // Gives two digits
		}
		else {
			res += "0" + Integer.toHexString(n);
		}
		return res;
	}
	
	/** Wraper function
	 * 
	 * @param hex String of a number in hexadecimal notation
	 * @return
	 */
	public static Color hexToColor(String hex) {
		
		return new Color(Integer.parseInt(hex, 16));
	}

	/** Paints the slide in the buffer maintaining the aspect ratio of the slide
	 *  drawing some margins when needed. It uses a back buffer
	 * 
	 * @param slide Slide to paint
	 * @param outCanvas Component to paint the 
	 */
	public static void render(Slide slide, Component outCanvas) {
		int width  = outCanvas.getWidth();
		int height = outCanvas.getHeight();
		
		// If the window height gets too small it crashes
		if (height < 1 || width < 1) return;
		
		Image back_buffer = outCanvas.createImage(width, height);

		Graphics cg = back_buffer.getGraphics();
		
		// Clear canvas
		{ // #EFICIENCY: Only do this if the canvas dimensions have change
			cg.setColor(new Color(50, 50, 50, 255)); // Default back fill color
			cg.fillRect(0, 0, width, height);
		}

		final float target_ratio = slide.aspect_ratio;
				
		Dimension dim = correctAspectRatio(target_ratio, width, height);				
		if (dim.height < 1 || dim.width < 1) return;
		// Center the slide in the window
		// Size of the margins
		int x_diff = (width  - dim.width)  / 2;
		int y_diff = (height - dim.height) / 2;

		
		renderSlide(slide, back_buffer, x_diff, y_diff, dim.width, dim.height);
				
				
				
		
		outCanvas.getGraphics().drawImage(back_buffer, 0, 0, null);
	}
	
	
	/**Paints ONLY the slide, in the given buffer
	 * 
	 * @param slide Slide to paint
	 * @param outBuffer Image object to paint to
	 * @param offset_x X offset of the window in pixels
	 * @param offset_y Y offset of the window in pixels
	 * @param width Width of the slide in pixels
	 * @param height Height of the slide in pixels
	 */
	public static void renderSlide(Slide slide, Image outBuffer, int offset_x, int offset_y, int width, int height) {
		Graphics2D g = (Graphics2D) outBuffer.getGraphics();
		
		// Draw background
		g.setColor(slide.bg_color);
		g.fillRect(offset_x, offset_y, width, height);
		g.setRenderingHint(
		        RenderingHints.KEY_TEXT_ANTIALIASING,
		        RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
		g.setRenderingHint(
		        RenderingHints.KEY_INTERPOLATION,
		        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		
		for (SlideItem item : slide.items) {
			Rectangle dim = getBounds(item, width, height);
			
			// #IMPORTANT(Imanol): there MUST be a case for every type of SlideItem children
			// #IMPORTANT(Imanol): there MUST be a case for every type of SlideItem children
			// #IMPORTANT(Imanol): there MUST be a case for every type of SlideItem children

			if (item instanceof SlideText) {
				
				SlideText text_item = (SlideText) item;
				
				g.setColor(text_item.fill_color);
				
				// Calculate font size in pt
				float size_px = height * text_item.h;
				int size_pt = roundToInt(size_px * SlideText.PX_TO_PT);
				
				Font font = new Font(text_item.font_name, text_item.font_style.style, size_pt);
				g.setFont(font);

				
				transform(g,
						dim.x + offset_x,
						dim.y + offset_y,
						text_item.rotation);
				
				int string_w = g.getFontMetrics(font).stringWidth(text_item.text);
				int displacement = 0;
				switch (text_item.alignment) {
				case CENTER:
					displacement = -string_w/2;
					break;
				case LEFT:
					displacement = -dim.width/2;
					break;
				case RIGHT:
					displacement = -string_w + dim.width/2;
				}
				
				g.drawString(text_item.text, displacement, dim.height/2-g.getFontMetrics(font).getDescent());
				if (text_item.underlined) g.fillRect(displacement, -g.getFontMetrics(font).getDescent(), string_w, dim.height/12); // Coordinates are the same as the baseline
				
				// #TODO: Use the outline color for something. Shadow, highlight...
				
				detransform(g,
						dim.x + offset_x,
						dim.y + offset_y,
						text_item.rotation);
			}
			else if (item instanceof SlideImage) {
				SlideImage image_item = (SlideImage) item;
				transform(g,
						dim.x + offset_x,
						dim.y + offset_y,
						image_item.rotation);
				if (image_item.image != null) {					
					g.drawImage(image_item.image, -dim.width/2,	-dim.height/2, dim.width, dim.height, null);
				}
				else {
					// #TODO: Temporarily paint a black square?
				}
				detransform(g,
						dim.x + offset_x,
						dim.y + offset_y,
						image_item.rotation);
			}
			else if (item instanceof SlideRect) {
				SlideRect rect_item = (SlideRect) item;
				g.setColor(rect_item.fill_color);
				
				transform(g,
						dim.x + offset_x,
						dim.y + offset_y,
						rect_item.rotation);
				
				g.fillRect(-dim.width/2, -dim.height/2, dim.width, dim.height);
				g.setColor(rect_item.outline);
				g.drawRect(-dim.width/2, -dim.height/2, dim.width, dim.height);
				
				detransform(g,
						dim.x + offset_x,
						dim.y + offset_y,
						rect_item.rotation);
			}
			
			// SlidePolygon children
			else if (item instanceof SlideCircle) {
				SlideCircle cir_item = (SlideCircle) item;
				g.setColor(cir_item.fill_color);
				
				transform(g,
						dim.x + offset_x,
						dim.y + offset_y,
						cir_item.rotation);
				
				g.fillOval(-dim.width/2, -dim.height/2, dim.width, dim.height);
				g.setColor(cir_item.outline);
				g.drawOval(-dim.width/2, -dim.height/2, dim.width, dim.height);
				
				detransform(g,
						dim.x + offset_x,
						dim.y + offset_y,
						cir_item.rotation);
			}
			else if (item instanceof SlidePolygon) {
				// #TODO #IDEA: Draw an outline of the shape
				SlidePolygon poly_item = (SlidePolygon) item;
				
				V2[] verteces = poly_item.getVerteces();
				int   nPoints = verteces.length;
				int[] xPoints = new int[nPoints];
				int[] yPoints = new int[nPoints];
				for (int i = 0; i < nPoints; i++) {
					// #NOTE: Coordinates in [-W/2..0..W/2] range
					// #NOTE: Coordinates in [-H/2..0..H/2] range
					// Because we want rotation to pivot the center of the shape
					V2 v = verteces[i];
					xPoints[i] = -dim.width/2  + (int)((float)dim.width * v.x);
					yPoints[i] = -dim.height/2 + (int)((float)dim.height * v.y);
				}
				g.setColor(poly_item.fill_color);
				
				transform(g,
						dim.x + offset_x,
						dim.y + offset_y,
						poly_item.rotation);
				g.fillPolygon(xPoints, yPoints, nPoints);
				g.setColor(poly_item.outline);
				g.drawPolygon(xPoints, yPoints, nPoints);
				
				detransform(g,
						dim.x + offset_x,
						dim.y + offset_y,
						poly_item.rotation);
			}
			
			else {
				System.out.println("Undefined routine for the item passed");
				assert false;
			}
			
		}
		
		
	}
	
	// #TODO: Create a class Transformation so that parameters are clear that must be the same in both (de)transform functions?
	
	/** Translates and then rotates the Graphics object, in that order
	 * 
	 * @param g Graphcis2D object
	 * @param x Displacement on X axis
	 * @param y Displacement on Y axis
	 * @param rot Rotation in radians
	 */
	private static void transform(Graphics2D g, int x, int y, float rot) {
		g.translate(x, y);
		g.rotate(rot);
	}
	/** Rotates and the translates the Graphics object with the negative values of the ones passed.
	 * Typically should have exactly the same arguments as transform does.
	 * 
	 * @param g
	 * @param x
	 * @param y
	 * @param rot
	 */
	private static void detransform(Graphics2D g, int x, int y, float rot) {
		g.rotate(-rot);
		g.translate(-x, -y);
	}
	
//	/** Draws a rectangle outline and the resize handle on the given pixel coordinates
//	 * 
//	 * @param x X coordinate. Left edge position. In pixels
//	 * @param y Y coordinate. Top edge position. In pixels
//	 * @param w Width. In pixels
//	 * @param h Height. In pixels
//	 * @param g Graphics object to paint the rectangle
//	 */
//	@Deprecated public static void drawSelectionRectangle(Rectangle rect, Graphics g) {
//		// #TODO: Add color, ... and other parameters
//		//#TODO: Add center cross
//		
//		g.setColor(Color.RED);
//		g.drawRect(rect.x, rect.y, rect.width, rect.height);
//		g.drawRect(rect.x+rect.width, rect.y+rect.height, 10, 10); // #HARDCODED: Must be the same parameters as in editor resize handle
//		
//	}
	
//	/** Draws a rectangle outline and the resize handle on the given pixel coordinates
//	 * 
//	 * @param aspectR float aspect ratio
//	 * @param item item selected
//	 * @param g Graphcis to which to paint
//	 * @param canvas_width Canvas width
//	 * @param canvas_height Canvas height
//	 */
//	public static void drawSelectionRectangle(float aspectR, SlideItem item, Graphics g, int canvas_width, int canvas_height) {
//		// #TODO #ROBUSTNESS: We should use a back buffer or something as currently the changing g's color sometimes affects other threads
//		g.setColor(Color.RED.darker());
//		Dimension slide_dim = correctAspectRatio(aspectR, canvas_width, canvas_height);
//		Rectangle rect = getBounds(item, slide_dim.width, slide_dim.height);
//		
//		rect.translate((canvas_width  - slide_dim.width) / 2,
//				       (canvas_height - slide_dim.height)/ 2);
//		
//		transform((Graphics2D)g,
//				rect.x,
//				rect.y,
//				item.rotation);
//		
//		g.drawRect(-rect.width/2, -rect.height/2, rect.width, rect.height); // Main selection outline
//		g.drawRect(rect.width/2, rect.height/2, 10, 10); // Handle
//		g.drawLine(-4, -4, 4, 4); // Item center cross
//		g.drawLine(-4, 4, 4, -4); // Item center cross
//		
//		detransform((Graphics2D)g,
//				rect.x+rect.width/2,
//				rect.y+rect.height/2,
//				item.rotation);
//	}
	
	/** Returns the dimensions for a correct aspect ratio with the bigeest area possible.
	 *  The result is always smaller or equal to the parameters passed. In pixels
	 */
	public static Dimension correctAspectRatio(float target_ratio, int width, int height) {
		Dimension result = new Dimension(width, height);
		
		float aspect_ratio = (float) width / (float) height;
		
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
		assert result.width <= width && result.height <= height: "Some result is bigger than the parameter passed";
		
		return result;
	}
	
	private static int roundToInt(float n) {
		return (int) (n + .5);
	}
	
	/** Calculates the bounds of the slide item in pixels for the given slide size. Not necesarilly the components size.
	 *  It does not take into account the possible margins to match the aspect ratio
	 * 
	 * @param item SlideItem which bounds to calculate
	 * @param slide_width Width of the slide in pixels. Not the component width
	 * @param slide_height Width of the slide in pixels. Not the component width
	 * @return Rectangle of the space occupyed by the item in the slide
	 */
	public static Rectangle getBounds(SlideItem item, int slide_width, int slide_height) {
		int X, Y, W, H;
		
		X = (int) (slide_width  * item.x);
		Y = (int) (slide_height * item.y);
		W = (int) (slide_width  * item.w);
		H = (int) (slide_height * item.h);
		return new Rectangle(X, Y, W, H);
	}
	
	/** Transforms a Point to the corresponding slide (0..1 coordinate system)
	 * 
	 * @param p Point passed
	 * @param slide Slide to which coordinates system transform the Point
	 * @param c The Component in which the slide is rendered
	 * @return Returns a V2 ( 2d float vector ) of the point after translation
	 */
	public static V2 toSlideCoordSys(Point p, Slide slide, Component c) {
		
		int width  = c.getWidth();
		int height = c.getHeight();
		
		final float target_ratio = slide.aspect_ratio;
		
		Dimension slide_dim = correctAspectRatio(target_ratio, width, height);
		
		int x_diff = (width  - slide_dim.width)  / 2;
		int y_diff = (height - slide_dim.height) / 2;
		
		float x = (float)(p.x - x_diff) / (float)slide_dim.width;
		float y = (float)(p.y - y_diff) / (float)slide_dim.height;
		
		return new V2(x, y);
	}
	
}
