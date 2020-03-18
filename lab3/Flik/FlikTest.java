import static org.junit.Assert.*;
import org.junit.Test;

public class FlikTest {
    @Test
    public void testIsSameNumber() {
        Integer a = 0;
        Integer b = 0;
        Integer c = 3;

        assertTrue(Flik.isSameNumber(a, b));
        assertFalse(Flik.isSameNumber(b, c));
    }
}
