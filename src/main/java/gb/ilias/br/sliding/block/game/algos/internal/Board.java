package gb.ilias.br.sliding.block.game.algos.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Board {

	private final Map<BlockCoordinate, Block>	myTray;		// our tray of
	// points
	// that have a block at
	// that location
	private final int							boardWidth;
	private final int							boardHeight;
	private final ArrayList<String>				myMoves;		// all the moves
	// made to
	// get the board
	// to the
	// configuration
	// it is
	// currently at
	// from the
	// starting
	// board
	private final HashSet<Block>				Blocks;		// all the

	// blocks this
	// board
	// contains

	public Board(int height, int width, Board parent) { // our constructor for
		// making a board that
		// isn't the initial
		// configuration board

		if (height > 256 || width > 256) {
			throw new IllegalArgumentException("Exceeded maximum allowed configuration for tray size.");
		}
		if (height < 0 || width < 0) {
			throw new IllegalArgumentException("Size must be a nonnegative number.");
		}
		this.boardWidth = width;
		this.boardHeight = height;

		// takes in a parent board because it must have all of the blocks it's
		// parent save for the one block that was moved to get it to this new
		// configuration
		// also it must have all of the moves it's parent had in its arraylist
		// plus one extra move
		this.myTray = new HashMap<BlockCoordinate, Block>();
		this.myMoves = new ArrayList<String>();
		this.Blocks = new HashSet<Block>();

		this.Blocks.addAll(parent.Blocks);
		this.myMoves.addAll(parent.myMoves);

		if (parent.myTray != null) {
			this.myTray.putAll(parent.myTray);
		}
	}

	public Board(int height, int width) { // initial board config constructor
		this.boardWidth = width;
		this.boardHeight = height;
		this.myTray = new HashMap<BlockCoordinate, Block>();
		this.myMoves = new ArrayList<String>();
		this.Blocks = new HashSet<Block>();
	}

	private void clearBlock(Block block) { // we made seperate methods for
		// clearBlock and fillBlock in our
		// tray because we use the code so
		// often it seemed extraneous to
		// keep typing it out

		for (int j = block.UpperLeft().getX(); j <= block.LowerRight().getX(); j++) {
			for (int k = block.UpperLeft().getY(); k <= block.LowerRight().getY(); k++) {
				this.myTray.remove(new BlockCoordinate(j, k));
				this.Blocks.remove(block);
			}
		}
	}

	private void fillBlock(Block block) {

		for (int j = block.UpperLeft().getX(); j <= block.LowerRight().getX(); j++) {
			for (int k = block.UpperLeft().getY(); k <= block.LowerRight().getY(); k++) {
				this.myTray.put(new BlockCoordinate(j, k), block);
				this.Blocks.add(block);
			}
		}
	}

	private boolean validMove(Block block, BlockCoordinate point) {
		if (point.getX() < 0 || point.getY() < 0) {
			return false;
		}

		if (point.getX() > this.boardWidth || point.getY() > this.boardHeight) {
			return false;
		}

		this.clearBlock(block);

		for (int j = point.getX(); j < point.getX() + block.getWidth(); j++) {
			for (int k = point.getY(); k < point.getY() + block.getHeight(); k++) {
				if (this.myTray.containsKey(new BlockCoordinate(j, k))) {
					this.fillBlock(block);
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @param block
	 * @param point
	 * @return
	 * @throws IllegalStateException
	 */
	public Board applyMove(Block block, BlockCoordinate point) throws IllegalStateException {
		final Block tempBlock = new Block(block.UpperLeft(), block.LowerRight());
		if (!this.validMove(block, point)) {
			throw new IllegalStateException("not a valid move!");
		} else {
			this.fillBlock(block);
			tempBlock.changeOrientation(point,
					new BlockCoordinate(point.getX() + block.getWidth() - 1, point.getY() + block.getHeight()
							- 1));
		}
		final Board board = new Board(this.boardHeight, this.boardWidth, this);
		board.clearBlock(block);
		board.fillBlock(tempBlock);
		final String moveMade = "MOVE (" + Integer.toString(block.UpperLeft().getY()) + " "
				+ Integer.toString(block.UpperLeft().getX()) + ") " + point.getDirection().name() + " to ("
				+ Integer.toString(point.getY()) + " " + Integer.toString(point.getX()) + ")";
		board.myMoves.add(moveMade);
		return board;
	}

	public void addBlock(Block block) throws IllegalStateException {

		if (this.myTray.containsValue(block)) {
			throw new IllegalStateException("block already in board!");
		}

		if (!this.validMove(block, block.UpperLeft())) {
			throw new IllegalStateException("not a valid block position!");
		}

		this.fillBlock(block);
	}

	public boolean sanityCheck() throws IllegalStateException {
		final ArrayList<BlockCoordinate> occupied = new ArrayList<BlockCoordinate>();
		if (this.Blocks != null) {
			for (final Block current : this.Blocks) {
				final ArrayList<BlockCoordinate> tempPoints = new ArrayList<BlockCoordinate>();
				if (current.UpperLeft().getX() < 0 || current.UpperLeft().getY() < 0
						|| current.LowerRight().getX() > this.boardWidth
						|| current.LowerRight().getY() > this.boardHeight) {
					throw new IllegalStateException("isOK Error: Blocks go out of dimensions given.");
				}

				if (current.UpperLeft().getX() > current.LowerRight().getX()
						|| current.UpperLeft().getY() > current.LowerRight().getY()) {
					throw new IllegalStateException(
							"isOK Error: The blocks' point values are not correctly inputted as upper left and lower right coordinates.");
				}

				for (int j = current.UpperLeft().getX(); j <= current.LowerRight().getX(); j++) {
					for (int k = current.UpperLeft().getY(); k <= current.LowerRight().getY(); k++) {
						tempPoints.add(new BlockCoordinate(j, k));
					}
				}

				for (final BlockCoordinate checkPoint : tempPoints) {
					if (occupied.contains(checkPoint)) {
						throw new IllegalStateException("isOK Error: Overlapping block space at ("
								+ checkPoint.getX() + "," + checkPoint.getY() + ")");
					} else {
						occupied.add(checkPoint);
					}
				}
			}
		}
		return true;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		int rtn = 0;
		for (final Block block : this.myTray.values()) {
			rtn += Math.pow(block.UpperLeft().getX(), 2) + Math.pow(block.UpperLeft().getY(), 3)
					+ Math.pow(block.LowerRight().getX(), 4) + Math.pow(block.LowerRight().getY(), 5);
		}
		return rtn;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		final Board board = (Board) o;
		if (board.boardHeight != this.boardHeight || board.boardWidth != this.boardWidth) {
			return false;
		} else if (!this.myTray.equals(board.myTray)) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @return
	 */
	public Map<BlockCoordinate, Block> getTray() {
		return this.myTray;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String board = "";

		for (int j = 0; j < this.boardHeight; j++) {
			for (int k = 0; k < this.boardWidth; k++) {
				if (this.myTray.containsKey(new BlockCoordinate(k, j))) {
					final Block block = this.myTray.get(new BlockCoordinate(k, j));
					final int size = block.getHeight() * block.getWidth();
					board += Integer.toString(size);
				} else {
					board += "O";
				}
			}
			board += "\n";
		}
		return board;
	}

	/**
	 * @return
	 */
	public String displayMoves() {
		String allMoves = "";
		for (int i = 0; i < this.myMoves.size(); i++) {
			allMoves += this.myMoves.get(i);
			allMoves += "\n";
		}
		return allMoves;
	}

	public int getHeight() {
		return this.boardHeight;
	}

	public int getWidth() {
		return this.boardWidth;
	}

	public HashSet<Block> getBlocks() {
		return this.Blocks;
	}
}