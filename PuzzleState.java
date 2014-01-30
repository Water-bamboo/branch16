import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * PuzzleState defines a state for the 8puzzle problem. The board is always
 * represented by a single dimensioned array, we attempt to provide the illusion
 * that the state representation is 2 dimensional and this works very well. In
 * terms of the actual tiles, '0' represents the hole in the board, and 0 is
 * treated special when generating successors. We do not treat '0' as a tile
 * itself, it is the "hole" in the board (as we refer to it herein)
 * 
 * @author Michael Langston && Gabe Ferrer
 * 
 */
public class PuzzleState
{
	public int PUZZLE_ROW = 4;
	public int PUZZLE_SIZE = 16;

	public int holeIndex = -1;
	public int outOfPlace = 0;
	public int manDist = 0;
	public int heuristic_cost = 1;//start to goal, not start to next.
	public final int[] GOAL_4 = { 0, 2, 1, 2, 2, 1, 2, 1, 1, 2, 1, 2, 2, 1, 2, 1};

	public char from_direction;
	public int[] curBoard;
	
	/**
	 * Constructor for PuzzleState
	 * 
	 * @param board
	 *            - the board representation for the new state to be constructed
	 */
	public PuzzleState(int[] board, char w)
	{
		curBoard = board;
		setOutOfPlace();
		setManDist();
		setHole();
		from_direction = w;
	}

	/*
	 * Set the 'tiles out of place' distance for the current board
	 */
	private void setOutOfPlace()
	{
		for (int i = 0; i < curBoard.length; i++)
		{
			if (curBoard[i] != GOAL_4[i])
			{
				outOfPlace++;
			}
		}
	}
	

	/*
	 * Set the Manhattan Distance for the current board
	 */
	private void setManDist()
	{
		manDist = 1;
	}

	/*
	 * Attempt to locate the "0" spot on the current board
	 * 
	 * @return the index of the "hole" (or 0 spot)
	 */
	public int setHole()
	{
		for (int i = 0; i < PUZZLE_SIZE; i++)
		{
			if (curBoard[i] == 0) {
				holeIndex = i;
				break;
			}
		}
		return holeIndex;
	}

	/*
	 * Makes a copy of the array passed to it
	 */
	private int[] copyBoard(int[] state)
	{
		int[] ret = new int[PUZZLE_SIZE];
		for (int i = 0; i < PUZZLE_SIZE; i++)
		{
			ret[i] = state[i];
		}
		return ret;
	}
	
	/**
	 * Is thought about in terms of NO MORE THAN 4 operations. Can slide tiles
	 * from 4 directions if hole is in middle. Two directions if hole is at a
	 * corner. Three directions if hole is in middle of a row or in middle of a column.
	 * 
	 * @return an ArrayList containing all of the successors for that state
	 */
	public ArrayList<PuzzleState> genSuccessors()
	{
		ArrayList<PuzzleState> successors = new ArrayList<PuzzleState>();
		int hole = holeIndex;
		
		// try to generate a state by sliding a tile leftwise into the hole
		// if we CAN slide into the hole
		if (hole != 0 && hole != 4 && hole != 8 && hole != 12) //checkMoveable(hole, Direction.Leftwise)) //hole != 0 && hole != 3 && hole != 6)
		{
			/*
			 * we can slide leftwise into the hole, so generate a new state for
			 * this condition and throw it into successors
			 */
			swapAndStore(hole - 1, hole, successors, 'L');
		}

		// try to generate a state by sliding a tile topwise into the hole
		if (hole != 12 && hole != 13 && hole != 14 && hole != 15) //checkMoveable(hole, Direction.Topwise)) //hole != 6 && hole != 7 && hole != 8)
		{
			swapAndStore(hole + 4, hole, successors, 'D');
		}

		// try to generate a state by sliding a tile bottomwise into the hole
		if (hole != 0 && hole != 1 && hole != 2 && hole != 3) //checkMoveable(hole, Direction.Bottomwise)) //hole != 0 && hole != 1 && hole != 2)
		{
			swapAndStore(hole - 4, hole, successors, 'U');
		}
		// try to generate a state by sliding a tile rightwise into the hole
		if (hole != 3 && hole != 7 && hole != 11 && hole != 15) //checkMoveable(hole, Direction.Rightwise)) //hole != 2 && hole != 5 && hole != 8)
		{
			swapAndStore(hole + 1, hole, successors, 'R');
		}

		return successors;
	}

	/*
	 * Switches the data at indices d1 and d2, in a copy of the current board
	 * creates a new state based on this new board and pushes into s.
	 */
	private void swapAndStore(int d1, int d2, ArrayList<PuzzleState> s, char w)
	{
		int[] cpy = copyBoard(curBoard);
		int temp = cpy[d1];
		cpy[d1] = curBoard[d2];
		cpy[d2] = temp;
		s.add((new PuzzleState(cpy, w)));
	}

	/**
	 * Check to see if the current state is the goal state.
	 * 
	 * @return - true or false, depending on whether the current state matches
	 *         the goal
	 */
	public boolean isGoal()
	{
		if (Arrays.equals(curBoard, GOAL_4)) {
			return true;
		}
		return false;
	}

	public void printStateInline() {
		for (int i = 0; i< curBoard.length; i++) {
			System.out.print(curBoard[i]+ " | ");
		}
		System.out.println();
	}
	
	/**
	 * Method to print out the current state. Prints the puzzle board.
	 */
	public void printState()
	{
		System.out.println("------------------");
		System.out.println(curBoard[0] + " | " + curBoard[1] + " | " + curBoard[2] + " | " + curBoard[3]);
		System.out.println("------------------");
		System.out.println(curBoard[4] + " | " + curBoard[5] + " | " + curBoard[6] + " | " + curBoard[7]);
		System.out.println("------------------");
		System.out.println(curBoard[8] + " | " + curBoard[9] + " | " + curBoard[10] + " | " + curBoard[11]);
		System.out.println("------------------");
		System.out.println(curBoard[12] + " | " + curBoard[13] + " | " + curBoard[14] + " | " + curBoard[15]);
		System.out.println("------------------");
	}

	/**
	 * Overloaded equals method to compare two states.
	 * 
	 * @return true or false, depending on whether the states are equal
	 */
	public boolean equals(PuzzleState s)
	{
		if (Arrays.equals(curBoard, s.curBoard))
		{
			return true;
		}
		else
			return false;
	}
}
