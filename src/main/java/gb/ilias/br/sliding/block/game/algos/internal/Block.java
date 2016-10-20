package gb.ilias.br.sliding.block.game.algos.internal;

/**
 * Definition of a block in the Tray. 1x1, 1x2, 2x2, 1x3...
 *
 * @author ilias
 * @since Oct 19, 2016
 */
public class Block {

	private BlockCoordinate	UpperLeft;
	private BlockCoordinate	LowerRight;
	private final String	size;

	public Block(BlockCoordinate one, BlockCoordinate two) {
		if (one.getX() < 0 || one.getY() < 0 || two.getX() < 0 || two.getY() < 0) {
			throw new IllegalArgumentException("Points can not be negative.");
		}
		if (one.getX() > two.getX() || one.getY() > two.getY()) {
			throw new IllegalArgumentException("BlockCoordinates are not UpperLeft and LowerRight");
		}
		this.UpperLeft = one;
		this.LowerRight = two;
		this.size = Integer.toString(this.getHeight()) + "x" + Integer.toString(this.getWidth());
	}

	public BlockCoordinate UpperLeft() {
		return this.UpperLeft;
	}

	public BlockCoordinate LowerRight() {
		return this.LowerRight;
	}

	public int getHeight() {
		return this.LowerRight.getY() - this.UpperLeft.getY() + 1;
	}

	public int getWidth() {
		return this.LowerRight.getX() - this.UpperLeft.getX() + 1;
	}

	public void changeOrientation(BlockCoordinate one, BlockCoordinate two) {
		final Block temp = new Block(one, two);
		if (temp.getHeight() != this.getHeight() || temp.getWidth() != this.getWidth()) {
			throw new IllegalArgumentException("Block size can not change");
		}
		this.UpperLeft = one;
		this.LowerRight = two;
	}

	public String getType() {
		return this.size;
	}

	public Block newCopy() {
		return new Block(this.UpperLeft(), this.LowerRight());
	}

	@Override
	public int hashCode() {
		return this.UpperLeft.getX() * 3 + this.UpperLeft.getY() * 11 + this.LowerRight.getX() * 19
				+ this.LowerRight.getY() * 17;
	}

	@Override
	public boolean equals(Object o) {
		final Block block = (Block) o;
		return block.UpperLeft.equals(this.UpperLeft) && block.LowerRight.equals(this.LowerRight);
	}

	@Override
	public String toString() {
		return " (" + this.UpperLeft + this.LowerRight + ") ";
	}
}