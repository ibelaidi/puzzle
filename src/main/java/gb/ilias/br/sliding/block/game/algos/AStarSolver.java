/**
 *
 */
package gb.ilias.br.sliding.block.game.algos;

import gb.ilias.br.sliding.block.game.algos.internal.NodeEntry;
import gb.ilias.br.sliding.block.game.algos.stats.HeuristicMethod;

import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Implementation of A* start algorithm.
 *
 * @author ilias
 * @since Oct 19, 2016
 */
public class AStarSolver extends AbstractSolver {

	/**
	 * Constructor of A* Solver
	 *
	 * @param hm
	 *            Takes heuristic method as argument
	 */
	public AStarSolver(HeuristicMethod hm) {
		super(hm);
	}

	/**
	 * @see gb.ilias.br.sliding.block.game.algos.AbstractSolver#createFringe()
	 */
	@Override
	protected Queue<NodeEntry> createFringe() {
		return new PriorityQueue<NodeEntry>(1000, (obj1, obj2) -> {
			if (obj1.getDistance() > obj2.getDistance()) {
				return 1;
			} else if (obj1.getDistance() < obj2.getDistance()) {
				return -1;
			} else {
				return 0;
			}
		});
	}

	/**
	 * @see gb.ilias.br.sliding.block.game.algos.AbstractSolver#getName()
	 */
	@Override
	protected String getName() {
		return "A*";
	}
}
