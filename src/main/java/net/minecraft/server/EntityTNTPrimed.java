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
        
        org.bukkit.craftbukkit.CraftServer server = this.world.getServer();

        ExplosionPrimeEvent event = new ExplosionPrimeEvent((org.bukkit.entity.Explosive) org.bukkit.craftbukkit.entity.CraftEntity.getEntity(server, this));
        server.getPluginManager().callEvent(event);

        if (!event.isCancelled()) {
            this.world.createExplosion(this, this.locX, this.locY + (double) (this.length / 2.0F), this.locZ, event.getRadius(), event.getFire(), true);
        }
        // CraftBukkit end
    }

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

    public float getHeadHeight() {
        return 0.0F;
    }
}
