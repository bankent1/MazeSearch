/**
 * Author: Travis Banken
 * MazeSearchMain.java
 * 
 * java MazeSolverMain infile.txt
 * 
 * This program is loosely based on the problem and strategy described by
 * Mike Pound in this video https://www.youtube.com/watch?v=rop0W4QDOUI
 * 
 * 
 * Notes on Program: This program will solve a binary maze. A binary maze 
 * is a nxm block of 0's and 1's where 0 represents a wall and 1 represents
 * a path. The maze is read from a .txt file as input and prints out the
 * solved maze in a file named DFS.txt.
 * 
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class MazeSearchMain {
	
	public static void main(String[] args) throws FileNotFoundException{
		Scanner infile = new Scanner(new File(args[0]));
		System.out.println("File: " + args[0]);
		
		// read in maze and build the graph representation of maze
		int[] dim = getMazeDim(infile);
		System.out.println("Size = " + dim[0] + "x" + dim[1]);
		infile = new Scanner(new File(args[0]));
		char[][] maze = buildMaze(infile, dim);
		
		// places nodes at decision points
		MazeGraph mGraph = placeNodes(maze, dim);
		System.out.println("Nodes = " + mGraph.size());
		
		// searches maze for solution and times result
		System.out.print("Finding Solution...");
		long startTime = System.nanoTime();
		if(DepthFirstSearch.search(mGraph, maze, mGraph.getNode(0), new ArrayList<Node>())) {
			long endTime = System.nanoTime();
			System.out.println("...done");
			long duration = (endTime - startTime) / 1000000;
			System.out.println("Solution found in " + duration + "ms");
		}
		else {
			System.out.println("\nSolution could not be found");
		}

		infile.close();
	}
	
	/*
	 * getMazeDim reads through the infile and counts the rows and columns
	 * 
	 * Returns an array of the row and col int values
	 */
	public static int[] getMazeDim(Scanner in) {
		int rows = 1;
		int col = 0;
		
		col = in.nextLine().length();
		
		while (in.hasNextLine()) {
			in.nextLine();
			rows++;
		}
		
		int[] dim = {rows, col};
		
		return dim;
	}
	
	/*
	 * buildMaze reads in a file row by row and constructs a 2d array to
	 * represent the maze.
	 * 
	 * Returns a 2d int array maze representation
	 */
	public static char[][] buildMaze(Scanner infile, int[] dim) {
		char[][] maze = new char[dim[0]][dim[1]];
		int r = 0;
		while (infile.hasNextLine()) {
			String rowStr = infile.nextLine();
			for (int i = 0; i < rowStr.length(); i++) {
				maze[r][i] = rowStr.charAt(i);
			}
			r++;
		}
		return maze;
	}
	
	/*
	 * placeNodes iterates through the 2d maze array and builds a graph
	 * representation of the maze with the nodes being the decision points.
	 * 
	 * Returns a MazeGraph object representing the maze
	 */
	public static MazeGraph placeNodes(char[][] maze, int[] dim) {
		MazeGraph mGraph = new MazeGraph();
		int[] colNeighbor = new int[dim[1]];
		int rowNeighbor = -1;
		int r = 0;
		int c = 0;
		
		// iterate first row
		for (c = 0; c < dim[1]; c++) {
			if (maze[r][c] == '0') {
				colNeighbor[c] = -1;
			}
			else if (maze[r][c] == '1'){
				colNeighbor[c] = 0;
				mGraph.addNode(new Node(r, c));
			}
		}
		
		// iterate though rest
		for (r = 1; r < dim[0]; r++) {
			for (c = 0; c < dim[1]; c++) {
				Node node = new Node(r, c);
				if (maze[r][c] == '0') { // at a wall
					colNeighbor[c] = -1;
					rowNeighbor = -1;
					continue;
				}
				if (r == (dim[0] - 1)) { // last row
					//System.out.println(c);
					node.addNeighbor(mGraph.getNode(colNeighbor[c]));
					mGraph.getNode(colNeighbor[c]).addNeighbor(node);
					mGraph.addNode(node);
					continue;
				}
				//System.out.println("Row: " + r + " Col: " + c);
				//System.out.println("Char: " + maze[r][c]);
				if (maze[r][c-1] == '0' && maze[r][c+1] == '0') { // no decision point
					//System.out.println(c);
					
					continue;
				}
				
				boolean eitherSide = (maze[r][c+1] == '1' && maze[r][c-1] == '1');
				if (maze[r][c-1] == '0') { // start of a corridor
					if (colNeighbor[c] != -1) {
						node.addNeighbor(mGraph.getNode(colNeighbor[c]));
						mGraph.getNode(colNeighbor[c]).addNeighbor(node);
					}
					mGraph.addNode(node);
					rowNeighbor = node.getIndex();
					colNeighbor[c] = node.getIndex();
				}
				else if (maze[r][c+1] == '0' || (eitherSide && (maze[r+1][c] == '1'
						|| maze[r-1][c] == '1'))) { // end of corridor or junction
					
					if (rowNeighbor != -1) {
						node.addNeighbor(mGraph.getNode(rowNeighbor));
						mGraph.getNode(rowNeighbor).addNeighbor(node);
					}
					if (colNeighbor[c] != -1) {
						node.addNeighbor(mGraph.getNode(colNeighbor[c]));
						mGraph.getNode(colNeighbor[c]).addNeighbor(node);
					}
					mGraph.addNode(node);
					rowNeighbor = node.getIndex();
					colNeighbor[c] = node.getIndex();
				}
				
			}
			
		} // end nested for
		
		return mGraph;
	}
	
}
