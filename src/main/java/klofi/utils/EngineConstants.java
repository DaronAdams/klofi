package klofi.utils;
/*
 Class containing all the constants used throughout the engine
 */
public class EngineConstants {
    public static int NUMBER_OF_MOUSE_BUTTONS = 3;
    public static int NUMBER_OF_KEYBINDS = 350;
    public static String VERTEX_SHADER_SRC = "#version 330 core\n" +
            "    layout (location = 0) in vec3 aPos;\n" +
            "    layout (location = 1) in vec2 aTexCoords;\n" +
            "\n" +
            "    uniform mat4 uProjection;\n" +
            "    uniform mat4 uView;\n" +
            "\n" +
            "    out vec2 fTexCoord;\n" +
            "\n" +
            "    void main()\n" +
            "    {\n" +
            "        fTexCoord = aTexCoords;\n" +
            "        gl_Position = uProjection * uView * vec4(aPos.x, aPos.y, aPos.z, 1.0);\n" +
            "    }";
    public static String FRAGMENT_SHADER_SRC = "#version 330 core\n" +
            "    uniform sampler2D TEX_SAMPLER;\n" +
            "    out vec4 FragColor;\n" +
            "\n" +
            "    in vec2 fTexCoord;\n" +
            "    void main()\n" +
            "    {\n" +
            "        FragColor = texture(TEX_SAMPLER, fTexCoord);\n" +
            "    }";
}
