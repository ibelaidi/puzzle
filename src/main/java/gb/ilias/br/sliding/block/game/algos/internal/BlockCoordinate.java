package gb.ilias.br.sliding.block.game.algos.internal;

/**
 * Represents the block coordinates. X/Y point in the tray. Could be Upper
 * Left or Lower Right point.
 *
 * @author ilias
 * @since Oct 18, 2016
 */
public class BlockCoordinate {
	private final int		x;
	private final int		y;
	private MoveDirection	direction;

	/**
	 * Constructor with predefined points: Upper Left or Lower Right point
	 *
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 */
	public BlockCoordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Constructor with predefined points: Upper Left or Lower Right point
	 *
	 * @param x
	 *            X coordinate
	 * @param y
	 *            Y coordinate
	 * @param direct
	 *            Direction to move the block to
	 */
	public BlockCoordinate(int x, int y, MoveDirection direct) {
		this(x, y);
		this.direction = direct;
	}

	/**
	 * @return the x
	 */
	public int getX() {
		return this.x;
	}

	/**
	 * @return the y
	 */
	public int getY() {
		return this.y;
	}

	/**
	 * @return the direction
	 */
	public MoveDirection getDirection() {
		return this.direction;
	}

	/**
	 * @param direction
	 *            the direction to set
	 */
	public void setDirection(MoveDirection direction) {
		this.direction = direction;
	}

	/**
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}

		final BlockCoordinate p2 = (BlockCoordinate) other;
		return p2.x == this.x && p2.y == this.y;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return " (" + this.x + this.y + ") ";
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + this.x;
		result = prime * result + this.y;
		return result;
	}

}