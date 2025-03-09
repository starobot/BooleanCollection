package bot.staro.booleans;

import java.util.HashMap;
import java.util.Map;

/**
 * A Boolean object which keeps a different value for each thread
 * @author Edward E Stamper
 */
public class ThreadLocalBoolean {
    // The per-thread storage
    private final Map<Thread, Boolean> value = new HashMap<>();

    /**
     * @param initialValue the initial boolean value, only written to the current thread
     */
    public ThreadLocalBoolean(boolean initialValue) {
        // set the value of the boolean to the initialValue
        setValue(initialValue);
    }

    /**
     * Initializes A ThreadLocalBoolean with the default current thread value of false
     */
    public ThreadLocalBoolean() {
        this(false);
    }

    /**
     * Sets the new value of the boolean for this thread
     * @param newValue the new value
     */
    public void setValue(boolean newValue) {
        // Get the current thread
        Thread currentThread = Thread.currentThread();
        // Set the value for this thread to newValue
        value.put(currentThread, newValue);
    }

    /**
     * @return the current boolean value on the current thread
     */
    public boolean getValue() {
        // get the boolean value for the current thread
        // with a default value of false
        return this.value.computeIfAbsent(
                Thread.currentThread(), (t) -> false);
    }
}
