package nu.danielsundberg.montyproblem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MontyHallToolTest {

    private static final String TRIES = "10";
    private static final String BOXES = "3";
    private static final String REMOVES = "1";
    private static final String WRONG_FORMAT = "ASDQWE123";
    private static final String TO_LARGE_REMOVES = "2";

    @Test
    public void verifieraCommandLineFunction() throws Exception {
        MontyHallTool.main(new String[]{TRIES,BOXES,REMOVES});
    }

    @Test(expected = NumberFormatException.class)
    public void kontrolleraFelaktigtNummerIArgumenten() throws Exception {
        MontyHallTool.main(new String[]{WRONG_FORMAT,BOXES,REMOVES});
    }

    @Test(expected = IllegalArgumentException.class)
    public void kontrolleraOgiltigtArgumentIRemoves() throws Exception {
        MontyHallTool.main(new String[]{TRIES,BOXES,TO_LARGE_REMOVES});
    }

}
