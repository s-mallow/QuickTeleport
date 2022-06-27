package me.smallow.quickteleport.commands;

import org.bukkit.entity.Player;

public interface SubCommand {

    void execute(Player p, String[] args);
}
