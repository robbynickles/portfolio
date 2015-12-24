/* QueueEmptyException.java */

package kruskalQueue;

/**
*  QueueEmptyException is a class that generates exception if a 
*  class in the wuList package attemptes to access and empty Queue.
**/
public class QueueEmptyException extends Exception {

	/** 
	*  QueueEmptyException() constructs a new QueueEmptyExcetion
	**/
	public QueueEmptyException() {
		super();
	}

	/**
	*  QueueEmptyException() constructs a new QueueEmpty Exception with a 
	*  string that is printed if the exception is printed in a catch clause.
	*  @param s the string that is printed if the caught exception is printed.
	**/
	public QueueEmptyException(String s) {
		super(s);
	}

}
