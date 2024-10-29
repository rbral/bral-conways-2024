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
/*

    // from class:
    @Test
    void toggleCell()
    {
        // given
        Grid model = mock();
        GridComponent view = mock();
        GridController controller = new GridController(model, view);
        doReturn(10).when(view).getCellSize();
        doReturn(100).when(model).getWidth();
        doReturn(100).when(model).getHeight();


        // when
        controller.toggleCell(50, 100);

        // then
        verify(model).put(5, 10);
        verify(view).repaint();
    }
*/

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


    // TODO missing toggleCellOff and switch up the methods

    @Test
    public void paste() {
        // given:
        Grid model = mock();
        GridComponent view = mock();
        GridController controller = new GridController(model, view);
        String url = "https://conwaylife.com/patterns/glider.rle";

        // when:
        controller.paste(GLIDER_RLE);

        // then:
        verify(model).loadRleFile(GLIDER_RLE);
        verify(view).repaint();
    }

    @Test
    public void pasteUrl()
    {

    }

    @Test
    public void pasteFile()
    {
        // given:
        Grid model = mock();
        GridComponent view = mock();
        GridController controller = new GridController(model, view);
        String filename = "glider.rle";
        String rle = """
                #C This is a glider.
                x = 3, y = 3
                bo$2bo$3o!
                """.trim().replace("\n", "\r\n");

        // when:
        controller.paste(filename);

        // then:
        verify(model).loadRleFile(rle);
        verify(view).repaint();
    }
}
