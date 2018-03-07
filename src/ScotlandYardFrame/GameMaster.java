package ScotlandYardFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import graph.Node;
import main.Main;
import player.Detective;
import player.MrX;
import player.Person;
import player.Person.Ticket;

public class GameMaster {

	private final String[] startPoints = { "13", "26", "29", "34", "50", "53", "91", "94", "103", "112", "117", "132",
			"138", "141", "155", "174", "197", "198" };

	private ScotlandYardGraph graph;
	private MrX mrx;
	private List<Detective> detectives;
	private List<Ticket> travelLog;
	private boolean gameOver;
	private Turn currentTurn;

	public GameMaster(ScotlandYardGraph g) {
		graph = g;
		mrx = new MrX("Mr.X", randomStartPoint(), mrxTickets());
		travelLog = new ArrayList<Ticket>();
		gameOver = false;

		detectives = new ArrayList<Detective>();
		detectives.add(new Detective("A", randomStartPoint(), detectiveTickets()));
		detectives.add(new Detective("B", randomStartPoint(), detectiveTickets()));
		detectives.add(new Detective("C", randomStartPoint(), detectiveTickets()));
		detectives.add(new Detective("D", randomStartPoint(), detectiveTickets()));
		detectives.add(new Detective("E", randomStartPoint(), detectiveTickets()));
	}

	// helper method to initialize
	private Node randomStartPoint() {
		Node node;
		while (true) {
			Random random = new Random();
			int r = random.nextInt(18);

			if (Main.DEBUG) {
				System.out.println("testcode: GameMaster-randomStartPoint()");
				System.out.println("random num is " + r + ": node name is " + startPoints[r] + "\n");
			}

			node = graph.getGraph().getOrCreateNode(startPoints[r]);
			if (!node.getOccupied() && !node.getHiding())
				break;
		}
		return node;
	}

	// helper method to initialize
	private Map<Ticket, Integer> mrxTickets() {
		Map<Ticket, Integer> retVal = new TreeMap<Ticket, Integer>();
		retVal.put(Ticket.TAXI, 4); // 4
		retVal.put(Ticket.BUS, 3); // 3
		retVal.put(Ticket.UNDERGROUND, 3); // 3
		retVal.put(Ticket.DOUBLE, 2); // 2
		retVal.put(Ticket.BLACK, 5); // 5
		return retVal;
	}

	// helper method to initialize
	private Map<Ticket, Integer> detectiveTickets() {
		Map<Ticket, Integer> retVal = new TreeMap<Ticket, Integer>();
		retVal.put(Ticket.TAXI, 10); // 10
		retVal.put(Ticket.BUS, 8); // 8
		retVal.put(Ticket.UNDERGROUND, 4); // 4
		retVal.put(Ticket.DOUBLE, 0); // 0
		retVal.put(Ticket.BLACK, 0); // 0
		return retVal;
	}

	public void moveMrX() throws Exception {
		mrx.seeTheBoard(this);
		mrx.move();
		// update travel log
		travelLog = mrx.getTravelLog();
	}

	public void showMrXTickets() {
		mrx.showLeftTickets();
	}

	public void showTravelLog() {
		for (int i = 0; i < travelLog.size(); i++) {
			System.out.println(i + 1 + ":" + travelLog.get(i));
		}
	}

	public Person checkWinner() {
		int stuck = 0;
		for (Detective d : detectives) {
			// count the number of detectives who cannot move
			if (d.possibleMoves().size() == 0)
				stuck++;
			// victory condition of detectives
			if (d.getPosition().getHiding() == true)
				return d;
		}
		// victory condition of MrX
		if (stuck == detectives.size()) {
			return mrx;
		}
		//victory condition of detectives
		if(mrx.possibleMoves().size()==0){
			return detectives.get(0);
		}
		
		
		return null;
	}

	// getter
	public MrX getMrx() {
		return mrx;
	}

	// getter
	public List<Detective> getDetectives() {
		return detectives;
	}

	public boolean isGameOver() {
		return gameOver;
	}

	public void setGameOver(boolean b) {
		gameOver = b;
	}

	public void gameStart() {
		currentTurn = Turn.MrX;
	}

	public void nextTurn() {
		currentTurn = currentTurn.next();
	}

	public Turn getTurn() {
		return currentTurn;
	}

	public boolean doesMrxShowUp() {
		int num = travelLog.size();
		if (num == 3 || num == 8 || num == 13 || num == 18) {
			return true;
		}
		return false;
	}

}
