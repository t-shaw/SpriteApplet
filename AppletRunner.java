import java.awt.Canvas;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.DoubleBuffer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;



public class AppletRunner
{
	public static CustomDrawingPanel content;
	public static JFrame window = new JFrame();
	public static Sprite sprt;
	static Graphics g;
	public static void main(String[] args)
	{
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setSize(800 , 600);
		
		
		
		BufferedImage s1,s2,s3,s4,s5,s6 = null;
		Image[] imgs = new Image[6];
		try{
			imgs[0] = ImageIO.read(new File("w1.jpg"));
			imgs[1] = ImageIO.read(new File("w2.jpg"));
			imgs[2] = ImageIO.read(new File("w3.jpg"));
			imgs[3] = ImageIO.read(new File("w4.jpg"));
			imgs[4] = ImageIO.read(new File("w5.jpg"));
			imgs[5] = ImageIO.read(new File("w6.jpg"));
		} catch (IOException e) {
			System.out.println("Can't load image");
		}
		
		
		Point loc = new Point();
		content = new CustomDrawingPanel();
		
		sprt = new Sprite(content, imgs, 0, 1, 1, loc, loc, 1, 0);
		
		
		
		
			//window.removeAll();
		
		for(int i = 0; i<6; i++)
		{
			
			window.add(content);
			
			
			window.setVisible(true);
		}	
	
		
		
		
	
		
	
	}
	
	
}

class CustomDrawingPanel extends JPanel
{
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		//draw here/
		
		AppletRunner.sprt.draw(g);
		AppletRunner.sprt.update();
		
	}
}