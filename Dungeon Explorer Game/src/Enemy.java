import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

public class Enemy extends GameObject{
	
	Handler handler;
	
	public boolean stop;
	
	public Enemy(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		//We have amended this so that the speed can easily be changed for each different game setting.
		this.speedX = Game.AISpeed;
		this.speedY = Game.AISpeed;
		this.handler = handler;
		stop = false;
	}
	

	public Rectangle getBoundary() {
		return new Rectangle(x, y, 32, 32);
	}
	
	public void tick() {		
		collision();
		
		//Within each tick, the enemy moves whatever speedX and speedY has been set to. 
		if(stop == false) {
			x += speedX;
			y += speedY;
		}
		
		//Have the enemy move upwards and downwards on the map
		if(y <= 34 || y >= Game.HEIGHT - 110) {
			speedY = speedY*(-1);
		} 
		//The same but for left to right
		if (x <= 0 || x >= Game.WIDTH - 60) {
			speedX = speedX*(-1);
		}
	}

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] monsterImg = null; 
	static {
		monsterImg = new Image[] {
			tk.getImage(Enemy.class.getResource("Images/Monster.png")),
		};
	}		
			
	public void render(Graphics g) {
		g.drawImage(monsterImg[0], x, y, null);
	}
	
	public void collision() {
		for(int i = 0; i< handler.object.size(); i++) {
			GameObject temObj = handler.object.get(i);
			
			if(temObj.getId() == ID.Boom) {
				if (getBoundary().intersects(temObj.getBoundary())) {
					ScoreBoard.score += 100;
					handler.removeObject(this);
				}
			}
			if(temObj.getId() == ID.Enemy) {
				if (getBoundary().intersects(temObj.getBoundary())&& this != temObj) {
					speedX=speedX*-1;
					speedY=speedY*-1;
				}
			}
			
			if(temObj.getId() == ID.Wall) {				
				if( (x > temObj.x) && (x <= temObj.x + 32)  && ((temObj.y <= y && y <= temObj.y+32) || (temObj.y <= y+32 && y+32 <= temObj.y+32) )  ) {					
					stop = true;		
					speedX = speedX * -1;	
				    stop = false;	
				}else if((x<temObj.x)&&(x+32 >= temObj.x) && ((temObj.y <= y && y <= temObj.y+32) || (temObj.y <= y+32 && y+32 <= temObj.y+32) )  ) {
					stop = true;
					speedX = speedX * -1;
					stop = false;
				}else if(( (temObj.x<=x && x<= temObj.x+32) || (temObj.x<=x+32 && x<= temObj.x+32) ) && (y>temObj.y)&&(y <= temObj.y+32) ) {
					stop = true;
					speedY = speedY * -1;
					stop = false;
				}else if(( (temObj.x<=x && x<= temObj.x+32) || (temObj.x<=x+32 && x<= temObj.x+32) ) && (y+32 == temObj.y) ) {
					stop = true;
					speedY = speedY * -1;
				    stop = false;
				}
			}
		}
	}
}