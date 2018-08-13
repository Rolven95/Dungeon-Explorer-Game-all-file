import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;

public class Gold extends GameObject{
	
	Handler handler;

	private Random r;
	public Gold(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		this.handler= handler;
		r = new Random();
	}
	
	public void tick() {
		collision();
	}
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] goldImg = null; 
	static {
		goldImg = new Image[] {
				tk.getImage(Gold.class.getResource("Images/Gold.png")),
		};
	}		

	public void render(Graphics g) {
		g.drawImage(goldImg[0], x, y, null);
	}

	public Rectangle getBoundary() {
		return new Rectangle(x,y,24,24);
	}

	public void collision() {
		for(int i = 0; i< handler.object.size(); i++) {
			GameObject temObj = handler.object.get(i);
			if(temObj.getId() == ID.Player) {
				if (getBoundary().intersects(temObj.getBoundary())) {
					//collision between Player and Gold
					this.handler.removeObject(this);
					
					for(int q=0; Game.RoomObj[Game.currentRoomNo][q]!=null; q++) {
						if (this == Game.RoomObj[Game.currentRoomNo][q]) {
							System.out.println("Got it " + Game.currentRoomNo + q);
							
							for (int p=q+1 ; Game.RoomObj[Game.currentRoomNo][p]!=null; p++) {	
								Game.RoomObj[Game.currentRoomNo][p-1]= Game.RoomObj[Game.currentRoomNo][p];
							}
						}
					}
				}
			}
			if(temObj.getId() == ID.Wall || ( temObj.getId() == ID.Gold && temObj != this) || temObj.getId() == ID.Gun) {
				if (getBoundary().intersects(temObj.getBoundary())) {
					
				   x=r.nextInt(Game.WIDTH - 100) + 50;
		           y=r.nextInt(Game.HEIGHT - 200) + 150;
		           
				}
			}
		}
	}
}
