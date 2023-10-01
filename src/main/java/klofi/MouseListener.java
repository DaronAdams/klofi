package klofi;

import klofi.utils.EngineConstants;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private double scrollX, scrollY;
    private double xPosition, yPosition, lastYPosition, lastXPosition;
    private boolean isDragging;
    private boolean mouseButtonPressed[] = new boolean[EngineConstants.NUMBER_OF_MOUSE_BUTTONS];

    private static MouseListener instance;

    private MouseListener() {
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPosition = 0.0;
        this.yPosition = 0.0;
        this.lastXPosition = 0.0;
        this.lastYPosition = 0.0;
    }

    public static MouseListener get() {
        if (MouseListener.instance == null) {
            MouseListener.instance = new MouseListener();
        }

        return MouseListener.instance;
    }

    public static void mousePositionCallback(long window, double xPosition,
                                             double yPosition) {
        get().lastXPosition = xPosition;
        get().lastYPosition = yPosition;
        get().xPosition = xPosition;
        get().yPosition = yPosition;
        get().isDragging = get().mouseButtonPressed[0] || get().mouseButtonPressed[1] || get().mouseButtonPressed[2];
    }

    public static void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            // For mice that have more than 3 buttons
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = true;
            }
        } else if (action == GLFW_RELEASE) {
            if (button < get().mouseButtonPressed.length) {
                get().mouseButtonPressed[button] = false;
                get().isDragging = false;
            }
        }
    }

    public static void mouseScrollCallback(long window, double xOffset, double yOffset) {
        get().scrollX = xOffset;
        get().scrollY = yOffset;
    }

    public static void endFrame() {
        get().scrollX = 0;
        get().scrollY = 0;
        get().lastXPosition = get().xPosition;
        get().lastYPosition = get().yPosition;
    }

    public static float getX() {
        return (float)get().xPosition;
    }

    public static float getY() {
        return (float)get().yPosition;
    }

    public static float getDx() {
        return (float)(get().lastXPosition - get().xPosition);
    }

    public static float getDy() {
        return (float)(get().lastYPosition - get().yPosition);
    }

    public static float getScrollX() {
        return (float)get().scrollX;
    }

    public static float getScrollY() {
        return (float)get().scrollY;
    }

    public static boolean isDragging() {
        return get().isDragging;
    }

    public static boolean mouseButtonDown(int button) {
        if (button < get().mouseButtonPressed.length) {
            return get().mouseButtonPressed[button];
        } else {
            return false;
        }
    }

}
