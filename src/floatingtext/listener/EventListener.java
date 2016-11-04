package floatingtext.listener;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerTeleportEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Position;
import cn.nukkit.utils.TextFormat;
import floatingtext.Main;
import floatingtext.manager.FloatingTextManager;

public class EventListener extends BaseListener<Main> {

	HashMap<String, String> queue;

	public EventListener(Main plugin) {
		super(plugin);
		queue = new HashMap<>();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) {
			sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.ingame"));
			return true;
		}
		if (args.length < 1) {
			return false;
		}
		queue.put(sender.getName(), String.join("", args));
		plugin.getDB().message(sender, "Touch the creation point FlotingText.");
		return true;
	}

	@EventHandler
	public void onTouch(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Position pos = event.getBlock();
		if (!queue.containsKey(player.getName()))
			return;
		FloatingTextManager.getInstance().addFloatingText(pos, queue.get(player.getName()));
		queue.remove(player.getName());
		plugin.getDB().message(player, "Add floatingtext success!");
	}

	@EventHandler
	public void onTelePort(PlayerTeleportEvent event) {
		if (event.getFrom().getLevel() != event.getTo().getLevel()) {
			FloatingTextManager.getInstance().spawnAllText(event.getPlayer());
		}
	}

	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		if (!player.hasPermission("floatingtext.remove"))
			return;
		Position pos = event.getBlock();
		if (FloatingTextManager.getInstance().exist(pos)) {
			FloatingTextManager.getInstance().removeFloatingText(pos);
			plugin.getDB().message(player, "Remove floatingtext success!");
		}
	}

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		FloatingTextManager.getInstance().spawnAllText(event.getPlayer());
	}
}
