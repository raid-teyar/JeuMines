package mines;

import java.awt.BorderLayout;
import java.security.NoSuchAlgorithmException;

import javax.swing.*;

// Source: http://zetcode.com/tutorials/javagamestutorial/minesweeper/

/**
 * The Mines class extends JFrame and represents the Minesweeper game window.
 */
public class Mines extends JFrame {
    /**
     * The serial version UID for this class.
     */
    private static final long serialVersionUID = 4772165125287256837L;

    /**
     * The width of the game board.
     */
    private static final int WIDTH = 260;

    /**
     * The height of the game board.
     */
    private static final int HEIGHT = 300;

    /**
     * The status bar that displays messages to the user.
     */
    private JLabel statusbar;

    public Mines() throws NoSuchAlgorithmException {

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Minesweeper");

        statusbar = new JLabel("");
        add(statusbar, BorderLayout.SOUTH);

        add(new Board(statusbar));

        setResizable(false);
        setVisible(true);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        new Mines();
    }
}