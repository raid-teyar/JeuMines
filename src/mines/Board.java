package mines;

import com.google.gson.annotations.SerializedName;
import kotlin.jvm.Transient;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Board extends JPanel {
    private static final long serialVersionUID = 6195235521361212179L;
    private static final int NUM_IMAGES = 13;
    private static final int CELL_SIZE = 15;
    private static final int COVER_FOR_CELL = 10;
    private static final int MARK_FOR_CELL = 10;
    private static final int EMPTY_CELL = 0;
    private static final int MINE_CELL = 9;
    private static final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    private static final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;
    private static final int DRAW_MINE = 9;
    private static final int DRAW_COVER = 10;
    private static final int DRAW_MARK = 11;
    private static final int DRAW_WRONG_MARK = 12;

    private int[] field;
    private boolean inGame;
    private int minesLeft;
    private transient Image[] img;
    private int mines = 40;
    private int rows = 16;
    private int cols = 16;
    private int allCells;
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
            position = (int) (allCells * random.nextDouble());
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


    public void findEmptyCells(int j) {

        int currentCol = j % cols;
        int cell;

        if (currentCol > 0) {
            cell = j - cols - 1;
            if (cell >= 0 && field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    findEmptyCells(cell);
            }

            cell = j - 1;
            if (cell >= 0 && field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    findEmptyCells(cell);
            }

            cell = j + cols - 1;
            if (cell < allCells && field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    findEmptyCells(cell);
            }
        }

        cell = j - cols;
        if (cell >= 0 && field[cell] > MINE_CELL) {
            field[cell] -= COVER_FOR_CELL;
            if (field[cell] == EMPTY_CELL)
                findEmptyCells(cell);
        }

        cell = j + cols;
        if (cell < allCells && field[cell] > MINE_CELL) {
            field[cell] -= COVER_FOR_CELL;
            if (field[cell] == EMPTY_CELL)
                findEmptyCells(cell);
        }

        if (currentCol < (cols - 1)) {
            cell = j - cols + 1;
            if (cell >= 0 && field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    findEmptyCells(cell);
            }

            cell = j + cols + 1;
            if (cell < allCells && field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    findEmptyCells(cell);
            }

            cell = j + 1;
            if (cell < allCells && field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    findEmptyCells(cell);
            }
        }

    }

    public void paint(Graphics g) {

        int cell = 0;
        int uncover = 0;


        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                cell = field[(i * cols) + j];

                if (inGame && cell == MINE_CELL)
                    inGame = false;

                if (!inGame) {
                    if (cell == COVERED_MINE_CELL) {
                        cell = DRAW_MINE;
                    } else if (cell == MARKED_MINE_CELL) {
                        cell = DRAW_MARK;
                    } else if (cell > COVERED_MINE_CELL) {
                        cell = DRAW_WRONG_MARK;
                    } else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                    }


                } else {
                    if (cell > COVERED_MINE_CELL)
                        cell = DRAW_MARK;
                    else if (cell > MINE_CELL) {
                        cell = DRAW_COVER;
                        uncover++;
                    }
                }

                g.drawImage(img[cell], (j * CELL_SIZE),
                        (i * CELL_SIZE), this);
            }
        }


        if (uncover == 0 && inGame) {
            inGame = false;
            statusbar.setText("Game won");
        } else if (!inGame)
            statusbar.setText("Game lost");
    }


    class MinesAdapter extends MouseAdapter {
        public void mousePressed(MouseEvent e) {

            int x = e.getX();
            int y = e.getY();

            int cCol = x / CELL_SIZE;
            int cRow = y / CELL_SIZE;

            boolean rep = false;


            if (!inGame) {
                try {
                    newGame();
                } catch (NoSuchAlgorithmException ex) {
                    System.out.println(ex.getMessage());
                }
                repaint();
            }


            if ((x < cols * CELL_SIZE) && (y < rows * CELL_SIZE)) {

                if (e.getButton() == MouseEvent.BUTTON3) {

                    if (field[(cRow * cols) + cCol] > MINE_CELL) {
                        rep = true;

                        if (field[(cRow * cols) + cCol] <= COVERED_MINE_CELL) {
                            if (minesLeft > 0) {
                                field[(cRow * cols) + cCol] += MARK_FOR_CELL;
                                minesLeft--;
                                statusbar.setText(Integer.toString(minesLeft));
                            } else
                                statusbar.setText("No marks left");
                        } else {

                            field[(cRow * cols) + cCol] -= MARK_FOR_CELL;
                            minesLeft++;
                            statusbar.setText(Integer.toString(minesLeft));
                        }
                    }

                } else {

                    if (field[(cRow * cols) + cCol] > COVERED_MINE_CELL) {
                        return;
                    }

                    if ((field[(cRow * cols) + cCol] > MINE_CELL) &&
                            (field[(cRow * cols) + cCol] < MARKED_MINE_CELL)) {

                        field[(cRow * cols) + cCol] -= COVER_FOR_CELL;
                        rep = true;

                        if (field[(cRow * cols) + cCol] == MINE_CELL)
                            inGame = false;
                        if (field[(cRow * cols) + cCol] == EMPTY_CELL)
                            findEmptyCells((cRow * cols) + cCol);
                    }
                }

                if (rep)
                    repaint();

            }
        }
    }
}