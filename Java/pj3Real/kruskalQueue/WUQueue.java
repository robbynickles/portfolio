/* WUQueue.java */

package kruskalQueue;

	/**
	 * WUQueue class is very similar to the LinkedQueue class from HW 8. 
	 * However, WUQueues are composed of WUNodes (which represent edges) instead 
	 * of SListNodes. WUNodes contain an item field (the weight of an edge), a
	 * vertex1 field, and a vertex2 field (the two endpoints of the edge). An 
	 * additional enqueue method has been added to take in not only an item but also a
	 * vertex1 and vertex2. A new method called frontNode was also added which returns 
	 * the WUNode at the front of the WUQueue.
	**/
public class WUQueue implements Queue {

  protected WUNode head;
  protected WUNode tail;
  protected int size;

  /**
   *  WUQueue() constructs an empty queue.
   **/
  public WUQueue() {
    size = 0;
    head = null;
    tail = null;
  }

  /** 
   *  size() returns the size of this Queue.
   *  @return the size of this Queue.
   *  Performance:  runs in O(1) time.
   **/
  public int size() {
    return size;
  }

  /**
   *  isEmpty() returns true if this Queue is empty, false otherwise.
   *  @return true if this Queue is empty, false otherwise. 
   *  Performance:  runs in O(1) time.
   **/
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   *  frontNode() returns the node at the front of the Queue.
   *  @return the node at the front of the Queue (without dequeuing it)
   *  @throws a QueueEmptyException if the Queue is empty.
   **/
   public WUNode frontNode() throws QueueEmptyException {
	if (head == null) {
      throw new QueueEmptyException();
    } else {
      return head;
    }
  }
  
  /**
   *  enqueue() inserts an object at the end of the Queue.
   *  @param item the item (usually a weight of an edge) to be enqueued.
   **/
	public void enqueue(Object item) {
		if (head == null) {
			head = new WUNode(item);
			tail = head;
		} else {
			tail.next = new WUNode(item);
			tail = tail.next;
		}
		size++;
	}
	
  /**
   *  enqueue() inserts an object at the end of the Queue.
   *  @param item the item (usually a weight of an edge) to be enqueued.
   *  @param vertex1 the first endpoint of the edge.
   *  @param vertex2 the second endpoint of the edge.
   **/ 
	public void enqueue(Object item, Object vertex1, Object vertex2) {
    if (head == null) {
		head = new WUNode(item);
		head.setVertex1(vertex1);
		head.setVertex2(vertex2);
		tail = head;
    } else {
		tail.next = new WUNode(item);
		tail.next.setVertex1(vertex1);
		tail.next.setVertex2(vertex2);
		tail = tail.next;
    }
    size++;
  }

  /**
   *  dequeue() removes and returns the object at the front of the Queue.
   *  @return the item dequeued.
   *  @throws a QueueEmptyException if the Queue is empty.
   **/
  public Object dequeue() throws QueueEmptyException {
    if (head == null) {
      throw new QueueEmptyException();
    } else {
      Object o = head.item;
      head = head.next;
      size--;
      if (size == 0) {
        tail = null;
      }
      return o;
    }
  }

  /**
   *  front() returns the object at the front of the Queue.
   *  @return the item at the front of the Queue.
   *  @throws a QueueEmptyException if the Queue is empty.
   **/
  public Object front() throws QueueEmptyException {
    if (head == null) {
      throw new QueueEmptyException();
    } else {
      return head.item;
    }
  }

  /**
   *
   *  nth() returns the nth item in this WUQueue, without removing it.
   *    Items in the queue are numbered from 1.
   *  @param n the number of the item to remove from this queue.
   */
  public Object nth(int n) {
    WUNode node = head;
    for (; n > 1; n--) {
      node = node.next;
    }
    return node.item;
  }

  /**
   *  append() appends the contents of q onto the end of this WUQueue.
   *    On completion, q is empty.
   *  @param q the WUQueue whose contents should be appended onto this
   *    WUQueue.
   **/
  public void append(WUQueue q) {
    if (head == null) {
      head = q.head;
    } else {
      tail.next = q.head;
    }
    if (q.head != null) {
      tail = q.tail;
    }
    size = size + q.size;
    q.head = null;
    q.tail = null;
    q.size = 0;
  }

  /**
   *  toString() converts this queue to a String.
   **/
  public String toString() {
    String out = "[ ";
    try {
      for (int i = 0; i < size(); i++) {
	out = out + front() + " ";
	enqueue(dequeue(),null,null);
      }
    } catch (QueueEmptyException uf) {
      System.err.println("Error:  attempt to dequeue from empty queue.");
    }
    return out + "]";
  }
}
