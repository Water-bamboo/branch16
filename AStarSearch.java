import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

/**
 * Defines an A* algorithms.
 * Every state of a board is a Vertex, from one state to another state means an edges.
 * @author Water-bamboo.
 */
public class AStarSearch {
	static int repeated_count = 0;
	static LinkedList<SearchNode> openSetQueue = new LinkedList<SearchNode>();
	static ArrayList<SearchNode> closedSet = new ArrayList<SearchNode>();
	
	//heuristic: true means: asm+aso;;
	//heuristic: false means: aso+asm;
	//if only asm or aso, it will every bad after test: at least take 30 minutes for a random board.
	//
	static boolean asm_and_aso = true;
	
	/**
	 * Initialization function for 4*4 puzzle.
	 * 
	 * @param board
	 *            - The starting state, represented as a linear array of length
	 * This method use out of place as Heuristic to evalue hcost.
	 */
	public static void search(int[] board, boolean d, boolean asm_then_aso)
	{
		asm_and_aso = asm_then_aso;
		SearchNode root = new SearchNode(new PuzzleState(board));

		openSetQueue.add(root);

		int searchCount = 1; // counter for number of iterations

		//A* openset
		while (!openSetQueue.isEmpty()) // while the queue is not empty
		{
			//hart:-begin-
			double lowestHCost = ((SearchNode) openSetQueue.get(0)).getHCost();
			int lowestCostIndex = 0;
			for (int i = 1; i < openSetQueue.size(); i++) {
				if (openSetQueue.get(i).getHCost() < lowestHCost) {
					lowestCostIndex = i;
					lowestHCost = ((SearchNode) openSetQueue.get(i)).getHCost();
				}
			}
			
			//found out the lowest out_of_place Node in all identical lowestHCost Node.
			int debug_same_oop = 0;
			if (asm_and_aso) {
				int outofplace = ((PuzzleState)((SearchNode) openSetQueue.get(lowestCostIndex)).getCurState()).getOutOfPlace();
				for (int i = 1; i < openSetQueue.size(); i++) {
					final SearchNode sn = (SearchNode)openSetQueue.get(i);
					if (sn.getHCost() == lowestHCost) {
						if (((PuzzleState)sn.getCurState()).getOutOfPlace() < outofplace) {
							lowestCostIndex = i;
						}
						debug_same_oop++;
					}
				}
			}
			else {
				int outofplace = ((PuzzleState)((SearchNode) openSetQueue.get(lowestCostIndex)).getCurState()).getManDist();
				for (int i = 1; i < openSetQueue.size(); i++) {
					final SearchNode sn = (SearchNode)openSetQueue.get(i);
					if (sn.getHCost() == lowestHCost) {
						if (((PuzzleState)sn.getCurState()).getManDist() < outofplace) {
							lowestCostIndex = i;
						}
						debug_same_oop++;
					}
				}
			}
			System.out.println("lowestHCost:"+lowestHCost+" have same_oop count="+debug_same_oop);
			
			SearchNode tempNode = (SearchNode) openSetQueue.remove(lowestCostIndex);
			//hart: -end-
			//SearchNode tempNode = (SearchNode) openSet.poll();
			//closedSet.add(tempNode);
			
			// if the tempNode is not the goal state
			if (!tempNode.getCurState().isGoal())
			{
				// generate tempNode's immediate successors
				ArrayList<State> tempSuccessors = tempNode.getCurState().genSuccessors();
				System.out.println("-----------tempSuccessors.size="+tempSuccessors.size()+"-----------");
				
				//A* closeset??? seams not!!.
				ArrayList<SearchNode> nodeSuccessors = new ArrayList<SearchNode>();

				/*
				 * Loop through the successors, wrap them in a SearchNode, check
				 * if they've already been evaluated, and if not, add them to
				 * the queue
				 */
				for (int i = 0; i < tempSuccessors.size(); i++)
				{
					/*
					 * Create a new SearchNode, with tempNode as the parent,
					 * tempNode's cost + the new cost (1) for this state,
					 * and the Out of Place h(n) value
					 */

					final State s = tempSuccessors.get(i);
					s.printState();
					System.out.println("For: tempSuccessors["+i+"].hole="+((PuzzleState)s).getHole());
					System.out.println("For: s.getManDist()=============="+((PuzzleState) s).getManDist());

					// make the node
					SearchNode checkedNode;
					if (asm_and_aso) {
						checkedNode = new SearchNode(tempNode, s, 
									tempNode.getCost() + s.findCost(),
									((PuzzleState) s).getManDist());
					}
					else {
						checkedNode = new SearchNode(tempNode, s, 
									tempNode.getCost() + s.findCost(),
									((PuzzleState) s).getOutOfPlace());
					}
					
					// Check for repeats before adding the new node
					//boolean isRepeated = checkRepeats_byclosedset(closedSet, checkedNode);
					boolean isRepeated = checkRepeats(checkedNode);
					System.out.println("For: isRepeated="+isRepeated);
					if (!isRepeated)
					{
						nodeSuccessors.add(checkedNode);
					}
					
					if (false) {
						try {
							Thread.sleep(3000);
						}
						catch(InterruptedException e) {
						}
					}
				}

				// Check to see if nodeSuccessors is empty. If it is, continue
				// the loop from the top
				if (nodeSuccessors.size() == 0)
					continue;

				SearchNode lowestNode = nodeSuccessors.get(0);

				/*
				 * This loop finds the lowest f(n) in a node, and then sets that
				 * node as the lowest.
				 */
				for (int i = 0; i < nodeSuccessors.size(); i++)
				{
					if (lowestNode.getFCost() > nodeSuccessors.get(i).getFCost())
					{
						lowestNode = nodeSuccessors.get(i);
					}
				}

				int lowestValue = (int) lowestNode.getFCost();

				// Adds any nodes that have that same lowest value. [Note: there may exist more than one node that has the same loweast value.]
				for (int i = 0; i < nodeSuccessors.size(); i++)
				{
					final SearchNode sn = nodeSuccessors.get(i);
					if (sn.getFCost() == lowestValue)
					{
						System.out.println("nodeSuccessor:["+i+"] is added to Queue.(man="+sn.getHCost()+")");
						openSetQueue.add(sn);
					}
				}

				searchCount++;
			}
			else
			// The goal state has been found. Print the path it took to get to
			// it.
			{
				// Use a stack to track the path from the starting state to the
				// goal state
				Stack<SearchNode> solutionPath = new Stack<SearchNode>();
				//solutionPath.push(tempNode);

				//System.out.println("tempNode.getParent()=" + tempNode.getParent());
				while (tempNode.getParent() != null)
				{
					solutionPath.push(tempNode);
					tempNode = tempNode.getParent();
					if (tempNode == null) 
						break;
				}
				
				if (tempNode != null) {
					solutionPath.push(tempNode);
				}

				// The size of the stack before looping through and emptying it.
				int loopSize = solutionPath.size();

				for (int i = 0; i < loopSize; i++)
				{
					tempNode = solutionPath.pop();
					tempNode.getCurState().printState();
					System.out.println();
					System.out.println();
				}
				((PuzzleState)(root.getCurState())).printStateInline();
				System.out.println("Use asm_than_aso("+asm_and_aso+"), the cost was: " + tempNode.getCost());
				if (d)
				{
					System.out.println("The number of nodes examined: "
							+ searchCount);
							
					System.out.println("The number of repeated count: "
							+ repeated_count);
				}

				System.exit(0);
			}
		}

		// This should never happen with our current puzzles.
		System.out.println("Error! No solution found!");
	}

	/*
	 * Helper method to check to see if a SearchNode has already been evaluated.
	 * Returns true if it has, false if it hasn't.
	 */
	private static boolean checkRepeats(SearchNode n)
	{
		boolean retValue = false;
		SearchNode currentNode = n;

		// While n's parent isn't null, check to see if it's equal to the node
		// we're looking for.
		while (n.getParent() != null && !retValue)
		{
			if (n.getParent().getCurState().equals(currentNode.getCurState()))
			{
				repeated_count++;
				retValue = true;
			}
			n = n.getParent();
		}

		return retValue;
	}
	
	/**
	  * I think original algorithms maybe wrong, so add this another method for checking repeated.
	  * check repeated by comparing to closeset.===this more similar to A* algorithms, and looks power than checkrepeat by parent.[Original algorithms]
	  * !!!!!! This method is uncorrect for check repeated, repeated only invalid at the same path from parent to current.!!!
	*/
	private static boolean checkRepeats_byclosedset(SearchNode n) {
		SearchNode currentNode = n;
		double currentHCost = n.getHCost();

		// While n's parent isn't null, check to see if it's equal to the node
		// we're looking for.
		final int size = closedSet.size();

		for (int i = size - 1; i >= 0; i--) {
			if (closedSet.get(i).getHCost() == currentHCost && closedSet.get(i).getCurState().equals(currentNode.getCurState()))
			{
				repeated_count++;
				return true;
			}
		}

		return false;
	}

}
