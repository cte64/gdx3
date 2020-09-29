package gameCode.Factory;

import gameCode.Infrastructure.Entity;

public abstract class AbstractFactory {
    public abstract Entity makeEntity(String id);

}
