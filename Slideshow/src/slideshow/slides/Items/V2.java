package slideshow.slides.Items;

public class V2 {

	public float x, y;
	
	public V2() {
		
	}
	
	public V2(float _x, float _y) {
		x = _x;
		y = _y;
	}

	public static V2 add(V2 u, V2 v) {
		return new V2(u.x + v.x, u.y + v.y);
	}
	
	public static V2 sub(V2 u, V2 v) {
		return new V2(u.x - v.x, u.y - v.y);
	}
	

	public static V2 rotate(V2 vec, float rotation) {
		V2 result = new V2(vec.x*(float)Math.cos(rotation) - vec.y*(float)Math.sin(rotation),
						   vec.x*(float)Math.sin(rotation) + vec.y*(float)Math.cos(rotation));
		return result;
	}
	

	@Override
	public String toString() {
		return "V2 [x=" + x + ", y=" + y + "]";
	}

	
	
}
