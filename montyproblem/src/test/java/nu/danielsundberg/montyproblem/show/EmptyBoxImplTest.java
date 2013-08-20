package nu.danielsundberg.montyproblem.show;

import nu.danielsundberg.montyproblem.show.box.EmptyBoxImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class EmptyBoxImplTest {

    private EmptyBoxImpl emptyBox;

    @Before
    public void setup() throws Exception {
        emptyBox = new EmptyBoxImpl();
    }

    @Test
    public void checkThatEmptyBoxDoesNotReturnPrize() throws Exception {
        assertThat(emptyBox.hasPrize(), is(Boolean.FALSE));
    }

}
