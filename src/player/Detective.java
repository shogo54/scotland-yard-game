package player;

import java.util.Map;

import graph.Node;

/**
 * Class to represent a detective who is seeking Mr.X
 * Inherit from Person class
 * 
 * It gets the information where to move by the user
 * 
 * @author Shogo Akiyama
 *
 */
public class Detective extends Person{

	public Detective(String s, Node startPoint, Map<Ticket, Integer> initialTickets) {
		super(s, startPoint, initialTickets);
		position.setOccupied(true);
	}
	
	public void moveTo(Node next, Ticket t, MrX m) {
		//precondition
		if(!position.getNeighbors().contains(next)){
			System.out.println("error: the moving node is not connected to current node");
			return;
		}
		
		// use a ticket and give it to MrX
		tickets.put(t, tickets.get(t)-1);
		m.receiveTicket(t);
		
		//change the position from current node to next node
		position.setOccupied(false);
		position=next;
		position.setOccupied(true);
	}
	
}
