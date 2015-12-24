                              PJ3 GRADER

Name and login of student submitting project: Robby Nickles, ik.

Partner one name and login: Daniel Price, hz.

Partner two name and login: Gaurang Garg, hx.         

   ================================================================
                               WUGraph  

Our implementation of a weighted, undirected graph for the most part
follows the diagram given in the project specs. One significant
difference, though, is that the nodes in the graph's list of vertices
do not point to their respective adjacency lists (adjaceny lists are  our data
structure that stores all edges going from a vertex. They are
discussed further below). Rather, each vertex becomes a key to a
hashtable entry and the value of that entry is the vertex's adjacency
list. Then that adjacency list contains a pointer to its vertex in the
graph's list of vertices. 

The structure of our implementation is as follows:

There are two hashtables, one containing vertices and their adjacency
lists and the other containing all edges. There is one list of the
graph's vertices.  For each vertex in the hash table there is also  an
adjacency list which containes all edges going from the vertex. 

Every new vertex object becomes a hash table entry. The value of that
entry is the vertex's adjacency list. The new vertex is inserted to
the back of the list of existing vertices. This list does not have any
pointers to the outside of the list. Each vertex's adjacency list
contains EdgeNodes.  

An EdgeNode has fields pointing to the vertices it connects, and the
weight of the edge  between the two vertices. EdgeNodes exist in
pairs, and each of the pair has a field pointing to the other of the
pair. For this reason, only a single EdgeNode of the pair becomes an
entry in edge hash table. The entries in the edge hash table are keyed
using the VertexPair class, which ensures that the ordering of
vertices does not matter. The value of the entries in the edge
hashtable  are the EdgeNodes. 

We ensured that removeVertex() runs in O(d) time by having each
adjacency list point to its vertex's node in the graph's list of
vertices. Furthermore, because each EdgeNode points to its partner,
removing edge (u,v) from u automatically removes edge (v, u) from
v. The method call, then, goes through only the vertex's adjacency
list removing edges as it goes, then removes the node it points to
from the graph's vertex list and finally removes the vertex from the
vertex hashtable which takes constant time. Therefore the running time
is set by the time required to step through the vertex's adjacency
list.

We ensured that getVertices() runs in O(|V|) time by maintaining a
list of the graph's vertices. The method call runs through the list
and places each vertex object into an array, which it returns. Thus
the  running time is set by the time required to step through the
vertex list. 

========================================================================
                         Kruskal's Algorithm

The Kruskal method calls several helper classes as it executes:

HashTableChained - stores the vertices of the graph and an associated
arbitrary integer value with which to locate the vertices in the
DisjointSet.

WUNode - An SListNode-type data structure specifically designed to
store edges. WUNodes store the weight of an edge as well as reference
to the two vertex objects.

WUQueue - Implements the Queue interface from HW 8. A WUQueue is a
queue of WUNodes. Performs all of the same basic functions as
LinkedQueue from HW 8.

ListSortsWUGraph - Performs sorts on WUQueues. Very similar to the
ListSorts class from HW 8, but designed specifically to accommodate
WUNodes and WUQueues. 

	DisjointSets - The DisjointSet class from HW 9. 

minSpanTree Summary:

Our minSpanTree method first constructs a new graph that contains all
of the same vertices as "this" graph. It then steps through "this"
graph and places every edge it encounters into WUNode objects and
stores them in WUQueue. As the algorithm encounters each new edge it
also deletes the edge from "this" graph so that we do not place
duplicate edges into the WUQueue. After deleting all of the edges from
"this" graph, the algorithm then goes through the WUQueue and adds
each of the edges back to "this" graph. Thus the method is like a
black box. Even though we modify "this" graph during the method, when
the method finishes "this" graph is exactly the same as it was before
the minSpanTree method call.  

When all of the edges are in the WUQueue, we then sort the WUQueue by
edge weight using the quicksort algorithm in the ListSortsWUGraph
class. We now have a sorted queue of edges, the next step is to insert
them, one by one, into the new graph. To do this, we place each of the
graph's vertices into a DisjointSet. Then, we repeatedly dequeue the
WUQueue and if the dequeued edge connects two vertices that are not
yet in the same set we add the edge to the new graph and update the
DisjointSet. We continue this process until we have added an amount of
edges equal to the number of vertices-1 to the graph. This ensures
that the graph is connected.  

The total running time of our minSpanTree is in O(v + e log e). The
method is related to the number of vertices in "this" graph as we had
to step through each vertex initially in order to add each vertex to a
hash table and locate all of the incident edges. This takes O(v)
time. The method is also related to the number of edges in "this"
graph because we have to step through each of the edges in order to
put them into a WUQueue. This takes O(e) time. However, we also run
quicksort on our queue of edges to prepare them to be inserted into
the graph which takes O(e log e) time. Therefore the running time of
quicksort dominates over the time required to step through the
edges. Thus the total running time for our algorithm is in O(v + e log
e).



