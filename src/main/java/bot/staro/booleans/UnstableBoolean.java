package bot.staro.booleans;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A boolean object that becomes more unstable with time.
 * The probability of returning a random value increases with time since the last update,
 * simulating a decay process where the value becomes less reliable over time.
 *
 * @author St4ro.
 */
public final class UnstableBoolean {
    private static final long NANOS_PER_MS = TimeUnit.MILLISECONDS.toNanos(1L);
    private static final long START_DECAY_TIME_MS = 1000;
    private final Random random = new Random();
    private boolean value;
    private volatile long lastUpdateTimestamp;

    /**
     * Creates a new UnstableBoolean with the default value (false).
     */
    public UnstableBoolean() {
        this(false);
    }

    /**
     * Creates a new UnstableBoolean with the specified value.
     *
     * @param value the initial value
     */
    public UnstableBoolean(boolean value) {
        this.value = value;
        resetTimestamp();
    }

    /**
     * Sets the value of this UnstableBoolean and resets the decay timer.
     *
     * @param value the new value
     */
    public void set(boolean value) {
        this.value = value;
        resetTimestamp();
    }

    /**
     * Gets the value of this UnstableBoolean.
     * As time passes since the last update, the probability of returning a random value increases.
     *
     * @return the value, which may be random depending on elapsed time
     */
    public boolean get() {
        long passedTime = getPassedTime();
        double randomProbability = calculateRandomnessProbability(passedTime);
        if (random.nextDouble() < randomProbability) {
            return random.nextBoolean();
        }

        return value;
    }

    /**
     * Calculates the probability of returning a random value based on elapsed time.
     *
     * @param passedTimeMs the elapsed time in milliseconds
     * @return the probability of returning a random value, between 0.0 and 1.0
     */
    private double calculateRandomnessProbability(long passedTimeMs) {
        long fullRandomnessTimeMs = Long.MAX_VALUE;
        if (passedTimeMs < START_DECAY_TIME_MS) {
            return 0.0;
        } else if (passedTimeMs == fullRandomnessTimeMs) {
            return 1.0;
        }

        return (double)(passedTimeMs - START_DECAY_TIME_MS) / (fullRandomnessTimeMs - START_DECAY_TIME_MS);
    }

    /**
     * Resets the timestamp to the current system time.
     */
    private void resetTimestamp() {
        this.lastUpdateTimestamp = getSystemTimeMillis();
    }

    /**
     * Gets the time passed since the last update in milliseconds.
     *
     * @return the elapsed time in milliseconds
     */
    private long getPassedTime() {
        return getSystemTimeMillis() - lastUpdateTimestamp;
    }

    /**
     * Gets the current system time in milliseconds.
     *
     * @return the current system time in milliseconds
     */
    private static long getSystemTimeMillis() {
        return System.nanoTime() / NANOS_PER_MS;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        UnstableBoolean other = (UnstableBoolean) obj;
        return value == other.value && lastUpdateTimestamp == other.lastUpdateTimestamp;
    }

    @Override
    public int hashCode() {
        int result = value ? 1231 : 1237;
        result = 31 * result + Long.hashCode(lastUpdateTimestamp);
        return result;
    }

}
