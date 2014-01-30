import java.util.Random;

public class ProblemSolver
{
	/*
	 * We expect arguments in the form:
	 * 
	 * ./ProblemSolver <-d> aso/asm <initial state>
	 * 
	 * Example: ./ProblemSolver asm/aso 0 1 2 3 4 5 6 7 8 9 10 11 12 13 14 15
	 * Example: ./ProblemSolver asm/aso <if any letter, will generate random 16 number>
	 * 
	 * See Readme for more information.
	 */
	public static void main(String[] args)
	{
		// Numbers to be adjusted if the debug toggle is present, as components
		// of args will be in different locations if it is.
		int searchTypeDebug = 0;
		int eightPuzzleDebug = 1;
		boolean debug = true;

		if (args.length > 1) {
			int[] initBoard = new int[16];
			if (args.length == 2) {
				//generate 16 random number
				Random r = new Random(15);
				
				int index = 0;
				do {
					int x = r.nextInt(16);
					System.out.print(" ("+index+") x="+x);
					boolean duplicate = false;
					for (int i = 0; i < 16; i++) {
						if (x == initBoard[i]) {
							duplicate = true;
							break;
						}
					}
					if (duplicate == false) {
						initBoard[index++] = x;
					}					
				} while(index < 15);
			}
			else if (args.length == 17) {
				for (int i = 1; i < 17; i++)
				{
					initBoard[i-1] = Integer.parseInt(args[i]);
				}
			}
			
			System.out.println();
			for (int i = 0; i < 16; i++) {
				System.out.print(initBoard[i]+" | ");
			}
			System.out.println();
			
			if (true) {
				String searchType = args[searchTypeDebug].toLowerCase();
				if (searchType.equals("aso"))
				{
					AStarSearch.search(initBoard, debug, false);
				}
				// Use AStarSearch.java with Manhattan Distance
				else if (searchType.equals("asm"))
				{
					AStarSearch.search(initBoard, debug, true);
				}
				else {
					printUsage();
				}
			}
		}
		else {
			printUsage();
		}
	}

	// Helper method to print the correct usage and end the program
	private static void printUsage()
	{
		System.out.println("Usage: ./Main <searchType> [Initial Puzzle State]");
		System.exit(-1);
	}
}
