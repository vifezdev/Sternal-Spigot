package me.suicidalkids.ion.visuals.commands;

import org.bukkit.command.Command;

public abstract class BaseCommand extends Command {

    public BaseCommand(String name) {
        super(name);
        this.description = "Ion Command " + name;
        this.setPermission("bukkit.command." + name);
    }

}
