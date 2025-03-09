package bot.staro.booleans;

/**
 * An immutable boolean wrapper.
 * Once created, the value of this boolean cannot be changed, making it
 * a great tool for declarative programming lovers.
 *
 * @author St4ro.
 */
public final class ImmutableBoolean {
    private final boolean value;

    /**
     * Creates a new ImmutableBoolean with the default value (false).
     */
    public ImmutableBoolean() {
        this(false);
    }

    /**
     * Creates a new ImmutableBoolean with the specified value.
     *
     * @param value the value
     */
    public ImmutableBoolean(boolean value) {
        this.value = value;
    }

    /**
     * Gets the value of this ImmutableBoolean.
     *
     * @return the value
     */
    public boolean getValue() {
        return this.value;
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

        ImmutableBoolean other = (ImmutableBoolean) obj;
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return value ? 1231 : 1237;
    }

}
