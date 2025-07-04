package io.github.some_example_name;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class Level1Screen implements Screen {

    private SpriteBatch batch;
    private World world;
    private Stage stage;
    private Texture slingshot_image;
    private Texture bird_image;
    private Body b_body;
    private Body p_body;
    private int score;
    private boolean b_launched;
    private Texture background;
    private Vector2 sling_pos;
    private Vector2 drag_pos;
    private boolean drag;
    private final float max_dragDistance = 1;
    private ShapeRenderer sh_render;
    private Vector2 slingshot_left_band;
    private Vector2 slingshot_right_band;
    private Body slingshot_structure;
    private Box2DDebugRenderer debug_render;
    private TextButton pause;
    private Skin skin1;
    private Array<Body> blocks;
    private Texture block1;
    private Texture block2;
    private Texture pig;
    private Sprite Slingshot_sprite;
    private Vector2 Slingshot_anchor;
    private Vector2 Bird_position;
    private Sprite Bird_sprite;
    final float convert_PIX_to_M = 50f;
    private Array<Body> destroy_these_bodies;
    private BitmapFont font;
    private GlyphLayout glyphLayout;
    private int attempt;
    private int remaining_pigs;
    private boolean bird_in_motion;

    private Main dam;

    public Level1Screen(Main dam) {
        this.dam=dam;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        destroy_these_bodies = new Array<>();
        font = new BitmapFont();
        bird_in_motion = false;
        font.getData().setScale(2f);
        font.setColor(1, 1, 1, 1);
        glyphLayout = new GlyphLayout();

        score = 0;
        attempt = 3;

        world = new World(new Vector2(0, -5f), true);

        BodyDef bird_define = new BodyDef();
        bird_define.type = BodyDef.BodyType.DynamicBody;
        bird_define.position.set(2, 2);
        b_body = world.createBody(bird_define);


        CircleShape bird_shape = new CircleShape();
        bird_shape.setRadius(0.2f);
        FixtureDef fixture_define = new FixtureDef();
        fixture_define.shape = bird_shape;
        fixture_define.density = 1.0f;
        fixture_define.restitution = 0.4f;
        fixture_define.friction = 0.5f;

        fixture_define.filter.categoryBits = 0x0002;
        fixture_define.filter.maskBits = 0x0001 | 0x0004;

        b_body.setUserData("Bird");
        b_body.createFixture(fixture_define);
        b_body.setBullet(true);
        bird_shape.dispose();



        skin1 = new Skin(Gdx.files.internal("freezing/skin/freezing-ui.json"));

        sh_render = new ShapeRenderer();
        slingshot_left_band = new Vector2(2, 1.5f);
        slingshot_right_band = new Vector2(2.5f, 1.5f);

        pause = new TextButton("Pause",skin1);
        pause.addListener(new ClickListener(){
            public void clicked(InputEvent event, float x, float y){
                dam.setScreen(new LevelEndScreen(dam));
            }
        });
        pause.setSize(100,50);
        stage.addActor(pause);

        background = new Texture("backgroundlevel.jpeg");
        block1 = new Texture("wood.png");
        block2 = new Texture("woods.png");
        pig = new Texture("pig.png");


        b_launched = false;

        slingshot();
        bird();
        pig(10,7,1);
        remaining_pigs = 1;

        setting_ground();

        blocks = new Array<>();

        float w_ = 1.0f;
        float h_ = 3.0f;
        setting_blocks(9,2,w_,h_,block1);
        setting_blocks(11,2,w_,h_,block1);
        setting_blocks(10,7,3.0f,1.0f,block2);



        debug_render = new Box2DDebugRenderer();
    }


    private void setting_blocks(float x_coordinate,float y_coordinate, float w_, float h_, Texture texture) {
        BodyDef block_define = new BodyDef();
        block_define.type = BodyDef.BodyType.DynamicBody;
        block_define.position.set(x_coordinate, y_coordinate + h_ / 2);

        Body block_ = world.createBody(block_define);


        PolygonShape shape_of_block = new PolygonShape();
        shape_of_block.setAsBox(w_ / 2, h_ / 2);

        FixtureDef block_fix = new FixtureDef();
        block_fix.shape = shape_of_block;
        block_fix.density = 1.0f;
        block_fix.restitution = 0.2f;
        block_fix.friction = 0.8f;

        block_fix.filter.categoryBits = 0x0004;
        block_fix.filter.maskBits = (short) 0xFFFF;

        block_.setUserData("Block");
        block_.createFixture(block_fix);
        block_.setBullet(false);
        block_.setUserData(texture);
        blocks.add(block_);

        shape_of_block.dispose();
    }

    private void setting_ground() {
        BodyDef ground_define = new BodyDef();
        ground_define.type = BodyDef.BodyType.StaticBody;
        ground_define.position.set(10, 0);

        Body ground = world.createBody(ground_define);

        PolygonShape shape_of_ground = new PolygonShape();
        shape_of_ground.setAsBox(10, 0.5f);

        FixtureDef fixture_of_ground = new FixtureDef();
        fixture_of_ground.shape = shape_of_ground;
        fixture_of_ground.friction = 2.0f;

        fixture_of_ground.filter.categoryBits = 0x0001;
        fixture_of_ground.filter.maskBits = 0x0004 | 0x0002;

        ground.setUserData("Ground");

        ground.createFixture(fixture_of_ground);
        shape_of_ground.dispose();
    }

    @Override
    public void render(float delta) {

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game_input();

        world.step(1/60f, 10, 8);
        world.setContinuousPhysics(true);
        world.setVelocityThreshold(1.0f);

        for (Body body : destroy_these_bodies) {
            world.destroyBody(body);
        }

        destroy_these_bodies.clear();

        batch.begin();
        batch.draw(background, 0, 0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        second_render();

        String score_ = "Score: " + score;
        glyphLayout.setText(font, score_);
        font.draw(batch, score_, 270, 475);

        String attempts = "Attempts: " + attempt;
        glyphLayout.setText(font, attempts);
        font.draw(batch, attempts,20, 475);
        batch.end();

        if (drag) {
            sh_render.begin(ShapeRenderer.ShapeType.Line);
            sh_render.setColor(1, 0, 0, 1);
            Vector2 bird_position = b_body.getPosition();
            sh_render.line(sling_pos.x * 50, sling_pos.y * 50, bird_position.x * 50, bird_position.y * 50);
            sh_render.end();
        }

        debug_render.render(world, stage.getCamera().combined.scl(1/50f));

        stage.act();
        stage.draw();

        if (p_body != null) {
            Vector2 pig_position = p_body.getPosition();
            if (pig_position.x < -2 || pig_position.x > 20 || pig_position.y < -2 || pig_position.y > 13) {
                destroy_these_bodies.add(p_body);
                p_body = null;
                score += 10;
            }
        }

        loss();
    }

    private void slingshot() {

        slingshot_image = new Texture("slingshot.png");
        Slingshot_sprite = new Sprite(slingshot_image);

        Slingshot_anchor = new Vector2(200, 200);

        Slingshot_sprite.setSize(50, 100);
        Slingshot_sprite.setOriginCenter();
        Slingshot_sprite.setPosition(Slingshot_anchor.x - 25, Slingshot_anchor.y - 50);

        sling_pos = new Vector2(2, 1);
        BodyDef sling_define = new BodyDef();
        sling_define.position.set(2, 1);
        sling_define.type = BodyDef.BodyType.StaticBody;
        slingshot_structure = world.createBody(sling_define);



        PolygonShape sling_shape = new PolygonShape();
        sling_shape.setAsBox(0.1f, 0.5f);
        FixtureDef fixture_define = new FixtureDef();
        fixture_define.shape = sling_shape;
        slingshot_structure.createFixture(fixture_define);

        sling_shape.dispose();
    }

    private void bird() {

        bird_image = new Texture("bird.png");
        Bird_sprite = new Sprite(bird_image);

        Bird_position = new Vector2(Slingshot_anchor.x, Slingshot_anchor.y);
        Bird_sprite.setSize(40, 40);
        Bird_sprite.setOriginCenter();
        Bird_sprite.setPosition(Bird_position.x, Bird_position.y);


    }

    private void pig(float x, float y, float height){
        BodyDef pig_define = new BodyDef();
        pig_define.type = BodyDef.BodyType.DynamicBody;
        pig_define.position.set(x, y + height / 2 + 0.2f);
        p_body = world.createBody(pig_define);

        CircleShape pig_shape = new CircleShape();
        pig_shape.setRadius(0.2f);
        FixtureDef fixture_define = new FixtureDef();
        fixture_define.shape = pig_shape;
        fixture_define.density = 1.0f;
        fixture_define.restitution = 0.4f;
        fixture_define.friction = 5.0f;

        fixture_define.filter.categoryBits = 0x0003;
        fixture_define.filter.maskBits = 0x0001 | 0x0004 | 0x0002;

        p_body.setUserData("Pig");

        p_body.createFixture(fixture_define);
        p_body.setBullet(true);
        pig_shape.dispose();
    }

    private void game_input() {
        if (Gdx.input.isTouched()) {
            Vector2 touch_position = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            drag_pos = new Vector2(touch_position.x / 50, touch_position.y / 50);
            float dist = sling_pos.dst(drag_pos);

            if (dist <= max_dragDistance && !b_launched) {
                drag = true;
                b_body.setTransform(drag_pos, 0);
            } else if (dist > max_dragDistance) {
                Vector2 direction = drag_pos.sub(sling_pos).nor();
                drag_pos.set(sling_pos.cpy().add(direction.scl(max_dragDistance)));
                b_body.setTransform(drag_pos, 0);
            }
        } else if (drag && !b_launched) {
            drag = false;
            b_launched = true;
//            attempt--;
            bird_in_motion = true;
            b_body.setTransform(drag_pos, 0);
            Vector2 launch_direction = sling_pos.cpy().sub(b_body.getPosition()).nor();
            float launch_power = sling_pos.dst(b_body.getPosition());
            Vector2 launch_velocity = launch_direction.scl(launch_power * 10);

            b_body.setTransform(sling_pos, 0);
            b_body.setLinearVelocity(0, 0);
            b_body.applyLinearImpulse(launch_velocity, b_body.getWorldCenter(), true);


        }
        if (b_launched) {
            if (b_body.getLinearVelocity().len() < 0.1f && b_body.getPosition().dst(sling_pos) > 0.1f) {
                Bird_Reset();
            }
            Vector2 bird_pos = b_body.getPosition();
            if (bird_pos.x < -2 || bird_pos.x > 20 || bird_pos.y < -2 || bird_pos.y > 13) {
                Bird_Reset();
            }
        }
    }

    private void Bird_Reset() {

        b_launched = false;
        bird_in_motion = false;
        b_body.setAwake(true);

        b_body.setTransform(sling_pos.x, sling_pos.y, 0);

        b_body.setLinearVelocity(0, 0);
        b_body.setAngularVelocity(0);
        b_body.setFixedRotation(true);

        b_body.setLinearDamping(0);
        b_body.setAngularDamping(0);

        if (attempt > 0) {
            bird();  // Reinitialize bird sprite and body
        } else {
            // Handle case when no attempts are left (e.g., Game Over)
            loss();
        }
    }


    private void loss() {
        // Handle the case when all pigs are destroyed or if no attempts remain
        if (remaining_pigs <= 0 || attempt <= 0) {
            // Show level end screen or similar logic
            dam.setScreen(new LevelEndScreen(dam));
        }
    }


    private void second_render() {
        Vector2 slingshot_position = slingshot_structure.getPosition();
        batch.draw(slingshot_image, slingshot_position.x * 50 - 25, slingshot_position.y * 50 - 50, 50, 100);

        Vector2 bird_position = b_body.getPosition();
        float x = b_body.getPosition().x * convert_PIX_to_M;
        float y = b_body.getPosition().y * convert_PIX_to_M;
        batch.draw(bird_image, x - Bird_sprite.getWidth() / 2, y - Bird_sprite.getHeight() / 2, Bird_sprite.getWidth(), Bird_sprite.getHeight());

        if (p_body != null) {
            Vector2 pig_position = p_body.getPosition();
            batch.draw(pig, pig_position.x * 50 - 10, pig_position.y * 50 - 10, 20, 20);
        }

        for (Body block : blocks){
            Vector2 position = block.getPosition();

            Texture which_block = (Texture) block.getUserData();

            PolygonShape shape = (PolygonShape) block.getFixtureList().first().getShape();
            Vector2 v1 = new Vector2();
            Vector2 v2 = new Vector2();
            shape.getVertex(0,v1);
            shape.getVertex(2, v2);
            float block_width_metre = Math.abs(v2.x - v1.x);
            float block_height_metre = Math.abs(v2.y - v1.y);

            float block_width_pixel = block_width_metre * convert_PIX_to_M;
            float block_height_pixel = block_height_metre * convert_PIX_to_M;

            batch.draw(
                which_block, position.x * convert_PIX_to_M - block_width_pixel / 2, position.y * convert_PIX_to_M - block_height_pixel / 2, block_width_pixel, block_height_pixel);
        }

        if (drag) {
            sh_render.begin(ShapeRenderer.ShapeType.Filled);
            sh_render.setColor(0.5f, 0.3f, 0.1f, 1);

            // Left band
            sh_render.rectLine(
                slingshot_left_band.x * 50, slingshot_left_band.y * 50, bird_position.x * 50, bird_position.y * 50, 5
            );

            // Right band
            sh_render.rectLine(
                slingshot_right_band.x * 50, slingshot_right_band.y * 50, bird_position.x * 50, bird_position.y * 50, 5
            );

            sh_render.end();
        }

        world.setContactListener(new ContactListener() {
            @Override
            public void beginContact(Contact contact) {
                Fixture f1 = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();
//                System.out.println("Collision detected between: " + fixtureA + " and " + fixtureB);

                Object first_body = f1.getBody().getUserData();
                Object Second_body = fixtureB.getBody().getUserData();

                if ((first_body == "Pig" && Second_body == "Ground") || (first_body == "Ground" && Second_body == "Pig")) {
                    if (p_body != null) {
                        destroy_these_bodies.add(p_body);
                        p_body = null;
                        score+=10;
                        remaining_pigs--;

                        if (remaining_pigs == 0){
                            dam.setScreen(new LevelEndScreen(dam));
                        }
                    }
                }
                if (("Bird".equals(first_body) && "Block".equals(Second_body)) || ("Block".equals(first_body) && "Bird".equals(Second_body))) {
                    score+=2;
                }

            }

            @Override
            public void endContact(Contact contact) { }

            @Override
            public void preSolve(Contact contact, Manifold oldManifold) { }

            @Override
            public void postSolve(Contact contact, ContactImpulse impulse) { }
        });

    }




    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true); // Update stage viewport
        updateButtonPosition();

    }

    private void updateButtonPosition() {
        // Button dimensions
        float buttonWidth = 100;
        float buttonHeight = 50;

        // Right-top position for the Pause button
        float pauseButtonX = Gdx.graphics.getWidth() - buttonWidth - 5; // 20 pixels padding from the right
        float pauseButtonY = Gdx.graphics.getHeight() - buttonHeight - 5; // 20 pixels padding from the top

        // Set size and position for the pause button
        pause.setSize(buttonWidth, buttonHeight);
        pause.setPosition(pauseButtonX, pauseButtonY);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void hide() {
         dispose();
    }

    @Override
    public void dispose() {
        for (Body block : blocks) {
            world.destroyBody(block);
        }
        blocks.clear();
        world.dispose();
        batch.dispose();
        stage.dispose();
        slingshot_image.dispose();
        bird_image.dispose();
        block1.dispose();
        block2.dispose();
        pig.dispose();
        sh_render.dispose();
        debug_render.dispose();
        background.dispose();
        font.dispose();
    }
}

