package bot.staro.booleans;

/**
 * A boolean array implementation that can hold an arbitrary number of boolean values.
 * This class represents a sequence of boolean values as an array of longs for efficiency.
 * Each long can store 64 boolean values using bit operations, making it heavier than a
 * standard boolean but capable of storing large boolean sequences.
 */
public final class BigBoolean {
    private final long[] payload;
    private final int dimension;

    /**
     * Creates a new BigBoolean with the specified dimension.
     *
     * @param dimension the number of boolean values this BigBoolean can hold
     */
    public BigBoolean(int dimension) {
        this.dimension = dimension;
        payload = new long[dimension/64+1];
    }

    /**
     * Creates a BigBoolean from a string representation.
     * The string should consist of '0' and '1' characters.
     *
     * @param src the string representation, where '1' represents true and '0' represents false
     */
    public BigBoolean(String src) {
        dimension = src.length();
        int size = dimension / 64 + 1;
        payload = new long[size];
        for (int i = 0; i < payload.length; i++) {
            String chunk = src.substring(i * 64);
            if ((i + 1) * 64 < src.length()) {
                chunk = chunk.substring(0, 64);
            }

            for (int j = 0; j < chunk.length(); j++) {
                char elem = chunk.charAt(j);
                if ('1' == elem) {
                    long bit = 1L << j;
                    payload[i] = payload[i] | bit;
                }
            }
        }
    }

    /**
     * Creates a BigBoolean with a single long value and the specified dimension.
     *
     * @param payload the long value representing the boolean values
     * @param dimension the number of boolean values this BigBoolean can hold
     */
    public BigBoolean(long payload, int dimension) {
        this.dimension = dimension;
        this.payload = new long[1];
        this.payload[0] = payload;
    }

    /**
     * Performs a logical AND operation between two BigBoolean objects.
     *
     * @param a the first BigBoolean operand
     * @param b the second BigBoolean operand
     * @return a new BigBoolean containing the result of the AND operation
     * @throws AssertionError if the dimensions of the operands are not equal
     */
    public static BigBoolean conjunction(BigBoolean a, BigBoolean b) {
        if (a.dimension != b.dimension) {
            throw new AssertionError("a.dimension != b.dimension");
        }

        BigBoolean ret = new BigBoolean(a.dimension);
        for (int i = 0; i < a.payload.length; i++) {
            ret.payload[i] = a.payload[i] & b.payload[i];
        }

        return ret;
    }

    /**
     * Returns the complement of this BigBoolean.
     * The complement flips all boolean values (true becomes false, false becomes true).
     *
     * @return a new BigBoolean containing the complement
     */
    public BigBoolean complement() {
        BigBoolean ret = new BigBoolean(dimension);
        for (int i = 0; i < payload.length; i++) {
            ret.payload[i] = ~payload[i];
        }

        return ret;
    }

    /**
     * Returns a string representation of this BigBoolean.
     * The string consists of '0' and '1' characters, where '1' represents true and '0' represents false.
     *
     * @return the string representation
     */
    @Override
    public String toString() {
        StringBuilder ret = new StringBuilder();
        for (int i = 0; i < payload.length; i++) {
            long chunk = payload[i];
            int dim = 64;
            if (i + 1 == payload.length) {
                dim = dimension - i * 64;
            }

            for (int j = 0; j < dim; j++) {
                long bit = 1L << j;
                if (bit == (bit & chunk)) {
                    ret.append('1');
                } else {
                    ret.append('0');
                }
            }
        }

        return ret.toString();
    }

    /**
     * Compares this BigBoolean with the specified object for equality.
     * Two BigBoolean objects are considered equal if they have the same dimension
     * and the same boolean values.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        BigBoolean other = (BigBoolean) obj;
        if (dimension != other.dimension) {
            return false;
        }

        for (int i = 0; i < payload.length; i++) {
            if (payload[i] != other.payload[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Returns a hash code value for this BigBoolean.
     *
     * @return a hash code value
     */
    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + dimension;
        for (long value : payload) {
            result = 31 * result + Long.hashCode(value);
        }

        return result;
    }

}
