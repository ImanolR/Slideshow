package slideshow.fileManagement;
import java.awt.Color;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

import slideshow.slides.Items.*;



public class SlideshowGenerator {

	
	public static Slideshow generateFromFile(File source) {
		Slideshow result = new Slideshow("");
		
		fillSlideshow(result, source);
		
		return result;
	}
	
	public static void fillSlideshow(Slideshow slideshow, File source) {
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
		fillSlideshow(slideshow, T.list);
		
		// check if the current index is out of range with the new slide count
	}
	
	private static HashMap<String, Float> vars = new HashMap<String, Float>();
	public static void fillSlideshow(Slideshow slideshow, ArrayList<String> T) {
		
		
		// check version   MUST be the first thing in the document
		int i = 0;
		if (!T.get(i).equals("[")) {
			// @ERROR
			System.out.println("ERROR: '[' character expected at the beginning of file");
		}
		i++;
		
		if (!T.get(i).equals("1")) { // (isNumber(T.get(i)) {
			// @ERROR
			System.out.println("ERROR: Wrong version number");
		}
		i++;
		
		if (!T.get(i).equals("]")) {
			// @ERROR
			System.out.println("ERROR: ']' expected at the end of the version number");
		}
		i++;
		
		Slide new_slide = null;
		for (; i < T.size(); i++) {
			String base = T.get(i); // this should always be => "@"
			if (T.get(i).equals("@")) {
				// All commands start with a '@'
				i++;
				String command = T.get(i);
				
				switch (command) {
				case "slide": {
					if (new_slide != null) {
						slideshow.addSlide(new_slide);
					}
					new_slide = new Slide();
				} break;
				
				case "background": {
					i++;
					int r = (int)(255*parseFloat(T.get(i)));
					i++;
					int g = (int)(255*parseFloat(T.get(i)));
					i++;
					int b = (int)(255*parseFloat(T.get(i)));
					i++;
					int a = (int)(255*parseFloat(T.get(i)));
					new_slide.setBackgroundColor(new Color(r, g, b, a));
				} break;
				
				case "text": {
					i++;
					String msg = removeQuotes(T.get(i));
					SlideText text_item = new SlideText(msg);
					// check other properties of the text
					while (i < T.size()-1 && !T.get(i+1).equals("@")) {
						i++;
						String prop = T.get(i); // prop: properties
						switch (prop) {
						case "pos": {
							i++;
							float x = parseFloat(T.get(i));
							i++;
							float y = parseFloat(T.get(i));
							text_item.x = x;
							text_item.y = y;
						} break;
				
						case "color": {
							i++;
							int r = (int)(255*parseFloat(T.get(i)));
							i++;
							int g = (int)(255*parseFloat(T.get(i)));
							i++;
							int b = (int)(255*parseFloat(T.get(i)));
							i++;
							int a = (int)(255*parseFloat(T.get(i)));
							text_item.color = new Color(r, g, b, a);
						} break;
						
						}
					}
					new_slide.addItem(text_item);
				} break;
				case "rect": {
					i++;
					float x = parseFloat(T.get(i));
					i++;
					float y = parseFloat(T.get(i));
					i++;
					float w = parseFloat(T.get(i));
					i++;
					float h = parseFloat(T.get(i));
					
					SlideRectangle rect_item = new SlideRectangle(x, y, w, h);
					
					if (i < T.size()-1) {
						i++;
						String prop = T.get(i); // prop: properties
						switch (prop) {
						case "color": {
							i++;
							int r = (int)(255*parseFloat(T.get(i)));
							i++;
							int g = (int)(255*parseFloat(T.get(i)));
							i++;
							int b = (int)(255*parseFloat(T.get(i)));
							i++;
							int a = (int)(255*parseFloat(T.get(i)));
							rect_item.setColor(new Color(r, g, b, a));
						} break;
						default:
							i--;
						}
					}
					new_slide.addItem(rect_item);
				} break;
				case "var": {
					i++;
					String name = T.get(i);
					i++;
					float value = parseFloat(T.get(i));
					
					vars.put(name, value);
				} break;
					
				}
			}
			else {
				System.out.println("ERROR: '@' expected, '"+base+"' found");
			}
		}
		slideshow.addSlide(new_slide);
	}

	
	private static String removeQuotes(String str) {
		// @Todo @Robustness: check if the string begins and ends with '"'
		return str.substring(1, str.length()-1); // endIndex exclusive
	}
	
	public static float parseFloat(String str) {
		if (str.charAt(0) == '$') {
			String var_name = str.substring(1);
			return vars.get(var_name);
		}
		return Float.parseFloat(str);
	}
	
//	public static int parseInt(String str) {
//		return Integer.parseInt(str);
//	}
	

	

}
