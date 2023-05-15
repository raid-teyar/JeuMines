package mines;

import java.awt.BorderLayout;
import java.security.NoSuchAlgorithmException;

import javax.swing.*;

// Source: http://zetcode.com/tutorials/javagamestutorial/minesweeper/

public class Mines extends JFrame {
    private static final long serialVersionUID = 4772165125287256837L;

    private static final int WIDTH = 260;
    private static final int HEIGHT = 300;

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