/**
 *
 */
package gb.ilias.br.sliding.block.game;

import gb.ilias.br.sliding.block.game.algos.AStarSolver;
import gb.ilias.br.sliding.block.game.algos.AbstractSolver;
import gb.ilias.br.sliding.block.game.algos.BFSSolver;
import gb.ilias.br.sliding.block.game.algos.BestFSSolver;
import gb.ilias.br.sliding.block.game.algos.internal.Board;
import gb.ilias.br.sliding.block.game.algos.stats.HeuristicMethod;
import gb.ilias.br.sliding.block.game.algos.stats.LinearConflictImpl;
import gb.ilias.br.sliding.block.game.algos.stats.ManhattanDistanceImpl;
import gb.ilias.br.sliding.block.game.utils.BoardFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main launcher for this Sliding-Block Puzzle..
 *
 * @author ilias
 * @since Oct 19, 2016
 */
public class SlidingBlockLauncher {

	private static Logger	log	= LoggerFactory.getLogger(SlidingBlockLauncher.class);

	/**
	 * Main method for this launcher
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			if (args.length < 1) {
				log.error("Wrong number arguments, got {} number of arguments. Expected 2+", args.length);
				System.exit(1);
			}
			String inputfile;
			String outputfile;
			String algo = null;
			String heuristic = null;
			if (args.length > 2) {
				int i = 0;
				if (args[0].startsWith("-a")) {
					algo = args[0].replace("-a", "");
				}
				if (args[1].startsWith("-h")) {
					heuristic = args[1].replace("-h", "");
					i = 2;
				}
				inputfile = args[i];
				outputfile = args[i + 1];
			} else {
				inputfile = args[0];
				outputfile = args[1];
			}
			final Board initBoard = BoardFactory.createBoard(inputfile, 0, 0);
			final Board targetBoard = BoardFactory.createBoard(outputfile, initBoard.getHeight(),
					initBoard.getWidth());
			final AbstractSolver solver = createSolver(algo, createHeuristicMethod(heuristic));
			solver.solve(initBoard, targetBoard);
		} catch (final Throwable e) {
			log.error(e.getMessage(), e);
		}
	}

	/**
	 * Creates the heuristic method for the engine solver from the given main
	 * arguments.
	 *
	 * @return Heuristic implementation
	 *
	 * @see LinearConflictImpl
	 * @see ManhattanDistanceImpl
	 */
	private static HeuristicMethod createHeuristicMethod(String heuristic) {
		if ("LC".equals(heuristic)) {
			return new LinearConflictImpl();
		}
		return new ManhattanDistanceImpl();
	}

	/**
	 * From input argument of main method, this will create an Engine Solver
	 * with the given Heuristic Method.
	 *
	 * @param hm
	 *            Heuristic Method to be used
	 * @return Engine solver
	 *
	 * @see AStarSolver
	 * @see BestFSSolver
	 * @see BFSSolver
	 * @see #createHeuristicMethod()
	 */
	private static AbstractSolver createSolver(String algo, HeuristicMethod hm) {
		if ("BFS".equals(algo)) {
			return new BFSSolver(hm);
		}
		if ("CFS".equals(algo)) {
			return new BestFSSolver(hm);
		}
		return new AStarSolver(hm);
	}
}
