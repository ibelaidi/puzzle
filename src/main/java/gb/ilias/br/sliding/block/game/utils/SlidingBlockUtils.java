/**
 *
 */
package gb.ilias.br.sliding.block.game.utils;

import gb.ilias.br.sliding.block.game.algos.internal.Board;

import java.util.BitSet;
import java.util.List;
import java.util.stream.IntStream;

/**
 * Utility class
 *
 * @author ilias
 * @since Oct 20, 2016
 */
public class SlidingBlockUtils {

	/**
	 * From the given Long value, this converts it into a BitSet
	 *
	 * @param value
	 *            long value
	 * @return BitSet
	 */
	public static BitSet convertToBitSet(long value) {
		return IntStream.range(0, Long.SIZE - 1).filter(i -> 0 != (value & 1L << i))
				.collect(BitSet::new, BitSet::set, BitSet::or);
	}

	/**
	 * From the given BitSet, this returns a long representation
	 *
	 * @param bitSet
	 *            BitSet to convert from
	 * @return Long value
	 */
	public static long fromBitSet(BitSet bitSet) {
		return bitSet.stream().mapToLong(i -> 1L << i).reduce(0, (a, b) -> a
		 | b);
	}

	/**
	 * Checks whether the BitSet representation of board's hashcode is already
	 * in the bitsets list.
	 *
	 * @param bitsets
	 *            List of BitSet
	 * @return <tt>True</tt> if the board is present in the bitsets,
	 *         <tt>False</tt> otherwise.
	 */
	public static boolean containsBitSet(List<BitSet> bitsets, Board board) {
		final int hashcode = board.hashCode();
		final BitSet toBitSet = convertToBitSet(hashcode);
		return bitsets.stream().filter(b -> compare(b, toBitSet) == 0).count() != 0;
	}

	/**
	 * Compares two given BitSet
	 *
	 * @param lhs
	 * @param rhs
	 * @return
	 */
	private static int compare(BitSet lhs, BitSet rhs) {
		if (lhs.equals(rhs)) {
			return 0;
		}
		final BitSet xor = (BitSet) lhs.clone();
		xor.xor(rhs);
		final int firstDifferent = xor.length() - 1;
		if (firstDifferent == -1) {
			return 0;
		}
		return rhs.get(firstDifferent) ? 1 : -1;
	}
}
