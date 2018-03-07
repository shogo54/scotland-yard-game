package player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ScotlandYardFrame.GameMaster;
import ai.AI;
import ai.CleverAI;
import ai.RandomAI;
import graph.Node;
import main.Main;

public class MrX extends Person {
	protected List<Ticket> travelLog;
	protected AI brain;

	public MrX(String s, Node startPoint, Map<Ticket, Integer> initialTickets) {
		super(s, startPoint, initialTickets);
		position.setHiding(true);
		travelLog = new ArrayList<Ticket>();

		// decide which brain this mrx equips based on Main.MRXTYPE
		switch (Main.MRXTYPE) {
		case Clever:
			brain = new CleverAI(this);
			break;
			
		case Random:
			brain = new RandomAI(this);
			break;
		}
	}

	public void useDoubleLoop() {
		
	}

	public void move() throws Exception {
		if(this.possibleMoves().size()==0)
			throw new Exception();
		brain.decide();
	}

	public void changePosition(Node next) {
		position.setHiding(false);
		position = next;
		position.setHiding(true);
	}

	public void useTicket(Ticket t) {
		travelLog.add(t);
		tickets.put(t, tickets.get(t) - 1);
	}

	public void receiveTicket(Ticket t) {
		tickets.put(t, tickets.get(t) + 1);
	}

	public List<Ticket> getTravelLog() {
		return travelLog;
	}

	public void seeTheBoard(GameMaster g) {
		brain.update(g);
	}
}
