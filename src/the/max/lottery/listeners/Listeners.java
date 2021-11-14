package the.max.lottery.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;

import the.max.lottery.Lottery;

public class Listeners implements Listener {
	
	private static Lottery _lottery;

	public Listeners(Lottery lottery) {
		_lottery = lottery;
	}
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		if (!_lottery._db.isInSystem(p.getName())) {
			_lottery._db.create(p.getName());
		}
	}
	
	@EventHandler
	public void invClick(InventoryClickEvent e) {
		if (e.getCurrentItem() != null && e.getClickedInventory() != null) {
			Inventory inv = e.getClickedInventory();
			if (inv.getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', _lottery._config.getString("message.inventory.name")))) {
				e.setCancelled(true);
			}
		}
	}
	
	@EventHandler
	public void closeInv(InventoryCloseEvent e) {
		Player p = (Player) e.getPlayer();
		if (_lottery._invs.containsKey(p) && !_lottery._invs.get(p).canClose()) {
			Bukkit.getScheduler().runTaskLater(_lottery, new Runnable() {
				public void run() {
					p.openInventory(_lottery._invs.get(p).getInventory());
				}
			}, 5L);
		}
	}
	
}
