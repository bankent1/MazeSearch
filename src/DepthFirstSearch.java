/**
 * Author: Travis Banken
 * DepthFirstSearch.java
 * 
 * 
 */
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class DepthFirstSearch {
	
	private static void process(char[][] maze) throws FileNotFoundException{
		PrintWriter outfile = new PrintWriter("DFS.txt");
		for (int r = 0; r < maze.length; r++) {
			for (int c = 0; c < maze[r].length; c++) {
				outfile.print(maze[r][c]);
			}
			outfile.println();
		}
		outfile.close();
	}
	
	public static boolean search(MazeGraph mGraph, char[][] maze, Node curr) throws FileNotFoundException {
		int[] loc = curr.getLoc();
		maze[loc[0]][loc[1]] = 'X';
		if (curr.getIndex() == (mGraph.size() - 1)) {
			//System.out.println("BASED");
			process(maze);
			return true;
		}
		if (curr.checkMarked()) {
			return false;
		}
		curr.mark();
		for (Node n : curr.getNeighbors()) {
			if (search(mGraph, maze, n)) {
				return true;
			}
		}
		curr.unmark();
		maze[loc[0]][loc[1]] = '1';
		return false;
	}
	
}
