package bral.conways;

public class Grid
{
    private int[][] field; // [height] [width]
    private int height;
    private int width;

    public Grid(int width, int height)
    {
        field = new int[height][width];
        this.width = width;
        this.height = height;
    }

    public void nextGen()
    {
        // for storing new grid generation:
        int[][] newField = new int[height][width];
        // using 3 by 3 field:
        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                int liveNeighbors = countLiveNeighbors(x, y);

                // if curr cell is alive:
                if (field[y][x] == 1)
                {
                    if (liveNeighbors < 2 || liveNeighbors > 3)
                    {
                        newField[y][x] = 0; // cell dies
                    } else
                    {
                        newField[y][x] = 1;
                    }
                } else if (liveNeighbors == 3) // if curr cell is dead
                {
                    newField[y][x] = 1; // becomes alive
                }

            }
        }

        // update field to nect gen:
        field = newField;
    }

    public int countLiveNeighbors(int x, int y)
    {
        int liveNeighbors = 0;
        for (int h = -1; h <= 1; h++) // horizontal check neighbors (negative means left of curr cell)
        {
            for (int v = -1; v <= 1; v++) // vertical check neighbors
            {
                if (h == 0 && v == 0)
                {
                    continue; // skip the cell itself
                }
                if (isInBounds(x + h, y + v) && field[y + v][x + h] == 1)
                {
                    liveNeighbors++;
                }
            }
        }
        return liveNeighbors;
    }

    private boolean isInBounds(int x, int y)
    {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    /*
    please note: comments are left only to show the efforts
    I invested in previous attempts,
    but they will be deleted.

    public void move(int x1, int y1, int x2, int y2)
    {
        field[y1][x1] = 0;
        field[y2][x2] = 1;
    }
    */

    public String toString()
    {
        StringBuilder builder = new StringBuilder();

        for (int y = 0; y < height; y++)
        {
            for (int x = 0; x < width; x++)
            {
                builder.append(field[y][x]);
            }
            builder.append("\n");
        }

        return builder.toString();
    }

    public void load(String gridString)
    {
        String[] substrings = gridString.split("\n");
        int positionY = 0;
        for (int i = 0; i < substrings.length; i++) {
            int positionX = 0;
            // for each of the "###" sets:
            String currString = substrings[i];
            for (int j = 0; j < currString.length(); j++) {
                char currChar = currString.charAt(j);
                if (currChar == '1') {
                    field[positionY][positionX] = 1;
                } else {
                    field[positionY][positionX] = 0;
                }
                positionX++;
            }
            positionY++;
        }
    }



}
