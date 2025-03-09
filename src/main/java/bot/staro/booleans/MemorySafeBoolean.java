package bot.staro.booleans;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * A boolean object that has no memory leak issues at all.
 *
 * @author St4ro.
 */
public class MemorySafeBoolean {
    private final Map<Integer, Boolean> cache = new WeakHashMap<>();
    private int lastKey = -1;

    public MemorySafeBoolean() {
        this(false);
    }

    /**
     * @param initialValue the initial boolean value
     */
    public MemorySafeBoolean(boolean initialValue) {
        setValue(initialValue);
    }

    /**
     * Sets the new value of the boolean
     */
    public void setValue(boolean value) {
        lastKey++;
        cache.put(lastKey, value);
    }

    /**
     * @return the current boolean value
     */
    public boolean getValue() {
        if (lastKey < 0 || !cache.containsKey(lastKey)) {
            return false;
        }

        return Boolean.TRUE.equals(cache.get(lastKey));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MemorySafeBoolean other = (MemorySafeBoolean) obj;
        return getValue() == other.getValue();
    }

    @Override
    public int hashCode() {
        return getValue() ? 1231 : 1237;
    }

    @Override
    public String toString() {
        return String.valueOf(getValue());
    }

}
