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
        pasteButton.addActionListener(e -> paste());

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

    private void paste()
    {
        try {
            String clipboardContents = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);

            String rleData = null;

            if (clipboardContents.startsWith("http://") ||
                    clipboardContents.startsWith("https://"))
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
            if (rleData != null) {
                // minimum grid size is 100
                int minGridSize = 100;
                // temp grid to determine size of pattern
                Grid tempGrid = new Grid(1, 1);
                tempGrid.loadRleFile(rleData);

                int requiredWidth = Math.max(minGridSize, tempGrid.getWidth());
                int requiredHeight = Math.max(minGridSize, tempGrid.getHeight());

                // set grid to new size if needed:
                if (requiredWidth > grid.getWidth() || requiredHeight > grid.getHeight())
                {
                    grid = new Grid(requiredWidth, requiredHeight);
                    //gridComponent = new GridComponent(grid);


                    // center pattern on the grid
                    /*int shiftX = (requiredWidth - grid.getWidth()) / 2;
                    int shiftY = (requiredHeight - grid.getHeight()) / 2;
                    for (int y = 0; y < grid.getHeight(); ++y) {
                        for (int x = 0; x < grid.getWidth(); ++x) {
                            if (grid.isAlive(x, y)) {
                                resizedGrid.put(x + shiftX, y + shiftY);
                            }
                        }
                    }
                    gridComponent = new GridComponent(resizedGrid);*/
                }


                // Center the pattern from tempGrid on the resized (or original) grid
                int shiftX = (grid.getWidth() - tempGrid.getWidth()) / 2;
                int shiftY = (grid.getHeight() - tempGrid.getHeight()) / 2;
                for (int y = 0; y < tempGrid.getHeight(); ++y) {
                    for (int x = 0; x < tempGrid.getWidth(); ++x) {
                        if (tempGrid.isAlive(x, y)) {
                            grid.put(x + shiftX, y + shiftY);
                        }
                    }
                }

                // update the grid component:
                gridComponent = new GridComponent(grid);
                getContentPane().removeAll(); // Remove the old component
                getContentPane().add(gridComponent, BorderLayout.CENTER);
                getContentPane().revalidate(); // Refresh the layout
                getContentPane().repaint(); // Repaint the new component

                //gridComponent.repaint();

                /*tempGrid = new Grid(requiredWidth, requiredHeight);
                gridComponent = new GridComponent(grid);*/

                /*Grid newGrid = new Grid(minGridSize, minGridSize);
                grid.loadRleFile(rleData);*/


            }
        } catch (UnsupportedFlavorException e)
        {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Clipboard does not contain text data.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to load RLE data.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "An unexpected error occurred: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }


}
