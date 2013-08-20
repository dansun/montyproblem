package nu.danielsundberg.montyproblem.show;

import nu.danielsundberg.montyproblem.show.box.WinningBoxImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class WinningBoxImplTest {

    private WinningBoxImpl winningBox;

    @Before
    public void setup() throws Exception {
        winningBox = new WinningBoxImpl();
    }

    @Test
    public void checkThatWinningBoxReturnsPrize() throws Exception {
        assertThat(winningBox.hasPrize(), is(Boolean.TRUE));
    }

}
