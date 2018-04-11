/**
 * Author: Travis Banken
 * DepthFirstSearch.java
 * 
 * 
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class DepthFirstSearch {
	private static char solChar = '*';
	
	private static void process(char[][] maze) throws FileNotFoundException{
		//System.out.println("Writing file");
		PrintWriter outfile = new PrintWriter("DFS.txt");
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
			if (first) {
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
	
	public static boolean search(MazeGraph mGraph, char[][] maze, Node curr,
			ArrayList<Node> sofar) throws FileNotFoundException {
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
			if (search(mGraph, maze, n, sofar)) {
				return true;
			}
		}
		curr.unmark();
		sofar.remove(sofar.size()-1);
		maze[loc[0]][loc[1]] = '1';
		return false;
	}
	
}
