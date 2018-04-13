/**
 * Author: Travis Banken
 * DepthFirstSearch.java
 * 
 * 
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

public class DepthFirstSearch {
	private static char solChar = '*';
	
	private static void process(char[][] maze){
		//System.out.println("Writing file");
		PrintWriter outfile = null;
		try {
			outfile = new PrintWriter("DFS.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int r = 0; r < maze.length; r++) {
			for (int c = 0; c < maze[r].length; c++) {
				outfile.print(maze[r][c]);
				//outfile.print("TEST ");
			}
			outfile.println();
		}
		outfile.close();
	}
	
	public static char[][] connectNodes(char[][] maze, ArrayList<Node> sofar) {
		Node prev = sofar.get(0);
		int sim = -1;
		int diff = -1;
		boolean first = true;
		for (Node curr : sofar) {
			if (first) { // skip first node
				first = false;
				continue;
			}
			if (curr.getLoc()[0] == prev.getLoc()[0]) { // same row
				sim = curr.getLoc()[0];
				diff = curr.getLoc()[1] - prev.getLoc()[1];
				if (diff > 0) {
					for (int c = prev.getLoc()[1]; c <= curr.getLoc()[1]; c++) {
						maze[sim][c] = solChar;
					}
				}
				if (diff < 0) {
					for (int c = prev.getLoc()[1]; c >= curr.getLoc()[1]; c--) {
						maze[sim][c] = solChar;
					}
				}
			}
			else if (curr.getLoc()[1] == prev.getLoc()[1]) { // same col
				sim = curr.getLoc()[1];
				diff =  curr.getLoc()[0] - prev.getLoc()[0];
				if (diff > 0) {
					for (int c = prev.getLoc()[0]; c <= curr.getLoc()[0]; c++) {
						maze[c][sim] = solChar;
					}
				}
				if (diff < 0) {
					for (int c = prev.getLoc()[0]; c >= curr.getLoc()[0]; c--) {
						maze[c][sim] = solChar;
					}
				}
			}
			prev = curr;
		} // end nested for
		
		return maze;
	}
	
	private static boolean isDeadEnd(Node node) {
		for (Node n : node.getNeighbors()) {
			if (!n.checkMarked()) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean recSearch(MazeGraph mGraph, char[][] maze, Node curr,
			ArrayList<Node> sofar) {
		int[] loc = curr.getLoc();
		maze[loc[0]][loc[1]] = solChar;
		if (curr.getIndex() == (mGraph.size() - 1)) {
			//System.out.println("BASED");
			sofar.add(curr);
			maze = connectNodes(maze, sofar);
			process(maze);
			return true;
		}
		if (curr.checkMarked()) {
			return false;
		}
		curr.mark();
		sofar.add(curr);
		for (Node n : curr.getNeighbors()) {
			if (recSearch(mGraph, maze, n, sofar)) {
				return true;
			}
		}
		curr.unmark();
		sofar.remove(sofar.size()-1);
		maze[loc[0]][loc[1]] = '1';
		return false;
	}
	
	// TODO: Fix iterative search
	public static boolean iterSearch(MazeGraph mGraph, char[][]maze, Node curr, ArrayList<Node> sofar) {
		Stack<Node> nodesLeft = new Stack<Node>();
		nodesLeft.push(curr);
		
		while (!nodesLeft.isEmpty()) {
			curr = nodesLeft.pop();

			
			if (!curr.checkMarked()) {
				curr.mark();
				if (isDeadEnd(curr) && curr.getIndex() != mGraph.size() - 1) {
					sofar = removeDeadEndPath(curr, sofar);
					continue;
				}
				sofar.add(curr);
				if (sofar.get(sofar.size()-1).getIndex() == mGraph.size() - 1) {
					maze = connectNodes(maze, sofar);
					process(maze);
					return true;
				}	
				
				for (Node n : curr.getNeighbors()) {
					if (!n.checkMarked()) {
						nodesLeft.push(n);
					}
				}
			}
			
		} // end while
		

		
		return false;
	}
	
	private static ArrayList<Node> removeDeadEndPath(Node curr, ArrayList<Node> sofar) {
		if (!isDeadEnd(curr)) {
			return sofar;
		}
		
		sofar.remove(curr);
		System.out.println("\nRemoved node at loc[" + curr.getLoc()[1] + "][" + curr.getLoc()[1] + "]");
		for (Node node : curr.getNeighbors()) {
			removeDeadEndPath(node, sofar);
		}
		return sofar;
	}
	
}

