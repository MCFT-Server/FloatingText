package floatingtext.database;

import java.util.LinkedHashMap;

import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.ConfigSection;
import cn.nukkit.utils.TextFormat;

abstract class BaseDB<T extends PluginBase> {
	protected T plugin;
	private LinkedHashMap<String, Config> dblist;
	private Config messages;
	private String prefix;
	private static final int m_version = 1;
	
	BaseDB(T plugin) {
		this.plugin = plugin;
		plugin.getDataFolder().mkdirs();
		dblist = new LinkedHashMap<>();
	}
	
	protected void initDB(String name, String file, int type) {
		initDB(name, file, type, new ConfigSection());
	}
	protected void initDB(String name, String file, int type, ConfigSection defaultmap) {
		dblist.put(name, new Config(file, type, defaultmap));
	}
	
	public Config getDB(String name) {
		return dblist.get(name);
	}
	
	protected void initMessage() {
		plugin.saveResource("messages.yml");
		messages = new Config("messages.yml", Config.YAML);
		updateMessage();
		prefix = get("default-prefix");
	}
	private void updateMessage() {
		if (dblist.get("messages").getInt("m_version", 1) < m_version) {
			plugin.saveResource("messages.yml", true);
			messages = new Config("messages.yml", Config.YAML);
		}
	}
	protected void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String get(String str) {
		return messages.getString(messages.getString("default-language") + "-" + str);
	}
	
	public void alert(CommandSender player, String message) {
		player.sendMessage(TextFormat.RED + prefix + " " + message);
	}
	public void message(CommandSender player, String message) {
		player.sendMessage(TextFormat.DARK_AQUA + prefix + " " + message);
	}
	
	public void save() {
		save(false);
	}
	public void save(boolean async) {
		for (Config db : dblist.values()) {
			db.save(async);
		}
	}
}
