package bot.staro.booleans;

/**
 * A boolean object that holds two values due to sudden gamma mutation.
 * This class maintains both a normal value and a mutant value, allowing
 * for dual-state behavior where both values affect the final result.
 *
 * @author St4ro.
 */
public final class MutantBoolean {
    private boolean value;
    private boolean mutantValue;

    /**
     * Creates a new MutantBoolean with default values (false, false).
     */
    public MutantBoolean() {
        this(false);
    }

    /**
     * Creates a new MutantBoolean with the specified value and default mutant value (false).
     *
     * @param value the normal value
     */
    public MutantBoolean(boolean value) {
        this(value, false);
    }

    /**
     * Creates a new MutantBoolean with the specified normal and mutant values.
     *
     * @param value the normal value
     * @param mutantValue the mutant value
     */
    public MutantBoolean(boolean value, boolean mutantValue) {
        this.value = value;
        this.mutantValue = mutantValue;
    }

    /**
     * Sets the mutant value of this MutantBoolean.
     *
     * @param mutantValue the new mutant value
     */
    public void setMutant(boolean mutantValue) {
        this.mutantValue = mutantValue;
    }

    /**
     * Sets the normal value of this MutantBoolean.
     *
     * @param value the new normal value
     */
    public void set(boolean value) {
        this.value = value;
    }

    /**
     * Sets both the normal and mutant values of this MutantBoolean.
     *
     * @param value the new normal value
     * @param mutantValue the new mutant value
     */
    public void set(boolean value, boolean mutantValue) {
        this.value = value;
        this.mutantValue = mutantValue;
    }

    /**
     * Gets the normal value of this MutantBoolean.
     *
     * @return the normal value
     */
    public boolean getValue() {
        return this.value;
    }

    /**
     * Gets the mutant value of this MutantBoolean.
     *
     * @return the mutant value
     */
    public boolean getMutantValue() {
        return this.mutantValue;
    }

    /**
     * Gets the combined value of this MutantBoolean.
     * The combined value is the logical AND of the normal and mutant values.
     *
     * @return the combined value
     */
    public boolean get() {
        return this.value && this.mutantValue;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        MutantBoolean other = (MutantBoolean) obj;
        return value == other.value && mutantValue == other.mutantValue;
    }

    @Override
    public int hashCode() {
        int result = value ? 1231 : 1237;
        result = 31 * result + (mutantValue ? 1231 : 1237);
        return result;
    }

}
