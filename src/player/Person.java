package player;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

import graph.Node;

public abstract class Person {
	protected String name;
	protected Node position;
	protected Map<Ticket, Integer> tickets;

	// inner enum
	public enum Ticket {
		TAXI, BUS, UNDERGROUND, BLACK, DOUBLE,
	}

	public Person(String s, Node startPoint, Map<Ticket, Integer> initialTickets) {
		name = s;
		position = startPoint;
		tickets = initialTickets;
	}

	public Set<Node> possibleMoves() {
		Set<Node> retVal = new TreeSet<Node>();
		for (Node next : position.getNeighbors()) {
			for (String way : position.getWay(next)) {
				if (next.getOccupied())
					continue;
				// test
				// System.out.println(next.getName() + " " + way + " " +
				// hasTicket(Ticket.valueOf(way)));
				if (!hasTicket(Ticket.valueOf(way)))
					continue;
				retVal.add(next);
			}
		}
		return retVal;
	}

	public void showLeftTickets() {
		for (Entry<Ticket, Integer> entry : tickets.entrySet()) {
			if (this instanceof Detective)
				if (entry.getKey() == Ticket.BLACK || entry.getKey() == Ticket.DOUBLE)
					continue;

			System.out.printf("%-12s %2d" + "\n", entry.getKey(), entry.getValue());
		}
	}

	public boolean hasTicket(Ticket t) {
		if (tickets.get(t) >= 1)
			return true;
		return false;
	}

	public int getNumTicket(Ticket t) {
		return tickets.get(t);
	}

	public String getName() {
		return name;
	}

	public Node getPosition() {
		return position;
	}
}
