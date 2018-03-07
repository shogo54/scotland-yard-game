package graph;

/**
 * Functor for visiting a node.
 * 
 * An instance of this class is passed to BFS or DFS,
 * and the visit() method will be called on each node.
 * 
 * @author jspacco
 *
 */
public interface NodeVisitor
{
    /**
     * Method for visiting a node. The exact behavior is undefined, and is
     * left to the implementing subclass (or abstract inner class).
     * 
     * You don't write this method directly; instead the user provides an implementation
     * that you will call on every node in a graph in a BFS or DFS order.
     * 
     * @param node
     */
    public void visit(Node node);
}
