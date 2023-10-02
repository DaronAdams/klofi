package klofi;

import java.awt.event.KeyEvent;

public class LevelEditorScene extends Scene {

    private boolean changingScene = false;
    private float timeToChangeScene = 2.0f;

    public LevelEditorScene() {
        System.out.println("LevelEditorScene");
    }

    @Override
    public void update(float deltaTime) {
        System.out.println("FPS: " + (1.0f / deltaTime));

        if (!changingScene && KeyListener.isKeyPressed(KeyEvent.VK_SPACE)) {
            changingScene = true;
        }

        if (changingScene && timeToChangeScene > 0) {
            timeToChangeScene -= deltaTime;
            Window.getWindow().r -= deltaTime * 5.0f;
            Window.getWindow().g -= deltaTime * 5.0f;
            Window.getWindow().b -= deltaTime * 5.0f;
            Window.getWindow().a -= deltaTime * 5.0f;
        } else if (changingScene) {
            Window.changeScene(1);
        }
    }
}
