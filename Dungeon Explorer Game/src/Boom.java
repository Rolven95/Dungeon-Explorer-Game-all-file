import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.util.Timer;
import java.util.TimerTask;

public class Boom extends GameObject{
	Handler handler;
	private boolean oneTimeRemove = false;
	public Boom(int x, int y, ID id, Handler handler) {
		super(x, y, id);
		this.handler = handler;
	}

	public void tick() {
	}

	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] boomImg = null; 
	static {
		boomImg = new Image[] {
				tk.getImage(Gold.class.getResource("Images/Boom.png")),
	};
	}		

	public void render(Graphics g) {
	g.drawImage(boomImg[0], x, y, null);
       if(this.oneTimeRemove == false) {
    	   		this.oneTimeRemove = true;
    	   		timer();
       }
	}
	
	public void timer() {
		Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                remove();
                this.cancel();
                            }
        }, 80);
	}
	
	public void remove(){
		handler.removeObject(this);
	}

	public Rectangle getBoundary() {
		return new Rectangle(x,y,32,32);
	}

	@Override
	public void collision() {
	}
	
}
