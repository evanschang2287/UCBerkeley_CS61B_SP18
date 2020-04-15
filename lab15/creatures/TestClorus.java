package creatures;

import huglife.*;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

public class TestClorus {
    @Test
    public void testReplicate() {
        Clorus c = new Clorus(2);
        Clorus offSpring = c.replicate();
        assertNotSame(c, offSpring);
    }

    @Test
    public void testChoose() {
        Clorus c = new Clorus(1.2);
        HashMap<Direction, Occupant> surrounded = new HashMap<Direction, Occupant>();
        surrounded.put(Direction.TOP, new Impassible());
        surrounded.put(Direction.BOTTOM, new Impassible());
        surrounded.put(Direction.LEFT, new Impassible());
        surrounded.put(Direction.RIGHT, new Impassible());

        //You can create new empties with new Empty();
        //Despite what the spec says, you cannot test for Cloruses nearby yet.
        //Sorry!

        Action actual = c.chooseAction(surrounded);
        Action expected = new Action(Action.ActionType.STAY);
        assertEquals(expected, actual);

        surrounded.put(Direction.LEFT, new Empty());
        Direction movDir = Direction.LEFT;
        actual = c.chooseAction(surrounded);
        expected = new Action(Action.ActionType.REPLICATE, movDir);
        assertEquals(expected, actual);

        surrounded.put(Direction.TOP, new Plip(1.1));
        movDir = Direction.TOP;
        actual = c.chooseAction(surrounded);
        expected = new Action(Action.ActionType.ATTACK, movDir);
        assertEquals(expected, actual);

        surrounded.put(Direction.RIGHT, new Empty());
        movDir = Direction.RIGHT;
        actual = c.chooseAction(surrounded);
        expected = new Action(Action.ActionType.MOVE, movDir);
        assertEquals(expected, actual);
    }

    public static void main(String[] args) {
        System.exit(jh61b.junit.textui.runClasses(TestPlip.class));
    }
}
