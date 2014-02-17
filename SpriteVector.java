import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Random;
import java.util.Vector;

public class SpriteVector extends Vector
{
	protected Background background;

	public SpriteVector(Background back)
	{
		super(50, 10);
		background = back;
	}

	public Background getBackground()
	{
		return background;
	}

	public void setBackground(Background back)
	{
		background = back;
	}

	// this next method will find a random empty spot
	public Point findEmptyPosition(Dimension screenSize)
	{
		Rectangle screen = new Rectangle(0, 0, screenSize.width,
		        screenSize.height);
		Random rand = new Random(System.currentTimeMillis());
		boolean empty = false;
		int numTries = 0;

		// Iterate and look for empty position
		while (!empty && numTries++ < 50)
		{
			screen.x = Math.abs(rand.nextInt() % background.getSize().width);
			screen.y = Math.abs(rand.nextInt() % background.getSize().height);

			// Iterate through sprites, checking if position is empty
			boolean collision = false;
			for (int i = 0; i < size(); i++)
			{
				Rectangle testPos = ((Sprite) elementAt(i)).getPosition();
				if (screen.intersects(testPos))
				{
					collision = true;
					break;
				}
			}
			empty = !collision;
		}
		return new Point(screen.x, screen.y);
	}

	Sprite isPointInside(Point pt)
	{
		// Iterate backward through the sprites, testing each
		for (int i = (size() - 1); i >= 0; i--)
		{
			Sprite s = (Sprite) elementAt(i);
			if (s.isPointInside(pt))
				return s;
		}
		return null;
	}

}
