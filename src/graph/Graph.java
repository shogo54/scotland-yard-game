package graph;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.Stack;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * A basic representation of a graph that can perform BFS, DFS, Dijkstra, and
 * Prim-Jarnik's algorithm for a minimum spanning tree.
 * 
 * @author jspacco
 *
 */
public class Graph {
	public final File DIR = new File("tests");
	private Map<String, Node> map;

	public Graph() {
		map = new TreeMap<String, Node>();
	}

	/**
	 * Return the {@link Node} with the given name.
	 * 
	 * If no {@link Node} with the given name exists, create a new node with
	 * the given name and return it. Subsequent calls to this method with the
	 * same name should then return the node just created.
	 * 
	 * @param name
	 * @return
	 */
	public Node getOrCreateNode(String name) {
		if (map.containsKey(name))
			return map.get(name);
		map.put(name, new Node(name));
		return map.get(name);
	}

	/**
	 * Return true if the graph contains a node with the given name, and false
	 * otherwise.
	 * 
	 * @param name
	 * @return
	 */
	public boolean containsNode(String name) {
		if (map.containsKey(name))
			return true;
		return false;
	}

	/**
	 * Return a collection of all of the nodes in the graph.
	 * 
	 * @return
	 */
	public Collection<Node> getAllNodes() {
		Set<Node> retVal = new TreeSet<Node>();
		for (Node n : map.values()) {
			retVal.add(n);
		}
		return retVal;
	}

	public int getNumOfAllWays() {
		int sum = 0;
		Set<String> counted = new TreeSet<String>();
		for (Node n : map.values()) {
			TreeSet<Node> neighbors = (TreeSet<Node>) n.getNeighbors();
			for (Node next : neighbors) {
				for (String way : n.getWay(next)) {
					if (counted.contains(n.getName() + " " + next.getName() + " " + way))
						continue;
					sum++;
					if (next.hasEdge(n))
						counted.add(next.getName() + " " + n.getName() + " " + way);
				}
			}
		}
		return sum;
	}

	public int getNumOfAllNodes() {
		int count = 0;
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String s = iterator.next();
			count++;
		}
		return count;
	}

	public boolean isCompleteGraph() {
		int nodeNum = getNumOfAllNodes();
		int edgeNum = getNumOfAllWays();

		if (edgeNum == nodeNum * (nodeNum - 1) / 2) {
			return true;
		}
		return false;
	}

	/**
	 * Perform a breadth-first search on the graph, starting at the node with
	 * the given name. The visit method of the {@link NodeVisitor} should be
	 * called on each node the first time we visit the node.
	 * 
	 * <b>NOTE:</b> This method visit nodes in alphabetical order, using the
	 * name of the node to sort alphabetically.
	 * 
	 * @param startNodeName
	 * @param v
	 */
	public void breadthFirstSearch(String startNodeName, NodeVisitor v) {
		// special case
		if (!map.containsKey(startNodeName))
			throw new NoSuchElementException();

		// implementation 1
		// Queue<Node> q = new ArrayDeque<Node>();
		// Set<Node> visited = new TreeSet<Node>();
		// q.offer(map.get(startNodeName));
		// while (!q.isEmpty()) {
		// Node n = q.poll();
		// v.visit(n);
		// visited.add(n);
		// for (Node neighbor : n.getNeighbors()) {
		// if (visited.contains(neighbor))
		// continue;
		// else if (q.contains(neighbor))
		// continue;
		// else
		// q.offer(neighbor);
		// }
		// }

		// implementation 3 (use class pseudo code)
		// Queue<Node> q = new ArrayDeque<Node>();
		// Set<Node> visited = new TreeSet<Node>();
		// q.offer(map.get(startNodeName));
		// visited.add(map.get(startNodeName));
		// while (!q.isEmpty()) {
		// Node n = q.poll();
		// v.visit(n);
		// for (Node neighbor : n.getNeighbors()) {
		// if (!visited.contains(neighbor))
		// q.offer(neighbor);
		// visited.add(neighbor);
		// }
		// }

		// implementation 2
		List<Node> list = new ArrayList<Node>();
		list.add(map.get(startNodeName));
		for (int i = 0; i < list.size(); i++) {
			Node n = list.get(i);
			v.visit(n);
			for (Node neighbor : n.getNeighbors()) {
				if (list.contains(neighbor))
					continue;
				list.add(neighbor);
			}
		}
	}

	/**
	 * Perform a depth-first search on the graph, starting at the node with the
	 * given name. The visit method of the {@link NodeVisitor} should be called
	 * on each node the first time we visit the node.
	 * 
	 * <b>NOTE:</b> This method visit nodes in alphabetical order, using the
	 * name of the node to sort alphabetically.
	 * 
	 * @param startNodeName
	 * @param v
	 */
	public void depthFirstSearch(String startNodeName, NodeVisitor v) {
		// special case
		if (!map.containsKey(startNodeName))
			throw new NoSuchElementException();

		// recursive implementation
		Set<Node> visited = new TreeSet<Node>();
		depthSearchHelper(map.get(startNodeName), v, visited);

		// implementation 3 (use class pseudo code)
		// Stack<Node> st = new Stack<Node>();
		// Set<Node> visited = new TreeSet<Node>();
		// st.push(map.get(startNodeName));
		// while (!st.isEmpty()) {
		// Node n = st.pop();
		// v.visit(n);
		// visited.add(n);
		// for (Node neighbor : n.getNeighbors()) {
		// if (!visited.contains(neighbor))
		// st.push(neighbor);
		// }
		// }

		// implementation 2 does not pass test 2
		// List<Node> list=new ArrayList<Node>();
		// list.add(map.get(startNodeName));
		// for (int i = 0; i < list.size(); i++) {
		// v.visit(list.get(i));
		// List<Node> reverseList = new ArrayList<Node>();
		// for (Node n : list.get(i).getNeighbors()) {
		// System.out.println("add " + n.getName());
		// reverseList.add(n);
		// }
		// for (int j = reverseList.size() - 1; j >= 0; j--) {
		// if (list.contains(reverseList.get(j))) {
		// continue;
		// } else {
		// list.add(reverseList.get(j));
		// break;
		// }
		// }
		// }
	}

	// recursive method
	private void depthSearchHelper(Node node, NodeVisitor v, Set<Node> visited) {
		v.visit(node);
		visited.add(node);
		List<Node> list = new ArrayList<Node>();
		for (Node n : node.getNeighbors()) {
			list.add(n);
		}
		for (int i = list.size() - 1; i >= 0; i--) {
			Node n = list.get(i);
			if (visited.contains(n))
				continue;
			depthSearchHelper(n, v, visited);
		}
	}

	private class Edge implements Comparable<Edge> {
		String start;
		String destination;
		int cost;

		private Edge(String s, String d, int c) {
			start = s;
			destination = d;
			cost = c;
		}

		@Override
		public boolean equals(Object o) {
			if (!(o instanceof Edge)) {
				return false;
			}
			Edge other = (Edge) o;
			if (start.equals(other.start) && destination.equals(other.destination))
				return true;
			else if (start.equals(other.destination) && destination.equals(other.start))
				return true;
			return false;
		}

		@Override
		public int hashCode() {
			return Objects.hash(start, destination, cost);
		}

		@Override
		public int compareTo(Edge other) {
			return cost - other.cost;
		}

		@Override
		public String toString() {
			return start + "-" + destination + "-" + cost;
		}
	}
}
