package me.suicidalkids.ion.blocks.dispenser;

import net.minecraft.server.Entity;

public interface DispenserOrder {

    void queueSpawn(Entity entity);

    void dispatch();

    default void addEntity(Entity entity) {
        entity.world.addEntity(entity);
        entity.world.makeSound(entity, "game.tnt.primed", 1.0F, 1.0F);
    }

}
