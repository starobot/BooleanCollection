package bot.staro.booleans;

/**
 * A boolean object that is uncertain about its own value.
 * @author St4ro.
 */
public class UncertainBoolean {
    private boolean value;

    /**
     * Creates a new UncertainBoolean with the default value (false).
     */
    public UncertainBoolean() {
        this(false);
    }

    /**
     * Creates a new UncertainBoolean with the specified value.
     * @param value the initial value.
     */
    public UncertainBoolean(boolean value) {
        this.value = value;
    }

    /**
     * Sets the value of this UncertainBoolean.
     *
     * @param value the new value
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    /**
     * Return null due to uncertainty.
     *
     * @return null
     */
    public Object getValue() {
        return null;
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

        UncertainBoolean other = (UncertainBoolean) obj;
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return value ? 1231 : 1237;
    }

}
