package floatingtext.database;

import cn.nukkit.utils.Config;
import floatingtext.Main;

public class DataBase extends BaseDB<Main> {

	public DataBase(Main plugin) {
		super(plugin);
		setPrefix("[FloatingText]");
		initDB("textlist", plugin.getDataFolder().getPath() + "/textlist.json", Config.JSON);
	}
}
