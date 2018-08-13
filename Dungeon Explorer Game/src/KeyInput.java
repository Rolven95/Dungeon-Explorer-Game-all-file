
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {
	
	private Handler handler;
	
	public KeyInput(Handler handler) {
		this.handler = handler;
	}
	
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		
		//We iterate through each game object in order to call the accessor method from the game object class within
		//its linked list.
		//This way could implement the use of having two players, which would satisfy the multiplayer element
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Player) {
				//key events for player 1
				
				if(key == KeyEvent.VK_UP && Player.up == false) tempObject.setSpeedY(-4);
				if(key == KeyEvent.VK_DOWN && Player.down == false) tempObject.setSpeedY(+4);
				if(key == KeyEvent.VK_LEFT && Player.left == false) tempObject.setSpeedX(-4);
				if(key == KeyEvent.VK_RIGHT && Player.right == false) tempObject.setSpeedX(4);
				
				if(key == KeyEvent.VK_G) Player.holdGun = false;
				if(key == KeyEvent.VK_SPACE) Player.shoot = true;
				
				if(key == KeyEvent.VK_R) {
					Game.over = false;
					Game.restart = true;
				}
			}
		}
		
	}
	
	public void keyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		
		for(int i = 0; i < handler.object.size(); i++) {
			GameObject tempObject = handler.object.get(i);
			
			if(tempObject.getId() == ID.Player) {
				//key events for player 1
				if(key == KeyEvent.VK_UP) tempObject.setSpeedY(0);
				if(key == KeyEvent.VK_DOWN) tempObject.setSpeedY(0);
				if(key == KeyEvent.VK_LEFT) tempObject.setSpeedX(0);
				if(key == KeyEvent.VK_RIGHT) tempObject.setSpeedX(0);
			}
		}
		
		if(key == KeyEvent.VK_ESCAPE) System.exit(1);
		
		
	}
	
}
