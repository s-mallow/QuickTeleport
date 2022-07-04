package me.smallow.quickwarp.commands;

import me.smallow.quickwarp.core.WarpManager;
import org.bukkit.entity.Player;

public abstract class SubCommand {

    protected WarpManager warpManager;

    public SubCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    public abstract void execute(Player p, String label, String[] args);
}
