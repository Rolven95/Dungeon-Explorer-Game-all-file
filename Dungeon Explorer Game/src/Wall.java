import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Wall extends GameObject{
	
	//specifies (x, y) coordinates of each wall
	public Wall(int x, int y, ID id) {
		super(x, y, id);
	}

	public void tick() {
	}

	public static Image[] getWallImg() {
		return getWallImg();
	}

	public static void setWallImg(Image[] wallImg) {
		Wall.wallImg = wallImg;
	}
	
	//This method defines the rectangle around a wall to check for intersection with another object
	public Rectangle getBoundary() {
		return new Rectangle(/*x-1, y-1*/ x, y, 32, 32); //not useful
	}
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] wallImg = null; 
	static {
		wallImg = new Image[] {
				tk.getImage(Wall.class.getResource("Images/Wall.png")),
		};
	}		
			
			
	public void render(Graphics g) {
		g.drawImage(wallImg[0], x, y, null);
	}

	public void collision() {
	}

}
