import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
/*Designed as shot gun, short shoot range, can be changed to pistol by change Boom class. 
 *Could be picked up, or throw away by press "G".
 * 
 * */


public class Gun extends GameObject{
	Handler handler;
	//private Font am = new Font("TimesRoman", Font.BOLD, 50);
	
	public static int ammo = 5; // The total allowed fire time
	
	public Gun(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void tick() {
		collision();
	}

	// load the Gun picture. 
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] gunImg = null; 
	static {
		gunImg = new Image[] {
				tk.getImage(Gun.class.getResource("Images/Gun.png")),
		};

	}		

	@Override
	public void render(Graphics g) {
		g.drawImage(gunImg[0], x, y, null);
		//String drawString = ""+ammo;
		//g.setFont(am);
		//g.drawString(drawString, 256, 256);
	}

	public Rectangle getBoundary() {
		return new Rectangle(x,y,24,24);
	}
	
	public void collision() {
		for(int i = 0; i< handler.object.size(); i++) {
			GameObject temObj = handler.object.get(i);
			if(temObj.getId() == ID.Player) {// if the gun is touched by player. Automatically picked up.
				if (getBoundary().intersects(temObj.getBoundary())) {
				Player.holdGun = true;
				Game.isAmmo = true;
				handler.removeObject(this);
				
				}
			}
		}
	}
}
