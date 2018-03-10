package game;

import java.awt.Point;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

import graph.Graph;
import graph.Node;

/**
 * Class to represent a graph of Scotland Yard
 * 
 * @author Shogo Akiyama
 *
 */
public class ScotlandYardGraph {

	// Map from location names (such as "1") to their (x, y) coordinates on the
	// screen
	// Use the built-in Point class in Java
	private Map<String, Point> pointMap;
	// the graph
	private Graph graph;

	public ScotlandYardGraph() throws IOException {
		// read the map of locations to points
		pointMap = readPositionPoints("files/scotpos.txt");
		// read the graph
		graph = readGraphFromFile(new FileInputStream("files/scotmap.txt"));
	}

	/**
	 * Read a Scotland Yard graph file from an input stream. The file contains a
	 * header line with the number of locations on the game board (which is 199)
	 * then the number of links between those locations (470). Locations are
	 * always numbered starting at 1.
	 * 
	 * Each of the 470 links will contain the two end-points of the link followed
	 * by the transportation type, which is either T for Taxi, B for Bus, U
	 * for Underground (subway), or S (Ship, which requires black card).
	 * 
	 * See files/scotmap.txt for the full file. The first few lines of the file
	 * look like this:
	 * 
	 * <pre>
	 * 199 470
	 * 1 8 T
	 * 1 9 T
	 * 1 58 B
	 * 1 46 B
	 * 1 46 U
	 * 2 20 T
	 * ...
	 * </pre>
	 * 
	 * 
	 * @param in
	 * @return
	 * @throws Exception
	 */
	public Graph readGraphFromFile(InputStream in) throws IOException {
		Graph retVal = new Graph();
		@SuppressWarnings("resource")
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
			String initial = line.substring(lastIndex + 1);
			String way = "";
			switch (initial) {
			case "T":
				way = "TAXI";
				break;
			case "B":
				way = "BUS";
				break;
			case "U":
				way = "UNDERGROUND";
				break;
			case "S":
				way = "BLACK";
				break;
			}
			n2.addUndirectedEdgeToNode(n1, way);
		}
		if (!(retVal.getNumOfAllNodes() == numNode))
			throw new IOException("num of nodes does not match: " + retVal.getNumOfAllNodes());
		if (!(retVal.getNumOfAllWays() == numEdge))
			throw new IOException("num of edges does not match: " + retVal.getNumOfAllWays());
		return retVal;
	}

	/**
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
	public Map<String, Point> readPositionPoints(String filename) throws IOException {
		Map<String, Point> map = new TreeMap<String, Point>();
		@SuppressWarnings("resource")
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

	// getter
	public Map<String, Point> getPointMap() {
		return pointMap;
	}

	// getter
	public Graph getGraph() {
		return graph;
	}

}
