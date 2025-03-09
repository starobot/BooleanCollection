package bot.staro.booleans;
import java.io.*;
import java.nio.file.Files;

/**
 * A boolean which has a value stored on disk
 * @author Edward E Stamper
 */
public class DiskBoolean {

    // the file that the value is stored.
    public File handle;

    /**
     * @param value the initial boolean value
     */
    public DiskBoolean(boolean value) {
        try {
            // Create the file handle for storing the boolean value
            handle = Files.createTempFile("boolean", "boolean").toFile();
        } catch (IOException e) {
            throw new RuntimeException("Could not allocate a file for DiskBoolean object.", e);
        }
        // update the value to the initial value
        try {
            setValue(value);
        } catch (IOException e) {
            throw new RuntimeException("Could not set initial DiskBoolean state", e);
        }
    }

    /**
     * @return the value saved on disk
     */
    public boolean getValue() throws IOException {
        // create a byte array to store the value in memory
        byte[] value = new byte[1];

        // create an input stream to read the data stored in "handle"
        try (InputStream in = new FileInputStream(handle)) {
            // read the value from the file
            in.read(value);
        }

        // return if the written value is "1", meaning true.
        // otherwise, false
        return value[0] == 1;
    }

    /**
     * @param value the value that will be written to the file
     */
    public void setValue(boolean value) throws IOException {
        // create an output stream that overrides current data in the file
        try (FileOutputStream out = new FileOutputStream(handle, false)) {
            // write a 1 for true
            // write a 0 byte for false
            final byte[] byteValue = new byte[] {
                    (byte)(value ? 1 : 0)};
            out.write(byteValue);
            out.flush();
        }
    }
}
