package bot.staro.booleans;

import java.util.Random;

/**
 * A boolean object that is both true and false until you observe it.
 * @author St4ro.
 */
public final class SchroedingerBoolean {
    private final Random random = new Random();
    private Boolean value = null;

    /**
     * Get the boolean outcome.
     * @return true or false randomly after the reset.
     */
    public boolean observe() {
        if (value == null) {
            value = random.nextBoolean();
        }

        return value;
    }

    /**
     * Sets the value to null, thereby putting it to the Schrödinger cat state again.
     */
    public void reset() {
        value = null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        SchroedingerBoolean other = (SchroedingerBoolean) obj;
        if (value == null) {
            return other.value == null;
        }
        return value.equals(other.value);
    }

    @Override
    public int hashCode() {
        return value == null ? 0 : value ? 1231 : 1237;
    }

    @Override
    public String toString() {
        return value == null ? "Undetermined" : value.toString();
    }

}
