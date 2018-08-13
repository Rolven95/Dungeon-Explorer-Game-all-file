import java.awt.Color;
import java.awt.Graphics;

public class Health {
	
	public static int health = 100;
	
	public void tick() {
	}
	
	public void  render(Graphics g) {
		
		g.setColor(Color.gray);
		g.fillRect(14 + 386, 15, 204, 34);
		g.setColor(Color.green);
		g.fillRect(16 + 386, 17, 2*health, 30);
		
	}
}