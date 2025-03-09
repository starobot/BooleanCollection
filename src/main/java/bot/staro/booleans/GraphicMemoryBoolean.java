package bot.staro.booleans;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLCapabilities;

import java.nio.ByteBuffer;

/**
 * A Boolean stored in graphics memory using OpenGL(lwjgl)
 * @author Edward E Stamper
 */
public class GraphicMemoryBoolean {

    static {
        // Assert LWJGL library is present
        try {
            Class.forName("org.lwjgl.opengl.GL11");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to find LWJGL classes, please make sure LWJGL library is present.", e);
        }
    }

    // The texture id which stores the current value
    private final int TEXTURE_ID;

    /**
     * @param initialValue the initial boolean value
     * @throws RuntimeException if the thread does not have GL context, or an invalid context
     */
    public GraphicMemoryBoolean(boolean initialValue) {
        // assure current thread can use GL11
        assertGLContext();

        // allocate a new texture ID
        TEXTURE_ID = GL11.glGenTextures();

        // write the initial value
        setValue(initialValue);
    }

    /**
     * Sets the new value of the boolean
     * @param newValue the new value for the boolean
     * NOTE, the GL texture id will always be changed!
     */
    public void setValue(boolean newValue) {
        // assure current thread can use GL11
        assertGLContext();

        // Setup GL state for texture uploads
        setUpGLState();

        // Initialize a direct buffer for opengl to use
        ByteBuffer buffer = ByteBuffer.allocateDirect(3);

        // write the boolean value to the buffer
        buffer.put(0, (byte) (newValue ? 1 : 0));

        // set the opengl texture to the TEXTURE_ID for this boolean
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, TEXTURE_ID);

        // Upload the boolean value as a 1x1 texture
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB8,
                1, 1, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE,
                buffer);
    }

    /**
     * @return the current boolean value from graphic memory
     * NOTE, the GL texture id will always be changed!
     */
    public boolean getValue() {
        // assure current thread can use GL11
        assertGLContext();

        // Setup GL state for texture reading
        setUpGLState();

        // set the opengl texture to the TEXTURE_ID for this boolean
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, TEXTURE_ID);

        // Initialize a direct buffer for the state to be written to
        ByteBuffer buffer = ByteBuffer.allocateDirect(3);

        // Write the state from graphic memory to the buffer
        GL11.glReadPixels(0, 0, 1, 1, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, buffer);

        byte valueAsByte = buffer.get(0);
        return valueAsByte == 1; // if 1, true was written, otherwise, return false
    }

    /**
     * Setup GL state so that reading & writing to pixel buffers
     * will not crash the jvm
     */
    private void setUpGLState() {
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ROW_LENGTH, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_PIXELS, 0);
        GL11.glPixelStorei(GL11.GL_UNPACK_SKIP_ROWS, 0);
    }

    /**
     * Assert that GL context in the current thread is both present,
     * & has capability to use GL11
     */
    private void assertGLContext() {
        try {
            GLCapabilities currentThreadCapabilities = GL.getCapabilities();
            if (!currentThreadCapabilities.OpenGL11) {
                throw new RuntimeException("Current thread has GL context that cannot use GL11");
            }
        } catch (IllegalStateException e) {
            throw new RuntimeException("Current thread has no GL context", e);
        }
    }
}
