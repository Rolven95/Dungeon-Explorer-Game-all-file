import java.awt.Canvas; //This is not needed anymore
import java.awt.Dimension;
import javax.swing.JFrame;

public class Window extends Canvas{ //We therefore do not need to extend canvas. 

	private static final long serialVersionUID = -240840600533728354L;
	
	public Window(int width, int height, String title, Game game) {
		
		JFrame frame = new JFrame(title);
		
		//Here we create the dimensions of our window
		frame.setMinimumSize(new Dimension(width, height));
		
		//This makes sure we can close the window once it has been open. It stops all of the operations.
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//This makes sure that we cannot resize the window once it has been open
		frame.setResizable(false);
		//This makes sure the screen starts in the middle of the screen
		frame.setLocationRelativeTo(null);
		//This makes sure the game is added to the pane
		frame.add(game);
		//This makes sure that the window pane is actually visible
		frame.setVisible(true);
		//Here we call the start method 
		game.start();
	}
}
