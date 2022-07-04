package me.smallow.quickwarp.commands;

import me.smallow.quickwarp.QuickWarp;
import me.smallow.quickwarp.core.WarpManager;
import me.smallow.quickwarp.commands.subs.AddCommand;
import me.smallow.quickwarp.commands.subs.RelocateCommand;
import me.smallow.quickwarp.commands.subs.RemoveCommand;
import me.smallow.quickwarp.commands.subs.RenameCommand;
import me.smallow.quickwarp.commands.subs.SetItemCommand;
import me.smallow.quickwarp.commands.subs.UseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;

public class CommandManager implements CommandExecutor {

    private Map<String, SubCommand> commandHandlers;

    public CommandManager(WarpManager warpManager, QuickWarp plugin){
        commandHandlers = Map.ofEntries(
                Map.entry("use", new UseCommand(warpManager, plugin)),
                Map.entry("add", new AddCommand(warpManager, plugin)),
                Map.entry("remove", new RemoveCommand(warpManager, plugin)),
                Map.entry("rename", new RenameCommand(warpManager, plugin)),
                Map.entry("relocate", new RelocateCommand(warpManager, plugin)),
                Map.entry("setitem", new SetItemCommand(warpManager, plugin))
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return true;
        }
        if (args.length > 0){
            String subc = args[0].toLowerCase();
            if (commandHandlers.containsKey(subc) && p.hasPermission("quickwarp." + args[0])){
                commandHandlers.get(subc).execute(p, label, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
        }
        if(p.hasPermission("quickwarp.use")){
            commandHandlers.get("use").execute(p, label,
                    args.length > 0 ? Arrays.copyOfRange(args, 1, args.length) : args);
        }
        return true;
    }
}
