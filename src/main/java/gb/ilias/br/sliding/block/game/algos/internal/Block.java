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
		if (one.x < 0 || one.y < 0 || two.x < 0 || two.y < 0) {
			throw new IllegalArgumentException("Points can not be negative.");
		}
		if (one.x > two.x || one.y > two.y) {
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
		return this.LowerRight.y - this.UpperLeft.y + 1;
	}

	public int getWidth() {
		return this.LowerRight.x - this.UpperLeft.x + 1;
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
		return this.UpperLeft.x * 3 + this.UpperLeft.y * 11 + this.LowerRight.x * 19 + this.LowerRight.y * 17;
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