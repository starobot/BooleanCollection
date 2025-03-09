package bot.staro.booleans;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * A boolean object holder for static usage.
 */
public final class SingletonBoolean {
    public static final SingletonBoolean INSTANCE = new SingletonBoolean();
    /**
     * An AtomicBoolean object for thread safe usage.
     */
    private final AtomicBoolean atomicBoolean = new AtomicBoolean(false);

    private SingletonBoolean() {
    }

    public void setValue(boolean value) {
        this.atomicBoolean.set(value);
    }

    public boolean getValue() {
        return this.atomicBoolean.get();
    }

}
