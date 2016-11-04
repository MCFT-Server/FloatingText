/*
The MIT License
Copyright (c) <2016> <wsj7178@naver.com>
Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:
The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.

version: 1
*/
package floatingtext.database;

import java.io.File;
import java.util.LinkedHashMap;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.command.SimpleCommandMap;
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
		dblist = new LinkedHashMap<String, Config>();
	}
	
	protected void initDB(String name, File file, int type) {
		initDB(name, file.toString(), type);
	}
	protected void initDB(String name, File file, int type, ConfigSection defaultMap) {
		initDB(name, file.toString(), type, defaultMap);
	}
	protected void initDB(String name, String file, int type) {
		initDB(name, file, type, new ConfigSection());
	}
	protected void initDB(String name, String file, int type, ConfigSection defaultMap) {
		dblist.put(name, new Config(file, type, defaultMap));
	}
	
	public Config getConfig() {
		return plugin.getConfig();
	}
	
	public void saveConfig() {
		plugin.saveConfig();
	}
	
	public void saveDefaultConfig() {
		saveDefaultConfig(false);
	}
	
	public void saveDefaultConfig(boolean replace) {
		plugin.saveResource("config.yml", replace);
	}
	
	public void reloadConfig() {
		plugin.reloadConfig();
	}
	
	public Config getDB(String name) {
		return dblist.get(name);
	}
	
	protected void initMessage() {
		plugin.saveResource("messages.yml");
		messages = new Config(plugin.getDataFolder().getPath() + "/messages.yml", Config.YAML);
		updateMessage();
		prefix = get("default-prefix");
	}
	private void updateMessage() {
		if (messages.getInt("m_version", 1) < m_version) {
			plugin.saveResource("messages.yml", true);
			messages = new Config(plugin.getDataFolder().getPath() + "/messages.yml", Config.YAML);
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
	
	public void registerCommand(String name, String description, String usage, String permission) {
		SimpleCommandMap map = plugin.getServer().getCommandMap();
		PluginCommand<T> cmd = new PluginCommand<T>(name, plugin);
		cmd.setDescription(description);
		cmd.setUsage(usage);
		cmd.setPermission(permission);
		map.register(name, cmd);
	}
}
