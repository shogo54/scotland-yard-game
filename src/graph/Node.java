
package graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

/**
 * Class to represent a single node (or vertex) of a graph.
 * 
 * Node can be used for either directed or undirected graphs, as well as for
 * weighted or unweighted graphs. For unweighted graphs, use something like 1
 * for all of the weights. For undirected graphs, add a directed edge in both
 * directions.
 * 
 * You want to make as many operations O(1) as possible, which means you will
 * probably use a lot of Maps.
 * 
 * Side note: You can tell that I come from a networking background and not a
 * mathematical background because I almost always use the term "node" instead
 * of "vertex".
 * 
 * @author Shogo Akiyama
 *
 */
public class Node implements Comparable<Node> {

	private String name;
	private Map<Node, List<String>> neighbors;
	// list<String> is the name of the types of transportation
	private boolean occupied; // occupied is true if a detective is there
	private boolean hiding; // hiding is true is Mr.X is there

	/**
	 * Create a new node with the given name. The newly created node should have
	 * no edges.
	 * 
	 * @param name
	 */
	public Node(String name) {
		this.name = name;
		neighbors = new TreeMap<Node, List<String>>();
		occupied = false;
		hiding = false;
	}

	private void put(Map<Node, List<String>> map, Node key, String value) {
		if (map.get(key) == null) {
			List<String> list = new ArrayList<>();
			list.add(value);
			map.put(key, list);
		} else {
			map.get(key).add(value);
		}
	}

	/**
	 * Return the name of the node, which is a String.
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Return a collection of nodes that the current node is connected to by an
	 * edge.
	 * 
	 * @return retVal
	 */
	public Collection<Node> getNeighbors() {
		TreeSet<Node> retVal = new TreeSet<Node>();
		for (Node n : neighbors.keySet()) {
			retVal.add(n);
		}
		return retVal;
	}

	/**
	 * Add a directed edge to the given node using the given weight.
	 * 
	 * @param n
	 * @param type
	 */
	public void addDirectedEdgeToNode(Node n, String type) {
		put(neighbors, n, type);
	}

	/**
	 * Add an undirected edge to the given node using the given weight. Remember
	 * than an undirected edge can be implemented using two directed edges.
	 * 
	 * @param n
	 * @param type
	 */
	public void addUndirectedEdgeToNode(Node n, String type) {
		addDirectedEdgeToNode(n, type);
		n.addDirectedEdgeToNode(this, type);
	}

	/**
	 * Remove the directed edge to the given node.
	 * 
	 * If there is no edge to the given node, throw IllegalStateException (which
	 * is a type of runtime exception).
	 * 
	 * @param n
	 * @throws IllegalStateException
	 */
	public void removeDirectedEdgeToNode(Node n) {
		if (neighbors.containsKey(n))
			neighbors.remove(n);
		else
			throw new IllegalStateException();
	}

	/**
	 * Remove an undirected edge to the given node. This means removing the edge
	 * to the given node, but also any edge from the given node back to this
	 * node.
	 * 
	 * Throw a IllegalStateException if there is no edge to the given node.
	 * 
	 * @param n
	 * @throws IllegalStateException
	 */
	public void removeUndirectedEdgeToNode(Node n) {
		this.removeDirectedEdgeToNode(n);
		n.removeDirectedEdgeToNode(this);
	}

	/**
	 * Return true if there is an edge to the given node from this node, and
	 * false otherwise.
	 * 
	 * @param other
	 * @return boolean 
	 */
	public boolean hasEdge(Node other) {
		if (neighbors.containsKey(other))
			return true;
		return false;
	}

	/**
	 * Get the weight of the edge to the given node.
	 * 
	 * If no such edge exists, throw {@link IllegalStateException}
	 * 
	 * @param n
	 * @return
	 * @throws IllegalStateException
	 */
	public List<String> getWay(Node n) {
		if (neighbors.containsKey(n))
			return neighbors.get(n);
		throw new IllegalStateException();
	}

	public boolean getOccupied() {
		return occupied;
	}

	public void setOccupied(boolean b) {
		occupied = b;
	}

	public boolean getHiding() {
		return hiding;
	}

	public void setHiding(boolean b) {
		hiding = b;
	}

	@Override
	public int compareTo(Node o) {
		// Nodes should be sorted alphabetically by their name
		// just delegate to the compareTo() method for String
		return name.compareTo(o.name);
	}

	@Override
	public String toString() {
		// Just return the Node's name
		return name;
	}
}
