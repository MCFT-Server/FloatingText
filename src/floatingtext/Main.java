package floatingtext;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import floatingtext.database.DataBase;
import floatingtext.listener.EventListener;
import floatingtext.manager.FloatingTextManager;

public class Main extends PluginBase {
	private EventListener listener;
	private DataBase db;
	private FloatingTextManager manager;
	
	@Override
	public void onEnable() {
		listener = new EventListener(this);
		db = new DataBase(this);
		manager = new FloatingTextManager(this);
		
		getServer().getPluginManager().registerEvents(listener, this);
	}
	
	@Override
	public void onDisable() {
		db.save();
	}
	
	public DataBase getDB() {
		return db;
	}
	
	public EventListener getListener() {
		return listener;
	}
	
	public FloatingTextManager getManager() {
		return manager;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		return listener.onCommand(sender, command, label, args);
	}
}
