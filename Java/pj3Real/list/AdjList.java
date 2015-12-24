/* AdjList.java */
package list;


/* The AdjList class is used to store a vertex's edges in a weighted, undirected graph.
   
   This AdjList is considered to belong to the vertex that was hashed with this AdjList.
   
   By traversing a vertex's adjList, all the vertices connected to it can be known. If 
   a separate list of all the vertices in a graph is maintained, a reference to 
   the vertex of this adjList can be stored.
*/

public class AdjList extends DList {
    // reference to the vertex of this in a list of all vertices in a graph.
    private DListNode myVertListNode;

    //public interface
    
    /* Construct a new AdjList that points to the node that represents the 
       possessing vertex in the list of all vertices of a graph. 
       @param node is the node in the graph's vertices list.
    */
    
    public AdjList(DListNode node) {
        head = new EdgeNode(null, null, null);
        head.next = head;
        head.prev = head;
        myVertListNode = node;
        size = 0;
    }
    
    /* Insert to the back a new EdgeNode.
       @param vert1 is one of the pair of objects the new edge connects.
       @param vert2 is one of the pair of objects the new edge connects.
       @param weight is the weight of the edge between vert1 and vert2.
    */
    
    public void insertBack(Object vert1, Object vert2, int weight) {
        EdgeNode edge = new EdgeNode(this, head.prev, head);
        edge.vertex1 = vert1;
        edge.vertex2 = vert2;
        edge.weight = weight;
        head.prev.next = edge;
        head.prev = edge;
        size++;
    }
    
    /* Retrieve the node from the graph's list of vertices that. The node represents the possessing
       vertex. 
       @return a member of graph's set of vertices.
    */
    
    public DListNode myVertListNode() {
        return myVertListNode;
    }

}
