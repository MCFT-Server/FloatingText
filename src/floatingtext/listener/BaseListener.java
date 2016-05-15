package floatingtext.listener;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;

abstract class BaseListener<T extends PluginBase> implements Listener {
	protected T plugin;
	
	BaseListener(T plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) { return true; }
}
