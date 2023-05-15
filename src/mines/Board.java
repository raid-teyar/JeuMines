package mines;

import com.google.common.annotations.VisibleForTesting;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**

 The Board class represents a Minesweeper game board, which is a rectangular grid of cells containing mines or numbers.
 The class extends JPanel and displays the game board to the user. The game board is randomly generated at the start of a new game,
 and the user can click on cells to reveal them. The user wins the game by revealing all non-mine cells, and loses the game by revealing
 a mine cell. The status bar component displays the number of mines left to be marked by the user.
 */
public class Board extends JPanel {
    /**
     * The version UID for serializing and deserializing this class.
     */
    private static final long serialVersionUID = 6195235521361212179L;

    /**
     * The number of different images used to represent cells.
     */
    private static final int NUM_IMAGES = 13;

    /**
     * The size of each cell, in pixels.
     */
    private static final int CELL_SIZE = 15;

    /**
     * The value used to cover a cell.
     */
    private static final int COVER_FOR_CELL = 10;

    /**
     * The value used to mark a cell.
     */
    private static final int MARK_FOR_CELL = 10;

    /**
     * The value used to represent an empty cell.
     */
    private static final int EMPTY_CELL = 0;

    /**
     * The value used to represent a cell that contains a mine.
     */
    private static final int MINE_CELL = 9;

    /**
     * The value used to represent a covered cell that contains a mine.
     */
    private static final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;

    /**
     * The value used to represent a marked cell that contains a mine.
     */
    private static final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    /**
     * The value used to draw a mine image.
     */
    private static final int DRAW_MINE = 9;

    /**
     * The value used to draw a cover image.
     */
    private static final int DRAW_COVER = 10;

    /**
     * The value used to draw a mark image.
     */
    private static final int DRAW_MARK = 11;

    /**
     * The value used to draw a wrong mark image.
     */
    private static final int DRAW_WRONG_MARK = 12;

    /**
     * The field representing the game board.
     */
    private int[] field;

    /**
     * A boolean indicating whether the game is currently in progress.
     */
    private boolean inGame;

    /**
     * The number of mines remaining on the game board.
     */
    private int minesLeft;

    /**
     * An array of images used to represent cells.
     */
    private transient Image[] img;

    /**
     * The number of mines on the game board.
     */
    private int mines = 40;

    /**
     * The number of rows on the game board.
     */
    private int rows = 16;

    /**
     * The number of columns on the game board.
     */
    private int cols = 16;

    /**
     * The total number of cells on the game board.
     */
    private int allCells;

    /**
     * The status bar component used to display game information.
     */
    private JLabel statusbar;


    public Board(JLabel statusbar) throws NoSuchAlgorithmException {

        this.statusbar = statusbar;

        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {
            img[i] =
                    (new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource((i)
                            + ".gif")))).getImage();
        }

        setDoubleBuffered(true);

        addMouseListener(new MinesAdapter());
        newGame();
    }


    /**
     * Starts a new Minesweeper game with a randomly generated minefield.
     *
     * @throws NoSuchAlgorithmException if a secure random number generator algorithm is not available
     */
    public void newGame() throws NoSuchAlgorithmException {
        Random random = SecureRandom.getInstanceStrong();
        int i = 0;
        int position;
        inGame = true;
        minesLeft = mines;
        allCells = rows * cols;
        field = new int[allCells];
        Arrays.fill(field, COVER_FOR_CELL);
        statusbar.setText(Integer.toString(minesLeft));

        while (i < mines) {
            position = random.nextInt(allCells);
            if (field[position] != COVERED_MINE_CELL) {
                field[position] = COVERED_MINE_CELL;
                i++;
                for (int j : getAdjacentCells(position)) {
                    if (j >= 0 && j < allCells && field[j] != COVERED_MINE_CELL) {
                        field[j] += 1;
                    }
                }
            }
        }
    }

    /**
     * Returns an array of integers representing the positions of all adjacent cells to a given position in the Minesweeper game board.
     *
     * @param position the position for which to retrieve adjacent cells
     * @return an array of integers representing the positions of all adjacent cells
     */
    @VisibleForTesting
    private int[] getAdjacentCells(int position) {
        int currentCol = position % cols;
        int row = position / cols;
        int[] adjacentCells = new int[8];
        int count = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = currentCol - 1; j <= currentCol + 1; j++) {
                if (i >= 0 && i < rows && j >= 0 && j < cols && !(i == row && j == currentCol)) {
                    adjacentCells[count++] = i * cols + j;
                }
            }
        }
        return Arrays.copyOf(adjacentCells, count);
    }

    /**
     * Finds all empty cells adjacent to the cell at index j and calls the checkCell method for each empty cell found.
     *
     * @param j the index of the cell to find empty neighbors for
     */
    public void findEmptyCells(int j) {
        int currentCol = j % cols;

        if (currentCol > 0) {
            checkCell(j - cols - 1);
            checkCell(j - 1);
            checkCell(j + cols - 1);
        }

        checkCell(j - cols);
        checkCell(j + cols);

        if (currentCol < (cols - 1)) {
            checkCell(j - cols + 1);
            checkCell(j + cols + 1);
            checkCell(j + 1);
        }
    }

    /**
     * Uncover the specified cell and check if it contains a mine or is empty.
     *
     * @param cell the cell to check
     */
    private void checkCell(int cell) {
        if (cell >= 0 && cell < allCells && field[cell] > MINE_CELL) {
            field[cell] -= COVER_FOR_CELL;
            if (field[cell] == EMPTY_CELL) {
                findEmptyCells(cell);
            }
        }
    }

    /**
     * Paints the Minesweeper game board using the specified graphics context.
     *
     * @param g the graphics context to use for painting the game board
     */
    @Override
    public void paint(Graphics g) {
        int cell;
        int uncover = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cell = field[(i * cols) + j];
                if (inGame && cell == MINE_CELL) {
                    inGame = false;
                }
                if (!inGame) {
                    cell = getGameOverCell(cell);
                } else {
                    cell = getInGameCell(cell);
                    if (cell == DRAW_COVER) {
                        uncover++;
                    }
                }
                g.drawImage(img[cell], (j * CELL_SIZE), (i * CELL_SIZE), this);
            }
        }

        updateStatusBar(uncover);
    }

    private int getGameOverCell(int cell) {
        if (cell == COVERED_MINE_CELL) {
            return DRAW_MINE;
        } else if (cell == MARKED_MINE_CELL) {
            return DRAW_MARK;
        } else if (cell > COVERED_MINE_CELL) {
            return DRAW_WRONG_MARK;
        } else if (cell > MINE_CELL) {
            return DRAW_COVER;
        }
        return cell;
    }

    /**
     * Returns the corresponding cell value for a given cell after the game is over.
     *
     * @param cell the cell value to be converted
     * @return the corresponding cell value after the game is over
     */
    private int getInGameCell(int cell) {
        if (cell > COVERED_MINE_CELL) {
            return DRAW_MARK;
        } else if (cell > MINE_CELL) {
            return DRAW_COVER;
        }
        return cell;
    }

    /**
     * Updates the game status bar based on the number of tiles uncovered.
     *
     * @param uncover the number of tiles uncovered
     */
    private void updateStatusBar(int uncover) {
        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        } else if (!inGame) {
            statusbar.setText("Game lost");
        }
    }


    class MinesAdapter extends MouseAdapter {

        /**
         * Invoked when the mouse button is pressed on the game board. This method handles left and right clicks on the board and initiates a new game if the game is not already in progress.
         *
         * @param e the MouseEvent that occurred
         */
        @Override
        public void mousePressed(MouseEvent e) {
            int x = e.getX();
            int y = e.getY();

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;

            if (!inGame) {
                try {
                    newGame();
                } catch (NoSuchAlgorithmException ex) {
                    Logger.getLogger(Board.class.getName()).log(Level.SEVERE, null, ex);
                }
                repaint();
                return;
            }

            if ((x >= cols * CELL_SIZE) || (y >= rows * CELL_SIZE)) {
                return;
            }

            int cellIndex = (cRow * cols) + cCol;
            int cellValue = field[cellIndex];

            if (e.getButton() == MouseEvent.BUTTON3) {
                handleRightClick(cellIndex, cellValue);
            } else {
                handleLeftClick(cellIndex, cellValue);
            }
        }

        /**
         * Handles a right-click on a cell in the Minesweeper game.
         *
         * @param cellIndex the index of the cell that was right-clicked
         * @param cellValue the value of the cell that was right-clicked
         */
        private void handleRightClick(int cellIndex, int cellValue) {
            if (cellValue > MINE_CELL) {
                if (cellValue <= COVERED_MINE_CELL) {
                    if (minesLeft > 0) {
                        field[cellIndex] += MARK_FOR_CELL;
                        minesLeft--;
                        statusbar.setText(Integer.toString(minesLeft));
                    } else {
                        statusbar.setText("No marks left");
                    }
                } else {
                    field[cellIndex] -= MARK_FOR_CELL;
                    minesLeft++;
                    statusbar.setText(Integer.toString(minesLeft));
                }
                repaint();
            }
        }

        /**
         * Handles a left-click event on a cell.
         *
         * @param cellIndex the index of the clicked cell
         * @param cellValue the value of the clicked cell
         */
        private void handleLeftClick(int cellIndex, int cellValue) {
            if (cellValue > COVERED_MINE_CELL) {
                return;
            }

            if ((cellValue > MINE_CELL) && (cellValue < MARKED_MINE_CELL)) {
                field[cellIndex] -= COVER_FOR_CELL;
                if (field[cellIndex] == MINE_CELL) {
                    inGame = false;
                }
                if (field[cellIndex] == EMPTY_CELL) {
                    findEmptyCells(cellIndex);
                }
                repaint();
            }
        }
    }
}