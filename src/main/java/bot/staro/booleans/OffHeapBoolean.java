package bot.staro.booleans;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 * A Boolean object that is stored in off-heap memory
 * @author Edward E Stamper
 */
public class OffHeapBoolean {
    // The unsafe instance for reading/writing off-heap
    private static final Unsafe UNSAFE = getUnsafe();

    // the address of the off-heap boolean
    private final long address;

    /**
     * @param initialValue the initial boolean value
     */
    public OffHeapBoolean(boolean initialValue) {
        // Allocate 1 byte of memory for the boolean
        address = UNSAFE.allocateMemory(1);
        // set the value of the boolean to the initialValue
        setValue(initialValue);
    }

    /**
     * Sets the new value of the boolean
     * @param value
     */
    public void setValue(boolean value) {
        // write the byte representing true/false to the off heap memory
        UNSAFE.putByte(address, (byte) (value ? 1 : 0));
    }

    /**
     * @return the current boolean value
     */
    public boolean getValue() {
        // read the off heap memory
        // if it is 1, return true
        // otherwise, return false
        return UNSAFE.getByte(address) == 1;
    }

    /**
     * @return the current instance of "theUnsafe"
     */
    private static Unsafe getUnsafe() {
        try {
            Field theUnsafeField = Unsafe.class.getDeclaredField("theUnsafe");
            // set the unsafe field as accessible
            theUnsafeField.setAccessible(true);
            // read & return the value of the unsafe field
            return (Unsafe) theUnsafeField.get(null);
        } catch (Exception e) {
            throw new RuntimeException("Unable to get Unsafe", e);
        }
    }

}