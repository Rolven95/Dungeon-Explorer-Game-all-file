import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

public class Ammo {
	
	public static int AmmoRemain = 5; //the remaining ammo number
	
	public void tick() {
	}
	
	private static Toolkit tk = Toolkit.getDefaultToolkit(); // loading images to an array
	private static Image[] AmmoImg = null; 
	static {
		AmmoImg = new Image[] {
				tk.getImage(Ammo.class.getResource("Images/Bullet0.png")), //0
				tk.getImage(Ammo.class.getResource("Images/Bullet1.png")), //1
				tk.getImage(Ammo.class.getResource("Images/Bullet2.png")), //2
				tk.getImage(Ammo.class.getResource("Images/Bullet3.png")), //3
				tk.getImage(Ammo.class.getResource("Images/Bullet4.png")), //4
				tk.getImage(Ammo.class.getResource("Images/Bullet5.png")), //5
		};
	}	
	public void  render(Graphics g) {
		g.drawImage(AmmoImg[this.AmmoRemain], 15, 15, null); 
	}
}
