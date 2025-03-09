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

}
