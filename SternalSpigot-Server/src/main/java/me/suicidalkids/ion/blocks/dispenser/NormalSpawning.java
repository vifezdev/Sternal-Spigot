package me.suicidalkids.ion.blocks.dispenser;

import net.minecraft.server.Entity;

import java.util.ArrayDeque;
import java.util.Queue;

public class NormalSpawning implements DispenserOrder {

    private final Queue<Entity> queuedSpawns = new ArrayDeque<>();

    @Override
    public void queueSpawn(Entity entity) {
        queuedSpawns.add(entity);
    }

    @Override
    public void dispatch() {
        for (Entity entity; (entity = queuedSpawns.poll()) != null;) {
            addEntity(entity);
        }
    }

}
