import java.awt.*;
import java.util.BitSet;


public class Sprite
{
	//These are sprite actions, aka things that sprites can do to themselves, such as kill or add a new sprite
	public static final int KILL = 0;
	public static final int SRESTOREPOS = 1;
	public static final int ADDSPRITE = 2;
	
	//these are bounds actions, aka actions that result from running into a boundery like the edge of the screen
	public static final int STOP = 0;
	public static final int BOUNCE = 1;
	public static final int WRAP = 2; 
	public static final int DIE = 3;
	
	//We need IMAGEOBSERVER class? And as such the component class implements the image observer interface, and the Applet is derived from component
	//This will most likely be the applet screen
	protected Component component;
	
	//the actual image
	protected Image[] image;
	
	//Probably deals with the image array
	protected int frame;
	//A way to easily reverse the animation. Usually set to 1, but can change to -1 for backwards
	protected int frameInc;
	//The next two are ways to contol frame speed
	protected int frameDelay;
	protected int frameTrigger;
	
	protected Rectangle position;
	protected Rectangle collision;
	
	//This is the importance of depth variable, i.e. planes should be drawn above tanks
	protected int zOrder;
	
	//TODO find out about this
	protected Point velocity;
	
	//Probably bounderies
	protected Rectangle bounds;
	
	//The variable that matches up with the bounds actions variables
	protected int boundsActions;
	
	//When this variable is set to true the sprite is hidden from view
	protected boolean hidden = false;
	
	
	//Constructor for a single image
	public Sprite(Component comp, Image img, Point pos, Point veloc, int z, int boundsActions)
	{
		component = comp;
		image = new Image[1];
		image[0] = img;
		
		setPosition(new Rectangle(pos.x, pos.y, img.getWidth(comp), img.getHeight(comp)));
		
		setVelocity(veloc);
		
		frame = 0;
		frameInc = 0;
		
		frameDelay = 0;
		frameTrigger = 0;
		
		zOrder = z;
		
		bounds = new Rectangle(0, 0, comp.getWidth(), comp.getHeight());
		this.boundsActions = boundsActions;
		
	}
	//This constructor takes in an array of images
	public Sprite(Component comp, Image[] img, int frame, int frameInc, int frameDelay, Point pos, Point veloc, int z, int boundActions)
	{
		component = comp;
		image = img;
		
		setPosition(new Rectangle(pos.x, pos.y, img[frame].getWidth(comp), img[frame].getHeight(comp)));
		
		setVelocity(veloc);
		
		this.frame = frame;
		this.frameInc = frameInc;
		this.frameDelay = frameDelay;
		this.frameTrigger = frameDelay;
		
		zOrder = z;
		bounds = new Rectangle(0, 0, comp.getWidth(), comp.getHeight());
		this.boundsActions = boundsActions;
	
	}
	
	public Point getVelocity()
	{
		return velocity;
	}
	public void setVelocity(Point vel)
	{
		velocity = vel;
	}
	
	public Rectangle getPosition()
	{
		return position;
	}
	//With these two methods you can set position either through rectangles or points, point just moves the rectangle
	void setPosition(Rectangle pos)
	{
		position = pos;
		setCollision();
	}
	
    public void setPosition(Point pos)
	{
		position.setLocation(pos.x, pos.y);
		setCollision();
	}
	
	protected void setCollision()
	{
		//For different sized sprites, the child class will have to calculate a smaller rectangle here
		collision = position;
	}
	
	boolean isPointInside(Point point)
	{
		return position.contains(point.x, point.y);
	}
	
	
	public void incFrame()
	{
		//First do a check if the frame should be incremented, bigger frame delay = longer animation
		if((frameDelay > 0) && (--frameTrigger <= 0))
		{
			
			//reset frame trigger
			frameTrigger = frameDelay;
			
			//increment frame
			frame += 1;
			System.out.println("am i here" +frame);
			//Now frame is checked to make sure it is inside the bounds of the image, so that later it can be indexed into the array
			if(frame > image.length-1)
			{
				frame = 0;
			}
			else if(frame < 0)
			{
				//In case of reverse
				frame = image.length - 1;
			}
		}
	}
	
	public BitSet update()
	{
		//This method is where the bulk of the work will be done
		
		//BitSet is a way to handle multiple flags or bits
		BitSet action = new BitSet();
		
		//Increment the frame
		incFrame();
		
		//Now update the position
		//It does it by sliding the position rectangle by the velocity
		Point pos = new Point(position.x, position.y);
		pos.translate(velocity.x, velocity.y);
		
		//Now to check the bounds
		//Wrap
		if(boundsActions == WRAP)
		{
			//If it goes off the left
			if((pos.x + position.width) < bounds.x)
			{ 
				pos.x = bounds.x + bounds.width;
			}
			//If it goes off the right
			if(pos.x > (bounds.x + bounds.width))
			{
				pos.x = bounds.x - position.width;
			}
			//If it goes off the top
			if((pos.y + position.height) < bounds.y)
			{
				pos.y = bounds.y + bounds.height;
			}
			//If it goes off bottom
			if(pos.y > (bounds.y + bounds.height))
			{
				pos.y = bounds.y - position.height;
			}
		}
		
		//For bounce
		else if(boundsActions == BOUNCE)
		{
			boolean bounce = false;
			Point   vel = new Point(velocity.x, velocity.y);
		    if (pos.x < bounds.x) {
		      bounce = true;
		      pos.x = bounds.x;
		      vel.x = -vel.x;
		    }
		    else if ((pos.x + position.width) > 
		      (bounds.x + bounds.width)) {
		      bounce = true;
		      pos.x = bounds.x + bounds.width - position.width;
		      vel.x = -vel.x;
		    }
		    if (pos.y < bounds.y) {
		      bounce = true;
		      pos.y = bounds.y;
		      vel.y = -vel.y;
		    }
		    else if ((pos.y + position.height) > 
		      (bounds.y + bounds.height)) {
		      bounce = true;
		      pos.y = bounds.y + bounds.height - position.height;
		      vel.y = -vel.y;
		    }
		    if (bounce)
		      setVelocity(vel);
		  }
		 // Die?
		  else if (boundsActions == DIE) {
		    if ((pos.x + position.width) < bounds.x ||
		      pos.x > bounds.width || 
		      (pos.y + position.height) < bounds.y ||
		      pos.y > bounds.height) {
		      action.set(KILL); 
		      return action;
		    }
		  }
		  // Stop (default)
		  else {
		    if (pos.x  < bounds.x || 
		      pos.x > (bounds.x + bounds.width - position.width)) {
		      pos.x = Math.max(bounds.x, Math.min(pos.x,
		        bounds.x + bounds.width - position.width));
		      setVelocity(new Point(0, 0)); 
		    }
		    if (pos.y  < bounds.y || 
		      pos.y > (bounds.y + bounds.height - position.height)) {
		      pos.y = Math.max(bounds.y, Math.min(pos.y,
		        bounds.y + bounds.height - position.height));
		      setVelocity(new Point(0, 0)); 
		    }
		  }
		  setPosition(pos);

		  return action;
		}
	
	public void draw(Graphics g)
	{
		if(!hidden)
		{
			System.out.println("Whats up here??");
			g.drawImage(image[frame], position.x, position.y, component);
		}
	}
	
	protected Sprite addSprite(BitSet action)
	{
		//It is up to the indivitual sprite classes to implement this
		return null;
	}
	
	protected boolean testCollision(Sprite test)
	{
		//Check with the collision rectangle
		if(test != this)
		{
			return collision.intersects(test.collision);
		}
		
		return false;
	}
}
	
	


