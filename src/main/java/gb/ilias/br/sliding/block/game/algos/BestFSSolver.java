/**
 *
 */
package gb.ilias.br.sliding.block.game.algos;

import gb.ilias.br.sliding.block.game.algos.internal.NodeEntry;
import gb.ilias.br.sliding.block.game.algos.stats.HeuristicMethod;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Implementation of Best First Search (or Cheapest First Search° algorithm.
 * Very naive.
 *
 * @author ilias
 * @since Oct 19, 2016
 */
public class BestFSSolver extends AbstractSolver {

	/**
	 * Constructor of Best SF Solver
	 *
	 * @param hm
	 *            Takes heuristic method as argument
	 */
	public BestFSSolver(HeuristicMethod hm) {
		super(hm);
	}

	/**
	 * @see gb.ilias.br.sliding.block.game.algos.AbstractSolver#createFringe()
	 */
	@Override
	protected Queue<NodeEntry> createFringe() {
		return new PriorityQueue<NodeEntry>(1000, (o1, o2) -> o1.getDistance() - o2.getDistance());
	}

	/**
	 * @see gb.ilias.br.sliding.block.game.algos.AbstractSolver#getName()
	 */
	@Override
	protected String getName() {
		return "Best First Search";
	}
}
