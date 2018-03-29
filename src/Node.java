/**
 * Author: Travis Banken
 * Node.java 
 *
 * 
 */

import java.util.ArrayList;

public class Node {
	ArrayList<Node> neighbors = new ArrayList<Node>();
	private boolean start = false;
	private boolean end = false;
	
	public void addNeighbor(Node n) {
		neighbors.add(n);
	}
	
	public void makeStart() {
		start = true;
	}
	
	public void makeEnd() {
		end = true;
	}
}
