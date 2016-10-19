/**
 *
 */
package gb.ilias.br.sliding.block.game.algos.internal;

/**
 * Move blocks direction.
 * @author ilias
 * @since Oct 19, 2016
 */
public enum MoveDirection {

	UP("up"), DOWN("down"), LEFT("left"), RIGHT("right");

	private String direction;

	MoveDirection(String o) {
		this.direction = o;
	}

	public String getDirection() {
		return this.direction;
	}
}
