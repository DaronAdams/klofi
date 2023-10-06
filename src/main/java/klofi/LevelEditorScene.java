package klofi;


import klofi.utils.EngineConstants;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private int vertexID, fragmentID, shaderProgram;
    private int vboID, vaoID, eboID;

    private float[] vertexArray = {
        // position             // color
        // (x, y, z)
        0.5f, -0.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, // Bottom Right - 0
        -0.5f, 0.5f, 0.0f,      1.0f, 1.0f, 0.0f, 1.0f, // Top Left     - 1
        0.5f, 0.5f, 0.0f,       0.0f, 0.0f, 1.0f, 1.0f, // Top Right    - 2
        -0.5f, -0.5f, 0.0f,     1.0f, 1.0f, 0.0f, 1.0f  // Bottom Left  - 3
    };

    // ----------------------------------------------------------------
    // Note: This array must be in counter-clockwise order
    // ----------------------------------------------------------------
    private int[] elementArray = {
        /*
                x       x


                x       x
        */
        2, 1, 0, // Top Right triangle
        0, 1, 3, // Bottom Left Triangle
    };

    public LevelEditorScene() {

    }

    @Override
    public void init() {
        // ----------------------------------------------------------------
        // Compile and Link shaders
        // ----------------------------------------------------------------

        // Load and compile the vertex shader
        vertexID = glCreateShader(GL_VERTEX_SHADER);
        // Pass the shader source code to the GPU
        glShaderSource(vertexID, EngineConstants.VERTEX_SHADER_SRC);
        glCompileShader(vertexID);

        // Check for errors during the compilation process
        int success = glGetShaderi(vertexID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(vertexID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'default.glsl'\n\t" +
                    "Vertex shader compilation failed.");
            System.out.println(glGetShaderInfoLog(vertexID, length));
            assert false : "";
        }

        // Load and compile the Fragment shader
        fragmentID = glCreateShader(GL_FRAGMENT_SHADER);
        // Pass the shader source code to the GPU
        glShaderSource(fragmentID, EngineConstants.FRAGMENT_SHADER_SRC);
        glCompileShader(fragmentID);

        // Check for errors during the compilation process
        success = glGetShaderi(fragmentID, GL_COMPILE_STATUS);
        if (success == GL_FALSE) {
            int length = glGetShaderi(fragmentID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'default.glsl'\n\t" +
                    "Fragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, length));
            assert false : "";
        }

        // Link shaders and check for errors
        shaderProgram = glCreateProgram();
        glAttachShader(shaderProgram, vertexID);
        glAttachShader(shaderProgram, fragmentID);
        glLinkProgram(shaderProgram);

        // Check for errors during linking
        success = glGetProgrami(shaderProgram, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int length = glGetProgrami(shaderProgram, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: 'default.glsl'\n\t" +
                    "Shader Linking failed.");
            System.out.println(glGetProgramInfoLog(shaderProgram, length));
            assert false : "";
        }

        // ----------------------------------------------------------------
        // Generate VAO, VBO, and EBO Buffer Objects, and send to the GPU
        // ----------------------------------------------------------------
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // Create a float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        // Create the VBO and upload the vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);

        // Create the index and load
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

        eboID = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

        // Add the vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int floatBytes = 4;
        int vertexSizesInBytes = (positionSize + colorSize) * floatBytes;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizesInBytes,
                0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false, vertexSizesInBytes,
                positionSize * floatBytes);
        glEnableVertexAttribArray(1);

    }

    @Override
    public void update(float deltaTime) {
        // Bind shader program
        glUseProgram(shaderProgram);
        // Bind the VAO we're using
        glBindVertexArray(vaoID);

        // Enable the vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        // Unbind everything
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);

        glBindVertexArray(0);

        glUseProgram(0);




    }
}
