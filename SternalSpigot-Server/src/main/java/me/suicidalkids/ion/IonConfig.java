package me.suicidalkids.ion;

import com.google.common.base.Throwables;
import me.suicidalkids.ion.visuals.commands.FlashingTNTCommand;
import me.suicidalkids.ion.visuals.commands.SandToggleCommand;
import me.suicidalkids.ion.visuals.commands.TNTToggleCommand;
import net.minecraft.server.MinecraftServer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.spigotmc.Metrics;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class IonConfig {

    private static File CONFIG_FILE;
    private static final String HEADER = "This is the main configuration file for Ion.\n" + "As you can see, there's tons to configure. Some options may impact gameplay, so use\n" + "with caution, and make sure you know what each option does before configuring.\n";
    /*========================================================================*/
    public static YamlConfiguration config;
    static int version;
    static Map<String, Command> commands;
    /*========================================================================*/

    public static void init(File configFile) {
        CONFIG_FILE = configFile;
        config = new YamlConfiguration();
        try {
            System.out.println("Loading Ion config from " + configFile.getName());
            config.load(CONFIG_FILE);
        } catch (IOException ex) {
        } catch (InvalidConfigurationException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not load ion.yml, please correct your syntax errors", ex);
            throw Throwables.propagate(ex);
        }
        config.options().header(HEADER);
        config.options().copyDefaults(true);

        commands = new HashMap<>();

        version = getInt("config-version", 1);
        set("config-version", 1);
        readConfig(IonConfig.class, null);
    }

    public static void registerCommands() {
        for (Map.Entry<String, Command> entry : commands.entrySet()) {
            MinecraftServer.getServer().server.getCommandMap().register( entry.getKey(), "Ion", entry.getValue() );
        }
    }

    private static void commands() {
        commands.put("tnttoggle", new TNTToggleCommand("tnttoggle"));
        commands.put("sandtoggle", new SandToggleCommand("sandtoggle"));
        commands.put("flashingtnt", new FlashingTNTCommand("flashingtnt"));
    }

    static void readConfig(Class<?> clazz, Object instance) {
        for (Method method : clazz.getDeclaredMethods()) {
            if (Modifier.isPrivate(method.getModifiers())) {
                if (method.getParameterTypes().length == 0 && method.getReturnType() == Void.TYPE) {
                    try {
                        method.setAccessible(true);
                        method.invoke(instance);
                    } catch (InvocationTargetException ex) {
                        throw Throwables.propagate(ex.getCause());
                    } catch (Exception ex) {
                        Bukkit.getLogger().log(Level.SEVERE, "Error invoking " + method, ex);
                    }
                }
            }
        }

        try {
            config.save(CONFIG_FILE);
        } catch (IOException ex) {
            Bukkit.getLogger().log(Level.SEVERE, "Could not save " + CONFIG_FILE, ex);
        }
    }

    private static void set(String path, Object val) {
        config.set(path, val);
    }

    private static <T> T getAndRemove(String path, T t) {
        Object obj = config.get(path, t);
        // Let's assume it's not what we're expecting
        if (t != null && obj instanceof MemorySection)
            return t;
        config.addDefault(path, null);
        config.set(path, null);
        return (T) obj;
    }

    private static boolean getBoolean(String path, boolean def) {
        config.addDefault(path, def);
        return config.getBoolean(path, config.getBoolean(path));
    }

    private static double getDouble(String path, double def) {
        config.addDefault(path, def);
        return config.getDouble(path, config.getDouble(path));
    }

    private static float getFloat(String path, float def) {
        config.addDefault(path, def);
        return config.getFloat(path, config.getFloat(path));
    }

    private static int getInt(String path, int def) {
        config.addDefault(path, def);
        return config.getInt(path, config.getInt(path));
    }

    private int getInterval(String path, int def) {
        return Math.max(getInt(path, def), 0) + 1;
    }

    private static <T> List getList(String path, T def) {
        config.addDefault(path, def);
        return (List<T>) config.getList(path, config.getList(path));
    }

    private static String getString(String path, String def) {
        config.addDefault(path, def);
        return config.getString(path, config.getString(path));
    }

    public static boolean usePandaWire;
    public static boolean optimisedGlowstone;
    private static void UsePandaWire() {
        boolean pandawire = getAndRemove("redstone.panda-wire", true);
        usePandaWire = getBoolean("redstone.panda-wire.enabled", pandawire);
        optimisedGlowstone = getBoolean("redstone.panda-wire.optimised", false);
    }

    public static int minSpawnDelay;
    public static int maxSpawnDelay;
    public static int spawnCount;
    public static int spawnRange;
    public static int maxNearbyEntities;
    public static int requiredPlayerRange;
    private static void Spawners() {
        minSpawnDelay = getInt("spawners.spawning.min-delay", 200);
        maxSpawnDelay = getInt("spawners.spawning.max-delay", 800);
        spawnCount = getInt("spawners.spawning.count", 4);
        spawnRange = getInt("spawners.spawning.range", 4);
        maxNearbyEntities = getInt("spawners.nearby.max-entities", 6);
        requiredPlayerRange = getInt("spawners.nearby.player-range", 16);
    }

}
