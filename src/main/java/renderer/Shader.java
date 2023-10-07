package renderer;


import klofi.utils.EngineConstants;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glGetShaderInfoLog;

public class Shader {
    private int shaderProgramID;
    private String vertexSource, fragmentSource;
    private String filepath;

    public Shader() {

    }

    public void compile() {
        int vertexID, fragmentID;
        // ----------------------------------------------------------------
        // Compile and link shaders
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
            System.out.println("ERROR: " + filepath + "\n\t" +
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
            System.out.println("ERROR: " + filepath + "\n\t" +
                    "Fragment shader compilation failed.");
            System.out.println(glGetShaderInfoLog(fragmentID, length));
            assert false : "";
        }

        // Link shaders and check for errors
        shaderProgramID = glCreateProgram();
        glAttachShader(shaderProgramID, vertexID);
        glAttachShader(shaderProgramID, fragmentID);
        glLinkProgram(shaderProgramID);

        // Check for errors during linking
        success = glGetProgrami(shaderProgramID, GL_LINK_STATUS);
        if (success == GL_FALSE) {
            int length = glGetProgrami(shaderProgramID, GL_INFO_LOG_LENGTH);
            System.out.println("ERROR: " + filepath + "\n\t" +
                    "Shader Linking failed.");
            System.out.println(glGetProgramInfoLog(shaderProgramID, length));
            assert false : "";
        }
    }

    public void use() {
        // Bind shader program
        glUseProgram(shaderProgramID);
    }

    public void detach() {
        glUseProgram(0);
    }

    public void uploadMat4f(String varName, Matrix4f mat4) {
        int varLocation = glGetUniformLocation(shaderProgramID, varName);
        FloatBuffer matBuffer = BufferUtils.createFloatBuffer(16);
        mat4.get(matBuffer);
        glUniformMatrix4fv(varLocation, false, matBuffer);
    }
}
