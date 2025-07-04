
package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;

public class LevelScreen implements Screen {
    private Texture background;
    private SpriteBatch batch;
    private Stage stage;
    private Skin skin;
    private TextButton level1;
    private TextButton level2;
    private TextButton level3;
    private TextButton backButton; // Back button
    private Main dam;

    public LevelScreen(Main dam) {
        this.dam = dam;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        background = new Texture("pics2.jpg");
        stage = new Stage(); // Initialize the stage
        Gdx.input.setInputProcessor(stage); // Set input processor
        skin = new Skin(Gdx.files.internal("freezing/skin/freezing-ui.json"));

        // Create Level buttons
        level1 = new TextButton("Level 1", skin);
        level2 = new TextButton("Level 2", skin);
        level3 = new TextButton("Level 3", skin);
        backButton = new TextButton("Back", skin);

        // Add click listeners for each level button
        level1.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dam.setScreen(new Level1Screen(dam));
            }
        });

        level2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to Level 2 game screen (implement this in your main class)
                // dam.setScreen(new Level2Screen(dam)); // Example
            }
        });

        level3.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Transition to Level 3 game screen (implement this in your main class)
                // dam.setScreen(new Level3Screen(dam)); // Example
            }
        });

        // Add click listener for back button
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dam.setScreen(new HomePage(dam)); // Go back to the main menu
            }
        });

        // Set sizes for the buttons
        level1.setSize(300, 150);
        level2.setSize(300, 150);
        level3.setSize(300, 150);
        backButton.setSize(300, 150);

        // Position the buttons
        updateButtonPositions();
        stage.addActor(level1);
        stage.addActor(level2);
        stage.addActor(level3);
        stage.addActor(backButton);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        // Begin drawing
        batch.begin();
        // Draw the image to cover the full screen
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update stage viewport
        updateButtonPositions(); // Update button positions on resize
    }

    private void updateButtonPositions() {
        // Center the buttons vertically with spacing
        float buttonSpacing = 20; // Spacing between buttons
        float totalHeight = (level1.getHeight() + level2.getHeight() + level3.getHeight() + backButton.getHeight() + 3 * buttonSpacing);

        // Center the buttons
        float centerY = (Gdx.graphics.getHeight() - totalHeight) / 2;

        // Set positions for each button
        level1.setPosition((Gdx.graphics.getWidth() - level1.getWidth()) / 2, centerY + (level1.getHeight() + buttonSpacing) * 3);
        level2.setPosition((Gdx.graphics.getWidth() - level2.getWidth()) / 2, centerY + (level2.getHeight() + buttonSpacing) * 2);
        level3.setPosition((Gdx.graphics.getWidth() - level3.getWidth()) / 2, centerY + (level3.getHeight() + buttonSpacing) * 1);
        backButton.setPosition((Gdx.graphics.getWidth() - backButton.getWidth()) / 2, centerY); // Position back button at the bottom
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

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
}
