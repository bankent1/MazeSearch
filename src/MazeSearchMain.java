/**
 * Author: Travis Banken
 * MazeSearchMain.java
 * 
 * java MazeSolverMain infile.txt
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
		System.out.println(dim[0] + "x" + dim[1]);
		infile = new Scanner(new File(args[0]));
		int[][] maze = buildMaze(infile, dim);
		//System.out.println(maze[2][2]);
		
		// TODO: search maze
		
		infile.close();
	}
	
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
	public static int[][] buildMaze(Scanner infile, int[] dim) {
		int[][] maze = new int[dim[0]][dim[1]];
		int r = 0;
		//MazeGraph mazeGraph = new MazeGraph();
		while (infile.hasNextLine()) {
			String rowStr = infile.nextLine();
			for (int i = 0; i < rowStr.length(); i++) {
				maze[r][i] = Integer.parseInt(rowStr.substring(i, i+1));
			}
			r++;
		}
		return maze;
	}
	
}
