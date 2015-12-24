/*EdgeNode.java*/

package list;



/*The EdgeNode class is used to store information about edges in an undirected,
  weighted graph. An EdgeNode knows the pair of vertices it connects, and its weight.
  Because the graph is undirected, EdgeNodes exist in pairs and each node of the pair 
  knows about the other node of the pair.
*/

public class EdgeNode extends DListNode {
    
    protected EdgeNode brother;
    protected Object vertex1;
    protected Object vertex2;
    protected int weight;
    
    //public interface

    /* Construct a new EdgeNode that points to its AdjList, and the next and previous 
       nodes.
       @param l is the AdjList the new EdgeNode points at.
       @param p is the DListNode behind the new node in l.
       @param n is the DListNode in front of the new node in l.
    */
    
    public EdgeNode(AdjList l, DListNode p, DListNode n) {
        myList = l;
        prev = p;
        next = n;
    }

    /* Set the brother field to another EdgeNode.
       @param b is the EdgeNode that will be this's brother.
    */
    
    public void setBrother(EdgeNode b) {
        brother = b;
    }
    
    /* Retrieve this node's second vertex.
       @return this's end point object.
    */
    
    public Object getVertex2() {
        return vertex2;
    }
    
    /* Retrieve this edge's weight.
       @return this's weight.
    */
    
    public int getWeight() {
        return weight;
    }
    
    /* Remove this edge from its adjList. Also, remove this's brother from 
       brother's adjList.
    */
    
    public void remove() {
        try {
            super.remove();
            if (brother != null) {
                brother.brother = null;
                brother.remove();
                brother = null;
            }
        } catch (InvalidNodeException e) {
            System.err.println("Attempt to remove invalid node.");
        }
    }
}
