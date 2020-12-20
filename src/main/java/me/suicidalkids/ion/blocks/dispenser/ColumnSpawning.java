package me.suicidalkids.ion.blocks.dispenser;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.server.Entity;
import org.bukkit.craftbukkit.util.LongHash;
import org.bukkit.util.NumberConversions;

import java.util.ArrayDeque;
import java.util.Queue;

public class ColumnSpawning implements DispenserOrder {

    private final Long2ObjectMap<Queue<Entity>> queuedSpawns = new Long2ObjectOpenHashMap<>();

    @Override
    public void queueSpawn(Entity entity) {
        int floorX = NumberConversions.floor(entity.locX);
        int floorZ = NumberConversions.floor(entity.locZ);
        long key = LongHash.toLong(floorX, floorZ);

        Queue<Entity> queue = queuedSpawns.get(key);
        if (queue == null) {
            queue = new ArrayDeque<>();
            queuedSpawns.put(key, queue);
        }

        queue.add(entity);
    }

    @Override
    public void dispatch() {
        for (Queue<Entity> queue : queuedSpawns.values()) {
            for (Entity entity; (entity = queue.poll()) != null; ) {
                addEntity(entity);
            }
        }

        queuedSpawns.clear();
    }

}
