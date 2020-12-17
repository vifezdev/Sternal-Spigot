package net.minecraft.server;

import org.bukkit.event.entity.ExplosionPrimeEvent; // CraftBukkit

public class EntityTNTPrimed extends Entity {

    public int fuseTicks;
    private EntityLiving source;
    public float yield = 4; // CraftBukkit - add field
    public boolean isIncendiary = false; // CraftBukkit - add field
    public org.bukkit.Location sourceLoc; // PaperSpigot

    // PaperSpigot start - TNT source location API
    public EntityTNTPrimed(World world) {
        this(null, world);
    }

    public EntityTNTPrimed(org.bukkit.Location loc, World world) {
        super(world);
        sourceLoc = loc;
    // PaperSpigot end
        this.k = true;
        this.setSize(0.98F, 0.98F);
        this.loadChunks = world.paperSpigotConfig.loadUnloadedTNTEntities; // PaperSpigot
    }

    public EntityTNTPrimed(org.bukkit.Location loc, World world, double d0, double d1, double d2, EntityLiving entityliving) {
        this(loc, world);
        this.setPosition(d0, d1, d2);
        float f = (float) (Math.random() * 3.1415927410125732D * 2.0D);

        this.motX = (double) (-((float) Math.sin((double) f)) * 0.02F);
        this.motY = 0.20000000298023224D;
        this.motZ = (double) (-((float) Math.cos((double) f)) * 0.02F);
        this.fuseTicks = 80;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
        this.source = entityliving;
        if (world.paperSpigotConfig.fixCannons) this.motX = this.motZ = 0.0F; // PaperSpigot - Fix cannons
    }

    protected void h() {}

    protected boolean s_() {
        return false;
    }

    public boolean ad() {
        return !this.dead;
    }

    public void t_() {
        if (world.spigotConfig.currentPrimedTnt++ > world.spigotConfig.maxTntTicksPerTick) { return; } // Spigot
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.motY -= 0.03999999910593033D;
        this.move(this.motX, this.motY, this.motZ);

        // PaperSpigot start - Drop TNT entities above the specified height
        if (this.world.paperSpigotConfig.tntEntityHeightNerf != 0 && this.locY > this.world.paperSpigotConfig.tntEntityHeightNerf) {
            this.die();
        }
        // PaperSpigot end

        // PaperSpigot start - Remove entities in unloaded chunks
        if (this.inUnloadedChunk && world.paperSpigotConfig.removeUnloadedTNTEntities) {
            this.die();
            this.fuseTicks = 2;
        }
        // PaperSpigot end

        this.motX *= 0.9800000190734863D;
        this.motY *= 0.9800000190734863D;
        this.motZ *= 0.9800000190734863D;
        if (this.onGround) {
            this.motX *= 0.699999988079071D;
            this.motZ *= 0.699999988079071D;
            this.motY *= -0.5D;
        }

        if (this.fuseTicks-- <= 0) {
            this.respawn(); // IonSpigot
            // CraftBukkit start - Need to reverse the order of the explosion and the entity death so we have a location for the event
            // this.die();
            if (!this.world.isClientSide) {
                this.explode();
            }
            this.die();
            // CraftBukkit end
        } else {
            this.W();
            this.world.addParticle(EnumParticle.SMOKE_NORMAL, this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D, new int[0]);
        }

    }

    private void explode() {
        // CraftBukkit start
        // float f = 4.0F;

        // PaperSpigot start - Force load chunks during TNT explosions
        ChunkProviderServer chunkProviderServer = ((ChunkProviderServer) world.chunkProvider);
        boolean forceChunkLoad = chunkProviderServer.forceChunkLoad;
        if (world.paperSpigotConfig.loadUnloadedTNTEntities) {
            chunkProviderServer.forceChunkLoad = true;
        }
        // PaperSpigot end

        org.bukkit.craftbukkit.CraftServer server = this.world.getServer();

        ExplosionPrimeEvent event = new ExplosionPrimeEvent((org.bukkit.entity.Explosive) org.bukkit.craftbukkit.entity.CraftEntity.getEntity(server, this));
        server.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            this.world.createExplosion(this, this.locX, this.locY + (double) (this.length / 2.0F), this.locZ, event.getRadius(), event.getFire(), true);
        }
        // CraftBukkit end

        // PaperSpigot start - Force load chunks during TNT explosions
        if (world.paperSpigotConfig.loadUnloadedTNTEntities) {
            chunkProviderServer.forceChunkLoad = forceChunkLoad;
        }
        // PaperSpigot end
    }
    // IonSpigot start - Merge Cannoning Entities
    @Override
    public boolean merge(Entity entity) {
        return world.ionConfig.tntMerging && entity.ticksLived == ticksLived + 1
            && entity.lastX == locX && entity.lastY == locY && entity.lastZ == locZ
            && entity.lastMotX == motX && entity.lastMotY == motY - 0.03999999910593033D
            && entity.lastMotZ == motZ && entity.getClass() == getClass()
            && ((EntityTNTPrimed) entity).fuseTicks == fuseTicks - 1 && fuseTicks > 0;
    }

    @Override
    protected void respawn() {
        // If this hasn't been merged just skip this process, no reason for it.
        if (this.potential <= 1) {
            return;
        }

        /*
         * We're going to use this for keeping track of the motion from the
         * explosion then applying that to this entity and exploding it again...
         * and again. This is not the most efficient method but it'll preserve
         * all of this entities metadata, and much better for plugins.
         */
        EntityTNTPrimed temp = new EntityTNTPrimed(new org.bukkit.Location(
                this.world.getWorld(), this.lastX, this.lastY, this.lastZ),
                this.world, this.lastX, this.lastY, this.lastZ, this.source);

        // Update properties
        temp.motX = this.lastMotX;
        temp.motY = this.lastMotY + 0.03999999910593033D; // Add gravity back
        temp.motZ = this.lastMotZ;
        temp.recalcPosition();

        // Add "temp" to the chunk
        Chunk chunk = world.getChunkAtWorldCoords(new BlockPosition(temp));
        chunk.a(temp);

        // We should only explode this TNT, otherwise metadata would have to be transferred.
        for (int i = 1; i < this.potential; ++i) {
            // Explode!
            this.explode();

            // This should be fine, it'll reassign during ticking.
            this.locX = this.lastX;
            this.locY = this.lastY;
            this.locZ = this.lastZ;
            // Ok, now apply the motion
            this.motX = temp.motX;
            this.motY = temp.motY;
            this.motZ = temp.motZ;
            // Update bounding box
            this.a(temp.getBoundingBox());
            // So it doesn't explode during ticking
            this.fuseTicks = 1;

            // Now, tick this entity
            this.t_();
        }

        // Remove "temp" from the chunk
        chunk.b(temp);
    }
    // IonSpigot end

    protected void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Fuse", (byte) this.fuseTicks);
        // PaperSpigot start - TNT source location API
        if (sourceLoc != null) {
            nbttagcompound.setInt("SourceLoc_x", sourceLoc.getBlockX());
            nbttagcompound.setInt("SourceLoc_y", sourceLoc.getBlockY());
            nbttagcompound.setInt("SourceLoc_z", sourceLoc.getBlockZ());
        }
        // PaperSpigot end
    }

    protected void a(NBTTagCompound nbttagcompound) {
        this.fuseTicks = nbttagcompound.getByte("Fuse");
        // PaperSpigot start - TNT source location API
        if (nbttagcompound.hasKey("SourceLoc_x")) {
            int srcX = nbttagcompound.getInt("SourceLoc_x");
            int srcY = nbttagcompound.getInt("SourceLoc_y");
            int srcZ = nbttagcompound.getInt("SourceLoc_z");
            sourceLoc = new org.bukkit.Location(world.getWorld(), srcX, srcY, srcZ);
        }
        // PaperSpigot end
    }

    public EntityLiving getSource() {
        return this.source;
    }

    // PaperSpigot start - Fix cannons
    @Override
    public double f(double d0, double d1, double d2) {
        if (!world.paperSpigotConfig.fixCannons) return super.f(d0, d1, d2);

        double d3 = this.locX - d0;
        double d4 = this.locY + this.getHeadHeight() - d1;
        double d5 = this.locZ - d2;

        return (double) MathHelper.sqrt(d3 * d3 + d4 * d4 + d5 * d5);
    }

    @Override
    public boolean aL() {
        return !world.paperSpigotConfig.fixCannons && super.aL();
    }

    @Override
    public float getHeadHeight() {
        return world.paperSpigotConfig.fixCannons ? this.length / 2 : 0.0F;
    }

    /**
     * Author: Jedediah Smith <jedediah@silencegreys.com>
     */
    @Override
    public boolean W() {
        if (!world.paperSpigotConfig.fixCannons) return super.W();

        // IonSpigot start - Optimise TNT Ticking
        /*
        // Preserve velocity while calling the super method
        double oldMotX = this.motX;
        double oldMotY = this.motY;
        double oldMotZ = this.motZ;

        super.W();

        this.motX = oldMotX;
        this.motY = oldMotY;
        this.motZ = oldMotZ;

        if (this.inWater) {
            // Send position and velocity updates to nearby players on every tick while the TNT is in water.
            // This does pretty well at keeping their clients in sync with the server.
            EntityTrackerEntry ete = ((WorldServer) this.getWorld()).getTracker().trackedEntities.get(this.getId());
            if (ete != null) {
                PacketPlayOutEntityVelocity velocityPacket = new PacketPlayOutEntityVelocity(this);
                PacketPlayOutEntityTeleport positionPacket = new PacketPlayOutEntityTeleport(this);

                for (EntityPlayer viewer : ete.trackedPlayers) {
                    if ((viewer.locX - this.locX) * (viewer.locY - this.locY) * (viewer.locZ - this.locZ) < 16 * 16) {
                        viewer.playerConnection.sendPacket(velocityPacket);
                        viewer.playerConnection.sendPacket(positionPacket);
                    }
                }
            }
        }
        */
        // IonSpigot end

        return this.inWater;
    }
    // PaperSpigot end
}
