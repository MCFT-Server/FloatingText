package floatingtext.listener;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.level.LevelLoadEvent;
import cn.nukkit.event.player.PlayerInteractEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.lang.TranslationContainer;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.utils.TextFormat;
import floatingtext.Main;

public class EventListener extends BaseListener<Main> {
	
	HashMap<String, String> queue;

	public EventListener(Main plugin) {
		super(plugin);
		queue = new HashMap<>();
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (! (sender instanceof Player)) {
			sender.sendMessage(new TranslationContainer(TextFormat.RED + "%commands.generic.ingame"));
			return true;
		}
		if (args.length < 1) {
			return false;
		}
		queue.put(sender.getName(), join(" ", args));
		plugin.getDB().message(sender, "Touch the creation point FlotingText.");
		return true;
	}
	
	private String join(String glue, String[] strs) {
		String string = "";
		for (String str : strs) {
			string += glue + str;
		}
		return string.substring(1);
	}
	
	@EventHandler
	public void onTouch(PlayerInteractEvent event) {
		Player player = event.getPlayer();
		Position pos = event.getBlock();
		if (!queue.containsKey(player.getName())) return;
		plugin.getDB().getDB("textlist").set(posToString(pos), queue.get(player.getName()));
		event.getBlock().getLevel().addParticle(new FloatingTextParticle(new Position(pos.x + 0.5, pos.y, pos.z + 0.5, pos.level), "", queue.get(player.getName()).replace("\\n", "\n")));
		queue.remove(player.getName());
		plugin.getDB().message(player, "Add floatingtext success!");
	}
	
	private String posToString(Position pos) {
		return (int)pos.x + ":" + (int)pos.y + ":" + (int)pos.z + ":" + pos.getLevel().getFolderName();
	}
	
	@EventHandler
	public void onBreak(BlockBreakEvent event) {
		Player player = event.getPlayer();
		Position pos = event.getBlock();
		if (plugin.getDB().getDB("textlist").exists(posToString(pos))) {
			plugin.getDB().getDB("textlist").remove(posToString(pos));
			FloatingTextParticle particle = new FloatingTextParticle(new Position(pos.x + 0.5, pos.y + 1, pos.z + 0.5, pos.level), "");
			particle.setInvisible();
			plugin.getDB().message(player, "Remove floating text.");
		}
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		plugin.getDB().initText(player);
	}
	
	@EventHandler
	public void onLevelLoad(LevelLoadEvent event) {
		for (Player player : plugin.getServer().getOnlinePlayers().values()) {
			plugin.getDB().initText(player);
		}
	}
}
