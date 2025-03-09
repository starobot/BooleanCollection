package bot.staro.booleans;

/**
 * A boolean wrapper with creepy behavior.
 * This class stores a boolean value but behaves abnormally,
 * as the getValue method always returns false regardless of the actual value.
 * @author St4ro.
 */
public final class CreepyBoolean {
    private boolean value;

    /**
     * Creates a new CreepyBoolean with the default value (false).
     */
    public CreepyBoolean() {
        this(false);
    }

    /**
     * Creates a new CreepyBoolean with the specified value.
     * @param value the initial value.
     */
    public CreepyBoolean(boolean value) {
        this.value = value;
    }

    /**
     * Sets the value of this CreepyBoolean.
     *
     * @param value the new value
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * Gets the value of this CreepyBoolean.
     * Note: This method always returns false regardless of the actual value.
     *
     * @return always false
     */
    public boolean getValue() {
        return false;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CreepyBoolean other = (CreepyBoolean) obj;
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return value ? 1231 : 1237;
    }

}
