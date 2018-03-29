/**
 * Author: Travis Banken
 * Maze.java
 * 
 */

import java.util.ArrayList;

public class MazeGraph {
	private ArrayList<Node> allNodes = new ArrayList<Node>();
	int size = 0;
	
	public void addNode(Node n) {
		allNodes.add(n);
		size++;
	}
	
	public void setStartEnd() {
		allNodes.get(0).makeStart();
		allNodes.get(size-1).makeEnd();
	}
	
	public int size() {
		return size;
	}
	
}
