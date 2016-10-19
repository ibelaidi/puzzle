/**
 *
 */
package gb.ilias.br.sliding.block.game.algos;

import gb.ilias.br.sliding.block.game.algos.internal.Block;
import gb.ilias.br.sliding.block.game.algos.internal.BlockCoordinate;
import gb.ilias.br.sliding.block.game.algos.internal.Board;
import gb.ilias.br.sliding.block.game.algos.internal.NodeEntry;
import gb.ilias.br.sliding.block.game.algos.stats.HeuristicMethod;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ilias
 * @since Oct 19, 2016
 */
public abstract class AbstractSolver {

	protected Logger				log	= LoggerFactory.getLogger(AbstractSolver.class);

	private final HeuristicMethod	heuristic;
	//
	protected Board					initialBoard;
	protected Board					targetBoard;
	protected Board					currentBoard;
	protected Queue<NodeEntry>		fringe;
	protected Set<Board>			boardSeen;
	protected NodeEntry				temp;
	protected boolean				debug;
	protected int					moveCount;
	protected int					boardMove;
	protected int					numBoard;
	protected long					startTime;
	protected long					stopTime;

	/**
	 * Abstract constructor. Only visible to direct implementors.
	 */
	protected AbstractSolver(HeuristicMethod hm) {
		super();
		this.heuristic = hm;
		this.boardSeen = new HashSet<Board>();
		this.fringe = this.createFringe();
	}

	/**
	 * Abstract Method to be implemented by different algorithms
	 *
	 * @param root
	 * @param goal
	 */
	public void solve(Board root, Board goal) {
		this.initialBoard = root;
		this.targetBoard = goal;
		this.currentBoard = root;
		this.boardSeen.add(root);
		this.fringe.add(new NodeEntry(root, 0));
		//
		this.startTime = System.currentTimeMillis();
		this.numBoard = 1;
		this.log.info("Initial board:");
		this.log.info(this.initialBoard.toString());
		this.log.info("Target board:");
		this.log.info(this.targetBoard.toString());
		this.moveCount = 0;
		while (!this.isSolved()) {
			this.boardMove = 0;
			this.findPossibleMoves(); // see below code
			if (this.fringe.isEmpty()) {
				this.log.info("No solution found :(  Exiting...");
				System.exit(1);
			}
			this.temp = this.fringe.remove(); // takes out board from fringe
			this.currentBoard = this.temp.getMyBoard();
			if (this.log.isDebugEnabled()) {
				this.stopTime = System.currentTimeMillis();
				this.log.debug("Time elapsed: {}", this.stopTime - this.startTime);
			}
			if (this.log.isDebugEnabled()) {
				this.log.debug("Current board's manhattan distance in queue: {}", this.temp.getDistance());
			}
			if (this.log.isDebugEnabled()) {
				this.log.info("Current board:");
				this.log.info(this.currentBoard.toString());
			}
			if (this.log.isDebugEnabled()) {
				this.log.debug("Blocks in current board: {}", this.currentBoard.getBlocks());
			}
			this.moveCount++;
		}
		this.log.info("Final board:");
		this.log.info(this.currentBoard.toString());
		this.log.info("Move count: {}", this.moveCount);
		this.log.info("Number of boards added to fringe: {}", this.numBoard);
		this.stopTime = System.currentTimeMillis();
		this.log.info("Final time elapsed: {} ms", this.stopTime - this.startTime);
		this.log.info(this.currentBoard.displayMoves()); // print out all
	}

	/**
	 * Checks whether the algorithm reached it's target board.
	 *
	 * @return <tt>True</tt> if board is solved, <tt>False</tt> otherwise.
	 */
	private boolean isSolved() {
		for (final Block i : this.targetBoard.getBlocks()) {
			if (!this.currentBoard.getBlocks().contains(i)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Finds all possible moves to be made against the empty space
	 */
	private void findPossibleMoves() {
		final Map<BlockCoordinate, Block> currTray = this.currentBoard.getTray();
		for (int i = 0; i < this.currentBoard.getWidth(); i++) {
			for (int j = 0; j < this.currentBoard.getHeight(); j++) {
				if (!currTray.containsKey(new BlockCoordinate(i, j))) { // looks
					// for
					// empty
					// spaces until
					// it finds one
					if (this.log.isDebugEnabled()) {
						this.log.debug("empty space: " + new BlockCoordinate(i, j));
					}
					if (i - 1 >= 0) { // tries moving blocks from the left right
						// down and up of that empty space
						this.applyMove(new BlockCoordinate(i - 1, j), "right");
					}
					if (i + 1 < this.currentBoard.getWidth()) {
						this.applyMove(new BlockCoordinate(i + 1, j), "left");
					}
					if (j - 1 >= 0) {
						this.applyMove(new BlockCoordinate(i, j - 1), "down");
					}
					if (j + 1 < this.currentBoard.getHeight()) {
						this.applyMove(new BlockCoordinate(i, j + 1), "up");
					}
				}
			}
		}
		if (this.log.isDebugEnabled()) {
			this.log.debug("number of possible moves this board can make: " + this.boardMove);
		}
		if (this.log.isDebugEnabled()) {
			this.log.debug("Free memory: {} MB" + Runtime.getRuntime().freeMemory() / Math.pow(1024, 2));
		}
	}

	private void applyMove(BlockCoordinate p, String direction) {
		final Map<BlockCoordinate, Block> currTray = this.currentBoard.getTray();
		if (currTray.containsKey(p)) {
			final BlockCoordinate upperl = currTray.get(p).UpperLeft();
			BlockCoordinate moveto = new BlockCoordinate(-1, -1);
			if (direction.equals("right")) {
				moveto = new BlockCoordinate(upperl.x + 1, upperl.y);
			} else if (direction.equals("left")) {
				moveto = new BlockCoordinate(upperl.x - 1, upperl.y);
			} else if (direction.equals("up")) {
				moveto = new BlockCoordinate(upperl.x, upperl.y - 1);
			} else if (direction.equals("down")) {
				moveto = new BlockCoordinate(upperl.x, upperl.y + 1);
			}

			try {
				final Board possibleBoard = this.currentBoard.makeMove(currTray.get(p), moveto); // makes
				if (this.log.isDebugEnabled()) {
					try {
						possibleBoard.sanityCheck();
					} catch (final IllegalStateException e) {
						this.log.error(e.getMessage(), e);
					}
				}
				if (this.log.isDebugEnabled()) {
					this.log.debug("new board's hashcode: " + this.currentBoard.hashCode());
				}
				if (!this.boardSeen.contains(possibleBoard)) {
					this.numBoard++;
					if (this.log.isDebugEnabled()) {
						this.log.debug("new block positions in board:");
						this.log.debug(possibleBoard.toString());
					}
					final NodeEntry n = new NodeEntry(possibleBoard, this.computeHeuristic(possibleBoard));
					this.fringe.add(n);
					this.boardSeen.add(possibleBoard);
					this.boardMove++;
				} else {
					if (this.log.isDebugEnabled()) {
						this.log.debug("A previously seen board type was reached and therefore not added to the fringe");
					}
				}
			} catch (final IllegalStateException e) {
				return;
			}
		}
	}

	/**
	 * Computes a heuristic from the underlying HeuristicMethod.
	 *
	 *
	 * @return Heuristic
	 */
	public int computeHeuristic(Board board) {
		return this.heuristic.computeHeuristic(this.targetBoard, board);
	}

	/**
	 * @return
	 */
	protected abstract Queue<NodeEntry> createFringe();

}
