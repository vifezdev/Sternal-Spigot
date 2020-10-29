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

}
