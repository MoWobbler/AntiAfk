package net.simpvp.AntiAfk;

import java.util.Iterator;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class KickPlayer implements CommandExecutor{
	private static long last_request = System.currentTimeMillis();
	
	public boolean onCommand(CommandSender sender,
			Command cmd,
			String label,
			String[] args) {

		if (cmd.getName().equals("afk")) {
			im_on(sender);
		}

		return true;
	}
	

	/* Similiar function to the online check one */
	public static void afk_check() {
		
		if (System.currentTimeMillis() < (AntiAfk.online_check_seconds * 1000) + last_request) {
			return;
		}
		
		last_request = System.currentTimeMillis();

		TextComponent msg = new TextComponent("[Announcement] Please verify that you're online by typing ");
		msg.setColor(ChatColor.AQUA);
		ClickEvent click = new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/afk");
		msg.setClickEvent(click);
		TextComponent msg2 = new TextComponent("/afk");
		msg2.setColor(ChatColor.GOLD);
		msg.addExtra(msg2);
		
		for (Player p : GetAfkPlayers.afkPlayers) {
			p.spigot().sendMessage(msg);
			p.sendTitle(ChatColor.GOLD + "/afk", "Please verify that you're online", 10, 550, 20);
		}
		
		AntiAfk.instance.getLogger().info("Preparing to kick players for afking in low tps");
		
		/* Activate in 20 seconds */
		final long activate_at = System.currentTimeMillis() + 20 * 1000;
		new BukkitRunnable() {
			@Override
			public void run() {
				if (System.currentTimeMillis() < activate_at) {
					return;
				}

				this.cancel();
				new BukkitRunnable() {
					@Override
					public void run() {
						Player p;
				 		for (Iterator<?> afkListIterator = AntiAfk.instance.getServer().getOnlinePlayers().iterator(); afkListIterator.hasNext();) {
				 			p = (Player) afkListIterator.next();
				 			if (GetAfkPlayers.isAfk(p)) {
				 				AntiAfk.instance.getLogger().info("Kicking players for afking in low tps");
				 				p.kickPlayer(ChatColor.RED + "Kicked for being afk in low tps");
				 			}
				 		}
					}
				}.runTaskLater(AntiAfk.instance, 0);
			}
		}.runTaskTimerAsynchronously(AntiAfk.instance, 20L, 20L);
	}
	
	private void im_on(CommandSender sender) {
		Player player = null;
		if (sender instanceof Player) {
			player = (Player) sender;
		} else {
			error_log("Only players can use this command.", sender);
			return;
		}

		if (GetAfkPlayers.isAfk(player)) {
		    GetAfkPlayers.afkPlayers.remove(player);
		    GetAfkPlayers.playerLastMoveTime.put(player, System.currentTimeMillis());
		    GetAfkPlayers.playerLastLocation.put(player, player.getLocation());
			player.sendMessage(ChatColor.GREEN + "Thank you!");
			player.resetTitle();
		} else {
			player.sendMessage(ChatColor.GREEN + "You're good.");
		}
	}
	
	private void error_log(String msg, CommandSender sender) {
		if (sender instanceof Player) {
			((Player) sender).sendMessage(ChatColor.RED + msg);
		} else {
			sender.sendMessage(msg);
		}
		if (!(sender instanceof ConsoleCommandSender)) {
			AntiAfk.instance.getLogger().info("OnlineCheck error: " + msg);
		}
    }
}
