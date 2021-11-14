package the.max.lottery;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import the.max.lottery.api.LotteryAPI;
import the.max.lottery.commands.Commands;
import the.max.lottery.config.Config;
import the.max.lottery.database.Database;
import the.max.lottery.listeners.Listeners;
import the.max.lottery.menu.Menu;

public class Lottery  extends JavaPlugin {

	private static Lottery _lottery;
	
	private static LotteryAPI _api;
	
	public Config _config, _players, _rewards;
	
	public Boolean sql = false;
	
	public Database _db;
	
	public String _prefix = "§8[§aLottery§8] §7";
	
	public HashMap<Player, Menu> _invs = new HashMap<Player, Menu>();
	
	/**
	 * TODO: Listeners - _db.create(name);
	 * TODO: 
	 * 
	 */
	
	@Override
	public void onEnable() {
		CommandSender s = Bukkit.getConsoleSender();
		send(s, "           §aLottery §7" + getDescription().getVersion() + "           ");
		send(s, "");
		send(s, " - Creating istance...");
		_lottery = this;
		send(s, " - Succesfully created istance");
		send(s, "");
		send(s, " - Registering api...");
		_api = new LotteryAPI(this);
		send(s, " - Succesfully registered api");
		send(s, "");
		send(s, " - Creating config...");
		_config = new Config(this, "config.yml", "config.yml");
		_rewards = new Config(this, "rewards.yml", "rewards.yml");
		send(s, " - Succesfully created config");
		send(s, "");
		send(s, " - Getting prefix from config...");
		_prefix = ChatColor.translateAlternateColorCodes('&', _config.getString("message.prefix"));
		send(s, " - Succesfully get prefix from config");
		send(s, "");
		send(s, " - Getting type of database from config...");
		if (_config.getString("database.type").equalsIgnoreCase("sql")) {
			sql = true;
			_db = new Database();
			_db.open();
			_db.query("CREATE TABLE IF NOT EXISTS `lottery_keys` (`username` varchar(50) NOT NULL, `keys` int(6) not null, UNIQUE KEY `username` (`username`) ) ENGINE=InnoDB DEFAULT CHARSET=latin1;");
			send(s, " - Succesfully get type SQL");
		} else {
			_players = new Config(this, "players.yml");
			send(s, " - Succesfully get type Yaml");
		}
		send(s, "");
		send(s, " - Registering listeners and commands...");
		Bukkit.getPluginManager().registerEvents(new Listeners(this), this);
		getCommand("lottery").setExecutor(new Commands(this));
		send(s, " - Succesfully registered listeners and commands");
		send(s, "");
		send(s, " - Creating schedulers...");
		new BukkitRunnable() {
			public void run() {
		        for (Player p : _invs.keySet()) {
		        	_invs.get(p).update();
		        }
			}
		}.runTaskTimer(this, 1L, 1L);
		send(s, " - Succesfully created schedulers");
		send(s, "");
		send(s, " §a- Succesfully enabled Lottery");
		send(s, "");
	}
	
	public static Lottery getLottery() {
		if (_lottery == null) {
			_lottery = new Lottery();
		}
		return _lottery;
	}
	
	public LotteryAPI getAPI() {
		if (_api == null) {
			_api = new LotteryAPI(getLottery());
		}
		return _api;
	}
	
	public void send(CommandSender s, String msg) {
		s.sendMessage(_prefix + msg);
	}
	
}
