package floatingtext.database;

import java.util.Map.Entry;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.utils.Config;
import floatingtext.Main;

public class DataBase extends BaseDB<Main> {

	public DataBase(Main plugin) {
		super(plugin);
		setPrefix("[FloatingText]");
		initDB("textlist", plugin.getDataFolder().getPath() + "/textlist.json", Config.JSON);
	}
	
	public void initText(Player player) {
		for(Entry<String, Object> entry : getDB("textlist").getAll().entrySet()) {
			Position pos = stringToPos(entry.getKey());
			if (pos.level == null) return;
			pos.level.addParticle(new FloatingTextParticle(pos, "", ((String) entry.getValue()).replace("\\n", "\n")), player);
		}
	}
	
	private Position stringToPos(String pos) {
		String args[] = pos.split(":");
		return new Position(Integer.parseInt(args[0]) + 0.5, Integer.parseInt(args[1]) + 1, Integer.parseInt(args[2]) + 0.5, Server.getInstance().getLevelByName(args[3]));
	}
}
