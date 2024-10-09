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

        pasteButton = new JButton("Paste");
        south.add(pasteButton);
        pasteButton.addActionListener(e -> defaultPaste());

        // put grid in center:
        gridComponent = new GridComponent(grid);
        JScrollPane scrollPane = new JScrollPane(gridComponent);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        main.add(scrollPane, BorderLayout.CENTER);

        //main.add(gridComponent, BorderLayout.CENTER);

        //pack(); // resize frame to fit preferred size
        //setLocationRelativeTo(null); // center window on screen
        setVisible(true);
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

    private void defaultPaste()
    {
            String rleData = "#C This is a glider.\nx = 3, y = 3\nbo$2bo$3o!";
            grid.loadRleFile(rleData);
            gridComponent.repaint();
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


            // experimenting...
            /*if (rleData != null) {
                // minimum grid size is 100
                int minGridSize = 100;
                // temp grid to determine size of pattern
                Grid tempGrid = new Grid(1, 1);
                tempGrid.loadRleFile(rleData);

                int patternWidth = tempGrid.getWidth();
                int patternHeight = tempGrid.getHeight();

                int requiredWidth = Math.max(minGridSize, patternWidth);
                int requiredHeight = Math.max(minGridSize, patternHeight);

                grid.setWidth(requiredWidth);
                grid.setHeight(requiredHeight);
                grid.setField(new int[requiredHeight][requiredWidth]);

                // center pattern on the grid
                int shiftX = (requiredWidth - patternWidth) / 2;
                int shiftY = (requiredHeight - patternHeight) / 2;

                for (int y = 0; y < grid.getHeight(); ++y) {
                    for (int x = 0; x < grid.getWidth(); ++x) {
                        if (grid.isAlive(x, y)) {
                            grid.put(x + shiftX, y + shiftY);
                        }
                    }
                }
                gridComponent.repaint();*/

                // update the grid component:
                /*
                please note: comments will be deleted after. they are only here
                to show the effort I invested with multiple attempts

               gridComponent = new GridComponent(grid);
                getContentPane().removeAll(); // Remove the old component
                getContentPane().add(gridComponent, BorderLayout.CENTER);
                getContentPane().revalidate(); // Refresh the layout
                getContentPane().repaint(); // Repaint the new component

                //gridComponent.repaint();
                tempGrid = new Grid(requiredWidth, requiredHeight);
                gridComponent = new GridComponent(grid);

                Grid newGrid = new Grid(minGridSize, minGridSize);
                grid.loadRleFile(rleData);*/


//            }


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
