import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.util.Random;

import javax.swing.JOptionPane;

/** This is the main class for our game. We are extending the Java abstract window toolkit canvas, 
 * which represents a rectangular window.
 *
 */

//We implement the Runnable interface to use the Thread execution
public class Game extends Canvas implements Runnable{
	
	private static final long serialVersionUID = 7580815534084638412L;
	
	//Dimensions for the window pane
	public static final int WIDTH = 1024;
	public static final int HEIGHT = ((WIDTH/4) * 3);
	
	//This determines whether the game is over. 
	public static boolean over;
	
	public static boolean restart = false;
	
	public static int AISpeed;
	public static int AIHarm;
	
	public static boolean isAmmo = false;
	public static boolean isAmmoBuilt = false;
	public static boolean whoIsYourDady = false;
	//At any moment this decides which instance of the room the player is in
	public static int currentRoomNo;
	//This will determine the room the player is going to enter once interacting with a door
	public static int nextRoomNo;
	//This enables the room to be changed. Without it, the player will constantly be changed between rooms
	//when they share an intersection with a door.
	public static boolean doorTouchFlag; 
	
	private Font gameOver = new Font("TimesRoman", Font.BOLD, 50);
	
	//We create a thread here from the Runnable interface that will run the game
	private Thread thread;
	private boolean running = false;
	
	private Random r;
	//
	private Handler handler;
	//This Health object will keep track of the players health when they collide with enemies
	private Health health;
	
	private Ammo ammo;
	//This ScoreBoard object will keep track of the progress when the player collects gold
	
	private ScoreBoard progress;
	private ScoreBoard score;
	
	public static GameObject[][] RoomObj = new GameObject[2][256];
	
	public Game() {
		
		//This opens the game window and starts the game
		new Window(WIDTH, HEIGHT, "Dungeon Explorer", this);

		r = new Random();
		
		restart();
		restart();
		startMenu();
		restart();
	
	}
	
	//This initialises our thread of execution for the game.
	public synchronized void start() {
		//We create a new thread object for Game which will run the run method from Runnable
		thread = new Thread(this);
		//starts the execution of this thread from the start method in Thread
		thread.start();
		//Change the boolean value initially set to false above to true
		running = true;
	}
	
	public synchronized void stop() {
		try {
			//Waits for this thread to die or catches an exception. If it dies, set running back to false.
			thread.join();
			running = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 
	 * This is the game loop that has been taken from online. It is a common loop to use for two 
	 * dimensional games. Reference to come shortly. This essentially makes sure that the game updates itself.
	 */
	public void run() {
		this.requestFocus();
		long lastTime = System.nanoTime();
		//This is the amount of ticks the game will have in one second
		double amountOfTicks = 60.0;
		//this is 1/60 seconds
		double nanoSec = 1000000000 / amountOfTicks;
		double delta = 0;
		
		//So, while running is equal to true. That is, our thread is executing, update the game.
		while(running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / nanoSec;
			lastTime = now;
			//every one second we go forward with one tick
			while(delta >=1) {
				tick();
				delta--;
			}
			//The game only updates as the loop is running
			if(running) {
				render();
			}
		}
		stop();
	}
	
	//This method calls the objects that do not inherit the GameObject class as well as the handler, which takes 
	//into account all of the objects under the Game Object parent class.
	private void tick() {
		handler.tick();
		//health.tick(); // We do not need this to call the tick method of health anymore.
		progress.tick();
		score.tick();
		if(Game.doorTouchFlag == false) {
			roomChange(nextRoomNo);
		} 
		if(isAmmo == true) {
			System.out.println("in game");
			
			ammo  = new Ammo();
			
			//This specifies the time to generate the ammo
			isAmmo = false;
			//This makes sure the ammo is generated only once
			isAmmoBuilt = true;
			
		}
		if(isWin()) {
			winMenu();
		}
	}
	
	//This method currently displays a game over message when the player has lost all of their health. 
	public void testGameOver(Graphics g) {
		if (over == true) {
			g.setFont(gameOver);
			endMenu();
			//g.drawString("Game Over", 512 - 150, 512/4*3);
			//g.drawString("Score: " + ScoreBoard.score + " out of 700", 512 -240, 512/4*3 + 50);

		}
	}
	
	
	private void render() {
		BufferStrategy bs = this.getBufferStrategy();
		//This creates a buffer strategy which is initially set to null. 
		if(bs == null) {
			//Create a buffer strategy of 2
			this.createBufferStrategy(2);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, WIDTH, HEIGHT);
		
		handler.render(g);
		health.render(g);
		progress.render(g);
		score.render(g);
		
		if(isAmmoBuilt) {
			ammo.render(g);
		}
		
		this.testGameOver(g);
		
		g.dispose();
		bs.show();
	}
	
	public void fourWall() {
		//Left hand wall
		for (int i = 0; i <= HEIGHT/32; i++) { 
			handler.addObject(new Wall(0, 0+i*32, ID.Wall));
		}
		//Right hand wall
		for (int i = 0; i <= HEIGHT/32; i++) {
			handler.addObject(new Wall(WIDTH-32, 0+i*32, ID.Wall));
		}
		//Lower hand wall
		for (int i = 0; i <= WIDTH/32; i++) {
			handler.addObject(new Wall(i*32, HEIGHT - 52, ID.Wall));
		}
		//Upper hand wall
		for (int i = 0; i <= WIDTH/32; i++) {
			handler.addObject(new Wall(i*32, 0, ID.Wall));
		}
		for (int i = 0; i <= WIDTH/32; i++) {
			handler.addObject(new Wall(i*32, 32, ID.Wall));
		}
	}
	
	public void room0Setting() {
		Game.RoomObj[0][0]= new Door(850,600,ID.Door); //850 600
		Game.RoomObj[0][1]=new Gun(300 , 300, ID.Gun, handler);
		
		for(int i = 2; i < 6; i++) {
			 Game.RoomObj[0][i]= new Gold(r.nextInt(WIDTH-100),r.nextInt(HEIGHT-200)+100,ID.Gold,handler);
			}
			for(int i = 6; i < 20; i++) {
				 Game.RoomObj[0][i]= new Wall(256,256+(i-5)*32,ID.Wall);
			}
			for(int i = 20; i < 35; i++) {
				 Game.RoomObj[0][i]= new Wall(512+32*(i-20),512,ID.Wall);
			}
			for(int i = 35; i < 55; i++) {
				 Game.RoomObj[0][i]= new Wall(128+32*(i-35),256,ID.Wall);
			}
			
			Game.RoomObj[0][55]= new Enemy(32*12, 32*10, ID.Enemy, handler);
			Game.RoomObj[0][56]=new Enemy(32*14, 32*7, ID.Enemy, handler);
			Game.RoomObj[0][57]=new Enemy(32*6, 32*6, ID.Enemy, handler);
			
	}
	
	public void room1Setting() {
		Game.RoomObj[1][0]= new Door(100-32,600,ID.Door);
		for(int i = 1; i < 5; i++) {
			 Game.RoomObj[1][i]= new Gold(r.nextInt(WIDTH-100),r.nextInt(HEIGHT-200)+100,ID.Gold,handler);
			}
			for(int i = 5; i < 20; i++) {
				 Game.RoomObj[1][i]= new Wall(128+32,128+128+(i-5)*32,ID.Wall);
			}
			for(int i = 20; i < 35; i++) {
				 Game.RoomObj[1][i]= new Wall(512+128-32+32*(i-20),512-128+32,ID.Wall);
			}
			for(int i = 35; i < 50; i++) {
				 Game.RoomObj[1][i]= new Wall(128+64+128+64,(i-35)*32,ID.Wall);
			}
			for(int i = 50; i < 55; i++) {
				 Game.RoomObj[1][i]= new Wall(128+32+256+64+128,128+32+128+128+128+(i-50)*32,ID.Wall);
			}
			for(int i = 55; i < 60; i++) {
				 Game.RoomObj[1][i]= new Wall(128+32+256+64+128+128, 128+(i-55)*32,ID.Wall);
			}
			
			
			
			Game.RoomObj[1][61] = new Enemy(32*20, 32*10, ID.Enemy, handler);
			Game.RoomObj[1][62] = new Enemy(32*7, 32*5, ID.Enemy, handler);
			Game.RoomObj[1][64] = new Enemy(32*10, 32*7, ID.Enemy, handler);
			Game.RoomObj[1][63] = new Enemy(32*12, 32*7, ID.Enemy, handler);
			Game.RoomObj[1][60] = new Enemy(32*16, 32*8, ID.Enemy, handler);
				
	}
	
	//Method for the changing of rooms
	public void roomChange(int NextRoom) {
		if(NextRoom!=Game.currentRoomNo) {
			for(int i=0; RoomObj[Game.currentRoomNo][i]!=null;i++){ //odd objects remove
				 handler.removeObject(RoomObj[Game.currentRoomNo][i]);
			}
			for(int i=0; RoomObj[NextRoom][i]!= null; i++){ // add new room objects
				 handler.addObject(RoomObj[NextRoom][i]);
			}
			Game.doorTouchFlag = true;
			Game.currentRoomNo = NextRoom;
			
		}
	}
	
	public void restart() {
		handler = new Handler();
		
		//This adds in the key input functionality to move the player
		this.addKeyListener(new KeyInput(handler));
		health = new Health();
		Health.health = 100;
		doorTouchFlag = false;
		currentRoomNo = 0;
		nextRoomNo = 0;
		over = false;
		progress = new ScoreBoard();
		score = new ScoreBoard();
		ScoreBoard.progress = 0;
		ScoreBoard.score = 0;
		Player.holdGun = false;
		Ammo.AmmoRemain = 5;
		Gun.ammo = 5;
		
		//The handler here can add an object of a specified id. Here the player.
		handler.addObject(new Player(76, 668, ID.Player, handler));
		fourWall();

		room0Setting();
		room1Setting();
		
		for(int i=0; RoomObj[0][i]!=null;i++) {
			handler.addObject(Game.RoomObj[0][i]); //adds room 0 objects
		} 
		isAmmo = false;
		isAmmoBuilt = false;
	}
	
	public void startMenu() {
		
		Object[] options = { "Hard", "Easy" ,"DADY" };
		int response = JOptionPane.showOptionDialog(this, "Select Difficulty", "",
				JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
		if (response == 0) {
			Game.AISpeed = 8;
			Game.AIHarm = 3;
			Game.whoIsYourDady = false;
			//restart();
		} else if (response == 1){
			Game.AISpeed = 4;
			Game.AIHarm = 3;
			Game.whoIsYourDady = false;
			//restart();
		}else if(response == 2){
			Game.AISpeed = 1;
			Game.AIHarm = 0;
			Game.whoIsYourDady = true;
		}
	}
	
	public void endMenu() {
		
		Object[] options = { "Restart: Hard", "Restart: Easy" , "DADY" ,  "QUIT"};
		int response = JOptionPane.showOptionDialog(this, "GAME OVER. You scored: " + ScoreBoard.score + " out of 1200", "",
				JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
		if (response == 0) {
			Game.AISpeed = 8;
			Game.AIHarm = 3;
			Game.whoIsYourDady = false;
			restart();
		} else if (response == 1){
			Game.AISpeed = 4;
			Game.AIHarm = 3;
			Game.whoIsYourDady = false;
			restart();
		} else if(response == 2) {
			Game.whoIsYourDady = true;
			Game.AISpeed = 1;
			Game.AIHarm = 0;
			restart();
		}
		else {
			System.exit(1);
		}
	}
	
	public void winMenu() {
		
		Object[] options = { "Restart: Hard", "Restart: Easy","Restart: DADY", "QUIT"};
		int response = JOptionPane.showOptionDialog(this, "YOU WIN! You scored: " + ScoreBoard.score + " out of 1200.", "",
				JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, null,
				options, options[0]);
		if (response == 0) {
			Game.AISpeed = 8;
			Game.AIHarm = 3;
			Game.whoIsYourDady = false;
			restart();
		} else if (response == 1){
			Game.AISpeed = 4;
			Game.AIHarm = 3;
			Game.whoIsYourDady = false;
			restart();
		}else if (response == 2){
			Game.whoIsYourDady = true;
			restart();
		}
		
		else{
			
			System.exit(1);
		}
		
	}

	public boolean isWin() {
		for(int i=0; RoomObj[0][i]!=null;i++) {
			if(RoomObj[0][i].id==ID.Gold) return false;
		}
		for(int i=0; RoomObj[1][i]!=null;i++) {
			if(RoomObj[1][i].id==ID.Gold) return false;	
		}
	return true;
	}
	
	public static void main(String args[]) {
		new Game();
	}
	
}