package bot.staro.booleans;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * A boolean object that attempts to patch the memory leak in {@link MemoryLeakBoolean} by using WeakReference.
 *
 * @author St4ro.
 */

//TODO: fix other memory leak.
public class OptimizedMemoryLeakBoolean {
    private final List<WeakReference<Boolean>> state = new ArrayList<>();

    /**
     * @param initialValue the initial boolean value
     */
    public OptimizedMemoryLeakBoolean(boolean initialValue) {
        setValue(initialValue);
    }

    /**
     * Sets the new value of the boolean
     */
    public void setValue(boolean value) {
        state.add(new WeakReference<>(value));
    }

    /**
     * @return the current boolean value
     */
    public boolean getValue() {
        int last = state.size() - 1;
        return Boolean.TRUE.equals(state.get(last).get());
    }

}
