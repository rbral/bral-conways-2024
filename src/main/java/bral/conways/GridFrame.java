package bral.conways;

import javax.swing.*;
import java.awt.*;

public class GridFrame extends JFrame
{
    private final Grid grid = new Grid(300, 400);
    private Timer timer;
    private GridComponent gridComponent;
    private JButton playPauseButton;

    public GridFrame()
    {
        setSize(800, 400);
        setTitle("Conway's Game of Life");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        // tells the JFrame to use this JPanel
        setContentPane(main);

        JPanel south = new JPanel();
        // put the panel I'm calling south in the SOUTH part of the screen:
        main.add(south, BorderLayout.SOUTH);

        playPauseButton = new JButton("Play");
        south.add(playPauseButton);

        playPauseButton.addActionListener(e -> playPause());

        // put grid in center:
        gridComponent = new GridComponent(grid);
        main.add(gridComponent, BorderLayout.CENTER);
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

}
