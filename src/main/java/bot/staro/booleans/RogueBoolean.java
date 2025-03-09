package bot.staro.booleans;

import java.util.Random;

/**
 * A boolean object that sometimes even refuses to be a boolean at all.
 *
 * @author St4ro.
 */
public final class RogueBoolean {
    private final Random random = new Random();
    private boolean value;

    public RogueBoolean() {
        this(false);
    }

    public RogueBoolean(boolean value) {
        this.value = value;
    }

    /**
     * Randomly gets either a current boolean value, or simply turns into an object.
     * @return either an Object or a boolean value.
     */
    public Object getValue() {
        boolean decision = random.nextBoolean();
        if (decision) {
            return new Object();
        }

        return value;
    }

    /**
     * Sets the desired boolean value.
     * @param value is a boolean duh.
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
        RogueBoolean other = (RogueBoolean) obj;
        return value == other.value;
    }

    @Override
    public int hashCode() {
        return value ? 1231 : 1237;
    }

    @Override
    public String toString() {
        return getValue() instanceof Boolean ? String.valueOf(value) : "I refuse to be a boolean, fuck you.";
    }

}
