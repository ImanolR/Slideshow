package slideshow.slides.Items;

import java.awt.Color;


/** Consists of an array of V2 that are mapped [0..1]
 *  to the slidePolygon sub coordinate system.
 *
 */
public abstract class SlidePolygon extends SlideItem {

	public static final int ARROW = 1;
	public static final int STAR  = 2;
	public static final int TRIANGLE = 3;
	public static final int DIAMOND = 4;
	public static final int CROSS = 5;
	public static final int BALLOON = 6;
	public int polygon_type;
	
	public SlidePolygon(float x, float y, float w, float h, Color color, Color outline_) {
		super(x, y, w, h, POLYGON);
		fill_color = color;
		outline = outline_;
		if (color == null)    fill_color = new Color(0,0,0,0);
		if (outline_ == null) outline    = new Color(0,0,0,0);
	}

	// #NOTE: Shapes are generated calling a static method in this class
	
	public static SlideArrow createArrow(float x, float y, float w, float h, Color color, Color outl) {
		return new SlideArrow(x, y, w, h, color, outl);
	}
	
	public static SlideCircle createCircle(float x, float y, float w, float h, Color color, Color outl) {
		return new SlideCircle(x, y, w, h, color, outl);
	}
	
	public static SlideStar createStar(float x, float y, float w, float h, Color color, Color outl) {
		return new SlideStar(x, y, w, h, color, outl);
	}
	
	public static SlideTriangle createTriangle(float x, float y, float w, float h, Color color, Color outl) {
		return new SlideTriangle(x, y, w, h, color, outl);
	}
	
	public static SlideDiamond createDiamond(float x, float y, float w, float h, Color color, Color outl) {
		return new SlideDiamond(x, y, w, h, color, outl);
	}
	
	public static SlideCross createCross(float x, float y, float w, float h, Color color, Color outl) {
		return new SlideCross(x, y, w, h, color, outl);
	}
	
	public static SlideBalloon createBalloon(float x, float y, float w, float h, Color color, Color outl) {
		return new SlideBalloon(x, y, w, h, color, outl);
	}
	
	public float getMinW() {
		return 0.02f;
	}

	public float getMinH() {
		return 0.02f;
	}
	public abstract V2[] getVerteces();
}

class SlideArrow extends SlidePolygon {
	
	public SlideArrow(float x, float y, float w, float h, Color color, Color outl) {
		super(x, y, w, h, color, outl);
		polygon_type = ARROW;
	}
	

	@Override
	public V2[] getVerteces() {
		final float thickness = .3f;
		V2[] result = new V2[] {
				new V2(thickness, 1.f),
				new V2(thickness, .3f),
				new V2(1.f, .3f),
				new V2(.5f, .0f), // Punta
				new V2(.0f, .3f),
				new V2(1-thickness, .3f),
				new V2(1-thickness, 1.f),
		};
		return result;
	}

}

class SlideStar extends SlidePolygon {
	
	public SlideStar(float x, float y, float w, float h, Color color, Color outl) {
		super(x, y, w, h, color, outl);
		polygon_type = STAR;
	}
	

	@Override
	public V2[] getVerteces() {
		float rotation = (float) (2 * Math.PI / 5);
		V2 Long  = new V2(0.0f, 0.5f);
		V2 Short = V2.rotate(new V2(0.0f, 0.25f), rotation / 2);
		V2 center = new V2(0.5f, 0.5f);
		V2[] result = new V2[] {
				V2.add(center, Long),
				V2.add(center,Short),
				V2.add(center,V2.rotate(Long,  rotation)),
				V2.add(center,V2.rotate(Short, rotation)),
				V2.add(center,V2.rotate(Long,  2*rotation)),
				V2.add(center,V2.rotate(Short, 2*rotation)),
				V2.add(center,V2.rotate(Long,  3*rotation)),
				V2.add(center,V2.rotate(Short, 3*rotation)),
				V2.add(center,V2.rotate(Long,  4*rotation)),
				V2.add(center,V2.rotate(Short, 4*rotation)),
		};
		return result;
	}
}

class SlideTriangle extends SlidePolygon {
	
	public SlideTriangle(float x, float y, float w, float h, Color color, Color outl) {
		super(x, y, w, h, color, outl);
		polygon_type = TRIANGLE;
	}
	

	@Override
	public V2[] getVerteces() {
		V2[] result = new V2[] {
				new V2(.0f, 1.f),
				new V2(0.5f, 0.f),
				new V2(1.f, 1.f),
		};
		return result;
	}
}
class SlideDiamond extends SlidePolygon {
	
	public SlideDiamond(float x, float y, float w, float h, Color color, Color outl) {
		super(x, y, w, h, color, outl);
		polygon_type = DIAMOND;
	}
	

	@Override
	public V2[] getVerteces() {
		V2[] result = new V2[] {
				new V2(.0f, 0.5f),
				new V2(0.5f, 0.f),
				new V2(1.f, 0.5f),
				new V2(0.5f, 1.f),
		};
		return result;
	}
}

class SlideCross extends SlidePolygon {
	
	public SlideCross(float x, float y, float w, float h, Color color, Color outl) {
		super(x, y, w, h, color, outl);
		polygon_type = CROSS;
	}
	

	@Override
	public V2[] getVerteces() {
		V2[] result = new V2[] {
				new V2(0.30f, 1.f),
				new V2(0.30f, 0.70f),
				new V2(0.f, 0.70f),
				new V2(0.f, 0.30f),
				new V2(0.30f, 0.30f),
				new V2(0.30f, 0.f),
				new V2(0.70f, 0.f),
				new V2(0.70f, 0.30f),
				new V2(1.f, 0.30f),
				new V2(1.f, 0.70f),
				new V2(0.70f, 0.70f),
				new V2(0.70f, 1.f),
				
		};
		return result;
	}
}

class SlideBalloon extends SlidePolygon {
	
	public SlideBalloon(float x, float y, float w, float h, Color color, Color outl) {
		super(x, y, w, h, color, outl);
		polygon_type = BALLOON;
	}
	

	@Override
	public V2[] getVerteces() {
		V2[] result = new V2[] {
				new V2(0.5f, 1.f),
				new V2(0.40f, 0.65f),
				new V2(0.f, 0.65f),
				new V2(0.f, 0.f),
				new V2(1.f, 0.f),
				new V2(1.f, 0.65f),
				new V2(0.60f, 0.65f),
				new V2(0.5f,1.f),
				
				
		};
		return result;
	}
}
/*
// #IDEA: This method has to be created in a parent class and added a call to it in every case of popUp.setObject()
public void update() {
	vertex = new V2[] {
			new V2(thikness, 1.f),
			new V2(thikness, .3f),
			new V2(1.f, .3f),
			new V2(.5f, .0f), // Point
			new V2(.0f, .3f),
			new V2(1-thikness, .3f),
			new V2(1-thikness, 1.f),
	};
}*/