    #type vertex
    #version 330 core
    layout (location = 0) in vec3 aPos;

    uniform mat4 uProjection;
    uniform mat4 uView;

    void main()
    {
        gl_Position = uProjection * uView * vec4(aPos.x, aPos.y, aPos.z, 1.0);
    }

    #type fragment
    #version 330 core
    out vec4 FragColor;

    void main()
    {
        FragColor = vec4(1.0f, 0.5f, 0.2f, 1.0f);
    }