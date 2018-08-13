import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Door extends GameObject{

	public Door(int x, int y, ID id) {
		super(x, y, id);
	}

	public void tick() {
	}
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] doorImg = null; 
	static {
		doorImg = new Image[] {
				tk.getImage(Door.class.getResource("Images/Door.png")),
		};
	}		

	public void render(Graphics g) {
		g.drawImage(doorImg[0], x, y, null);
	}
	
	//Places rectangle around the door image to check for intersection with another object
	public Rectangle getBoundary() {
		return new Rectangle(x,y,32,32);
	}

	@Override
	public void collision() {
		// TODO Auto-generated method stub
		
	}
}
