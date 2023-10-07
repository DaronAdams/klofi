package klofi;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class LevelEditorScene extends Scene {

    private int shaderProgram;
    private int vboID, vaoID, eboID;
    private Shader defaultShader;

    private float[] vertexArray = {
        // position             // color
        // (x, y, z)
        50.5f, -50.5f, 0.0f,      1.0f, 0.0f, 0.0f, 1.0f, // Bottom Right - 0
        -50.5f, 50.5f, 0.0f,      1.0f, 1.0f, 0.0f, 1.0f, // Top Left     - 1
        50.5f, 50.5f, 0.0f,       1.0f, 0.0f, 0.0f, 1.0f, // Top Right    - 2
        -50.5f, -50.5f, 0.0f,     0.0f, 0.0f, 0.0f, 1.0f  // Bottom Left  - 3
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
        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader();
        defaultShader.compile();

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
        camera.position.x -= deltaTime * 50.f;
        defaultShader.use();
        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());

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

        defaultShader.detach();




    }
}
