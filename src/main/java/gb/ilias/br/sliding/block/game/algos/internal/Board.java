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

		for (int j = block.UpperLeft().x; j <= block.LowerRight().x; j++) {
			for (int k = block.UpperLeft().y; k <= block.LowerRight().y; k++) {
				this.myTray.remove(new BlockCoordinate(j, k));
				this.Blocks.remove(block);
			}
		}
	}

	private void fillBlock(Block block) {

		for (int j = block.UpperLeft().x; j <= block.LowerRight().x; j++) {
			for (int k = block.UpperLeft().y; k <= block.LowerRight().y; k++) {
				this.myTray.put(new BlockCoordinate(j, k), block);
				this.Blocks.add(block);
			}
		}
	}

	private boolean validMove(Block block, BlockCoordinate point) {
		if (point.x < 0 || point.y < 0) {
			return false;
		}

		if (point.x > this.boardWidth || point.y > this.boardHeight) {
			return false;
		}

		this.clearBlock(block);

		for (int j = point.x; j < point.x + block.getWidth(); j++) {
			for (int k = point.y; k < point.y + block.getHeight(); k++) {
				if (this.myTray.containsKey(new BlockCoordinate(j, k))) {
					this.fillBlock(block);
					return false;
				}
			}
		}
		return true;
	}

	public Board makeMove(Block block, BlockCoordinate point) throws IllegalStateException {

		final Block tempBlock = new Block(block.UpperLeft(), block.LowerRight()); // copy
		// of
		// the
		// block
		// being
		// moved

		if (!this.validMove(block, point)) {
			throw new IllegalStateException("not a valid move!");
		} else {
			this.fillBlock(block); // refill the block in because we are
			// creating a new Board with the updated
			// block position
			tempBlock.changeOrientation(point, new BlockCoordinate(point.x + block.getWidth() - 1, point.y
					+ block.getHeight() - 1));
		}

		final Board board = new Board(this.boardHeight, this.boardWidth, this);
		board.clearBlock(block); // clear the original position of the block in
		// the new board
		board.fillBlock(tempBlock); // fill the new board with the updated block
		// position

		final String moveMade = Integer.toString(block.UpperLeft().y) + " "
				+ Integer.toString(block.UpperLeft().x) + " " + Integer.toString(point.y) + " "
				+ Integer.toString(point.x);
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

				// This checks to make sure the blocks are within the actual
				// board dimensions.
				if (current.UpperLeft().x < 0 || current.UpperLeft().y < 0
						|| current.LowerRight().x > this.boardWidth
						|| current.LowerRight().y > this.boardHeight) {
					throw new IllegalStateException("isOK Error: Blocks go out of dimensions given.");
				}

				// This checks to make sure the upper left point values are
				// smaller or equal to the lower right point values.
				if (current.UpperLeft().x > current.LowerRight().x
						|| current.UpperLeft().y > current.LowerRight().y) {
					throw new IllegalStateException(
							"isOK Error: The blocks' point values are not correctly inputted as upper left and lower right coordinates.");
				}

				// This adds blocks into an array list by converting them into
				// points,
				// so the values of blocks that occupy more than two spaces can
				// be checked for all of the blocks they occupy.
				for (int j = current.UpperLeft().x; j <= current.LowerRight().x; j++) {
					for (int k = current.UpperLeft().y; k <= current.LowerRight().y; k++) {
						tempPoints.add(new BlockCoordinate(j, k));
					}
				}

				// This checks for overlapping blocks.
				for (final BlockCoordinate checkPoint : tempPoints) {
					if (occupied.contains(checkPoint)) {
						throw new IllegalStateException("isOK Error: Overlapping block space at ("
								+ checkPoint.x + "," + checkPoint.y + ")");
					} else {
						occupied.add(checkPoint);
					}
				} // end overlapping block check
			} // end for loop for hash set of blocks
		} // end null check
		return true;
	} // end isOK

	@Override
	public int hashCode() { // new hashCode

		int rtn = 0;
		for (final Block block : this.myTray.values()) {
			rtn += Math.pow(block.UpperLeft().x, 2) + Math.pow(block.UpperLeft().y, 3)
					+ Math.pow(block.LowerRight().x, 4) + Math.pow(block.LowerRight().y, 5);
		}
		return rtn;
	}

	@Override
	public boolean equals(Object o) {
		final Board board = (Board) o;
		if (board.boardHeight != this.boardHeight || board.boardWidth != this.boardWidth) {
			return false;
		} else if (!this.myTray.equals(board.myTray)) { // same board if trays
			// are equal
			return false;
		} else {
			return true;
		}
	}

	public Map<BlockCoordinate, Block> getTray() {
		return this.myTray;
	}

	@Override
	public String toString() { // prints out the size of the block at each
		// location for example a 1x1 block at 0,0 would
		// just be the number 1 at the location 0,0
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

	public String displayMoves() { // prints all the moves made previously to
		// get to this board in myMoves

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