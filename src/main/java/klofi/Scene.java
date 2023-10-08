package klofi;

import java.util.ArrayList;
import java.util.List;

public abstract class Scene {

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
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject gameObject) {
        if (!isRunning) {
            gameObjectsList.add(gameObject);
        } else {
            gameObjectsList.add(gameObject);
            gameObject.start();
        }
    }
    public abstract void update(float deltaTime);
}
