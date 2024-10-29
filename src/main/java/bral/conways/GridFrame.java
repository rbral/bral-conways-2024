package bral.conways;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class GridFrame extends JFrame
{
    private Grid grid;
    private GridComponent gridComponent;
    private GridController controller;
    private Timer timer;
    private JButton playPauseButton;
    private JButton pasteButton;

    public GridFrame()
    {
        setSize(600, 600);
        setTitle("Conway's Game of Life");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // initialize model, view, and controller
        grid = new Grid(100, 100);
        gridComponent = new GridComponent(grid);
        controller = new GridController(grid, gridComponent);

        // set up layout
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        setContentPane(main); // tells the JFrame to use this JPanel

        JPanel south = new JPanel();
        main.add(south, BorderLayout.SOUTH);

        playPauseButton = new JButton("Play");
        south.add(playPauseButton);
        playPauseButton.addActionListener(e -> playPause());

        pasteButton = new JButton("Paste");
        south.add(pasteButton);
        pasteButton.addActionListener(e -> paste());

        main.add(gridComponent, BorderLayout.CENTER);

        setVisible(true);



        //gridComponent.addMouseListener(); // TODO need to finish this

    }



    private void playPause()
    {
        if (timer == null || !timer.isRunning())
        {
            // game is not running, so then start playing:
            timer = new Timer(1000, e -> {
                grid.nextGen();
                gridComponent.repaint();
            });
            timer.start();

            // set button text to Pause:
            playPauseButton.setText("Pause");
        } else {
            timer.stop();

            // set button text to Play:
            playPauseButton.setText("Play");
        }

    }

    private void paste()
    {
        try {
            String clipboardContents =
                    (String) Toolkit.getDefaultToolkit().getSystemClipboard()
                                    .getData(DataFlavor.stringFlavor);
            controller.paste(clipboardContents);
        } catch (UnsupportedFlavorException | IOException e)
        {
            e.printStackTrace();
        }
    }


}
