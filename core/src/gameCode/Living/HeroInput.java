package gameCode.Living;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import gameCode.Infrastructure.*;

public class HeroInput extends Component {


    float deltaX = 1.5f;
    
    private boolean up;
    private boolean down;

    public HeroInput() {
        
        type = "input";
        up = false;
        down = false;
    }

    public void update(Entity entity) {

        if(Gdx.input.isKeyPressed(Input.Keys.A)) { entity.velAng -= deltaX; }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) { entity.velAng += deltaX; }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) { entity.velMag += deltaX; }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) { entity.velMag -= deltaX; }
        if(Gdx.input.isKeyPressed(Input.Keys.P)) { entity.velMag = 0; entity.velAng = 0; }



        float a = 0.1f;
        if(Gdx.input.isKeyPressed(Input.Keys.N)) { entity.angle += a; }
        if(Gdx.input.isKeyPressed(Input.Keys.M)) { entity.angle -= a; }



        entity.body.setLinearVelocity(entity.velAng, entity.velMag);
        entity.body.setAngularVelocity(entity.angle);




    }
}
