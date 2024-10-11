package bral.conways;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class GridComponent extends JComponent
{
    private final Grid grid;
    /*private final int height;
    private final int width;*/

    private final int cellSize;

    public int getCellSize() {
        return cellSize;
    }

    public GridComponent(Grid grid)
    {
        this.grid = grid;
        cellSize = 5;
        int width = grid.getWidth() * cellSize;
        int height = grid.getHeight() * cellSize;
        setPreferredSize(new Dimension(width, height));

        addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int posX = e.getX() / cellSize;
                int posY = (getHeight() - e.getY()) / cellSize; // because y lines start at bottom

                if (grid.isInBounds(posX, posY))
                {
                    if (!grid.isAlive(posX, posY))
                    {
                        grid.put(posX, posY);
                    } else {
                        grid.remove(posX, posY);
                    }
                }
                repaint();
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });


    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        g.setColor(Color.lightGray);
        g.fillRect(0, 0, getWidth(), getHeight());

        // grid lines:
        g.setColor(Color.white);
        for (int x = 0; x < getWidth(); x += cellSize)
        {
            g.drawLine(x, 0, x, getHeight());
        }

        for (int y = getHeight(); y > 0; y -= cellSize)
        {
            g.drawLine(0, y, getWidth(), y);
        }

        // alive cells:
        g.setColor(Color.black);

        for (int y = 0; y < grid.getHeight(); y++)
        {
            for (int x = 0; x < grid.getHeight(); x++)
            {
                if (grid.isAlive(x, y))
                {
                    int positionX = x * cellSize;
                    int positionY = getHeight() - (y + 1) * cellSize;
                    g.fillRect(positionX, positionY, cellSize, cellSize);
                }
            }
        }
    }


}
