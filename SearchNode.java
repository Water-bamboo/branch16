/**
 * 
 * Class to represent a SearchNode. This will be a wrapper for a State, and
 * track the cost to get to that state and the state's parent node.
 * 
 * @author Michael Langston && Gabe Ferrer
 * 
 */
public class SearchNode
{

	public PuzzleState curState;
	public SearchNode parent;
	private double cost; // cost to get to this state
	private double hCost; // heuristic cost
	private double fCost; // f(n) cost

	/**
	 * Constructor for the root SearchNode
	 * 
	 * @param s
	 *            the state passed in
	 */
	public SearchNode(PuzzleState s)
	{
		curState = s;
		parent = null;
		cost = 0;
		hCost = 0;
		fCost = 0;
	}

	/**
	 * Constructor for all other SearchNodes
	 * 
	 * @param prev
	 *            the parent node
	 * @param s
	 *            the state
	 * @param c
	 *            the g(n) cost to get to this node
	 * @param h
	 *            the h(n) cost to get to this node
	 */
	public SearchNode(SearchNode prev, PuzzleState s, double c)
	{
		parent = prev;
		curState = s;
		cost = c;
		hCost = s.outOfPlace;//how many movements to get to this node just like a Weigh.
		fCost = cost + hCost;
	}

	/**
	 * @return the cost
	 */
	public double getCost()
	{
		return cost;
	}

	/**
	 * 
	 * @return the heuristic cost
	 */
	public double getHCost()
	{
		return hCost;
	}

	/**
	 * 
	 * @return the f(n) cost for A*
	 */
	public double getFCost()
	{
		return fCost;
	}
}
