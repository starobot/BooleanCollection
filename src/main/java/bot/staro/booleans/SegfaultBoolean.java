package bot.staro.booleans;

import java.util.concurrent.ThreadLocalRandom;

/**
 * A Boolean object that attempts to write to invalid memory, and crashes the JVM
 * @author Edward E Stamper
 */
public class SegfaultBoolean extends OffHeapBoolean {
    public SegfaultBoolean() {
        this(false);
    }

    /**
     * @param initialValue the initial boolean value
     */
    public SegfaultBoolean(boolean initialValue) {
        super(initialValue); // initialize as an OffHeapBoolean
        // set to an invalid address
        this.address = ThreadLocalRandom.current().nextInt();
    }

}
