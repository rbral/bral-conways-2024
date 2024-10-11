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

        /*
        if you call a void method on a mock, it has no effect
        if the method has a return type of:
            boolean return type >> it returns false
            int >> returns 0
            object >> returns null
        so you need to specify what it should return with doReturn


        the only object that should not be a mock is the object
        that you are currently testing
         */
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
