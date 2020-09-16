package gameCode.Living;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.mygdx.game.Engine;
import gameCode.Infrastructure.*;

public class HeroInput extends Component {


    float deltaX = 10.0f;
    
    private boolean up;
    private boolean down;

    public HeroInput() {
        type = "input";
        up = false;
        down = false;
    }

    public void update(Entity entity) {

        if(Gdx.input.isKeyPressed(Input.Keys.A)) { entity.accelerate(deltaX, 180.0f); }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) { entity.accelerate(deltaX, 0.0f); }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) { entity.accelerate(deltaX, 90.0f); }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) { entity.accelerate(deltaX, 270.0f); }
        if(Gdx.input.isKeyPressed(Input.Keys.P)) { entity.accelerate(-1.0f, 0); }

        if(Gdx.input.isKeyPressed(Input.Keys.U)) { Engine.get().getGraphics().getCameraHelper().addZoom(0.01f); }
        if(Gdx.input.isKeyPressed(Input.Keys.I)) { Engine.get().getGraphics().getCameraHelper().addZoom(-0.01f); }

        float a = 0.1f;
        if(Gdx.input.isKeyPressed(Input.Keys.N)) { entity.angle += a; }
        if(Gdx.input.isKeyPressed(Input.Keys.M)) { entity.angle -= a; }
    }
}
