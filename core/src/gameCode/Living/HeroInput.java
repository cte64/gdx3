package gameCode.Living;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import gameCode.Infrastructure.*;

public class HeroInput extends Component {


    float deltaX = 10.6f;
    
    private boolean up;
    private boolean down;

    public HeroInput() {
        
        type = "input";
        up = false;
        down = false;
    }

    public void update(Entity entity) {

        if(Gdx.input.isKeyPressed(Input.Keys.A)) { entity.x_pos -= deltaX; }
        if(Gdx.input.isKeyPressed(Input.Keys.D)) { entity.x_pos += deltaX; }
        if(Gdx.input.isKeyPressed(Input.Keys.W)) { entity.y_pos += deltaX; }
        if(Gdx.input.isKeyPressed(Input.Keys.S)) { entity.y_pos -= deltaX; }



    }
}
