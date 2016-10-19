/**
 *
 */
package gb.ilias.br.sliding.block.game.algos.stats;

import gb.ilias.br.sliding.block.game.algos.internal.Block;
import gb.ilias.br.sliding.block.game.algos.internal.Board;

/**
 * Implementation of Manhattan Distance which is is the distance between two
 * points measured along axes at right angles.
 *
 * @author ilias
 * @since Oct 19, 2016
 */
public class ManhattanDistanceImpl extends HeuristicMethod {

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

	/**
	 * @see gb.ilias.br.sliding.block.game.algos.stats.HeuristicMethod#getName()
	 */
	@Override
	public String getName() {
		return "Manhattan-Distance";
	}

}
