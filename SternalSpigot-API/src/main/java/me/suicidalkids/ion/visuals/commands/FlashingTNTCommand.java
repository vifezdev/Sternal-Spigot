package me.suicidalkids.ion.visuals.commands;

import me.suicidalkids.ion.visuals.Visuals;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlashingTNTCommand extends BaseCommand {

    public FlashingTNTCommand(String name) {
        super(name);
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender) || !(sender instanceof Player)) {
            return true;
        }

        Player player = (Player) sender;
        Visuals visuals = player.getVisuals();

        visuals.toggle(Visuals.VisualType.FLASHING_TNT);

        String action = "Disabled";
        if (visuals.isEnabled(Visuals.VisualType.FLASHING_TNT)) {
            action = "Enabled";
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format("&8(&7!&8) &f%s Flashing TNT!", action)));
        return true;
    }

}
