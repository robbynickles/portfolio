/* ListSortsWUGraph.java */

package kruskalQueue;

/**
*  ListSortsWUGraph is very similar to the ListSorts class from HW 8. 
*  However ListSortsWuGraph is designed specifically to take in WUQueues
*  instead of LinkedQueues. ListSortsWUGraph needed to be new class instead of a subclass
*  of ListSorts because WUqueues are not LinkedQueue objects.
**/

public class ListSortsWUGraph {


   /**
   *  quickSort() sorts q from smallest to largest using quicksort.
   *  @param q is a LinkedQueue of Comparable objects.
   **/
	public static void quickSort(WUQueue q) {
		if (q.isEmpty()) {
			return;
		} else {
			int pivot = (int) Math.ceil(q.size()*Math.random());
			Comparable pivotItem = (Comparable) q.nth(pivot);
			WUQueue qSmall = new WUQueue();
			WUQueue qEquals = new WUQueue();
			WUQueue qLarge = new WUQueue();
			partition(q, pivotItem, qSmall, qEquals, qLarge);
			quickSort(qSmall);
			quickSort(qLarge);
			q.append(qSmall);
			q.append(qEquals);
			q.append(qLarge);
		}
	}
	
   /**
   *  partition() partitions qIn using the pivot item.  On completion of
   *  this method, qIn is empty, and its items have been moved to qSmall,
   *  qEquals, and qLarge, according to their relationship to the pivot.
   *  @param qIn is a WUQueue of Comparable objects.
   *  @param pivot is a Comparable item used for partitioning.
   *  @param qSmall is a WUQueue, in which all items less than pivot
   *    will be enqueued.
   *  @param qEquals is a WUQueue, in which all items equal to the pivot
   *    will be enqueued.
   *  @param qLarge is a WUQueue, in which all items greater than pivot
   *    will be enqueued.  
   **/  
	public static void partition(WUQueue qIn, Comparable pivot, 
                               WUQueue qSmall, WUQueue qEquals, 
                               WUQueue qLarge) {
		Comparable current;
		Object currentVertex1;
		Object currentVertex2;
		try {
		while (!qIn.isEmpty()) {
				currentVertex1 = qIn.frontNode().vertex1();
				currentVertex2 = qIn.frontNode().vertex2();
				current = (Comparable) qIn.dequeue();
				if (current.compareTo(pivot) < 0 ) {
					qSmall.enqueue(current,currentVertex1,currentVertex2);
				} else if (current.compareTo(pivot) == 0 ) {
					qEquals.enqueue(current,currentVertex1,currentVertex2);
				} else {
					qLarge.enqueue(current,currentVertex1,currentVertex2);
				}
			}
		} catch (QueueEmptyException e) {
			System.out.println(e);
		}
	}
  
 }
