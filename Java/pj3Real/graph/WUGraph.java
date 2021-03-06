/* WUGraph.java */

package graph;

import dict.*;
import list.*;

/**
 * The WUGraph class represents a weighted, undirected graph.  Self-edges are
 * permitted.
 */

public class WUGraph {
    
    protected DList vertList;
    protected HashTableChained vertTable;
    protected HashTableChained edgeTable;
    //no need for counters. edges (u,v) and (v,u) are stored as one entry in edgeTable.
  /**
   * WUGraph() constructs a graph having no vertices or edges.
   *
   * Running time:  O(1).
   */
    public WUGraph() {
        vertList = new DList();
        vertTable = new HashTableChained();
        edgeTable = new HashTableChained();
    }

  /**
   * vertexCount() returns the number of vertices in the graph.
   *
   * Running time:  O(1).
   */
    public int vertexCount() {
        return vertList.length();
    }

  /**
   * edgeCount() returns the number of edges in the graph.
   *
   * Running time:  O(1).
   */
    public int edgeCount() {
        return edgeTable.size();
    }

  /**
   * getVertices() returns an array containing all the objects that serve
   * as vertices of the graph.  The array's length is exactly equal to the
   * number of vertices.  If the graph has no vertices, the array has length
   * zero.
   *
   * (NOTE:  Do not return any internal data structure you use to represent
   * vertices!  Return only the same objects that were provided by the
   * calling application in calls to addVertex().)
   *
   * Running time:  O(|V|).
   */
    public Object[] getVertices() {
        try {
            Object[] myVerts = new Object[vertList.length()];
            DListNode current = (DListNode) vertList.front();
            for (int i = 0; i < vertList.length(); i++) {
                myVerts[i] = current.item();
                current = (DListNode) current.next();
            }
            return myVerts;
        } catch (InvalidNodeException e) {
            System.err.println("Tried to use invalid node.");
            return null;
        }
    }

  /**
   * addVertex() adds a vertex (with no incident edges) to the graph.  The
   * vertex's "name" is the object provided as the parameter "vertex".
   * If this object is already a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(1).
   */
    public void addVertex(Object vertex) {
        if (vertTable.find(vertex) == null) {
            vertList.insertBack(vertex);
            vertTable.insert(vertex, new AdjList((DListNode) vertList.back()));
        }
    }
            
  /**
   * removeVertex() removes a vertex from the graph.  All edges incident on the
   * deleted vertex are removed as well.  If the parameter "vertex" does not
   * represent a vertex of the graph, the graph is unchanged.
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
    public void removeVertex(Object vertex) {
        Entry vertEntry = vertTable.find(vertex);
        if (vertEntry != null) {
            try {
                AdjList adjList = (AdjList) vertEntry.value();
                EdgeNode edge = (EdgeNode) adjList.front();
                
                while (edge.isValidNode()) {//remove incident edges.
                    VertexPair vertPair = new VertexPair(vertex, edge.getVertex2());
                    EdgeNode temp = edge;
                    edge = (EdgeNode) edge.next();
                    temp.remove();
                    edgeTable.remove(vertPair);
                }
                
                adjList.myVertListNode().remove();
                vertTable.remove(vertex);

            } catch (InvalidNodeException e) {
                System.err.println("Tried to use invalid node.");
            }
        }
    }
            

  /**
   * isVertex() returns true if the parameter "vertex" represents a vertex of
   * the graph.
   *
   * Running time:  O(1).
   */
    public boolean isVertex(Object vertex) {
        return (vertTable.find(vertex) != null);
    }

  /**
   * degree() returns the degree of a vertex.  Self-edges add only one to the
   * degree of a vertex.  If the parameter "vertex" doesn't represent a vertex
   * of the graph, zero is returned.
   *
   * Running time:  O(1).
   */
        public int degree(Object vertex) {
            Entry vertEntry = vertTable.find(vertex);
            if (vertEntry != null) {
                return ((AdjList) vertEntry.value()).length();
            } else {
                return 0;
            }
        }        

  /**
   * getNeighbors() returns a new Neighbors object referencing two arrays.  The
   * Neighbors.neighborList array contains each object that is connected to the
   * input object by an edge.  The Neighbors.weightList array contains the
   * weights of the corresponding edges.  The length of both arrays is equal to
   * the number of edges incident on the input vertex.  If the vertex has
   * degree zero, or if the parameter "vertex" does not represent a vertex of
   * the graph, null is returned (instead of a Neighbors object).
   *
   * The returned Neighbors object, and the two arrays, are both newly created.
   * No previously existing Neighbors object or array is changed.
   *
   * (NOTE:  In the neighborList array, do not return any internal data
   * structure you use to represent vertices!  Return only the same objects
   * that were provided by the calling application in calls to addVertex().)
   *
   * Running time:  O(d), where d is the degree of "vertex".
   */
    public Neighbors getNeighbors(Object vertex) {
        Entry vertEntry = vertTable.find(vertex);
        if (vertEntry != null && degree(vertex) > 0) {
            try {
                AdjList adjList = (AdjList) vertEntry.value();
                Neighbors neighbors = new Neighbors();
                neighbors.neighborList = new Object[adjList.length()];
                neighbors.weightList = new int[adjList.length()];
                EdgeNode edge = (EdgeNode) adjList.front();
                
                for (int i = 0; i < adjList.length(); i++) {
                    neighbors.neighborList[i] = edge.getVertex2();
                    neighbors.weightList[i] = edge.getWeight();
                    edge = (EdgeNode) edge.next();
                }
                return neighbors;
            } catch (InvalidNodeException e) {
                System.err.println("Tried to use invalid node.");
                return null;
            }
        } else {
            return null;
        }
    }
 /**
   * addEdge() adds an edge (u, v) to the graph.  If either of the parameters
   * u and v does not represent a vertex of the graph, the graph is unchanged.
   * The edge is assigned a weight of "weight".  If the edge is already
   * contained in the graph, the weight is updated to reflect the new value.
   * Self-edges (where u == v) are allowed.
   *
   * Running time:  O(1).
   */
    public void addEdge(Object u, Object v, int weight) {
        if (isVertex(u) && isVertex(v)) {
            VertexPair vertPair = new VertexPair(u, v);
            //to update existing edges, remove and add edge again with weight.
            if (isEdge(u, v)) {
                ((EdgeNode) edgeTable.find(vertPair).value()).remove();
                edgeTable.remove(vertPair);
            }
            if (u.equals(v)) {//self-edge case.
                Entry vertEntry = vertTable.find(u);
                AdjList adjList = (AdjList) vertEntry.value();
                adjList.insertBack(u, v, weight);
                EdgeNode edge = (EdgeNode) adjList.back();
                edgeTable.insert(vertPair, edge); 
            } else {
                Entry vertEntry1 = vertTable.find(u);
                Entry vertEntry2 = vertTable.find(v);
                AdjList adjList1 = (AdjList) vertEntry1.value();
                AdjList adjList2 = (AdjList) vertEntry2.value();
                adjList1.insertBack(u, v, weight);
                adjList2.insertBack(v, u, weight);
                EdgeNode edge1 = (EdgeNode) adjList1.back();
                EdgeNode edge2 = (EdgeNode) adjList2.back();
                edge1.setBrother(edge2);
                edge2.setBrother(edge1);
                edgeTable.insert(vertPair, edge1);//inserting edge1 is arbitrary.
            }
        }
    }

  /**
   * removeEdge() removes an edge (u, v) from the graph.  If either of the
   * parameters u and v does not represent a vertex of the graph, the graph
   * is unchanged.  If (u, v) is not an edge of the graph, the graph is
   * unchanged.
   *
   * Running time:  O(1).
   */
    public void removeEdge(Object u, Object v) {
        if (isVertex(u) && isVertex(v) && isEdge(u, v)) {
            VertexPair vertPair = new VertexPair(u, v);
            Entry edgeEntry = edgeTable.find(vertPair);
            EdgeNode edge = (EdgeNode) edgeEntry.value();
            edge.remove();
            edgeTable.remove(vertPair);
        }
    }
  /**
   * isEdge() returns true if (u, v) is an edge of the graph.  Returns false
   * if (u, v) is not an edge (including the case where either of the
   * parameters u and v does not represent a vertex of the graph).
   *
   * Running time:  O(1).
   */
    public boolean isEdge(Object u, Object v) {
        return (edgeTable.find(new VertexPair(u, v)) != null);
    }

  /**
   * weight() returns the weight of (u, v).  Returns zero if (u, v) is not
   * an edge (including the case where either of the parameters u and v does
   * not represent a vertex of the graph).
   *
   * (NOTE:  A well-behaved application should try to avoid calling this
   * method for an edge that is not in the graph, and should certainly not
   * treat the result as if it actually represents an edge with weight zero.
   * However, some sort of default response is necessary for missing edges,
   * so we return zero.  An exception would be more appropriate, but
   * also more annoying.)
   *
   * Running time:  O(1).
   */
    public int weight(Object u, Object v) {
        if (isEdge(u, v)) {
            Entry edgeEntry = edgeTable.find(new VertexPair(u, v));
            EdgeNode edge = (EdgeNode) edgeEntry.value();
            return edge.getWeight();
        } else {
            return 0;
        }
    }

}
