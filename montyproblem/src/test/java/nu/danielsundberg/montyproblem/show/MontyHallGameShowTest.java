package nu.danielsundberg.montyproblem.show;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MontyHallGameShowTest {

    private MontyHallGameShow montyHallGameShow;

    private static final int ANNAT_ANTAL = 10;

    @Before
    public void setup() throws Exception {
        montyHallGameShow = new MontyHallGameShow();
    }

    @Test
    public void verifieraDefaultAntalBoxes() throws Exception {
        assertThat(montyHallGameShow.getBoxes().size(),
                is(MontyHallGameShow.DEFAULT_ANTAL_LADOR));
    }

    @Test
    public void verifieraAngivetAntalBoxes() throws Exception {
        montyHallGameShow = new MontyHallGameShow(ANNAT_ANTAL);
        assertThat(montyHallGameShow.getBoxes().size(),
                is(ANNAT_ANTAL));
    }

    @Test
    public void verifieraBorttagAvSlumpvisBoxInteTarBortPriset() throws Exception {
        montyHallGameShow = new MontyHallGameShow(ANNAT_ANTAL);
        for(int i = montyHallGameShow.getBoxes().size(); i >= 2; i--) {
            montyHallGameShow.removeRandomBoxesExcept(1, new Box[]{montyHallGameShow.getBoxWithPrize()});
            boolean showHasPrize = false;
            for(Box boxToChek : montyHallGameShow.getBoxes()) {
                if(boxToChek.hasPrize()) {
                    showHasPrize = true;
                    break;
                }
            }
            assertThat(showHasPrize,
                    is(Boolean.TRUE));
        }
    }

    @Test
    public void verifieraGodtyckligtAntalSlumpvisaValAldrigBlirAngivenBox() throws Exception {
        Box aBox = montyHallGameShow.selectRandomBox();
        for(int i = 0; i < 1000; i++) {
            assertThat(montyHallGameShow.selectRandomBoxExcept(aBox).equals(aBox),
                    is(Boolean.FALSE));
        }
    }

}
