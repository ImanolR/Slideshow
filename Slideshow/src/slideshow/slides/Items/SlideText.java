package slideshow.slides.Items;
import java.awt.Color;
import java.awt.Font;

public class SlideText extends SlideItem {

	public enum Justification {
		LEFT, CENTER, RIGHT
	}
	
	public String text;
	
	public String font_name;
	static String DEFAULT_FONT_NAME = "Arial";
	
	public int    font_style;
	static final int DEFAULT_STYLE = Font.PLAIN;
	
	public float  font_size; // fraction of the height of the window, [0, 1] range
	static final float DEFAULT_SIZE = 0.1f;
	
	public static final float PX_TO_PT = 1.3333f;
	public static final float PT_TO_PX = 0.75F;
	// @Note: font size 1 px = 0.75 pt
	//					1 pt = 1.33 px
	
	
	public Color color;
	static final Color DEFAULT_COLOR= new Color(0, 0, 0, 255);
	
	public Justification just;
	static final Justification DEFAULT_JUST = Justification.CENTER;
	
	public SlideText() {
		super(.25f, .25f);
		text = "";
		font_size = DEFAULT_SIZE;
		font_style = DEFAULT_STYLE;
		color = DEFAULT_COLOR;
		just = DEFAULT_JUST;
	}
	
	public SlideText(String t) {
		super(.25f, .25f);
		text = t;
		font_size = DEFAULT_SIZE;
		font_style = DEFAULT_STYLE;
		color = DEFAULT_COLOR;
		just = DEFAULT_JUST;
	}
	
	public SlideText(float x, float y, String t, float size) {
		super(x, y);
		text = t;
		font_name = DEFAULT_FONT_NAME;
		setFont(null, Font.PLAIN, size);
		color = DEFAULT_COLOR;
		just = DEFAULT_JUST;
	}
	
	public void setText(String t) {
		text = t;
	}
	
	public void setFont(String name, int style, float size) {
		if (name != null) {
			font_name = name;
		}
		font_style = style;
		font_size = size;
		
	}
	
	public void setSize(float size) {
		font_size = size;
	}
	
	public void setColor(Color c) {
		color = c;
	}

	@Override
	public void update(double dt) {
		
	}

	public String toString() {
		return "Text \""+text+"\"";
	}
}
