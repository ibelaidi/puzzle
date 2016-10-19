/**
 *
 */
package gb.ilias.br.sliding.block.game.algos.stats;

import gb.ilias.br.sliding.block.game.algos.internal.Board;

/**
 * Abstract empty implementation of heuristics that'd be used in the solver
 * algorithm methods.
 *
 * @author ilias
 * @since Oct 19, 2016
 */
public abstract class HeuristicMethod {

	/**
	 * Heuristic method to be implemented by underlying.
	 *
	 * @param targetBoard
	 *            Final/Target board
	 * @param board
	 *            Actual board
	 * @return Heuristic weight.
	 */
	public abstract int computeHeuristic(Board targetBoard, Board board);
}
