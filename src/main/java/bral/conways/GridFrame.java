package bral.conways;

import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GridFrame extends JFrame
{
    private Grid grid = new Grid(100, 100);
    private Timer timer;
    private GridComponent gridComponent;
    private JButton playPauseButton;
    private JButton pasteButton;
    private GridController controller;

    public GridFrame()
    {
        setSize(600, 600);
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

        pasteButton = new JButton("Paste");
        south.add(pasteButton);
        pasteButton.addActionListener(e -> paste());

        // put grid in center:
        gridComponent = new GridComponent(grid);
        JScrollPane scrollPane = new JScrollPane(gridComponent);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        main.add(scrollPane, BorderLayout.CENTER);

        setVisible(true);

        Grid game = new Grid(300, 300);
        GridComponent gridComponent = new GridComponent(game);
        controller = new GridController(game, gridComponent);

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

            String rleData = null;

            if (clipboardContents.startsWith("http://")
                    || clipboardContents.startsWith("https://"))
            {
                // load file from a URL
                try (InputStream in = new URL(clipboardContents).openStream()) {
                    rleData = IOUtils.toString(in, "UTF-8");
                }
            } else if (new File(clipboardContents).exists())
            {
                // load file from local file
                try (FileInputStream fis = new FileInputStream(new File(clipboardContents))) {
                    rleData = IOUtils.toString(fis, "UTF-8");
                }
            } else
            {
                // treat as raw RLE data
                rleData = clipboardContents;
            }

            // display the patten on grid:
            grid.loadRleFile(rleData);
            gridComponent.repaint();

            controller.paste(clipboardContents); // TODO delete rest of this method


        } catch (UnsupportedFlavorException e)
        {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
