package slideshow.slides.Items;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class SlideText extends SlideItem {

	public static final float PT_TO_PX = 1.33333F;
	public static final float PX_TO_PT = 0.75F;
	
	public static enum Alignment {
		LEFT,
		CENTER,
		RIGHT
	}
	
	public static enum FontStyle{
		PLAIN     (Font.PLAIN),
		BOLD      (Font.BOLD),
		ITALIC    (Font.ITALIC),
		BOLD_ITALIC (Font.BOLD | Font.ITALIC);
		
		public final int style;
		
		FontStyle(int style) {
			this.style = style;
		}
	}
	
	public String text;
	
	// #NOTE(Imanol): as the size of the font can vary in relation to the window size
	// we cannot keep a reference to the font. So we have to keep track of the data that
	// defines the font we will need to fetch
	// public Font font;
	public String font_name;
	public FontStyle font_style;
	
	public Alignment alignment;
	
/*  INHERITED:
	Color fill_color; // This could be the background/highlight color?
	Color outline; // This is the text color
*/
	public boolean underlined;
	public boolean double_underline;
	public boolean crossed;
	
	public SlideText(String t) {
		super();
		text = t;
		alignment = Alignment.CENTER;
		font_name = "Arial";
		font_style = FontStyle.PLAIN;
		h = .2f;
		outline = Color.BLACK;
		fill_color = new Color(0,0,0,255);
		type = TEXT;
	}
	
	public SlideText(String t, float x, float y) {
		super(x, y);
		text = t;
		alignment = Alignment.CENTER;
		font_name = "Arial";
		font_style = FontStyle.PLAIN;
		h = .2f;
		outline = Color.BLACK;
		fill_color = new Color(0,0,0,255);
		type = TEXT;
	}

//	@Override
//	public float getMinW() {
//		return 0.02f;
//	}
//
//	@Override
//	public float getMinH() {
//		return 0.008f;
//	}
	
}
