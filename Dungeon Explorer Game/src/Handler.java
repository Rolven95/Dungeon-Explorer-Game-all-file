import java.awt.Graphics;
import java.util.LinkedList;

public class Handler {
	
	//We create a linked list of game objects to iterate through the game objects that inherit this class.
	//This will be helpful when we implement collision methods to detect if two objects are, at some point in time, 
	//intersecting. For example, with the method to check whether our gold is intersecting any objects at the 
	//instantiation of each room.
	LinkedList<GameObject> object = new LinkedList<GameObject>();
	
	
	public void tick() {
		//It is not possible to access a particular element of a linked list, so we iterate through each object in 
		//the list to use that object. We then use the tick method of their own class. 
		for(int i = 0; i < object.size(); i++) {
			GameObject tempObject = object.get(i);
			tempObject.tick();
		}
	}
	
	//This does the same thing, but for the particular Game Objects render method.
	public void render(Graphics g) {
		for(int i = 0; i < object.size(); i++) {
			GameObject tempObject = object.get(i);
			tempObject.render(g);
		}
	}
	
	//This will allow us to add particular objects to the game
	public void addObject(GameObject object) {
		this.object.add(object);
	}
	
	//This will allow us to remove particular objects from the game
	public void removeObject(GameObject object) {
		this.object.remove(object);
	}
}
