package slideshow.fileManagement;

import java.util.ArrayList;

class Tokenizer {
	ArrayList<String> list;
	String token;
	
	public Tokenizer() {
		list = new ArrayList<String>(); 
		token = "";
	}
	
	public Tokenizer(String text) {
		list = new ArrayList<String>(); 
		token = "";
		
		tokenize(text);
	}
	
	public void tokenize(String text) {
		//String token = "";
		int index = 0;
		while (index < text.length()) {
			char c = text.charAt(index);
			
			if (c == ';') {
				// Ignore comments!!
				index++;
				c = text.charAt(index);
				while (c != '\n') {
					index++;
					if (index >= text.length()) break;
					c = text.charAt(index);
				}
			}
			
			if (Character.isLetter(c)) {
				// If it is a identifier aka it can contain letters or numbers
				token += c;
				index++;
				c = text.charAt(index);
				while (!Character.isWhitespace(c)) {
					// @Debug
					if (c == '"') break;
					
					token += c;
					index++;
					if (index >= text.length()) break;
					c = text.charAt(index);
				}
				addToken();
			}
			else if (Character.isDigit(c)) {
				// We don't allow floating point numbers to begin with a '.', for example: ".05"
				token += c;
				index++;
				c = text.charAt(index);
				while (Character.isDigit(c) || c == '.') {
					token += c;
					index++;
					if (index >= text.length()) break;
					c = text.charAt(index);
				}
				addToken();
			}
			else if (c == '$') {
				index++;
				token += c;
				c = text.charAt(index);
				if (Character.isLetter(c)) {
					// If it is a identifier aka it can contain letters or numbers
					token += c;
					index++;
					c = text.charAt(index);
					while (!Character.isWhitespace(c)) {
						// @Debug
						if (c == '"') break;
						
						token += c;
						index++;
						if (index >= text.length()) break;
						c = text.charAt(index);
					}
					addToken();
				}
			}
			else if (c == '"') {
				// The strings are stored in memory with '"' mark at the beginning,
				// and at the end the '"' 
				do {
					token += c;
					index++;
					c = text.charAt(index);
				} while (c != '"');
				// @Note: not adding the token inserts the final " at the end of the string
				// in the 'if (isSymbol(c))' code
			}
			if (isSymbol(c)) {
				// '"' is handled above
				token += c;
				index++;
				addToken();
			}
			
			if (c == '\n') {
				token += c;
				addToken();
			}
			if (Character.isWhitespace(c)) index++;
		}
		
	}
	
	private boolean isSymbol(char c) {
		if ((c >= '!' && c <= '/') ||
		    (c >= ':' && c <= '@') ||
		    (c >= '[' && c <= '`') ||
		    (c >= '{' && c <= '~')) {
			return true;
		}
		return false;
	}
	
	private void addToken() {
		list.add(token);
		this.token = "";
	}
}