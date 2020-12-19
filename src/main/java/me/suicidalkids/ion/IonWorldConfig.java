package me.suicidalkids.ion;

import net.minecraft.server.World;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

public class IonWorldConfig {

    private final String worldName;
    private final YamlConfiguration config;
    private final World world;
    private boolean verbose;

    public IonWorldConfig(World world, String worldName) {
        this.worldName = worldName;
        this.config = IonConfig.config;
        this.world = world;
        init();
    }

    public void init() {
        this.verbose = getBoolean("verbose", true);

        log("-------- World Settings For [" + worldName + "] --------");
        IonConfig.readConfig(IonWorldConfig.class, this);
    }

    private void log(String s) {
        if (verbose) {
            Bukkit.getLogger().info(s);
        }
    }

    private void set(String path, Object val) {
        config.set(path, val);
    }

    private boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean("world-settings." + worldName + "." + path, config.getBoolean(path));
    }

    private double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble("world-settings." + worldName + "." + path, config.getDouble(path));
    }

    private int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt("world-settings." + worldName + "." + path, config.getInt(path));
    }

    private int getInterval(String path, int def) {
        return Math.max(getInt(path, def), 0) + 1;
    }

    private float getFloat(String path, float def) {
        config.addDefault(path, def);
        return config.getFloat("world-settings." + worldName + "." + path, config.getFloat(path));
    }

    private <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return (List<T>) config.getList("world-settings." + worldName + "." + path, config.getList(path));
    }

    private String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString("world-settings." + worldName + "." + path, config.getString(path));
    }

    public boolean constantExplosions;
    private void ConstantExplosions() {
        constantExplosions = getBoolean("explosions.constant-radius", false);
    }

    public boolean explosionProtectedRegions;
    private void ExplosionProtectedRegions() {
        explosionProtectedRegions = getBoolean("explosions.protected-regions", true);
    }

    public boolean fixSandUnloading;
    private void FixSandUnloading() {
        fixSandUnloading = getBoolean("sand.fix-unloading", false);
    }

    public boolean movementCache;
    private void MovementCache() {
        movementCache = getBoolean("movement.cache", true);
    }

    public boolean requirePlayers;
    public boolean checkMaxEntities;
    public boolean checkConditions;
    private void Spawners() {
        checkConditions = getBoolean("spawners.spawning.check-conditions", true);
        checkMaxEntities = getBoolean("spawners.nearby.check-max-entities", true);
        requirePlayers = getBoolean("spawners.nearby.require-players", true);
    }

    public boolean footstepSounds;
    public boolean silenceSounds;
    private void Sounds() {
        footstepSounds = getBoolean("footstep-sounds", true);
        silenceSounds = getBoolean("silence-sounds", false);
    }

    public int noclipInterval;
    public int mergeInterval;
    public int hopperInterval;
    private void Items() {
        noclipInterval = getInterval("items.noclip-delay", 0);
        mergeInterval = getInterval("items.merge-delay", 0);
        hopperInterval = getInterval("items.hoppers.delay", 0);
    }

    public boolean mergeSpawnedItems;
    private void MergeSpawnedItems() {
        mergeSpawnedItems = getBoolean("items.merged-spawned-items", false);
    }

    public boolean nerfSpawnedLogic;
    public boolean nerfNaturalSpawns;
    private void NerfSpawnedLogic() {
        nerfSpawnedLogic = getBoolean("spawners.nerf-spawned-logic", false);
        nerfNaturalSpawns = getBoolean("nerf-natural-spawns", false);
    }

    public boolean movementEntityCollisions;
    private void MovementEntityCollisions() {
        movementEntityCollisions = getBoolean("movement.entity-collisions", true);
    }

    public boolean lagCompensatedPotions;
    private void LagCompensatedPotions() {
        lagCompensatedPotions = getBoolean("potions.lag-compensated", true);
    }

    public boolean smoothPotting;
    private void SmoothPotting() {
        smoothPotting = getBoolean("potions.smooth", true);
    }

    public boolean pearlThroughGates;
    public boolean pearlThroughString;
    public boolean pearlThroughCobwebs;
    private void PearlThrough() {
        pearlThroughGates = getBoolean("pearls.through.fence-gates", true);
        pearlThroughString = getBoolean("pearls.through.string", false);
        pearlThroughCobwebs = getBoolean("pearls.through.cobwebs", false);
    }

    public boolean tntMerging;
    public boolean sandMerging;
    private void Merging() {
        tntMerging = getBoolean("tnt.merging", true);
        sandMerging = getBoolean("sand.merging", true);
    }

    public int potionTime;
    public float potionSpeed;
    public float potionGravity;
    public float potionVerticalOffset;
    private void Potions() {
        potionTime = getInt("potions.time", 5);
        potionGravity = getFloat("potions.gravity", 0.05F);
        potionSpeed = getFloat("potions.speed", 0.5F);
        potionVerticalOffset = getFloat("potions.vertical-offset", -20.0F);
    }

    public boolean hoppersCheckPower;
    public boolean hoppersAlwaysAcceptItems;
    private void Hoppers() {
        hoppersCheckPower = getBoolean("hoppers.check-power", true);
        hoppersAlwaysAcceptItems = getBoolean("hoppers.always-accept-items", false);
    }

    public boolean lagCompensatedPearls;
    private void LagCompensatedPearls() {
        lagCompensatedPearls = getBoolean("pearls.lag-compensated", true);
    }

}
