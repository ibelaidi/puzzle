/**
 *
 */
package gb.ilias.br.sliding.block.game.algos.internal;

import java.util.Comparator;

/**
 * @author ilias
 * @since Oct 19, 2016
 */
public class NodeComparator implements Comparator<NodeEntry> {

	/**
	 * Compares the distance based on the Manhattan formula
	 *
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	@Override
	public int compare(NodeEntry obj1, NodeEntry obj2) {

		if (obj1.getDistance() > obj2.getDistance()) {
			return 1;
		} else if (obj1.getDistance() < obj2.getDistance()) {
			return -1;
		} else {
			return 0;
		}
	}

}
