package net.simpvp.AntiAfk;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AntiAfkCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player)
            player = (Player) sender;

        if (player == null) {
            return true;
        }

        if (!player.isOp()) {
            player.sendMessage(ChatColor.RED + "You do not have permission to use this command");
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(ChatColor.RED + "Correct usage: /antiafk <add/remove> <player>");
            return true;
        }

        if (!(args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove"))) {
            player.sendMessage(ChatColor.RED + "Correct usage: /antiafk <add/remove> <player>");
            return true;
        }

        Player afker = Bukkit.getServer().getPlayer(args[1]);

        if (afker == null) {
            player.sendMessage(ChatColor.RED + args[1] + " is not online");
            return true;
        }

        if (args[0].equalsIgnoreCase("add") ) {
            addPlayerToAfkList(player, afker);
            return true;
        }

        if (args[0].equalsIgnoreCase("remove")) {
            removePlayerFromAfkList(player, afker);
            return true;
        }
        return true;
    }


    /* Add to the list of players we check */
    private void addPlayerToAfkList(Player sender, Player afker) {
        UUID uuid = afker.getUniqueId();

        if (GetAfkPlayers.kick_players.contains(uuid.toString())) {
            sender.sendMessage(ChatColor.RED + afker.getDisplayName() + " is already in the afk list!");
            return;
        }

        GetAfkPlayers.kick_players.add(uuid.toString());
        AntiAfk.instance.reloadConfig();
        AntiAfk.instance.getConfig().set("Kick_players", GetAfkPlayers.kick_players);
        AntiAfk.instance.saveConfig();
        sender.sendMessage(ChatColor.GREEN + afker.getDisplayName() + " has been added to the afk list");
        AntiAfk.instance.getLogger().info(afker.getDisplayName() + " has been added to the afk list");
    }


    /* Remove from the list of players we check */
    private void removePlayerFromAfkList(Player sender, Player afker) {
        UUID uuid = afker.getUniqueId();

        if (!GetAfkPlayers.kick_players.contains(uuid.toString())) {
            sender.sendMessage(ChatColor.RED + afker.getDisplayName() + " is not in the afk list!");
            return;
        }

        KickPlayer.exemptPlayers.add(afker);
        GetAfkPlayers.playerLocations.remove(uuid);
        GetAfkPlayers.playerTimes.remove(uuid);
        GetAfkPlayers.kick_players.remove(uuid.toString());
        AntiAfk.instance.reloadConfig();
        AntiAfk.instance.getConfig().set("Kick_players", GetAfkPlayers.kick_players);
        AntiAfk.instance.saveConfig();
        sender.sendMessage(ChatColor.GREEN + afker.getDisplayName() + " has been removed from the afk list");
        AntiAfk.instance.getLogger().info(afker.getDisplayName() + " has been removed from the afk list");
    }
}
