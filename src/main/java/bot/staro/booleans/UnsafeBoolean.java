package bot.staro.booleans;

import java.lang.ref.WeakReference;

/**
 * A Boolean object that is stored with a weak reference.
 * So that the Garbage Collector may remove it at any time
 * @author Edward E Stamper
 */
public class UnsafeBoolean {
    // The weak reference object that stores the current value
    private WeakReference<Boolean> value;

    /**
     * @param initialValue the initial boolean value
     */
    public UnsafeBoolean(boolean initialValue) {
        // set the value of the boolean to the initialValue
        setValue(initialValue);
    }

    /**
     * Sets the new value of the boolean
     * @param newValue the new value for the boolean
     */
    public void setValue(boolean newValue) {
        // Initialize the weak reference for storing the boolean
        value = new WeakReference<>(newValue);
    }

    /**
     * @return the current boolean value, may be null
     * @throws NullPointerException if the object is garbage collected
     */
    public boolean getValue() {
        // get the boolean value from the weak reference
        Boolean value = this.value.get();
        // assert the value is not null
        if (value == null) {
            throw new NullPointerException("Unsafe Boolean object has been garbage collected");
        }

        return value;
    }

}
