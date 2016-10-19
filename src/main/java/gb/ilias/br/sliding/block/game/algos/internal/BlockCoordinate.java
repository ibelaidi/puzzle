package gb.ilias.br.sliding.block.game.algos.internal;

/**
 * Represents the block coordinates. X/Y point in the tray. Could be Upper
 * Left or Lower Right point.
 *
 * @author ilias
 * @since Oct 18, 2016
 */
public class BlockCoordinate {
	public int	x;
	public int	y;

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