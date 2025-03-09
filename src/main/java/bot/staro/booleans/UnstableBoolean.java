package bot.staro.booleans;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * A boolean object that becomes more unstable with time.
 * @author St4ro.
 */
public final class UnstableBoolean {
    private static final long NANOS_PER_MS = TimeUnit.MILLISECONDS.toNanos(1L);
    private static final long START_DECAY_TIME_MS = 1000;
    private final Random random = new Random();
    private boolean value;
    private volatile long lastUpdateTimestamp;

    public UnstableBoolean(boolean value) {
        this.value = value;
        resetTimestamp();
    }

    public UnstableBoolean() {
        this(false);
    }

    public void set(boolean value) {
        this.value = value;
        resetTimestamp();
    }

    public boolean get() {
        long passedTime = getPassedTime();
        double randomProbability = calculateRandomnessProbability(passedTime);
        if (random.nextDouble() < randomProbability) {
            return random.nextBoolean();
        }

        return value;
    }

    private double calculateRandomnessProbability(long passedTimeMs) {
        long fullRandomnessTimeMs = Long.MAX_VALUE;
        if (passedTimeMs < START_DECAY_TIME_MS) {
            return 0.0;
        } else if (passedTimeMs == fullRandomnessTimeMs) {
            return 1.0;
        }

        return (double)(passedTimeMs - START_DECAY_TIME_MS) / (fullRandomnessTimeMs - START_DECAY_TIME_MS);
    }

    private void resetTimestamp() {
        this.lastUpdateTimestamp = getSystemTimeMillis();
    }

    private long getPassedTime() {
        return getSystemTimeMillis() - lastUpdateTimestamp;
    }

    private static long getSystemTimeMillis() {
        return System.nanoTime() / NANOS_PER_MS;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

}
