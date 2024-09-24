package bral.conways;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GridTest {

    @Test
    public void string()
    {
        // given
        Grid grid = new Grid(3, 3);

        // when
        String actual = grid.toString();

        // then
        assertEquals("000\n000\n000\n", actual);
    }

    @Test
    public void load()
    {
        // given
        Grid grid = new Grid(3, 3);

        // when
        grid.load("000\n111\n000");

        // then
        assertEquals("000\n111\n000\n", grid.toString());
    }

    @Test
    public void nextGen()
    {
        // given
        Grid grid = new Grid(3, 3);

        // when
        grid.load("000\n111\n000");
        grid.nextGen();

        // then
        assertEquals("010\n010\n010\n", grid.toString());
    }

    @Test
    public void loadRleFile() throws IOException, URISyntaxException {
        // given
        Grid grid = new Grid(3, 3);

        // when
        //grid.loadFileRLE("#C This is a glider.\nx = 3, y = 3\nbo$2bo$3o!");

        // Access the file using the class loader
        ClassLoader classLoader = getClass().getClassLoader();
        Path path = Paths.get(classLoader.getResource("glider.rle").toURI());

        String rleData = new String(Files.readAllBytes(path));
        grid.loadRleFile(rleData);

        // then
        assertEquals("010\n001\n111\n", grid.toString());
    }


}
