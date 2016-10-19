/**
 *
 */
package gb.ilias.br.sliding.block.game.algos.stats;

import gb.ilias.br.sliding.block.game.algos.internal.Block;
import gb.ilias.br.sliding.block.game.algos.internal.Board;

/**
 * Implementation of Linear Conflict Tiles which is:
 * <p>
 * <i> Two tiles tj and tk are in a linear conflict if tj and tk are in the same
 * line, the goal positions of tj and tk are both in that line, tj is to the
 * right of tk and goal position of tj is to the left of the goal position of
 * tk. </i>
 * </p>
 *
 * @author ilias
 * @since Oct 19, 2016
 */
public class LinearConflictImpl extends HeuristicMethod {

	/**
	 * @see gb.ilias.br.sliding.block.game.algos.stats.HeuristicMethod#computeHeuristic(gb.ilias.br.sliding.block.game.algos.internal.Board)
	 */
	@Override
	public int computeHeuristic(Board targetBoard, Board board) {
		int sumdistance = 0;
		for (final Block b : targetBoard.getBlocks()) {
			for (final Block i : board.getBlocks()) {
				if (b.getType().equals(i.getType())) {
					sumdistance += Math.abs(i.UpperLeft().x - b.UpperLeft().x);
					sumdistance += Math.abs(i.UpperLeft().y - b.UpperLeft().y);
				}
			}
		}
		return sumdistance;
	}

}
