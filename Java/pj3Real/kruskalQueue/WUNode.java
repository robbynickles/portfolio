/* WUNode.java */

package kruskalQueue;

	/**
	 * WUNode class is a simple data structure similar to a SList but is built
	 * specifically to be able to accomodate edges from a WUGraph. WUNodes contain 
	 * an item field (the weight of an edge), a vertex1 field, and a vertex2 
	 * field (the two endpoints of the edge). WUNodes are designed to go into
	 * WUQueues. 
	**/
  public class WUNode {

	protected Object vertex1;
	protected Object vertex2;
	protected Object item;
    protected WUNode next;
	
	/**
    *  WUNode() constructs a new WUNode with a null next pointer.
	*  @param weight the weight to be stored in the item field of the WUNode.
    **/
	protected WUNode(Object weight) {
		item = weight;
	}
	
	/**
    *  WUNode() constructs a new WUNode.
	*  @param weight the weight to be stored in the item field of the WUNode.
	*  @param next the next WUNode in the WUQueue.
    **/
	protected WUNode(Object weight, WUNode next) {
		item = weight;
		this.next = next;
    }
  
	/**
    *  item() returns the object in the item field of the WUNode.
	*  @return the object in the item field (usually an edge weight).
    **/
	public Object item() {
		return item;
	}
	
	/**
    *  next() returns the next WUNode.
	*  @return the next WUNode in the WUQueue.
    **/
	public Object next() {
		return next;
	}
	
	/**
    *  setVertex1() sets the value of vertex1.
	*  @param v1 the object to set as the new vertex1.
    **/
	public void setVertex1(Object v1) {
		vertex1 = v1;
	}
	
	/**
    *  setVertex2() sets the value of vertex2.
	*  @param v2 the object to set as the new vertex2.
    **/
	public void setVertex2 (Object v2) {
		vertex2 = v2;
	}
	
	/**
    *  vertex1() returns the object in vertex1.
	*  @return the object in vertex1 (usually an edge endpoint).
    **/
	public Object vertex1() {
		return vertex1;
	}
	
	/**
    *  vertex2() returns the object in vertex2.
	*  @return the object in vertex2 (usually an edge endpoint).
    **/
	public Object vertex2() {
		return vertex2;
	}
	
}
