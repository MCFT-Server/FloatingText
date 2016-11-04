package floatingtext.manager;

import java.util.LinkedList;
import java.util.List;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Position;
import cn.nukkit.utils.Config;
import floatingtext.Main;
import floatingtext.database.DataBase;
import floatingtext.entity.EntityFloatingText;

public class FloatingTextManager {
	private Main plugin;
	private List<EntityFloatingText> textList;
	private static FloatingTextManager instance;

	public FloatingTextManager(Main plugin) {
		this.plugin = plugin;
		init();

		if (instance == null) {
			instance = this;
		}
	}

	public static FloatingTextManager getInstance() {
		return instance;
	}

	private void init() {
		textList = new LinkedList<>();
		getTextListConfig().getAll().forEach((key, text) -> {
			Position pos = stringToPos(key);
			textList.add(new EntityFloatingText(pos, text.toString()));
		});
	}

	private DataBase getDB() {
		return plugin.getDB();
	}

	private Config getTextListConfig() {
		return getDB().getDB("textlist");
	}

	private Position stringToPos(String pos) {
		String args[] = pos.split(":");
		return new Position(Integer.parseInt(args[0]) + 0.5, Integer.parseInt(args[1]), Integer.parseInt(args[2]) + 0.5,
				Server.getInstance().getLevelByName(args[3]));
	}

	private String posToString(Position pos) {
		StringBuffer str = new StringBuffer();
		return str.append((int) pos.x).append(':').append((int) pos.y).append(':').append((int) pos.z).append(':')
				.append(pos.getLevel().getFolderName()).toString();
	}
	
	public List<EntityFloatingText> getFloatingTextList() {
		return textList;
	}

	public void addFloatingText(Position pos, String text) {
		getTextListConfig().set(posToString(pos), text);
		EntityFloatingText floating = new EntityFloatingText(pos, text);
		getFloatingTextList().add(floating);
		floating.spawnToAll();
	}
	
	public void removeFloatingText(Position pos) {
		for (EntityFloatingText floatingText : getFloatingTextList()) {
			if (floatingText.getPosition().equals(pos) && pos.level == floatingText.getPosition().level) {
				getFloatingTextList().remove(floatingText);
				floatingText.despawnFromAll();
			}
		}
		getTextListConfig().remove(posToString(pos));
	}
	
	public void spawnAllText(Player player) {
		getFloatingTextList().forEach(floatingText -> {
			floatingText.spawnTo(player);
		});
	}
	
	public boolean exist(Position pos) {
		for (EntityFloatingText text : getFloatingTextList()) {
			if (text.getPosition().equals(pos) && text.getPosition().level == pos.getLevel()) {
				return true;
			}
		}
		return false;
	}
}
