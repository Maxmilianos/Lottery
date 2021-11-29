package the.max.lottery.api;

import org.bukkit.entity.Player;

import the.max.lottery.Lottery;
import the.max.lottery.menu.Menu;

public class LotteryAPI {
	
	private static LotteryAPI _api;
	
	private static Lottery _lottery;
	
	public LotteryAPI(Lottery lottery) {
		_api = this;
		_lottery = lottery;
	}
	
	public Boolean isInSystem(String name) {
		Boolean is = false;
		if (_lottery._sql) {
			is = _lottery._db.isInSystem(name);
		} else {
			is = _lottery._players.contains(name);
		}
		return is;
	}
	
	public Integer getKeys(String name) {
		Integer keys = 0;
		if (_lottery._sql) {
			keys = _lottery._db.getKeys(name);
		} else {
			keys = _lottery._players.getInt(name + ".keys");
		}
		return keys;
	}
	
	public void setKeys(String name, Integer to) {
		if (_lottery._sql) {
			_lottery._db.set(name, to);
		} else {
			_lottery._players.set(name + ".keys", to);
			_lottery._players.save();
		}
	}
	
	public void addKeys(String name, Integer to) {
		setKeys(name, getKeys(name) + to);
	}
	
	public void removeKeys(String name, Integer to) {
		if (getKeys(name) < to) {
			setKeys(name, 0);
		}
		setKeys(name, getKeys(name) - to);
	}
	
	public void openLottery(Player p) {
		_lottery._invs.put(p, new Menu(p));
		_lottery._invs.get(p).update();
		p.openInventory(_lottery._invs.get(p).getInventory());
	}
	
	
}
