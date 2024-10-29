package bral.conways;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class GridFrame extends JFrame
{
    private Grid grid;
    private GridComponent gridComponent;
    private GridController controller;
    private Timer timer;
    private JButton playPauseButton;
    private JButton playButton;
    private JButton pauseButton;
    private JButton pasteButton;

    public GridFrame()
    {
        setSize(600, 600);
        setTitle("Conway's Game of Life");
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // initialize model:
        grid = new Grid(100, 100);

        // initialize view:
        gridComponent = new GridComponent(grid);
        gridComponent.addMouseListener(new MouseListener()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                controller.toggleCell(e.getX(), e.getY());
            }

            @Override
            public void mousePressed(MouseEvent e)
            {

            }

            @Override
            public void mouseReleased(MouseEvent e)
            {

            }

            @Override
            public void mouseEntered(MouseEvent e)
            {

            }

            @Override
            public void mouseExited(MouseEvent e)
            {

            }
        });

        // initialize controller:
        controller = new GridController(grid, gridComponent);

        // set up layout
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        setContentPane(main); // tells the JFrame to use this JPanel

        JPanel south = new JPanel();
        main.add(south, BorderLayout.SOUTH);

        playButton = new JButton("Play");
        playButton.addActionListener(e -> play());
        south.add(playButton);

        pauseButton = new JButton("Pause");
        pauseButton.setEnabled(false);
        pauseButton.addActionListener(e -> pause());
        south.add(pauseButton);

        pasteButton = new JButton("Paste");
        south.add(pasteButton);
        pasteButton.addActionListener(e -> paste());

        main.add(gridComponent, BorderLayout.CENTER);

        setVisible(true);
    }

    private void play()
    {
        controller.startTimer();
        playButton.setEnabled(false);
        pauseButton.setEnabled(true);
    }

    private void pause()
    {
        controller.stopTimer();
        pauseButton.setEnabled(false);
        playButton.setEnabled(true);
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
