package bral.conways;

import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

public class GridControllerTest
{
    private static final String GLIDER_RLE = """
                #C This is a glider.
                x = 3, y = 3
                bo$2bo$3o!
                """;
    @Test
    void toggleCellOn()
    {
        // given
        Grid model = mock();
        GridComponent view = mock();
        GridController controller = new GridController(model, view);
        doReturn(5).when(view).getCellSize();
        doReturn(100).when(view).getWidth();
        doReturn(529).when(view).getHeight(); // that's the height that the screen opens with

        doReturn(false).when(model).isAlive(10, 85);
        doReturn(true).when(model).isInBounds(10, 85);

        // when
        controller.toggleCell(50, 100);

        // then
        verify(model).put(10, 85);
        verify(view).repaint();
    }

    @Test
    void toggleCellOff()
    {
        // given
        Grid model = mock();
        GridComponent view = mock();
        GridController controller = new GridController(model, view);
        doReturn(5).when(view).getCellSize();
        doReturn(100).when(view).getWidth();
        doReturn(529).when(view).getHeight(); // that's the height that the screen opens with

        doReturn(true).when(model).isAlive(10, 85);
        doReturn(true).when(model).isInBounds(10, 85);

        // when
        controller.toggleCell(50, 100);

        // then
        verify(model).remove(10, 85);
        verify(view).repaint();
    }

    @Test
    public void pasteText()
    {
        // given
        Grid model = mock();
        GridComponent view = mock();
        GridController controller = new GridController(model, view);
        String rleData = "#C This is a glider.\n" +
                "x = 3, y = 3\n" +
                "bo$2bo$3o!";

        // when
        controller.paste(rleData);

        // then
        verify(model).loadRleFile(rleData);
        verify(view).repaint();

    }

    @Test
    public void pasteUrl() {
        // given:
        Grid model = mock();
        GridComponent view = mock();
        GridController controller = new GridController(model, view);
        String url = "https://conwaylife.com/patterns/glider.rle";
        String rleData = "#N Glider\r\n" +
                "#O Richard K. Guy\r\n" +
                "#C The smallest, most common, and first discovered spaceship. Diagonal, has period 4 and speed c/4.\r\n" +
                "#C www.conwaylife.com/wiki/index.php?title=Glider\r\n" +
                "x = 3, y = 3, rule = B3/S23\r\n" +
                "bob$2bo$3o!";

        // when:
        controller.paste(url);

        // then:
        verify(model).loadRleFile(rleData);
        verify(view).repaint();
    }

    @Test
    public void pasteFilepath()
    {
        // given:
        Grid model = mock();
        GridComponent view = mock();
        GridController controller = new GridController(model, view);
        String filepath = "C:\\Users\\rbral\\IdeaProjects\\bral-conways-2024\\src\\main\\resources\\glider.rle";
        String rleData = "#C This is a glider.\r\n" +
                "x = 3, y = 3\r\n" +
                "bo$2bo$3o!";

        // when:
        controller.paste(filepath);

        // then:
        verify(model).loadRleFile(rleData);
        verify(view).repaint();
    }
}
