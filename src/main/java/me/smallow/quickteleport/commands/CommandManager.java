package me.smallow.quickteleport.commands;

import me.smallow.quickteleport.commands.subs.AddCommand;
import me.smallow.quickteleport.commands.subs.RelocateCommand;
import me.smallow.quickteleport.commands.subs.RemoveCommand;
import me.smallow.quickteleport.commands.subs.RenameCommand;
import me.smallow.quickteleport.commands.subs.UseCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Map;

public class CommandManager implements CommandExecutor {

    private Map<String, SubCommand> commandHandlers;

    public CommandManager(){
        commandHandlers = Map.ofEntries(
                Map.entry("use", new UseCommand()),
                Map.entry("add", new AddCommand()),
                Map.entry("remove", new RemoveCommand()),
                Map.entry("rename", new RenameCommand()),
                Map.entry("relocate", new RelocateCommand())
        );
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player p)) {
            sender.sendMessage(ChatColor.RED + "Only players can use this command");
            return true;
        }
        if (args.length > 0){
            if (commandHandlers.containsKey(args[0]) && p.hasPermission("quickteleport." + args[0])){
                commandHandlers.get(args[0]).execute(p, Arrays.copyOfRange(args, 1, args.length));
                return true;
            }
        }
        if(p.hasPermission("quickteleport.use")){
            commandHandlers.get("use").execute(p,
                    args.length > 0 ? Arrays.copyOfRange(args, 1, args.length) : args);
        }
        return true;
    }
}
