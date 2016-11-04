package floatingtext.entity;

import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.AddPlayerPacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;

public class EntityFloatingText {
	private Position pos;
	private String text;
	private long eid;

	public EntityFloatingText(Position pos, String text) {
		this.pos = pos;
		this.text = text;
		eid = Entity.entityCount++;
	}

	public void spawnTo(Player player) {
		if (pos.level == player.level) {
			AddPlayerPacket pk = new AddPlayerPacket();
			pk.uuid = UUID.randomUUID();
			pk.username = "";
			pk.item = Item.get(Item.AIR);
			pk.entityUniqueId = eid;
			pk.entityRuntimeId = eid;
			pk.x = (float) pos.x + 0.5f;
			pk.y = (float) pos.y - 1.7f;
			pk.z = (float) pos.z + 0.5f;
			pk.speedX = 0;
			pk.speedY = 0;
			pk.speedZ = 0;
			pk.yaw = 0;
			pk.pitch = 0;
			long flags = 0;
			flags |= 1 << Entity.DATA_FLAG_INVISIBLE;
			flags |= 1 << Entity.DATA_FLAG_CAN_SHOW_NAMETAG;
			flags |= 1 << Entity.DATA_FLAG_ALWAYS_SHOW_NAMETAG;
			flags |= 1 << Entity.DATA_FLAG_IMMOBILE;
			pk.metadata = new EntityMetadata().putLong(Entity.DATA_FLAGS, flags).putString(Entity.DATA_NAMETAG, text.replace("<br>", "\n").replace("\\n", "\n"))
					.putLong(Entity.DATA_LEAD_HOLDER_EID, -1).putByte(Entity.DATA_LEAD, 0);
			player.dataPacket(pk);
		}
	}

	public void spawnToAll() {
		Server.getInstance().getOnlinePlayers().forEach((uuid, player) -> {
			spawnTo(player);
		});
	}

	public void despawnFrom(Player player) {
		RemoveEntityPacket pk = new RemoveEntityPacket();
		pk.eid = eid;
		player.dataPacket(pk);
	}

	public void despawnFromAll() {
		Server.getInstance().getOnlinePlayers().forEach((uuid, player) -> {
			despawnFrom(player);
		});
	}

	public Position getPosition() {
		return pos;
	}

}
