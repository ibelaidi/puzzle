/**
 *
 */
package gb.ilias.br.sliding.block.game.algos;

import gb.ilias.br.sliding.block.game.algos.internal.NodeEntry;
import gb.ilias.br.sliding.block.game.algos.stats.HeuristicMethod;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Classic implementation of Breadth Frst Search algorithm.
 *
 * @author ilias
 * @since Oct 19, 2016
 */
public class BFSSolver extends AbstractSolver {

	/**
	 * Constructor of Breadth SF Solver
	 *
	 * @param hm
	 *            Takes heuristic method as argument
	 */
	public BFSSolver(HeuristicMethod hm) {
		super(hm);
	}

	/**
	 * @see gb.ilias.br.sliding.block.game.algos.AbstractSolver#createFringe()
	 */
	@Override
	protected Queue<NodeEntry> createFringe() {
		return new LinkedList<NodeEntry>();
	}
}
