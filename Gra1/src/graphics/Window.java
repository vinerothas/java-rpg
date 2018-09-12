package graphics;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Window {

	public JFrame window;
	public Canvas canvas;
	public BufferStrategy bs;
	private GraphicsEnvironment ge;
	private GraphicsDevice gd;
	public GraphicsConfiguration gc;
	public Graphics graphics;
	protected Graphics2D g;
	public BufferedImage buffer;
	private int width;// = Constants.WINDOW_REGION_VIEW_WIDTH+Constants.WINDOW_UI_WIDTH;
	private int height;// = Constants.WINDOW_REGION_VIEW_HEIGHT;
	public boolean fullscreen = false;
	//TODO woda
	//TODO zdejmowanie przedmiotow
	//TODO ziekszyc atak broni wraz z levelem gracza
	
	public void initJFrame(int w, int h){
		width = w;
		height = h;
		window = new JFrame("Game");
		if(fullscreen)window.setUndecorated(true);
		window.setIgnoreRepaint(true);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);				
		canvas = new Canvas();
		canvas.setIgnoreRepaint(true);
		window.setResizable(false);
		
		ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    gd = ge.getDefaultScreenDevice();
	    
	    if(fullscreen){
	    	height = gd.getDisplayMode().getHeight();
	    	width = gd.getDisplayMode().getWidth();
	    }
		
		canvas.setSize(width,height);
		//window.setSize(width,height);
		
		window.add(canvas);
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		//System.out.println(window.getWidth()+" "+window.getHeight()+" "+canvas.getSize());
		
		canvas.createBufferStrategy(2);
		bs = canvas.getBufferStrategy();
		
		if(fullscreen)gd.setFullScreenWindow(window);
	    
	    gc = gd.getDefaultConfiguration();
	    buffer = gc.createCompatibleImage(width,height);
	    graphics = null;
	    g = null;
	    
	    loadingScreen();
	}
	
	private void loadingScreen(){
		g = buffer.createGraphics();
		
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		g.setColor(Color.YELLOW);
		g.setFont(new Font("Arial",Font.PLAIN,24));
		g.drawString("Initializing...", (width/2)-50, (height/2));
		
		graphics = bs.getDrawGraphics();
		graphics.drawImage(buffer, 0, 0, null);        
        bs.show();       
        if (graphics != null) graphics.dispose();
        if (g != null) g.dispose();
	}
}
