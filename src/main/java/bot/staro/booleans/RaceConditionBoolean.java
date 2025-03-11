package bot.staro.booleans;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

/**
 * A boolean object that executes async to intentionally
 * cause race conditions & sync issues
 *
 * @author St4ro.
 */
public final class RaceConditionBoolean {
    private boolean value;

    public RaceConditionBoolean() {
        this(false);
    }

    public RaceConditionBoolean(boolean value) {
        setValue(value);
    }

    /**
     * Sets the value async using CompletableFuture and a virtual thread per task executor.
     * @param value is a boolean duh.
     */
    public void setValue(boolean value) {
        CompletableFuture.runAsync(() -> this.value = value, Executors.newVirtualThreadPerTaskExecutor());
    }

    /**
     * Get the current value on thread.
     * @return value.
     */
    public boolean getValue() {
        return this.value;
    }

}
