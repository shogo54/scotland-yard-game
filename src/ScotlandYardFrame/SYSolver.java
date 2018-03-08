package ScotlandYardFrame;

import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import graph.Graph;
import graph.Node;

/**
 * 
 * @author Shogo Akiyama
 *
 */
public class SYSolver {
	/**
	 * Read a Scotland Yard graph file from an input stream. The file contains a
	 * header line with the number of locations on the game board (which is 199)
	 * then the number of links between those locations (469). Locations are
	 * always numbered starting at 1.
	 * 
	 * Each of the 469 links will contain the two endpoints of the link followed
	 * by the transportation type, which is either T for Taxi, B for Bus, or U
	 * for Underground (subway).
	 * 
	 * See files/scotmap.txt for the full file. The first few lines of the file
	 * look like this:
	 * 
	 * <pre>
	 * 199 469
	 * 1 8 T
	 * 1 9 T
	 * 1 58 B
	 * 1 46 B
	 * 1 46 U
	 * 2 20 T
	 * ...
	 * </pre>
	 * 
	 * Keep in mind that some nodes may be connected by both bus and by taxi.
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public static Graph readGraphFromFile(InputStream in) throws IOException {
		Graph retVal = new Graph();
		Scanner sc = new Scanner(in);
		int numNode = sc.nextInt();
		int numEdge = sc.nextInt();
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.isEmpty())
				continue;
			int index = line.indexOf(" ");
			int lastIndex = line.lastIndexOf(" ");
			Node n1 = retVal.getOrCreateNode(line.substring(0, index));
			Node n2 = retVal.getOrCreateNode(line.substring(index + 1, lastIndex));
			n2.addUndirectedEdgeToNode(n1, line.substring(lastIndex + 1));
		}
		if (!(retVal.getNumOfAllNodes() == numNode))
			throw new IOException("num of nodes does not match: " + retVal.getNumOfAllNodes());
		if (!(retVal.getNumOfAllWays() == numEdge))
			throw new IOException("num of edges does not match: " + retVal.getNumOfAllWays());
		return retVal;
	}

	/**
	 * 
	 * Read from the given inputstream a mapping of numbered locations on the
	 * Scotland Yard game board to their corresponding x y coordinates, in a
	 * format like this:
	 * 
	 * <pre>
	 * 199
	 * 1 143 45
	 * 2 329 49
	 * 3 406 40
	 * 4 504 44
	 * 5 784 46
	 * 6 844 50
	 * 7 941 52
	 * 8 97 79
	 * 9 169 84
	 * 10 350 97
	 * </pre>
	 * 
	 * The first number (i.e. 199) is the number of points, then each line is
	 * the location number (from 1 to 199) then the x and y coordinates.
	 * 
	 * So for example:
	 * 
	 * 3 406 40
	 * 
	 * means that location 3 is at (406, 40). The x, y coordinates are in
	 * pixels.
	 * 
	 * The full file is in files/scotpos.txt.
	 * 
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static Map<String, Point> readPositionPoints(String filename) throws IOException {
		Map<String, Point> map = new TreeMap<String, Point>();
		Scanner sc = new Scanner(new File(filename));
		int numNode = sc.nextInt();
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			if (line.isEmpty())
				continue;
			int index = line.indexOf(" ");
			int lastIndex = line.lastIndexOf(" ");
			Point p = new Point(Integer.parseInt(line.substring(index + 1, lastIndex)),
					Integer.parseInt(line.substring(lastIndex + 1)));
			map.put(line.substring(0, index), p);
		}

		if (!(map.size() == numNode)) {
			System.out.println("num of the nodes does not match. return null: " + map.size());
			return null;
		}
		return map;
	}

	private static void put(Map<Integer, Set<String>> map, Integer key, String value) {
		if (map.get(key) == null) {
			Set<String> set = new TreeSet<String>();
			set.add(value);
			map.put(key, set);
		} else {
			map.get(key).add(value);
		}
	}

	/**
	 * Get the next 5 possible moves that Mr X could make starting at the given
	 * start node.
	 * 
	 * Assume that we don't know or care about the types of transportation that
	 * Mr X is using.
	 * 
	 * @param g
	 * @param start
	 * @return
	 */
	public static Map<Integer, Set<String>> getNextFivePossibleMoves(Graph g, String start) {
		Map<Integer, Set<String>> retVal = new TreeMap<Integer, Set<String>>();
		put(retVal, 0, start);

		for (int i = 0; i <= 4; i++) {
			for (String s : retVal.get(i)) {
				for (Node next : g.getOrCreateNode(s).getNeighbors()) {
					put(retVal, i + 1, next.getName());
				}
			}
		}
		return retVal;
	}

	/**
	 * Get the next 5 possible moves that Mr X could make starting at the given
	 * start node.
	 * 
	 * Assume that that given list of transportation types tells us what type of
	 * transportation Mr X uses for each move.
	 * 
	 * The given list of transportTypes contains 5 strings that are either
	 * "any", "taxi", "bus" or "underground". If a transport type is "any" then
	 * any type of transportation can be used.
	 * 
	 * If the type of transportation makes a move impossible, then the set of
	 * possible locations should be empty
	 * 
	 * 
	 * @param g
	 * @param start
	 * @param transportTypes
	 * @return
	 */
	public static Map<Integer, Set<String>> getNextFivePossibleMoves(Graph g, String start,
			List<String> transportTypes) {
		Map<Integer, Set<String>> retVal = new TreeMap<Integer, Set<String>>();
		put(retVal, 0, start);

		for (int i = 0; i <= 4; i++) { // the number of moves
			for (String s : retVal.get(i)) { // previous node
				Node preNode = g.getOrCreateNode(s);
				// neighbors of the previous node
				for (Node next : preNode.getNeighbors()) {
					String type = transportTypes.get(i);
					if (type.equals("any"))
						put(retVal, i + 1, next.getName());
					else {
						// type of transportation
						for (String way : preNode.getWay(next)) {
							if (type.equals(convert(way)))
								put(retVal, i + 1, next.getName());
						}
					}
				}
			}
			if(retVal.get(i+1)==null){
				Set<String> set = new TreeSet<String>();
				retVal.put(i+1, set);
			}
		}
		return retVal;
	}

	// helper method
	private static String convert(String s) {
		String retVal = "";
		switch (s) {
		case "T":
			retVal = "taxi";
			break;
		case "B":
			retVal = "bus";
			break;
		case "U":
			retVal = "underground";
			break;
		default:
			retVal = null;
			System.out.println("error. transport type (String) is not proper : " + s);
		}
		return retVal;
	}

	private SYSolver() {
		// private constructor to prevent creating instances
		// this class exists only to hold static methods
	}

}
