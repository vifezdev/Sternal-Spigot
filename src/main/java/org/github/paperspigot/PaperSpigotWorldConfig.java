package org.github.paperspigot;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class PaperSpigotWorldConfig
{

    private final String worldName;
    private final YamlConfiguration config;
    private boolean verbose;

    public PaperSpigotWorldConfig(String worldName)
    {
        this.worldName = worldName;
        this.config = PaperSpigotConfig.config;
        init();
    }

    public void init()
    {
        this.verbose = getBoolean( "verbose", true );

        log( "-------- World Settings For [" + worldName + "] --------" );
        PaperSpigotConfig.readConfig( PaperSpigotWorldConfig.class, this );
    }

    private void log(String s)
    {
        if ( verbose )
        {
            Bukkit.getLogger().info( s );
        }
    }

    private void set(String path, Object val)
    {
        config.set( "world-settings.default." + path, val );
    }

    private boolean getBoolean(String path, boolean def)
    {
        config.addDefault( "world-settings.default." + path, def );
        return config.getBoolean( "world-settings." + worldName + "." + path, config.getBoolean( "world-settings.default." + path ) );
    }

    private double getDouble(String path, double def)
    {
        config.addDefault( "world-settings.default." + path, def );
        return config.getDouble( "world-settings." + worldName + "." + path, config.getDouble( "world-settings.default." + path ) );
    }

    private int getInt(String path, int def)
    {
        config.addDefault( "world-settings.default." + path, def );
        return config.getInt( "world-settings." + worldName + "." + path, config.getInt( "world-settings.default." + path ) );
    }

    private float getFloat(String path, float def)
    {
        // TODO: Figure out why getFloat() always returns the default value.
        return (float) getDouble( path, (double) def );
    }

    private <T> List getList(String path, T def)
    {
        config.addDefault( "world-settings.default." + path, def );
        return (List<T>) config.getList( "world-settings." + worldName + "." + path, config.getList( "world-settings.default." + path ) );
    }

    private String getString(String path, String def)
    {
        config.addDefault( "world-settings.default." + path, def );
        return config.getString( "world-settings." + worldName + "." + path, config.getString( "world-settings.default." + path ) );
    }

    public boolean allowUndeadHorseLeashing;
    private void allowUndeadHorseLeashing()
    {
        allowUndeadHorseLeashing = getBoolean( "allow-undead-horse-leashing", false );
        log( "Allow undead horse types to be leashed: " + allowUndeadHorseLeashing );
    }

    public double squidMinSpawnHeight;
    public double squidMaxSpawnHeight;
    private void squidSpawnHeight()
    {
        squidMinSpawnHeight = getDouble( "squid-spawn-height.minimum", 45.0D );
        squidMaxSpawnHeight = getDouble( "squid-spawn-height.maximum", 63.0D );
        log( "Squids will spawn between Y: " + squidMinSpawnHeight + " and Y: " + squidMaxSpawnHeight );
    }

    public float playerBlockingDamageMultiplier;
    private void playerBlockingDamageMultiplier()
    {
        playerBlockingDamageMultiplier = getFloat( "player-blocking-damage-multiplier", 0.5F );
        log( "Player blocking damage multiplier set to " + playerBlockingDamageMultiplier );
    }

    public int cactusMaxHeight;
    public int reedMaxHeight;
    private void blockGrowthHeight()
    {
        cactusMaxHeight = getInt( "max-growth-height.cactus", 3 );
        reedMaxHeight = getInt( "max-growth-height.reeds", 3 );
        log( "Max height for cactus growth " + cactusMaxHeight + ". Max height for reed growth " + reedMaxHeight );
    }

    public int fishingMinTicks;
    public int fishingMaxTicks;
    private void fishingTickRange()
    {
        fishingMinTicks = getInt( "fishing-time-range.MinimumTicks", 100 );
        fishingMaxTicks = getInt( "fishing-time-range.MaximumTicks", 900 );
    }

    public float blockBreakExhaustion;
    public float playerSwimmingExhaustion;
    private void exhaustionValues()
    {
        blockBreakExhaustion = getFloat( "player-exhaustion.block-break", 0.025F );
        playerSwimmingExhaustion = getFloat( "player-exhaustion.swimming", 0.015F );
    }

    public int softDespawnDistance;
    public int hardDespawnDistance;
    private void despawnDistances()
    {
        softDespawnDistance = getInt( "despawn-ranges.soft", 32 ); // 32^2 = 1024, Minecraft Default
        hardDespawnDistance = getInt( "despawn-ranges.hard", 128 ); // 128^2 = 16384, Minecraft Default;

        if ( softDespawnDistance > hardDespawnDistance ) {
            softDespawnDistance = hardDespawnDistance;
        }

        log( "Living Entity Despawn Ranges:  Soft: " + softDespawnDistance + " Hard: " + hardDespawnDistance );

        softDespawnDistance = softDespawnDistance*softDespawnDistance;
        hardDespawnDistance = hardDespawnDistance*hardDespawnDistance;
    }
}
