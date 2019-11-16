package slideshow.viewer;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;

import slideshow.fileManagement.SlideshowGenerator;
import slideshow.slides.Items.SlideImage;
import slideshow.slides.Items.SlideItem;
import slideshow.slides.Items.SlideRect;
import slideshow.slides.Items.SlideText;
import slideshow.slides.Items.Slideshow;

public class Viewer implements KeyListener {

/*	
	------------------
	    TO DO
	------------------
	
	- Fullscreen mode
	
	- Cursor
	  - Smooth moving of the cursor
	
	- File loading
	  - Basic format (text document)
	    - Templates
	      - @slide templetName
	      - @template templetName
	   - Images
*/
	
	
	private JFrame window;
	private JPanel canvas;
	
	private Slideshow slideshow;
	private File slideshow_file;
	
	public static void main(String[] args) {
		new Viewer();
	}

	public boolean running;
	public Viewer() {
		// Init window
		slideshow_file = new File("src"+File.separator+"test.show");
		
		window = new JFrame();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(1280, 720);
		window.addKeyListener(this);
		
		canvas = new JPanel();
		canvas.setIgnoreRepaint(true);
		
		window.getContentPane().add(canvas);
		
		window.setVisible(true);
		
		// Load slideshow
		slideshow = new Slideshow("");
		hotLoad();
		
		window.setTitle(slideshow.name);
		
		// Main loop
		running = true;
		long now, last = System.nanoTime(); 
		while (running) {
			now = System.nanoTime();
			update(now-last);
			render();
			
			// Hot-load slideshow
			// We might want to move this to the keyEvents handling "F5"
			hotLoad();

			last = now;
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				// @Todo: handle exception ¬¬
			}
		}
		
		window.dispose();
	}
	
	private long lastModified;
	private void hotLoad() {
		long checked = slideshow_file.lastModified();
		if (checked > lastModified) {
			SlideshowGenerator.loadSlideshow(slideshow, slideshow_file);
			lastModified = checked;
		}
	}
	
	/**
	 * Updates the items of the slide. Items that contain animations, etc.
	 * @param elapsed_t in nanoseconds
	 */
	private void update(long elapsed_t) {
		double dt = (double) elapsed_t / 1000000000;
		for (SlideItem item : slideshow.currentSlide().items) {
			item.update(dt);
		}
	}

	private void render() {
		
		assert slideshow != null: "Error: the slideshow is not loaded"; 
		
		Renderer.render(slideshow.currentSlide(), canvas);
	}

	private boolean sys_cursor_hidden = false;
	@Override
	public void keyPressed(KeyEvent k) {
		int key = k.getKeyCode();
		switch (key) {
		case KeyEvent.VK_ESCAPE:
			running = false;
			break;
		case KeyEvent.VK_LEFT:
			slideshow.prev();
			break;
		case KeyEvent.VK_RIGHT:
			slideshow.next();
			break;
		case KeyEvent.VK_H:
			if (sys_cursor_hidden) {
				sys_cursor_hidden = false;
				window.setCursor(Cursor.getDefaultCursor());
			}
			else {
				//System.out.println("hide cursor"); // @Debug
				sys_cursor_hidden = true;
				window.setCursor(window.getToolkit().createCustomCursor(new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB), new Point(0, 0), null));
			}
			break;
		case KeyEvent.VK_F5:
			hotLoad();
		default:
		}
	}


	@Override
	public void keyReleased(KeyEvent k) {
	}


	@Override
	public void keyTyped(KeyEvent k) {
	}

}
