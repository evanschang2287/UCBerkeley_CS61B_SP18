package creatures;

import huglife.*;

import java.awt.*;
import java.util.Map;
import java.util.List;

public class Clorus extends Creature {
    private final int r;
    private final int g;
    private final int b;

    public Clorus(double e) {
        super("clorus");
        r = 34;
        g = 0;
        b = 231;
        energy = e;
    }

    public Clorus() {
        this(1);
    }

    public Color color() {
        return color(r, g, b);
    }

    public void attack(Creature c) {
        energy += c.energy();
    }

    public void move() {
        energy -= 0.03;
    }

    public void stay() {
        energy -= 0.01;
    }

    public Clorus replicate() {
        energy *= 0.5;
        double offSpringEnergy = energy;
        return new Clorus(offSpringEnergy);
    }

    public Action chooseAction(Map<Direction, Occupant> neighbors) {
        List<Direction> empty = getNeighborsOfType(neighbors, "empty");
        List<Direction> enemy = getNeighborsOfType(neighbors, "plip");

        if (empty.size() == 0) {
            return new Action(Action.ActionType.STAY);
        } else {
            if (enemy.size() > 0) {
                Direction movDir = HugLifeUtils.randomEntry(enemy);
                return new Action(Action.ActionType.ATTACK, movDir);
            } else if (energy >= 1.0) {
                Direction movDir = HugLifeUtils.randomEntry(empty);
                return new Action(Action.ActionType.REPLICATE, movDir);
            } else {
                Direction movDir = HugLifeUtils.randomEntry(empty);
                return new Action(Action.ActionType.MOVE, movDir);
            }
        }
    }
}
