import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Random;
import javax.swing.JLabel;

public class Player extends GameObject {
	
	Handler handler;
	public static boolean left = false;
	public static boolean right = false;
	public static boolean up = false;
	public static boolean down = false;
	
	public static boolean holdGun = false;
	private int facing = 0; // mid:0, up:1, Upleft:2, Left:3, DownLeft 4, Down 5, DownRight 6, Right 7, UpRight 8;
	private int lastFacing = 0;
	public static boolean shoot = false; 
	
	public Player(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		this.handler = handler;
		//speedX = 5;
		//speedY = 5;
	}
	
	//Inserts an invisible rectangle around the player that detects any intersection with another rectangle
	//to allow collision of objects
	public Rectangle getBoundary() {
		return new Rectangle(x,y,32,32);
	}
	
	public void tick() {	
		y += speedY;
		x += speedX;
			
		collision();	
		face();
		faceRemain();
		
		if (this.holdGun == true && this.shoot == true && Gun.ammo >= 1){
			
			if(Game.whoIsYourDady == false) {
				Gun.ammo -= 1 ;
				Ammo.AmmoRemain -= 1;
			}

			
			int newX, newY;
			
			if (this.lastFacing == 1 || this.lastFacing == 2) {
				newX = x;
				newY = y-32;
				
			}
			else if(this.lastFacing == 3 || this.lastFacing == 4) {
				newX = x-32;
				newY = y;
			}
			else if(this.lastFacing == 5 || this.lastFacing == 6) {
				newX = x;
				newY = y+32;
			}
			else {
				newX = x+32;
				newY = y;
			}
			handler.addObject(new Boom(newX, newY, ID.Boom, handler));
			
			shoot = false;
		}
	}
	
	public void collision() {
		for(int i = 0; i< handler.object.size(); i++) {
			
			GameObject temObj = handler.object.get(i);
			if(temObj.getId() == ID.Enemy) {
				if (getBoundary().intersects(temObj.getBoundary())) {
					//collision between Player and Enemy
					//This removed the players health. Set size accordingly
					
					if(Game.whoIsYourDady == false) {
						Health.health -= Game.AIHarm;
					}
					
					Health.health -= Game.AIHarm;
					if(Health.health < 0) {
						this.handler.removeObject(this);
						Game.over = true;
						Health.health = 0;
					}
				}	
			}
			if(temObj.getId() == ID.Gold) {
				if (getBoundary().intersects(temObj.getBoundary())) {
					//collision between Player and Gold
					ScoreBoard.progress += 25;
					ScoreBoard.score += 50;
					//Create a completion screen for if gold score is greater than 175.
				}
			}
			if(temObj.getId() == ID.Wall) {				
				if( (x == temObj.x + 32) && ((y >= temObj.y && y <= temObj.y+32) || (temObj.y <= y+32 && y+32 <= temObj.y+32) )  ) {					
					x += 4;
					left = true;
				} else if((x+32 == temObj.x) && ((temObj.y <= y && y <= temObj.y+32) || (temObj.y <= y+32 && y+32 <= temObj.y+32) )  ) {
					x -= 4;
					right = true;
				} else if(( (temObj.x<=x && x<= temObj.x+32) || (temObj.x<=x+32 && x<= temObj.x+32) ) && (y == temObj.y+32) ) {
					up = true;
					y += 4;
				} else if(( (temObj.x <= x && x <= temObj.x+32) || (temObj.x <= x+32 && x <= temObj.x+32) ) && (y+32 == temObj.y) ) {
					down = true;
					y -= 4;
				
				}
				else {
					left = false;
					right = false;
					up = false;
					down = false;
				}
			}
			
			if(temObj.getId() == ID.Door) {
				if (getBoundary().intersects(temObj.getBoundary())) {
				  if(Game.currentRoomNo==0 && (Game.doorTouchFlag==false)) Game.nextRoomNo=1;
				  if(Game.currentRoomNo==1 && Game.doorTouchFlag==false) Game.nextRoomNo=0;
				  //System.out.println("Touched the Door");
				  //Game.DoorTouchFlag=1;
				}
				else {
					Game.doorTouchFlag=false;
				}
			}
		}
	}
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] playerImg = null; 
	static {
		playerImg = new Image[] {
				tk.getImage(Player.class.getResource("Images/Player.png")), 
				
				tk.getImage(Player.class.getResource("Images/PlayerWithGunUp.png")), //1
				tk.getImage(Player.class.getResource("Images/PlayerWithGunUpLeft.png")), //2
				tk.getImage(Player.class.getResource("Images/PlayerWithGunLeft.png")), //3
				tk.getImage(Player.class.getResource("Images/PlayerWithGunDownLeft.png")), //4
				tk.getImage(Player.class.getResource("Images/PlayerWithGunDown.png")), //5
				tk.getImage(Player.class.getResource("Images/PlayerWithGunDownRight.png")), //6
				tk.getImage(Player.class.getResource("Images/PlayerWithGunRight.png")), //7
				tk.getImage(Player.class.getResource("Images/PlayerWithGunUpRight.png")),	 //8
		};
	}		

	public void render(Graphics g) {	
		if(holdGun == false) g.drawImage(playerImg[0], x, y, null); 
		if(holdGun == true) {
			
			if(speedX == 0 && speedY == 0) g.drawImage(playerImg[0], x, y, null); 
			
			if(this.lastFacing==1) g.drawImage(playerImg[1], x, y, null); //1
			if(this.lastFacing==2) g.drawImage(playerImg[2], x, y, null); //2
			if(this.lastFacing==3) g.drawImage(playerImg[3], x, y, null); //3
			if(this.lastFacing==4) g.drawImage(playerImg[4], x, y, null); //4
			if(this.lastFacing==5) g.drawImage(playerImg[5], x, y, null); //5
			if(this.lastFacing==6) g.drawImage(playerImg[6], x, y, null); //6
			if(this.lastFacing==7) g.drawImage(playerImg[7], x, y, null); //7
			if(this.lastFacing==8) g.drawImage(playerImg[8], x, y, null); //8
		} 
	}
	public void face() {
		if(speedX == 0 && speedY == 0) this.facing=0; //0
		if(speedX == 0 && speedY < 0) this.facing=1; //1
		if(speedX < 0 && speedY < 0) this.facing=2; //2
		if(speedX < 0 && speedY == 0) this.facing=3; //3
		if(speedX < 0 && speedY > 0) this.facing=4; //4
		if(speedX == 0 && speedY > 0) this.facing=5; //5
		if(speedX > 0 && speedY > 0) this.facing=6; //6
		if(speedX > 0 && speedY == 0) this.facing=7; //7
		if(speedX > 0 && speedY < 0) this.facing=8; //8
	}
	
public void faceRemain() {
		
		if (this.holdGun == false) {		                      //only when player holds a gun, direction matters
			this.lastFacing=0;
		}
		else {													// when hold a gun
			if(this.facing!=0) this.lastFacing = this.facing; //store the last moving direction when player stops
			
			if(this.facing==0) {
				if (this.lastFacing == 1 || this.lastFacing == 2) { 
					this.lastFacing=1;
				}
				else if(this.lastFacing == 3 || this.lastFacing == 4) {
					this.lastFacing=3;
				}
				else if(this.lastFacing == 5 || this.lastFacing == 6) {
					this.lastFacing=5;
				}
				else {
				
					this.lastFacing=7;
				}
			}
		}
	}
}