package com.juricicjuraj.ai.fasttime;

import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;

import static java.time.temporal.ChronoUnit.MINUTES;

/**
 * Minimal, fast implementation of Duration. Uses minute as base unit.
 */
final public class Duration {
    // most used instances
    public static final Duration ZERO = new Duration(0);
    public static final Duration TEN_MINUTES = new Duration(10);
    public static final Duration HALF_HOUR = new Duration(30);
    public static final Duration ONE_HOUR = new Duration(60);

    private long minutes;

    private Duration(long minutes) {
        this.minutes = minutes;
    }

    @Deprecated
    public long getMinutes() {
        return minutes;
    }

    public long toMinutes() {
        return minutes;
    }

    public boolean isNegative() {
        return minutes < 0;
    }

    public Duration multipliedBy(long multiplicand) {
        return new Duration(minutes * multiplicand);
    }

    public double dividedBy(Duration divisor) {
        return this.minutes / (double) divisor.minutes;
    }

    /**
     * Divides two duration, rounding the result.
     *
     * @param divisor
     * @param roundUp if true, will round up; if false, will round down.
     * @return
     */
    public long dividedBy(Duration divisor, boolean roundUp) {
        if (roundUp) {
            return (this.minutes + divisor.minutes - 1) / divisor.minutes;
        } else {
            return this.minutes / divisor.minutes;
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Temporal> T addSelfToTemporal(T temporal) {
        return (T) temporal.plus(minutes, ChronoUnit.MINUTES);
    }

    public static Duration ofMinutes(long minutes) {
        switch ((int) minutes) {
            case 0:
                return ZERO;
            case 10:
                return TEN_MINUTES;
            case 30:
                return HALF_HOUR;
            case 60:
                return ONE_HOUR;
        }

        return new Duration(minutes);
    }

    public static Duration ofHours(long hours) {
        return new Duration(hours * 60);
    }

    public static Duration between(Temporal startInclusive, Temporal endExclusive) {
        return new Duration(startInclusive.until(endExclusive, MINUTES));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Duration duration = (Duration) o;

        return minutes == duration.minutes;
    }

    @Override
    public int hashCode() {
        return (int) (minutes ^ (minutes >>> 32));
    }
}
