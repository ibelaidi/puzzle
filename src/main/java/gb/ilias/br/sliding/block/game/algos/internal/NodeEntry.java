/**
 *
 */
package gb.ilias.br.sliding.block.game.algos.internal;


/**
 * @author ilias
 * @since Oct 19, 2016
 */
public class NodeEntry {

	private final int	distance;
	private final Board	myBoard;

	public NodeEntry(Board b, int dis) {
		this.myBoard = b;
		this.distance = dis;
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		return this.distance;
	}

	/**
	 * @return the myBoard
	 */
	public Board getMyBoard() {
		return this.myBoard;
	}

}
