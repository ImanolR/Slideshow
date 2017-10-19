package slideshow.viewer;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;

import slideshow.fileManagement.SlideshowGenerator;
import slideshow.slides.Items.Slide;
import slideshow.slides.Items.SlideItem;
import slideshow.slides.Items.Slideshow;

public class Editor extends JFrame implements WindowListener {

	public static void main(String[] args) {
		Editor e = new Editor();
		Slideshow presentation = new Slideshow("Test");
		SlideshowGenerator.fillSlideshow(presentation, new File("src"+File.separator+"test.show"));
		e.slideshow = presentation;
		e.updateComponents();
	}
	
	private JPanel canvas;
	private JPanel box;
	private JTree item_tree;
	
	private Slideshow slideshow;
	
	// Default constructor. Opens a new empty proyect
	public Editor() {
		
		slideshow = new Slideshow("*New*");
		slideshow.addSlide(new Slide());
		
		
		setSize(1280, 720);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);
		
		// Menubar
		{
			JPanel menubar = new JPanel();
			menubar.setOpaque(true);
			menubar.setBackground(new Color(30,30,30));
			menubar.add(new JButton("New"));
			menubar.add(new JButton("Open"));
			menubar.add(new JButton("Save"));
			menubar.add(new JButton("Close"));
			menubar.add(new JButton("AAA"));
			getContentPane().add(menubar, BorderLayout.NORTH);
		}
		
		// Right toolbar
		{
			JPanel right = new JPanel();
			right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
			right.setOpaque(true);
			right.setBackground(new Color(30,30,30));
			right.add(new JButton("+")); // Add new slide
			right.add(new JButton("-"));
			right.add(new JButton("Prev"));
			right.add(new JButton("Next"));
			right.add(new JButton("Move up"));
			right.add(new JButton("Move down"));
			right.add(new JButton("Clone")); // @Todo: Should we implement this functionality?
			
			// Slide miniatures
			box = new JPanel();
			box.setLayout(new BoxLayout(box, BoxLayout.Y_AXIS));
			JScrollPane scroll = new JScrollPane(box);

			right.add(scroll);
			
			getContentPane().add(right, BorderLayout.EAST);
		}
		
		// Left toolbar
		{
			JPanel left = new JPanel();
			left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
			left.setOpaque(true);
			left.setBackground(new Color(30,30,30));
			// Toolbar
			JPanel toolbar = new JPanel();
			left.add(toolbar);
			{
				toolbar.add(new JButton("Seleccionar"));
				toolbar.add(new JButton("Texto"));
				toolbar.add(new JButton("Rectangulo"));
			}
			item_tree = new JTree();
			JScrollPane scroll = new JScrollPane(item_tree);
			left.add(scroll);
			getContentPane().add(left, BorderLayout.WEST);
		}
				
		// Center
		{
			canvas = new JPanel();
			JButton button = new JButton("Hello world");
			button.addActionListener( (e)->{
				Renderer.renderSlideCentered(slideshow.currentSlide(), canvas.getGraphics(), canvas.getWidth(), canvas.getHeight(), 0);
			});
			canvas.add(button);
			getContentPane().add(canvas, BorderLayout.CENTER);
		}
		setVisible(true);
	}
	
	public void updateMiniatures() {
		
		if (slideshow == null) return;
		
		box.removeAll();
		int available_width  = box.getWidth();
		int available_height = (int)((float)available_width / (16f/9f));
		for (int i = 0; i < slideshow.slideCount(); i++) {
			BufferedImage miniature = new BufferedImage(available_width, available_height, BufferedImage.TYPE_4BYTE_ABGR);
			Renderer.renderSlideCentered(slideshow.getSlide(i),
					                     miniature.getGraphics(),
					                     miniature.getWidth(),
					                     miniature.getHeight(),
					                     0);
			
			box.add(new JButton(new ImageIcon(miniature)));
		}
	}
	
	public void updateItemTree() {
		DefaultTreeModel model = (DefaultTreeModel) item_tree.getModel();

		// @Todo: check if it needs update
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(slideshow);
		
		for (int slide_index = 0; slide_index < slideshow.slideCount(); slide_index++) {
			Slide slide = slideshow.getSlide(slide_index);
			DefaultMutableTreeNode slide_node = new DefaultMutableTreeNode(slide);
			root.add(slide_node);
			ArrayList<SlideItem> items = slide.items;
			for (int i = 0; i < items.size(); i++) {
				slide_node.add(new DefaultMutableTreeNode(items.get(i)));
			}
		}
		model.setRoot(root);
	}
	
	public void updateComponents() {
		updateMiniatures();
		updateItemTree();
		Renderer.renderSlideCentered(slideshow.currentSlide(), canvas.getGraphics(), canvas.getWidth(), canvas.getHeight(), 0);
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// @Todo: ask to save before closing
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		updateMiniatures();
	}
}