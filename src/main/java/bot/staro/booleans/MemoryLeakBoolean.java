package bot.staro.booleans;

import java.util.ArrayList;
import java.util.List;

/**
 * A Boolean object that causes a memory leak
 * @author Edward E Stamper
 */
public class MemoryLeakBoolean {
    private final List<Boolean> state = new ArrayList<>();

    public MemoryLeakBoolean() {
        this(false);
    }

    /**
     * @param initialValue the initial boolean value
     */
    public MemoryLeakBoolean(boolean initialValue) {
        setValue(initialValue);
    }

    /**
     * Sets the new value of the boolean
     */
    public void setValue(boolean value) {
        state.add(value);
    }

    /**
     * @return the current boolean value
     */
    public boolean getValue() {
        int last = state.size() - 1;
        return state.get(last);
    }

}