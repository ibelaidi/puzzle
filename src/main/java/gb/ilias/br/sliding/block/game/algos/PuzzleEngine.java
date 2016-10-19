/*
 * package gb.ilias.br.sliding.block.game.algos;
 * 
 * import gb.ilias.br.sliding.block.game.algos.internal.Block;
 * import gb.ilias.br.sliding.block.game.algos.internal.BlockCoordinate;
 * import gb.ilias.br.sliding.block.game.algos.internal.Board;
 * import gb.ilias.br.sliding.block.game.algos.internal.NodeComparator;
 * import gb.ilias.br.sliding.block.game.algos.internal.NodeEntry;
 * import gb.ilias.br.sliding.block.game.utils.Debug;
 * 
 * import java.util.HashSet;
 * import java.util.Map;
 * import java.util.PriorityQueue;
 * import java.util.Set;
 *//**
 * @author ilias
 * @since Oct 18, 2016
 */
/*
 * public class PuzzleEngine {
 * 
 * private final Board initialBoard;
 * private final Board targetBoard;
 * private Board currentBoard;
 * private final PriorityQueue<NodeEntry> fringe;
 * private final Set<Board> boardSeen;
 * private NodeEntry temp;
 * private boolean debug;
 * private int moveCount;
 * private int boardMove;
 * private int numBoard;
 * private long startTime;
 * private long stopTime;
 *//**
 * @param root
 * @param goal
 */
/*
 * public PuzzleEngine(Board root, Board goal) {
 * this.initialBoard = root;
 * this.targetBoard = goal;
 * this.currentBoard = root;
 * this.boardSeen = new HashSet<Board>();
 * this.boardSeen.add(root);
 * this.fringe = new PriorityQueue<NodeEntry>(1000, new NodeComparator());
 * this.fringe.add(new NodeEntry(root, 0));
 * }
 * 
 * private int calculateManhattan(Board board) {
 * int sumdistance = 0; // calculates distance between blocks in current
 * // board and goal board
 * for (final Block b : this.targetBoard.getBlocks()) {
 * for (final Block i : board.getBlocks()) {
 * if (b.getType().equals(i.getType())) {
 * sumdistance += Math.abs(i.UpperLeft().x - b.UpperLeft().x);
 * sumdistance += Math.abs(i.UpperLeft().y - b.UpperLeft().y);
 * }
 * }
 * }
 * 
 * return sumdistance;
 * }
 * 
 * public void solve() {
 * if (this.debug && (Debug.time || Debug.all)) {
 * this.startTime = System.currentTimeMillis();
 * }
 * if (this.debug && (Debug.numBoard || Debug.all)) {
 * this.numBoard = 1;
 * }
 * if (this.debug && (Debug.printSGF || Debug.all)) {
 * System.out.println("Initial board:" + "\n" + this.initialBoard);
 * System.out.println("Target board:" + "\n" + this.targetBoard);
 * }
 * if (this.debug && (Debug.numM || Debug.all)) {
 * this.moveCount = 0;
 * }
 * while (!this.isSolved()) {
 * if (this.debug && (Debug.possM || Debug.all)) {
 * this.boardMove = 0;
 * }
 * this.findPossibleMoves(); // see below code
 * if (this.fringe.isEmpty()) { // all boards have been seen and no
 * // solution found
 * // System.out.println("No solution found");
 * System.exit(1);
 * }
 * this.temp = this.fringe.remove(); // takes out board from fringe
 * this.currentBoard = this.temp.getMyBoard();
 * if (this.debug && (Debug.time || Debug.all)) {
 * this.stopTime = System.currentTimeMillis();
 * System.out.println("time elapsed: " + (this.stopTime - this.startTime));
 * }
 * if (this.debug && (Debug.man || Debug.all)) {
 * System.out.println("current board's manhattan distance (priority) in queue: "
 * + this.temp.getDistance());
 * }
 * if (this.debug && (Debug.printBd || Debug.all)) {
 * System.out.println("current board:" + "\n" + this.currentBoard);
 * }
 * if (this.debug && (Debug.printB || Debug.all)) {
 * System.out.println("Blocks in current board: " +
 * this.currentBoard.getBlocks());
 * }
 * if (this.debug && (Debug.numM || Debug.all)) {
 * this.moveCount++;
 * }
 * }
 * if (this.debug && (Debug.printSGF || Debug.all)) {
 * System.out.println("final board:" + "\n" + this.currentBoard);
 * }
 * if (this.debug && (Debug.numM || Debug.all)) {
 * System.out.println("move count: " + this.moveCount);
 * }
 * if (this.debug && (Debug.numBoard || Debug.all)) {
 * System.out.println("number of boards added to fringe:" + this.numBoard);
 * }
 * if (this.debug && (Debug.time || Debug.all)) {
 * this.stopTime = System.currentTimeMillis();
 * System.out.println("final time elapsed: " + (this.stopTime -
 * this.startTime));
 * }
 * System.out.print(this.currentBoard.displayMoves()); // print out all
 * // moves taken to
 * // get there
 * 
 * }
 * 
 * private boolean isSolved() {
 * for (final Block i : this.targetBoard.getBlocks()) { // checks to see if
 * // all
 * // blocks in goal board
 * // are in same position
 * // in current board
 * if (!this.currentBoard.getBlocks().contains(i)) {
 * return false;
 * }
 * }
 * return true;
 * }
 * 
 * private void findPossibleMoves() {
 * final Map<BlockCoordinate, Block> currTray = this.currentBoard.getTray(); //
 * our
 * // board
 * // implementation
 * for (int i = 0; i < this.currentBoard.getWidth(); i++) {
 * for (int j = 0; j < this.currentBoard.getHeight(); j++) {
 * if (!currTray.containsKey(new BlockCoordinate(i, j))) { // looks
 * // for
 * // empty
 * // spaces until
 * // it finds one
 * if (this.debug && (Debug.empty || Debug.all)) {
 * System.out.println("empty space: " + new BlockCoordinate(i, j));
 * }
 * if (i - 1 >= 0) { // tries moving blocks from the left right
 * // down and up of that empty space
 * this.tryMove(new BlockCoordinate(i - 1, j), "right");
 * }
 * if (i + 1 < this.currentBoard.getWidth()) {
 * this.tryMove(new BlockCoordinate(i + 1, j), "left");
 * }
 * if (j - 1 >= 0) {
 * this.tryMove(new BlockCoordinate(i, j - 1), "down");
 * }
 * if (j + 1 < this.currentBoard.getHeight()) {
 * this.tryMove(new BlockCoordinate(i, j + 1), "up");
 * }
 * }
 * }
 * }
 * if (this.debug && (Debug.possM || Debug.all)) {
 * System.out.println("number of possible moves this board can make: " +
 * this.boardMove);
 * }
 * if (this.debug && (Debug.mem || Debug.all)) {
 * System.out.println("Free memory: " + Runtime.getRuntime().freeMemory());
 * }
 * 
 * }
 * 
 * private void tryMove(BlockCoordinate p, String direction) {
 * final Map<BlockCoordinate, Block> currTray = this.currentBoard.getTray();
 * if (currTray.containsKey(p)) { // picks a point to moveTo depending on
 * // direction specified
 * final BlockCoordinate upperl = currTray.get(p).UpperLeft();
 * BlockCoordinate moveto = new BlockCoordinate(-1, -1);
 * if (direction.equals("right")) {
 * moveto = new BlockCoordinate(upperl.x + 1, upperl.y);
 * } else if (direction.equals("left")) {
 * moveto = new BlockCoordinate(upperl.x - 1, upperl.y);
 * } else if (direction.equals("up")) {
 * moveto = new BlockCoordinate(upperl.x, upperl.y - 1);
 * } else if (direction.equals("down")) {
 * moveto = new BlockCoordinate(upperl.x, upperl.y + 1);
 * }
 * 
 * try {
 * final Board possibleBoard = this.currentBoard.makeMove(currTray.get(p),
 * moveto); // makes
 * if (this.debug) {
 * try {
 * possibleBoard.sanityCheck();
 * } catch (final IllegalStateException e) {
 * System.out.println(e.getMessage());
 * }
 * }
 * if (this.debug && (Debug.hash || Debug.all)) {
 * System.out.println("new board's hashcode: " + this.currentBoard.hashCode());
 * }
 * if (!this.boardSeen.contains(possibleBoard)) {
 * if (this.debug && (Debug.numBoard || Debug.all)) {
 * this.numBoard++;
 * }
 * if (this.debug && (Debug.showM || Debug.all)) {
 * System.out.println("new block positions in board: " + "\n" + possibleBoard);
 * }
 * final NodeEntry n = new NodeEntry(possibleBoard,
 * this.calculateManhattan(possibleBoard));
 * this.fringe.add(n);
 * this.boardSeen.add(possibleBoard);
 * if (this.debug && (Debug.possM || Debug.all)) {
 * this.boardMove++;
 * }
 * } else {
 * if (this.debug && (Debug.seen || Debug.all)) {
 * System.out
 * .println(
 * "A previously seen board type was reached and therefore not added to the fringe"
 * );
 * }
 * }
 * } catch (final IllegalStateException e) {
 * return;
 * }
 * 
 * }
 * 
 * }
 *//**
 * @param debug
 *            the debug to set
 */
/*
 * public void setDebug(boolean debug) {
 * this.debug = debug;
 * }
 * 
 * }
 */