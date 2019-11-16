package slideshow.fileManagement;
import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import slideshow.slides.Items.*;



public class SlideshowGenerator {

	public static Slideshow generateFromFile(File source) {
		Slideshow result = new Slideshow("");
		
		loadSlideshow(result, source);
		
		return result;
	}
	
	public static void loadSlideshow(Slideshow slideshow, File source) {
		String file = "";

		// load file to memory
		try {
			BufferedReader fr = new BufferedReader(new FileReader(source));
			String line = fr.readLine();
			while(line != null) {
				file += line + '\n';
				line = fr.readLine();
			}
			fr.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		slideshow.empty();
		
		// Split file into words, numbers, symbols
		Tokenizer T = new Tokenizer(file);
		
		
		// @Debug: print all the tokens
		for (int i = 0; i < T.list.size(); i++) {
			System.out.println(T.list.get(i));
		}
		
		// Parsing and Slideshow building
		generateSlideshow(slideshow, T.list);
		
		// check if the current index is out of range with the new slide count
	}
	
	//private static HashMap<String, Float> vars = new HashMap<String, Float>();
	//private static HashMap<String, ArrayList<String>> templates = new HashMap<String, ArrayList<String>>();
	//private static HashMap<String, Macro> macros = new HashMap<String, Macro>();
	
	private static Scope scope;
	
	private static int i;
	
	private static void generateSlideshow(Slideshow slideshow, ArrayList<String> T) {
			
		// #TODO: Is version REALLY necessary?
		
		// check version   MUST be the first thing in the document
		scope = new Scope(null);
		i = 0;
		if (!T.get(i).equals("[")) {
			// @ERROR
			System.err.println("ERROR: '[' character expected at the beginning of file");
		}
		i++;
		
		if (!T.get(i).equals("1")) { // (isNumber(T.get(i)) {
			// @ERROR
			System.err.println("ERROR: Wrong version number");
		}
		i++;
		
		if (!T.get(i).equals("]")) {
			// @ERROR
			System.err.println("ERROR: ']' expected at the end of the version number");
		}
		i++;
		
		Slide new_slide = null;
		for (; i < T.size(); i++) {
			String base = T.get(i); // Should we use getToken() to expand any possible macro?
			if (base.equals("@")) {
				// All commands start with a '@'
				i++;
				
				// @Note: We don't want to expand this if it is a macro. If we did we will have to 
				// Remove the case of this being a macro in the switch. But the thing i dislike most
				// is that we will have to write macros like this.
				// @macro TitlePage title
				//   slide          ; I don't like not writing the '@' I think it makes it confusing
				//   @text title ...
				String command = T.get(i);
				
				switch (command) {
				case "slide": {
					new_slide = new Slide();
					slideshow.addSlide(new_slide);
				} break;
				
				case "background": {
					i++;
					int r = (int)(255*parse_expr(T));
					int g = (int)(255*parse_expr(T));
					int b = (int)(255*parse_expr(T));
					int a = (int)(255*parse_expr(T));
					new_slide.bg_color = new Color(r, g, b, a);
				} break;
				
				case "text": {
					i++;
					String msg = removeQuotes(getToken(T));
					i++;
					SlideText text_item = new SlideText(msg);
					// check other properties of the text
					while (i < T.size()-1 && !T.get(i+1).equals("\n") && !T.get(i+1).equals("@")) {
						String prop = getToken(T); // prop: properties
						switch (prop) {
						case "pos": {
							i++;
							float x = parse_expr(T);
							float y = parse_expr(T);
							float w = parse_expr(T);
							float h = parse_expr(T);
							
							text_item.x = x;
							text_item.y = y;
							text_item.w = w;
							text_item.h = h;
						} break;
				
						case "fill_color": {
							i++;
							int r = (int)(255*parse_expr(T));
							int g = (int)(255*parse_expr(T));
							int b = (int)(255*parse_expr(T));
							int a = (int)(255*parse_expr(T));
							text_item.fill_color = new Color(r, g, b, a);
						} break;
						
						case "outline": {
							i++;
							int r = (int)(255*parse_expr(T));
							int g = (int)(255*parse_expr(T));
							int b = (int)(255*parse_expr(T));
							int a = (int)(255*parse_expr(T));
							text_item.outline = new Color(r, g, b, a);
						} break;
						
						case "rot": {
							i++;
							float rotation = parse_expr(T);
							text_item.rotation = rotation;
						} break;
						
						case "font_name": {
							i++;
							String font_name = removeQuotes(getToken(T));
							i++;
							text_item.font_name = font_name;
						} break;

						case "font_style": {
							i++;
							String font_style = removeQuotes(getToken(T));
							i++;
							text_item.font_style = SlideText.FontStyle.valueOf(font_style.toUpperCase());
						} break;

						case "alignment": {
							i++;
							String alignment = removeQuotes(getToken(T));
							i++;
							text_item.alignment = SlideText.Alignment.valueOf(alignment.toUpperCase());
						} break;

						case "underlined": {
							i++;
							int underlined = parseInt(getToken(T));
							i++;
							text_item.underlined = underlined == 0 ? false : true;
						} break;
						
						default: {
							System.err.println("Unknown property: " + prop);
						} break;
						}
					}
					new_slide.items.add(text_item);
				} break;
				case "rect": {
					i++;
					float x = parse_expr(T);
					float y = parse_expr(T);
					float w = parse_expr(T);
					float h = parse_expr(T);
					
					SlideRect rect_item = new SlideRect(x, y, w, h);
					
					while (i < T.size()-1 && !T.get(i+1).equals("@")) {
						i++;
						String prop = T.get(i); // prop: properties
						switch (prop) {
						case "fill_color": {
							i++;
							int r = (int)(255*parse_expr(T));
							int g = (int)(255*parse_expr(T));
							int b = (int)(255*parse_expr(T));
							int a = (int)(255*parse_expr(T));
							rect_item.fill_color = new Color(r, g, b, a);
						} break;
						
						case "outline": {
							i++;
							int r = (int)(255*parse_expr(T));
							int g = (int)(255*parse_expr(T));
							int b = (int)(255*parse_expr(T));
							int a = (int)(255*parse_expr(T));
							rect_item.outline = new Color(r, g, b, a);
						} break;
						case "rot": {
							i++;
							float rotation = parse_expr(T);
							rect_item.rotation = rotation;
						} break;
						default: {
							System.err.println("Unknown property: " + prop);
						} break;
						}
					}
					new_slide.items.add(rect_item);
				} break;
				case "circle": {
					i++;
					float x = parse_expr(T);
					float y = parse_expr(T);
					float w = parse_expr(T);
					float h = parse_expr(T);
					
					SlideCircle circ_item = new SlideCircle(x, y, w, h);
					
					while (i < T.size()-1 && !T.get(i+1).equals("@")) {
						i++;
						String prop = T.get(i); // prop: properties
						switch (prop) {
						case "fill_color": {
							i++;
							int r = (int)(255*parse_expr(T));
							int g = (int)(255*parse_expr(T));
							int b = (int)(255*parse_expr(T));
							int a = (int)(255*parse_expr(T));
							circ_item.fill_color = new Color(r, g, b, a);
						} break;
						
						case "outline": {
							i++;
							int r = (int)(255*parse_expr(T));
							int g = (int)(255*parse_expr(T));
							int b = (int)(255*parse_expr(T));
							int a = (int)(255*parse_expr(T));
							circ_item.outline = new Color(r, g, b, a);
						} break;
						case "rot": {
							i++;
							float rotation = parse_expr(T);
							circ_item.rotation = rotation;
						} break;
						default: {
							System.err.println("Unknown property: " + prop);
						} break;
						}
					}
					new_slide.items.add(circ_item);
				} break;
				
				case "image": { // #CopyPasted
					i++;
					String path = removeQuotes(T.get(i));
					SlideImage img_item = new SlideImage(new File(path));
					// check other properties of the text
					while (i < T.size()-1 && !T.get(i+1).equals("@")) {
						i++;
						String prop = T.get(i); // prop: properties
						switch (prop) {
						case "pos": {
							i++;
							float x = parse_expr(T);
							float y = parse_expr(T);
							float w = parse_expr(T);
							float h = parse_expr(T);
							
							img_item.x = x;
							img_item.y = y;
							img_item.w = w;
							img_item.h = h;
						} break;
				
						case "fill_color": {
							i++;
							int r = (int)(255*parse_expr(T));
							int g = (int)(255*parse_expr(T));
							int b = (int)(255*parse_expr(T));
							int a = (int)(255*parse_expr(T));
							img_item.fill_color = new Color(r, g, b, a);
						} break;
						
						case "outline": {
							i++;
							int r = (int)(255*parse_expr(T));
							int g = (int)(255*parse_expr(T));
							int b = (int)(255*parse_expr(T));
							int a = (int)(255*parse_expr(T));
							img_item.outline = new Color(r, g, b, a);
						} break;
						
						case "rot": {
							i++;
							float rotation = parse_expr(T);
							img_item.rotation = rotation;
						} break;
						default: {
							System.err.println("Unknown property: " + prop);
						} break;
						}
					}
					new_slide.items.add(img_item);
				} break;
				
				case "shape": {
					i++;
					String type = removeQuotes(T.get(i));
					SlidePolygon poly_item = null;
					switch (Integer.parseInt(type)) {
					case SlidePolygon.ARROW: {
						poly_item = SlidePolygon.createArrow(0, 0, 0, 0, null, null); // #HACK: Create an appropiate method for this
					} break;
					
					case SlidePolygon.CROSS: {
						poly_item = SlidePolygon.createCross(0, 0, 0, 0, null, null); // #HACK: Create an appropiate method for this
					} break;
					
					case SlidePolygon.DIAMOND: {
						poly_item = SlidePolygon.createDiamond(0, 0, 0, 0, null, null); // #HACK: Create an appropiate method for this
					} break;
					
					case SlidePolygon.STAR: {
						poly_item = SlidePolygon.createStar(0, 0, 0, 0, null, null); // #HACK: Create an appropiate method for this
					} break;
					
					case SlidePolygon.TRIANGLE: {
						poly_item = SlidePolygon.createTriangle(0, 0, 0, 0, null, null); // #HACK: Create an appropiate method for this
					} break;

					case SlidePolygon.BALLOON: {
						poly_item = SlidePolygon.createBalloon(0, 0, 0, 0, null, null); // #HACK: Create an appropiate method for this
					} break;
					
					default:
						assert false;
					}
					// check other properties of the shape
					while (i < T.size()-1 && !T.get(i+1).equals("@")) {
						i++;
						String prop = T.get(i); // prop: properties
						switch (prop) {
						case "pos": {
							i++;
							float x = parse_expr(T);
							float y = parse_expr(T);
							float w = parse_expr(T);
							float h = parse_expr(T);
							
							poly_item.x = x;
							poly_item.y = y;
							poly_item.w = w;
							poly_item.h = h;
						} break;
				
						case "fill_color": {
							i++;
							int r = (int)(255*parse_expr(T));
							int g = (int)(255*parse_expr(T));
							int b = (int)(255*parse_expr(T));
							int a = (int)(255*parse_expr(T));
							poly_item.fill_color = new Color(r, g, b, a);
						} break;
						
						case "outline": {
							i++;
							int r = (int)(255*parse_expr(T));
							int g = (int)(255*parse_expr(T));
							int b = (int)(255*parse_expr(T));
							int a = (int)(255*parse_expr(T));
							poly_item.outline = new Color(r, g, b, a);
						} break;
						
						case "rot": {
							i++;
							float rotation = parse_expr(T);
							poly_item.rotation = rotation;
						} break;
						default: {
							System.err.println("Unknown property: " + prop);
						} break;
						}
					}
					new_slide.items.add(poly_item);
				} break;
				
				case "var": {
					i++;
					String name = T.get(i);
					i++;
					float value = parse_expr(T);
					
					scope.put_var(name, value);
				} break;
				case "template": {
					i++;
					String name = T.get(i);
					i++;
					assert false : "Todo: Think how we are going to store templates";
					
					//templates.put(name, template);
				} break;
				case "macro": {
					i++;
					String name = T.get(i);
					i++;
					ArrayList<String> params = new ArrayList<String>();
					while (!T.get(i).equals("\n")) {
						params.add(T.get(i));
						i++;
					}
					i++;
					ArrayList<String> body = new ArrayList<String>();
					while (!T.get(i).equals("\n")) {
						String token = T.get(i);
						if (token.equals("\\")) i++; // Skip next token
						body.add(T.get(i));
						i++;
					}
					//i++;
					scope.put_macro(name, new Macro(name, params, body));
				} break;
				default: {
					if (scope.contains_macro(command)) {
						expandMacro(T, command);
						i--; // @Todo: To compensate for the i++ of the for loop
					}
					else {
						System.err.println("Unknown command: " + command);
					}
				} break;
				}
			}
			else if (base.equals("\n")) {
			}
			else if (base.equals("{")) {
				scope = new Scope(scope);
			}
			else if (base.equals("}")) {
				// @Todo: Error checking
				scope = scope.parent;
			}
			else {
				System.out.println("ERROR: '@' expected, '"+base+"' found");
			}
		}
		slideshow.addSlide(new_slide);
	}

	
	private static void expandMacro(ArrayList<String> T, String macro_name) {
		Macro macro = scope.get_macro(macro_name);
		i++;
		ArrayList<String> args = new ArrayList<String>();
		for (int arg = 0; arg < macro.params.size(); arg++) {
			args.add(T.get(i));
			i++;
		}
		
		// Insert the tokens in the stream
		// This is not very performant but does the job easily
		for (int t = 0; t < macro.body.size(); t++) {
			String token = macro.body.get(t);
			if (macro.params.contains(token)) {
				token = args.get(macro.params.indexOf(token));
			}
			T.add(i+t, token);
		}
	}
	
	private static String getToken(ArrayList<String> T) {
		String s = T.get(i);
		if (scope.contains_macro(s)) {
			expandMacro(T, s);
			s = getToken(T);
		}
		return s;
	}
	
	private static String removeQuotes(String str) {
		// @Todo @Robustness: check if the string begins and ends with '"'
		return str.substring(1, str.length()-1); // endIndex exclusive
	}
	
	public static float parseFloat(String str) {
		if (str.charAt(0) == '$') {
			String var_name = str.substring(1);
			if (scope.contains_var(var_name)) {
				return scope.get_var(var_name);
			}
			else {
				System.err.println("'" + var_name + "' variable was not declared!, returning 0");
				return 0;
			}
		}
		return Float.parseFloat(str);
	}
	
	public static int parseInt(String str) {
		return Integer.parseInt(str);
	}
	
	public static float parse_mul(ArrayList<String> T) {
		float lhs = 0;
		String t = getToken(T);
		if (t.equals("(")) {
			i++;
			lhs = parse_expr(T);
			
			if (!getToken(T).equals(")")) {
				System.err.println("Malformed expression, parentesys is never closed");
			}
			i++;
		}
		else {
			lhs = parseFloat(t);
			i++;
		}
		
		String op = getToken(T);
		if (!op.equals("*") && !op.equals("/")) {
			return lhs;
		}
		
		i++;
		
		float rhs = parse_mul(T);
		
		float result = 0;
		if (op.equals("*")) {
			result = lhs * rhs;
		}
		else if (op.equals("/")) {
			result = lhs / rhs;
		}
		else {
			System.err.println("Unknown operator: '" + op + "'");
		}
		
		return result;
	}
	
	public static float parse_add(ArrayList<String> T) {
		float lhs = parse_mul(T);
		String op = getToken(T);
		if (!op.equals("+") && !op.equals("-")) {
			return lhs;
		}
		i++;
		
		float rhs = parse_mul(T);
		
		float result = 0;
		if (op.equals("+")) {
			result = lhs + rhs;
		}
		else if (op.equals("-")) {
			result = lhs - rhs;
		}
		else {
			System.err.println("Unknown operator: '" + op + "'");
		}
		
		return result;
	}
	
	public static float parse_expr(ArrayList<String> T) {
		float result = parse_add(T);
		return result;
	}

}

class Scope {
	public Scope parent;
	private HashMap<String, Float> vars;
	private HashMap<String, Macro> macros;
	
	public Scope(Scope parent) {
		this.parent = parent;
		vars = new  HashMap<>();
		macros = new HashMap<>();
	}
	
	public boolean contains_var(String name) {
		Scope s = this;
		while (s != null) {
			if (s.vars.containsKey(name)) {
				return true;
			}
			s = s.parent;
		}
		return false;
	}
	
	public float get_var(String name) {
		Scope s = this;
		while (s != null) {
			if (s.vars.containsKey(name)) {
				return s.vars.get(name);
			}
			s = s.parent;
		}
		
		assert false : "We should never reach here. Use contains_var";
		return 0;
	}
	
	public void put_var(String name, Float value) {
		vars.put(name, value);
	}
	
	public boolean contains_macro(String name) {
		Scope s = this;
		while (s != null) {
			if (s.macros.containsKey(name)) {
				return true;
			}
			s = s.parent;
		}
		return false;
	}
	
	public Macro get_macro(String name) {
		Scope s = this;
		while (s != null) {
			if (s.macros.containsKey(name)) {
				return s.macros.get(name);
			}
			s = s.parent;
		}
		
		assert false : "We should never reach here. Use contains_var";
		return null;
	}
	
	public void put_macro(String name, Macro macro) {
		macros.put(name, macro);
	}
}


class Macro {
	public String name;
	public ArrayList<String> params;
	public ArrayList<String> body;
	
	public Macro(String name, ArrayList<String> params, ArrayList<String> body) {
		this.name = name;
		this.params = params;
		this.body = body;
	}
}
