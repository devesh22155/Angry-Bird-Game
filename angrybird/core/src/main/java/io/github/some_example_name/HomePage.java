package io.github.some_example_name;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class HomePage implements Screen {
    private Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private TextButton Play_button;
    private TextButton Exit_button;

    private Main dam;


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f,0.15f,0.2f,1f);
        // Clear the screen before drawing the background
//        Gdx.gl.glClearColor(0, 0, 0, 1);

        // Begin drawing
        batch.begin();
        // Draw the image to cover the full screen by setting width and height to screen size
        batch.draw(background, 0, 0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("pics1.jpg");
        stage = new Stage(); // Initialize the stage
        Gdx.input.setInputProcessor(stage); // Set input processor
        skin = new Skin(Gdx.files.internal("freezing/skin/freezing-ui.json"));

        // Create the Play button
        Play_button = new TextButton("Play", skin);
        Play_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dam.setScreen(new LevelScreen(dam));
            }
        });

        // Create the Exit button
        Exit_button = new TextButton("Exit", skin);
        Exit_button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit(); // Exit the application
            }
        });

        // Set sizes for the buttons
        Play_button.setSize(300, 150);
        Exit_button.setSize(300, 150);

        // Position the buttons
        updateButtonPosition();
        stage.addActor(Play_button);
        stage.addActor(Exit_button);
    }
    private void updateButtonPosition() {
        // Button dimensions
        float buttonWidth = 150;
        float buttonHeight = 75;

        // Right-bottom position for the Play button
        float playButtonX = Gdx.graphics.getWidth() - buttonWidth - 20; // 20 pixels padding from the right
        float playButtonY = 20; // 20 pixels padding from the bottom
        Play_button.setSize(buttonWidth, buttonHeight);
        Play_button.setPosition(playButtonX, playButtonY);

        // Left-bottom position for the Exit button
        float exitButtonX = 20; // 20 pixels padding from the left
        float exitButtonY = 20; // Same padding from the bottom
        Exit_button.setSize(buttonWidth, buttonHeight);
        Exit_button.setPosition(exitButtonX, exitButtonY);
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update stage viewport

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        // Dispose of the SpriteBatch and Texture to free resources
        batch.dispose();
        background.dispose();
        stage.dispose();
        skin.dispose();
    }
    public HomePage(Main dam) {
        this.dam = dam;
    }
}
