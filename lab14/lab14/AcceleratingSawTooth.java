package lab14;

import lab14lib.Generator;

public class AcceleratingSawTooth implements Generator {
    private int period;
    private int state;
    private final double factor;
    private int cnt;

    public AcceleratingSawTooth(int period, double factor) {
        this.period = period;
        this.factor = factor;
        state = 0;
        cnt = 0;
    }

    public double next() {
        if (cnt == period) {
            period *= factor;
            cnt = 0;
        }
        cnt = cnt + 1;
        state = (state + 1) % period;
        return normalize(state);
    }

    private double normalize(int state) {
        int half = period / 2;
        return (double) (state - half) / half;
    }
}
