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

    // getters:
    public int[][] getField() {
        return field;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }


    public void put(int x, int y)
    {
        field[y][x] = 1;
    }

    public void remove(int x, int y)
    {
        field[y][x] = 0;
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

        // update field to next gen:
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

    public boolean isInBounds(int x, int y)
    {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public boolean isAlive(int x, int y)
    {
        return field[y][x] == 1;
    }

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


    public void loadRleFile(String rle)
    {
        // another way to read file:
        //String wholeFile = IOUtils.toString(new FileReader(filepath));

        String[] lines = rle.split("\n");
        // for keeping track of where to place cells on grid:
        int positionX = 0;
        int positionY = 0;

        for (String line : lines)
        {
            if (line.startsWith("#C") || line.startsWith("#c"))
            {
                continue; // skip over comments
            }
            if (line.startsWith("x"))
            {
                // set the dimentions
                String[] dimentions = line.split(",");
                // getting the number after the = sign:
                int width = Integer.parseInt(dimentions[0].split("=")[1].trim());
                int height = Integer.parseInt(dimentions[1].split("=")[1].trim());
                field = new int[height][width];
                this.width = width;
                this.height = height;
            } else
            {
                int count = 1; // default count

                char[] charArray = line.toCharArray();
                for (int ix = 0; ix < charArray.length; ix++)
                {
                    if (Character.isDigit(charArray[ix]))
                    {
                        // check for multi digit numbers:
                        StringBuilder num = new StringBuilder();
                        int tempIx = ix;

                        while (tempIx < charArray.length && Character.isDigit(charArray[tempIx]))
                        {
                            num.append(charArray[tempIx]);
                            ++tempIx;
                        }
                        // now we have a complete number so update count:
                        count = Integer.parseInt(num.toString());

                        // update ix to continue after the multi - digit number:
                        ix = tempIx - 1; // -1 because main loop will do ix++
                    }
                    if (charArray[ix] == 'b')
                    {
                        // dead cells: move positionX to the right
                        positionX += count;
                        count = 1;
                    } else if (charArray[ix] == 'o')
                    {
                        // alive cells: put count live cells
                        for (int jx = 0; jx < count; jx++)
                        {
                            put(positionX++, positionY);
                        }
                        count = 1;
                    } else if (charArray[ix] == '$')
                    {
                        // new line so reset:
                        positionX = 0;
                        ++positionY;
                        count = 1;
                    } else if (charArray[ix] == '!')
                    {
                        return; // end of RLE file
                    }
                }
            }
        }
    }

}
