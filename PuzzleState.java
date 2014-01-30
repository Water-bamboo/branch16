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
public class PuzzleState implements State
{
	private int PUZZLE_ROW = 4;
	private int PUZZLE_SIZE = 16;

	private int outOfPlace = 0;
	private int manDist = 0;

	/*
	private final int Leftwise = 1;
	private final int Rightwise = 2;
	private final int Topwise = 3;
	private final int Bottomwise = 4;*/
	//public enum Direction { Leftwise, Rightwise, Topwise, Bottomwise }
	
	//private final int[] GOAL_3 = { 1, 2, 3, 4, 5, 6, 7, 8, 0 };
	private final int[] GOAL_4 = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

	private int[] curBoard;
	
	/**
	 * Constructor for PuzzleState
	 * 
	 * @param board
	 *            - the board representation for the new state to be constructed
	 */
	public PuzzleState(int[] board)
	{
		curBoard = board;
		/*
		if (board.length == 9) {
			PUZZLE_ROW = 3;
			PUZZLE_SIZE = board.length;//9;
		}
		else if (board.length == 16) {
			PUZZLE_ROW = 4;
			PUZZLE_SIZE = board.length;//9;
		}
		else {
			PUZZLE_ROW = 1;
			PUZZLE_SIZE = 1;
			System.out.println("PuzzleState argument error!!! board.length == "+board.length);
		}*/
		setOutOfPlace();
		setManDist();
	}

	/**
	 * How much it costs to come to this state
	 */
	@Override
	public double findCost()
	{
		return 1;
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
		// linearly search the array independent of the nested for's below
		int index = -1;

		// just keeps track of where we are on the board (relatively, can't use
		// 0 so these
		// values need to be shifted to the right one place)
		for (int y = 0; y < PUZZLE_ROW/*3*/; y++)
		{
			for (int x = 0; x < PUZZLE_ROW/*3*/; x++)
			{
				index++;

				// sub 1 from the val to get the index of where that value
				// should be
				//int val = (curBoard[index] - 1);//first tile is a hole
				int val = (curBoard[index]);//first tile is *NOT* a hole

				/*
				 * If we're not looking at the hole. The hole will be at
				 * location -1 since we subtracted 1 before to turn val into the
				 * index
				 */
				if (val >= 0/*hole*/)
				{
					// Horizontal offset, mod the tile value by the horizontal
					// dimension
					int horiz = val % PUZZLE_ROW/*3*/;
					// Vertical offset, divide the tile value by the vertical
					// dimension
					int vert = val / PUZZLE_ROW/*3*/;

					manDist += Math.abs(vert - (y)) + Math.abs(horiz - (x));
				}
				// If we are looking at the hole, skip it
			}
		}
	}

	/*
	 * Attempt to locate the "0" spot on the current board
	 * 
	 * @return the index of the "hole" (or 0 spot)
	 */
	public int getHole()
	{
		// If returning -1, an error has occured. The "hole" should always exist
		// on the board and should always be found by the below loop.
		int holeIndex = -1;

		for (int i = 0; i < PUZZLE_SIZE; i++)
		{
			if (curBoard[i] == 0)
				holeIndex = i;
		}
		return holeIndex;
	}

	/**
	 * Getter for the outOfPlace value
	 * 
	 * @return the outOfPlace h(n) value
	 */
	public int getOutOfPlace()
	{
		return outOfPlace;
	}

	/**
	 * Getter for the Manhattan Distance value
	 * 
	 * @return the Manhattan Distance h(n) value
	 */
	public int getManDist()
	{
		return manDist;
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
	@Override
	public ArrayList<State> genSuccessors()
	{
		ArrayList<State> successors = new ArrayList<State>();
		int hole = getHole();

		// try to generate a state by sliding a tile leftwise into the hole
		// if we CAN slide into the hole
		if (hole != 0 && hole != 4 && hole != 8 && hole != 12) //checkMoveable(hole, Direction.Leftwise)) //hole != 0 && hole != 3 && hole != 6)
		{
			/*
			 * we can slide leftwise into the hole, so generate a new state for
			 * this condition and throw it into successors
			 */
			swapAndStore(hole - 1, hole, successors);
		}
		

		// try to generate a state by sliding a tile topwise into the hole
		if (hole != 12 && hole != 13 && hole != 14 && hole != 15) //checkMoveable(hole, Direction.Topwise)) //hole != 6 && hole != 7 && hole != 8)
		{
			swapAndStore(hole + 4, hole, successors);
		}

		// try to generate a state by sliding a tile bottomwise into the hole
		if (hole != 0 && hole != 1 && hole != 2 && hole != 3) //checkMoveable(hole, Direction.Bottomwise)) //hole != 0 && hole != 1 && hole != 2)
		{
			swapAndStore(hole - 4, hole, successors);
		}
		// try to generate a state by sliding a tile rightwise into the hole
		if (hole != 3 && hole != 7 && hole != 11 && hole != 15) //checkMoveable(hole, Direction.Rightwise)) //hole != 2 && hole != 5 && hole != 8)
		{
			swapAndStore(hole + 1, hole, successors);
		}

		return successors;
	}

	/*
	 * Switches the data at indices d1 and d2, in a copy of the current board
	 * creates a new state based on this new board and pushes into s.
	 */
	private void swapAndStore(int d1, int d2, ArrayList<State> s)
	{
		int[] cpy = copyBoard(curBoard);
		int temp = cpy[d1];
		cpy[d1] = curBoard[d2];
		cpy[d2] = temp;
		s.add((new PuzzleState(cpy)));
	}

	/**
	 * Check to see if the current state is the goal state.
	 * 
	 * @return - true or false, depending on whether the current state matches
	 *         the goal
	 */
	@Override
	public boolean isGoal()
	{/*
		if (PUZZLE_ROW == 3) {
			if (Arrays.equals(curBoard, GOAL_3))
			{
				return true;
			}
		}
		else if (PUZZLE_ROW == 4) {*/
			if (Arrays.equals(curBoard, GOAL_4))
			{
				return true;
			}
		//}
		return false;
	}

	@Override
	public void printStateInline() {
		for (int i = 0; i< curBoard.length; i++) {
			System.out.print(curBoard[i]+ " | ");
		}
		System.out.println();
	}
	
	/**
	 * Method to print out the current state. Prints the puzzle board.
	 */
	@Override
	public void printState()
	{/*
		if (PUZZLE_ROW == 3) {
			System.out.println(curBoard[0] + " | " + curBoard[1] + " | "
					+ curBoard[2]);
			System.out.println("---------");
			System.out.println(curBoard[3] + " | " + curBoard[4] + " | "
					+ curBoard[5]);
			System.out.println("---------");
			System.out.println(curBoard[6] + " | " + curBoard[7] + " | "
					+ curBoard[8]);

		}
		else if (PUZZLE_ROW == 4) {*/
			System.out.println("------------------");
			System.out.println(curBoard[0] + " | " + curBoard[1] + " | " + curBoard[2] + " | " + curBoard[3]);
			System.out.println("------------------");
			System.out.println(curBoard[4] + " | " + curBoard[5] + " | " + curBoard[6] + " | " + curBoard[7]);
			System.out.println("------------------");
			System.out.println(curBoard[8] + " | " + curBoard[9] + " | " + curBoard[10] + " | " + curBoard[11]);
			System.out.println("------------------");
			System.out.println(curBoard[12] + " | " + curBoard[13] + " | " + curBoard[14] + " | " + curBoard[15]);
			System.out.println("------------------");
		//}
	}

	/**
	 * Overloaded equals method to compare two states.
	 * 
	 * @return true or false, depending on whether the states are equal
	 */
	@Override
	public boolean equals(State s)
	{
		if (Arrays.equals(curBoard, ((PuzzleState) s).getCurBoard()))
		{
			return true;
		}
		else
			return false;

	}

	/**
	 * Getter to return the current board array
	 * 
	 * @return the curState
	 */
	public int[] getCurBoard()
	{
		return curBoard;
	}

}
