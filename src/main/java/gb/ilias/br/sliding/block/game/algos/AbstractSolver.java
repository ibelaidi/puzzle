/**
 *
 */
package gb.ilias.br.sliding.block.game.algos;

import gb.ilias.br.sliding.block.game.algos.internal.Block;
import gb.ilias.br.sliding.block.game.algos.internal.BlockCoordinate;
import gb.ilias.br.sliding.block.game.algos.internal.Board;
import gb.ilias.br.sliding.block.game.algos.internal.MoveDirection;
import gb.ilias.br.sliding.block.game.algos.internal.NodeEntry;
import gb.ilias.br.sliding.block.game.algos.stats.HeuristicMethod;
import gb.ilias.br.sliding.block.game.utils.SlidingBlockUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Solver Engine. It is based on different implementations of
 * algorithms.
 *
 * @author ilias
 * @since Oct 19, 2016
 *
 * @see BFSSolver
 * @see BestFSSolver
 * @see AStarSolver
 */
public abstract class AbstractSolver {

	protected Logger				log	= LoggerFactory.getLogger(AbstractSolver.class);

	private final HeuristicMethod	heuristic;
	//
	protected Board					initialBoard;
	protected Board					targetBoard;
	protected Board					currentBoard;
	protected Queue<NodeEntry>		fringe;
	protected Set<Board>			boardTraversed;
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
		this.boardTraversed = new HashSet<>();
		this.fringe = this.createFringe();
		this.log.info("[{}] Algorithm activated with heuristic: [{}]", this.getName(), hm.getName());
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
		this.addBoardToTraversed(root);
		this.fringe.add(new NodeEntry(root, 0));
		//
		this.startTime = System.currentTimeMillis();
		this.numBoard = 1;
		this.log.info("Initial board:");
		this.log.info("\n{}",this.initialBoard.toString());
		this.log.info("Target board:");
		this.log.info("\n{}",this.targetBoard.toString());
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
				this.log.info("\n{}",this.currentBoard.toString());
			}
			if (this.log.isDebugEnabled()) {
				this.log.debug("Blocks in current board: {}", this.currentBoard.getBlocks());
			}
			this.moveCount++;
		}
		this.log.info("Final board:");
		this.log.info("\n{}",this.currentBoard.toString());
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
				if (!currTray.containsKey(new BlockCoordinate(i, j))) {
					if (this.log.isDebugEnabled()) {
						this.log.debug("Empty space: {}", new BlockCoordinate(i, j));
					}
					if (i - 1 >= 0) { // tries moving blocks from the left right
						// down and up of that empty space
						this.applyMove(new BlockCoordinate(i - 1, j, MoveDirection.RIGHT),
								MoveDirection.RIGHT);
					}
					if (i + 1 < this.currentBoard.getWidth()) {
						this.applyMove(new BlockCoordinate(i + 1, j, MoveDirection.LEFT), MoveDirection.LEFT);
					}
					if (j - 1 >= 0) {
						this.applyMove(new BlockCoordinate(i, j - 1, MoveDirection.DOWN), MoveDirection.DOWN);
					}
					if (j + 1 < this.currentBoard.getHeight()) {
						this.applyMove(new BlockCoordinate(i, j + 1, MoveDirection.UP), MoveDirection.UP);
					}
				}
			}
		}
		if (this.log.isDebugEnabled()) {
			this.log.debug("Number of possible moves this board can make: {}", this.boardMove);
		}
		if (this.log.isDebugEnabled()) {
			this.log.debug("Free memory: {} MB", Runtime.getRuntime().freeMemory() / Math.pow(1024, 2));
		}
	}

	/**
	 * Applies the move against the given Block of the current board.
	 *
	 * @param p
	 *            Block being moved
	 * @param direction
	 *            Direction of the move
	 */
	private void applyMove(BlockCoordinate p, MoveDirection direction) {
		final Map<BlockCoordinate, Block> currTray = this.currentBoard.getTray();
		if (currTray.containsKey(p)) {
			final BlockCoordinate upperl = currTray.get(p).UpperLeft();
			BlockCoordinate moveto = new BlockCoordinate(-1, -1);
			if (direction.equals(MoveDirection.RIGHT)) {
				moveto = new BlockCoordinate(upperl.getX() + 1, upperl.getY(), MoveDirection.RIGHT);
			} else if (direction.equals(MoveDirection.LEFT)) {
				moveto = new BlockCoordinate(upperl.getX() - 1, upperl.getY(), MoveDirection.LEFT);
			} else if (direction.equals(MoveDirection.UP)) {
				moveto = new BlockCoordinate(upperl.getX(), upperl.getY() - 1, MoveDirection.UP);
			} else if (direction.equals(MoveDirection.DOWN)) {
				moveto = new BlockCoordinate(upperl.getX(), upperl.getY() + 1, MoveDirection.DOWN);
			}

			try {
				final Board possibleBoard = this.currentBoard.applyMove(currTray.get(p), moveto); // makes
				if (this.log.isDebugEnabled()) {
					try {
						possibleBoard.sanityCheck();
					} catch (final IllegalStateException e) {
						this.log.error(e.getMessage());
					}
				}
				if (this.log.isDebugEnabled()) {
					this.log.debug("New board's hashcode: {}", this.currentBoard.hashCode());
				}
				if (!this.boardTraversed.contains(possibleBoard)) {
					this.numBoard++;
					if (this.log.isDebugEnabled()) {
						this.log.debug("New block positions in board:");
						this.log.debug(possibleBoard.toString());
					}
					final NodeEntry n = new NodeEntry(possibleBoard, this.computeHeuristic(possibleBoard));
					this.fringe.add(n);
					this.addBoardToTraversed(possibleBoard);
					this.boardMove++;
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

	/**
	 * Name of the Algorithm being used to solve the puzzle.
	 *
	 * @return name
	 */
	protected abstract String getName();

	/**
	 * @param board
	 */
	private void addBoardToTraversed(Board board) {
		//if (!SlidingBlockUtils.containsBitSet(this.boardTraversed, board)) {
			//this.boardTraversed.add(SlidingBlockUtils.convertToBitSet(board.hashCode()));
		//}
		this.boardTraversed.add(board);
	}

}
