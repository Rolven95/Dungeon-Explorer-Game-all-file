import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class ScoreBoard {
	
	public static int progress = 0;
	public static int score = 0;
	
	public void tick() {
	}
	
	public void render(Graphics g) {
		
		g.setColor(Color.gray);
		g.fillRect(1024 - 218, 15, 204, 34);
		g.setColor(Color.YELLOW);
		g.fillRect(1024 - 216, 17, progress, 30);
		
		g.setFont(new Font("TimesRoman", Font.BOLD, 20));
		g.drawString("Score: " + score, 1024/2 - 50, 82);
		
	}
}