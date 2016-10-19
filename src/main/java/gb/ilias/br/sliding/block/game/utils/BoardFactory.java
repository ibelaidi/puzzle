package gb.ilias.br.sliding.block.game.utils;

import gb.ilias.br.sliding.block.game.algos.internal.Block;
import gb.ilias.br.sliding.block.game.algos.internal.BlockCoordinate;
import gb.ilias.br.sliding.block.game.algos.internal.Board;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Creates boards from Input files.
 *
 * @author ilias
 * @since Oct 18, 2016
 */
public class BoardFactory {

	private static Logger	log	= LoggerFactory.getLogger(BoardFactory.class);

	/**
	 * From given parameters, this will create a representation of the board
	 * either initial or target states.
	 *
	 * @param inputfile
	 *            Initial/Target file board
	 * @param row
	 *            Number of rows in the board
	 * @param col
	 *            Columns in the board
	 * @throws IOException
	 */
	public static Board createBoard(String inputfile, int row, int col) throws IOException {
		int numrows = 0;
		int numcols = 0;
		Board boardInProgress = null;
		BufferedReader in = null;
		try {
			in = new BufferedReader(new InputStreamReader(new FileInputStream(inputfile)));
			if (row == 0 && col == 0) {
				try {
					final String inputdimension = in.readLine();
					final String[] dimension = inputdimension.split(" ");
					if (dimension.length != 2) {
						log.error("There must be two number for the dimension of the board");
						System.exit(1);
					}
					numrows = Integer.parseInt(dimension[0]);
					numcols = Integer.parseInt(dimension[1]);

				} catch (final IOException e) {
					log.error("Empty file");
					System.exit(1);
				} catch (final NumberFormatException e1) {
					log.error("The dimension of the board is not a number");
					System.exit(1);
				}
			} else {
				numrows = row;
				numcols = col;
			}
			boardInProgress = new Board(numrows, numcols);
			while (true) {
				try {
					final String nextline = in.readLine();
					if (nextline == null) {
						break;
					} else {
						final String[] point = nextline.split(" ");
						if (point.length != 4) {
							log.error("Wrong number of input in {}", nextline);
						}
						final BlockCoordinate upperLeft = new BlockCoordinate(Integer.parseInt(point[1]),
								Integer.parseInt(point[0]));
						final BlockCoordinate lowerRight = new BlockCoordinate(Integer.parseInt(point[3]),
								Integer.parseInt(point[2]));
						final Block a = new Block(upperLeft, lowerRight);
						boardInProgress.addBlock(a);
						System.out.println(boardInProgress);
					}
				} catch (final IOException e) {
					log.error(e.getMessage(), e);
					System.exit(1);
				} catch (final NumberFormatException e1) {
					log.error("The row or column of the block is not a number: {}", e1.getMessage(), e1);
					System.exit(1);
				}
			}
		} catch (final Exception e) {
			log.error("Couldn't access file! {}", e.getMessage(), e);
			System.exit(1);
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return boardInProgress;
	}

}
