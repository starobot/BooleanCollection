package bot.staro.booleans;

/**
 * A boolean object that changes state every time you check it.
 *
 * @author St4ro.
 */
public final class QuantumFlipBoolean {
    private boolean value;

    public QuantumFlipBoolean() {
        this(false);
    }

    public QuantumFlipBoolean(boolean value) {
        this.value = value;
    }

    /**
     * Shows the current value and flips it for the next check.
     * @return the initial value.
     */
    public boolean getValue() {
        boolean snapShotValue = this.value;
        this.value = !this.value;
        return snapShotValue;
    }

    /**
     * Sets the value.
     * @param value is a boolean.
     */
    public void setValue(boolean value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        QuantumFlipBoolean other = (QuantumFlipBoolean) obj;
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return value ? 1231 : 1237;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
