package bot.staro.booleans;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;
import java.util.jar.JarOutputStream;
import java.util.zip.ZipEntry;

/**
 * A Boolean object that is stored within another JVM process
 * The remote process can be killed by calling .close(),
 * or will be killed when the parent process is no longer running
 *
 * @author Edward E Stamper
 */
public class RemoteProcessBoolean implements Closeable {
    // ==================================================
    // initialize new jar file for boolean storage on other processes
    private static final File booleanStorageJar;

    static {
        try {
            booleanStorageJar = createJarFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // ==================================================

    private final Process remoteProcess;


    /**
     * Initialize a new RemoteProcessBoolean with a default value of false
     */
    public RemoteProcessBoolean() {
        this(false);
    }

    /**
     * Initialize a new RemoteProcessBoolean with the given default value,
     * also starts the new process
     */
    public RemoteProcessBoolean(boolean defaultValue) {
        // create a command that starts the newly created jar file
        // using the current java exe
        String file = "\"" + booleanStorageJar.getAbsolutePath() + "\"";

        // get the current process ID so the remote process can know when to close
        String pid = String.valueOf(ProcessHandle.current().pid());
        // java -jar booleanStorage.jar [pid]
        String[] command = new String[]{
                getJavaExePath(), "-jar", file, pid
        };

        // spawn a new subprocess to host the boolean value
        try {
            //remoteProcess = Runtime.getRuntime().exec(command);
            remoteProcess = new ProcessBuilder(command).start();
        } catch (IOException e) {
            throw new RuntimeException("Could not create subprocess", e);
        }
        setValue(defaultValue);
    }

    /**
     * Writes the new value to the remote process
     */
    public void setValue(boolean defaultValue) {
        // Assert the object has not been closed
        if (!remoteProcess.isAlive()) {
            throw new RuntimeException("RemoteProcessBoolean has been closed, cannot write.");
        }

        // create an output stream to write to the remote process input stream
        try {
            DataOutputStream out = new DataOutputStream(remoteProcess.getOutputStream());
            // write the instruction to be interpreted by RemoteProcessBoolean.main
            byte instruction = (byte) (defaultValue ? 1 : 0);
            out.writeByte(instruction);
            out.flush();
        } catch (IOException e) {
            throw new RuntimeException("Failed to write to remote process", e);
        }
    }

    /**
     * Requests an update from the remote process and returns the provided boolean
     */
    public boolean getValue() {
        // Assert the object has not been closed
        if (!remoteProcess.isAlive()) {
            throw new RuntimeException("RemoteProcessBoolean has been closed, cannot read.");
        }

        boolean value = false;
        synchronized (this) {
            try {
                DataOutputStream out = new DataOutputStream(remoteProcess.getOutputStream());
                // write the instruction to be interpreted by RemoteProcessBoolean.main
                // instruction (2) causes the program to print the boolean to out (remoteProcess.in)
                byte instruction = (byte) 2;
                out.writeByte(instruction);
                out.flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            // Read the state of the boolean
            try {
                BufferedReader reader = remoteProcess.inputReader();
                while (!reader.ready()) {
                    // Wait until has a new line
                }
                String line = reader.readLine();
                value = Boolean.parseBoolean(line);
            } catch (IOException e) {
                throw new RuntimeException("Error reading process output stream", e);
            }
        }
        return value;
    }

    /**
     * The main method that will run on the other JVM
     */
    private static boolean value = false;
    public static void main(String[] args) throws IOException {
        // get parent process ID
        long pid = Long.parseLong(args[0]);
        // create an input stream to read incoming data
        DataInputStream inputStream = new DataInputStream(new BufferedInputStream(System.in));
        // Listen for instruction bytes
        // 0 = set to false
        // 1 = set to true
        // 2 = print value to out
        while (true) {
            if (inputStream.available() > 0) {
                byte instruction = inputStream.readByte();
                switch (instruction) {
                    case 0 -> value = false;
                    case 1 -> value = true;
                    case 2 -> // print value to OUT
                            System.out.println(value);
                }
            }
            // check if parent process is still alive, otherwise, close
            if (ProcessHandle.of(pid).isEmpty()) System.exit(0);
        }
    }

    /**
     * Creates a jar file that will be used to store and update the remote boolean
     * @return the created or existing jar file
     */
    private static File createJarFile() throws IOException {
        // create a jar file with this class included, so that we can run it to store out boolean
        File booleanStorage = new File("booleanStorage.jar");
        // if the file is already written, use the existing file
        if (booleanStorage.exists()) return booleanStorage;

        // does not exist, write the new file
        JarOutputStream jarOutputStream = new JarOutputStream(Files.newOutputStream(booleanStorage.toPath()));

        // Write a manifest for the jar file
        ZipEntry MANIFESTMF = new ZipEntry("META-INF/MANIFEST.MF");
        jarOutputStream.putNextEntry(MANIFESTMF);
        jarOutputStream.write((
                """
                        Manifest-Version: 1.0
                        Main-Class: bot.staro.booleans.RemoteProcessBoolean
                        """
                ).getBytes(StandardCharsets.UTF_8)
        );
        jarOutputStream.flush();

        // add this class to the jar file
        appendClassToJar("bot/staro/booleans/RemoteProcessBoolean.class", jarOutputStream);

        jarOutputStream.flush();

        jarOutputStream.close();

        // finished writing the jar file, return the newly created file
        return booleanStorage;
    }

    /**
     * Adds a class from the current running JVM to a jarFile
     * @param className the name of the class from the current jar to be added
     * @param jarOutputStream the output stream of the jar file to write to
     * @throws IOException if failed to write, or if JVM does not return a resource for provided class
     */
    private static void appendClassToJar(String className, JarOutputStream jarOutputStream) throws IOException {
        // create a new zip entry to write the class
        ZipEntry clazz = new ZipEntry(className);

        // set the jar file to write the zip entry
        jarOutputStream.putNextEntry(clazz);

        // get the bytes of the class from the current running jar
        ClassLoader cl = RemoteProcessBoolean.class.getClassLoader();
        InputStream stream = cl.getResourceAsStream(className);
        if (stream == null) throw new RuntimeException("Could not find " + className);

        // write the existing class to the new jar
        jarOutputStream.write(stream.readAllBytes());
        jarOutputStream.flush();
    }

    /**
     * @return the java exe path of the current running JVM
     */
    private static String getJavaExePath() {
        // get the current process command
        Optional<String> processCommand = ProcessHandle.current().info().command();
        // return the process command if present, otherwise, default to "java"
        return processCommand.orElse("java");
    }

    @Override
    public void close() {
        remoteProcess.destroy();
    }

}
