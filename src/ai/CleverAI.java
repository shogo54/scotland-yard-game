package ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import graph.Node;
import player.Detective;
import player.MrX;

/**
 * Class to represent an AI 
 * Inherit from RandomAI class (so implement AI)
 * 
 * This class has the decision making based on the current positions of 
 * all Detectives. It tries to avoid the places where the Detectives can be. 
 * 
 * It gets the information where to move by the user
 * 
 * @author Shogo Akiyama
 *
 */
public class CleverAI extends RandomAI{

	public CleverAI(MrX mrx) {
		super(mrx);
	}

	@Override
	public void decide() throws Exception {
		List<Node> list = new ArrayList<Node>();
		List<Node> pureList = new ArrayList<Node>();
		for (Node n : mrx.getPosition().getNeighbors()) {
			list.add(n);
			pureList.add(n);
			//test message
			//System.out.println(n.getName() +"is added");
		}

		//make a set of all detectives next possible positions
		Set<Node> detP = new TreeSet<Node>();
		for (Detective d : gmCopy.getDetectives()) {
			for (Node n : d.possibleMoves()) {
				detP.add(n);
			}
		}

		//if MrX's next position is the same as detectives' one, 
		//remove that position from the choice
		for (Node n : detP) {
			if (list.contains(n)) {
				list.remove(n);
				//test message
				//System.out.println(n.getName()+"is removed");
			}
		}

		//if list is empty, recover the list before removed some positions 
		if (list.size() == 0)
			list = pureList;

		super.randomChoose(list);
	}

}
