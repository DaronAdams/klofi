package klofi;

import renderer.Renderer;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

    protected Renderer renderer = new Renderer();
    protected  Camera camera;
    protected List<GameObject> gameObjectsList = new ArrayList<>();

    private boolean isRunning = false;

    public Scene() {

    }

    public void init() {

    }

    public void start() {
        for (GameObject gameObject: gameObjectsList) {
            gameObject.start();
            this.renderer.add(gameObject);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject gameObject) {
        if (!isRunning) {
            gameObjectsList.add(gameObject);
        } else {
            gameObjectsList.add(gameObject);
            gameObject.start();
            this.renderer.add(gameObject);
        }
    }

    public Camera camera() {
        return this.camera;
    }
    public abstract void update(float deltaTime);
}
