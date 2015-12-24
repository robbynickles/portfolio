/* Kruskal.java */

import graph.*;
import set.*;
import kruskalQueue.*;
import list.*;
import dict.*;

/**
 * The Kruskal class contains the method minSpanTree(), which implements
 * Kruskal's algorithm for computing a minimum spanning tree of a graph.
 */

public class Kruskal {

  /**
   * minSpanTree() returns a WUGraph that represents the minimum spanning tree
   * of the WUGraph g.  The original WUGraph g is NOT changed.
   */
  public static WUGraph minSpanTree(WUGraph g) {

	WUGraph minTree = new WUGraph();
	Object[] vertices = g.getVertices();
	WUQueue edges = new WUQueue();
	Neighbors vertNeighbors;
	HashTableChained vertHashTable = new HashTableChained(vertices.length*2);
	
	// Fill in minTree with all of the vertices from g. At the same time hash each of the vertices
	// to a hash table. Also checks the edges of each vertex and adds each new edge encountered 
	// to the edges queue.
	try {

            Edge[] edgeArray = new Edge[g.edgeCount];
            int xx = 0;
            
		for (int i=0; i<vertices.length; i++) {
			minTree.addVertex(vertices[i]);
			vertNeighbors = g.getNeighbors(vertices[i]);
			vertHashTable.insert(vertices[i],new Integer(i));
                        
                        
                        
			if (vertNeighbors != null) {
				for (int j=0; j<vertNeighbors.neighborList.length; j++) {
                                    edgeArray[xx] = new Edge(vertices[j], vertNeighbors,neighborList[j], vertNeighbors.weightList[j]);
                                    //edges.enqueue(new Integer(vertNeighbors.weightList[j]),vertices[i],vertNeighbors.neighborList[j]);
                                    //g.removeEdge(vertices[i],vertNeighbors.neighborList[j]);
				}
			}
		}
		
		// Add edges back to g, so that the method ends with g unaltered.
		WUNode currentEdge;
		Object currentVertex1;
		Object currentVertex2;
		int currentWeight;
		for (int i=0; i<edges.size(); i++) {
			currentEdge = edges.frontNode();
			currentVertex1 = currentEdge.vertex1();
			currentVertex2 = currentEdge.vertex2();
			currentWeight = ((Integer) currentEdge.item()).intValue();
			g.addEdge(currentVertex1,currentVertex2,currentWeight);
			edges.dequeue();
			edges.enqueue(currentEdge.item(),currentEdge.vertex1(),currentEdge.vertex2());
		}
		
		// Sorts the edges queue with quickSort and then places edges, one by one, into the minTree
		// graph to generate a minimum spanning tree.
		//ListSortsWUGraph.quickSort(edges);
                edgeArray = Radix2.radixSort(edgeArray);
                
		DisjointSets vertexSet = new DisjointSets(g.vertexCount());
		Object vertex1;
		Object vertex2;
		int weight;
		int check1;
		int check2;
		int hashCheck1;
		int hashCheck2;
		WUNode frontEdge;
		int i=0;
		while (i<vertices.length-1 && edges.size() != 0) {
			frontEdge = edges.frontNode();
			vertex1 = frontEdge.vertex1();
			vertex2 = frontEdge.vertex2();
			check1 = ((Integer) vertHashTable.find(vertex1).value()).intValue();
			check2 = ((Integer) vertHashTable.find(vertex2).value()).intValue();
			weight = ((Integer) edges.dequeue()).intValue();
			hashCheck1 = vertexSet.find(check1);
			hashCheck2 = vertexSet.find(check2);
			if (hashCheck1 != hashCheck2) {
				vertexSet.union(hashCheck1,hashCheck2);
				minTree.addEdge(vertex1,vertex2,weight);
				i++;
			}
		}
		return minTree;
	} catch (kruskalQueue.QueueEmptyException e) {
		System.out.println(e);
		return null;
	}
  }	
	
}
