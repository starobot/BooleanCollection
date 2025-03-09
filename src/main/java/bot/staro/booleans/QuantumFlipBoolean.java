package bot.staro.booleans;

/**
 * A boolean object that changes state every time you check it.
 *
 * @author St4ro.
 */
public class QuantumFlipBoolean {
    private boolean value;

    /**
     * Creates a default instance of QuantumFlipBoolean with the default value = false.
     */
    public QuantumFlipBoolean() {
        this(false);
    }

    /**
     * Creates an instance of QuantumFlipBoolean.
     * @param value the initial value.
     */
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

}
