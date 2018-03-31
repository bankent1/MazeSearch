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
 * Notes on Program:
 * 
 * 
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class MazeSearchMain {
	
	public static void main(String[] args) throws FileNotFoundException{
		Scanner infile = new Scanner(new File(args[0]));
		
		// TODO: build maze
		int[] dim = getMazeDim(infile);
		System.out.println("Size = " + dim[0] + "x" + dim[1]);
		infile = new Scanner(new File(args[0]));
		char[][] maze = buildMaze(infile, dim);
		//System.out.println(maze[0][2]);
		
		// TODO: place nodes
		MazeGraph mGraph = placeNodes(maze, dim);
		System.out.println("Nodes = " + mGraph.size());
		
		// TODO: search maze
		DepthFirstSearch.search(mGraph, maze, mGraph.getNode(0));
		
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
					System.out.println(c);
					node.addNeighbor(mGraph.getNode(colNeighbor[c]));
					mGraph.getNode(colNeighbor[c]).addNeighbor(node);
					mGraph.addNode(node);
					continue;
				}
				if (maze[r][c-1] == '0' && maze[r][c+1] == '0') { // no decision point
					continue;
				}
//				if (maze[r-1][c] == '1' && maze[r+1][c] == '0') { // path below with no walls either side
//					System.out.println(c);
//					if (rowNeighbor != -1) {
//						node.addNeighbor(mGraph.getNode(rowNeighbor));
//						mGraph.getNode(rowNeighbor).addNeighbor(node);
//					}
//					if (colNeighbor[c] != -1) {
//						node.addNeighbor(mGraph.getNode(colNeighbor[c]));
//						mGraph.getNode(colNeighbor[c]).addNeighbor(node);
//					}
//					mGraph.addNode(node);
//					rowNeighbor = node.getIndex();
//					colNeighbor[c] = node.getIndex();
//				}
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
				else if (maze[r][c+1] == '0' || (eitherSide && (maze[r+1][c] == '1' || maze[r-1][c] == '1'))) { // end of a corridor
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
