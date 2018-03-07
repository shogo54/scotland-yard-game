package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ScotlandYardFrame.GameMaster;
import graph.Node;
import player.MrX;
import player.Person.Ticket;

public class RandomAI implements AI {

	GameMaster gmCopy;
	MrX mrx;
	
	public RandomAI(MrX mrx) {
		this.mrx=mrx;
	}

	public void update(GameMaster g){
		gmCopy=g;
	}
	
	public void decide() throws Exception {
		// create a list of the possible next positions
		List<Node> nextNodeList = new ArrayList<Node>();
		for (Node n : mrx.getPosition().getNeighbors()) {
			nextNodeList.add(n);
		}
		randomChoose(nextNodeList);
	}

	protected void randomChoose(List<Node> nextNodeList) throws Exception {
		// decide where to go randomly
		Random randomNode = new Random();
		while (true) {
			// special case list is empty - throw exception
			if(nextNodeList.size()==0)
				throw new Exception();

			// decide where to go
			int nodeIndex = randomNode.nextInt(nextNodeList.size());
			Node next = nextNodeList.get(nodeIndex);
			// if that position is already occupied,
			// remove that node from possible list and choose again
			if (next.getOccupied()) {
				nextNodeList.remove(next);
				continue;
			}

			// create a list of the possible ways to go to the next node
			List<String> wayList = new ArrayList<String>();
			for (String way : mrx.getPosition().getWay(next)) {
				if (mrx.hasTicket(Ticket.valueOf(way)))
					wayList.add(way);
			}
			// if there is no ticket to go to that node,
			// remove that node from possible list and choose again
			if (wayList.size() == 0) {
				nextNodeList.remove(next);
				continue;
			}

			// decide which ticket (way) to use randomly
			Random randomWay = new Random();
			int wayIndex = randomWay.nextInt(wayList.size());
			mrx.useTicket(Ticket.valueOf(wayList.get(wayIndex)));
			mrx.changePosition(next);
			return;
		}
	}

}
