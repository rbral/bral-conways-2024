package bral.conways;

import org.apache.commons.io.IOUtils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class GridController
{
    Grid model;
    GridComponent view;

    public GridController(Grid model, GridComponent view)
    {
        this.model = model;
        this.view = view;
    }

    public void startTimer()
    {

    }

    public void stopTimer()
    {

    }

    public void paste(String clipboardContents)
    {
        try {
            /*String clipboardContents =
                    (String) Toolkit.getDefaultToolkit().getSystemClipboard()
                            .getData(DataFlavor.stringFlavor);*/

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
            model.loadRleFile(rleData);
            view.repaint();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toggleCell(int screenX, int screenY)
    {
        int posX = screenX / view.getCellSize();
        int posY = (model.getHeight() - screenY) / view.getCellSize(); // because y lines start at bottom

        if (model.isInBounds(posX, posY))
        {
            if (!model.isAlive(posX, posY))
            {
                model.put(posX, posY);
            } else {
                model.remove(posX, posY);
            }
        }
        view.repaint();
    }
}
