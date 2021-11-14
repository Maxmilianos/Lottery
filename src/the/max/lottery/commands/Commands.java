package the.max.lottery.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import the.max.lottery.Lottery;
import the.max.lottery.menu.Menu;

public class Commands implements CommandExecutor {
	
	private static Lottery _lottery;

	public Commands(Lottery lottery) {
		_lottery = lottery;
	}
	
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("lottery")) {
			if (args.length == 0) {
				if (s instanceof Player) {
					Player p = (Player) s;
					_lottery.send(p, "Keys: §a" + _lottery.getAPI().getKeys(p.getName()));
				} else {
					_lottery.send(s, "/lottery set <Name> <To>");
					_lottery.send(s, "/lottery add <Name> <To>");
					_lottery.send(s, "/lottery remove <Name> <To>");
					_lottery.send(s, "/lottery info");
					_lottery.send(s, "/lottery help");
					_lottery.send(s, "/lottery reload");
				}
			} else {
				if (args[0].equalsIgnoreCase("help") && s.hasPermission("lottery.commands.help")) {
					if (args.length == 1) {
						_lottery.send(s, "/lottery set <Name> <To>");
						_lottery.send(s, "/lottery add <Name> <To>");
						_lottery.send(s, "/lottery remove <Name> <To>");
						_lottery.send(s, "/lottery info");
						_lottery.send(s, "/lottery help");
						_lottery.send(s, "/lottery reload");
					} else {
						_lottery.send(s, "/lottery help");
					}
				} else if (args[0].equalsIgnoreCase("reload") && s.hasPermission("lottery.commands.reload")) {
					if (args.length == 1) {
						_lottery._config.reload();
						_lottery._players.reload();
						_lottery._rewards.reload();
					} else {
						_lottery.send(s, "/lottery reload");
					}
				} else if (args[0].equalsIgnoreCase("set") && s.hasPermission("lottery.commands.set")) {
					if (args.length == 3) {
						String name = args[1];
						if (isInteger(args[2])) {
							Integer to = Integer.valueOf(args[2]);
							_lottery.getAPI().setKeys(name, to);
							_lottery.send(s, "You set §a" + to + "§7 Lottery keys to §a" + name + "§7.");
						} else {
							_lottery.send(s, "/lottery set <Name> §a<To>");
							_lottery.send(s, "To must be integer!");
						}
					} else {
						_lottery.send(s, "/lottery set <Name> <To>");
					}
				} else if (args[0].equalsIgnoreCase("add") && s.hasPermission("lottery.commands.add")) {
					if (args.length == 3) {
						String name = args[1];
						if (isInteger(args[2])) {
							Integer to = Integer.valueOf(args[2]);
							_lottery.getAPI().addKeys(name, to);
							_lottery.send(s, "You added §a" + to + "§7 Lottery keys to §a" + name + "§7.");
						} else {
							_lottery.send(s, "/lottery add <Name> §a<To>");
							_lottery.send(s, "To must be integer!");
						}
					} else {
						_lottery.send(s, "/lottery add <Name> <To>");
					}
				} else if (args[0].equalsIgnoreCase("remove") && s.hasPermission("lottery.commands.remove")) {
					if (args.length == 3) {
						String name = args[1];
						if (isInteger(args[2])) {
							Integer to = Integer.valueOf(args[2]);
							_lottery.getAPI().removeKeys(name, to);
							_lottery.send(s, "You removed §a" + to + "§7 Lottery keys to §a" + name + "§7.");
						} else {
							_lottery.send(s, "/lottery remove <Name> §a<To>");
							_lottery.send(s, "To must be integer!");
						}
					} else {
						_lottery.send(s, "/lottery remove <Name> <To>");
					}
				} else if (args[0].equalsIgnoreCase("open")) {
					if (s instanceof Player) {
						Player p = (Player) s; 
						if (_lottery.getAPI().getKeys(p.getName()) >= 1) {
							_lottery.getAPI().openLottery(p);
							_lottery.getAPI().removeKeys(p.getName(), 1);
							_lottery.send(p, "Actual keys: §a" + _lottery.getAPI().getKeys(p.getName()));
						} else {
							_lottery.send(p, "You don't have any keys for Lottery.");
						}
					} else {
						_lottery.send(s, "/lottery set <Name> <To>");
						_lottery.send(s, "/lottery add <Name> <To>");
						_lottery.send(s, "/lottery remove <Name> <To>");
						_lottery.send(s, "/lottery info");
						_lottery.send(s, "/lottery help");
						_lottery.send(s, "/lottery reload");
					}
				} else {
					if (s instanceof Player) {
						Player p = (Player) s;
						_lottery.send(p, "Keys: §a" + _lottery.getAPI().getKeys(p.getName()));
					} else {
						_lottery.send(s, "/lottery set <Name> <To>");
						_lottery.send(s, "/lottery add <Name> <To>");
						_lottery.send(s, "/lottery remove <Name> <To>");
						_lottery.send(s, "/lottery info");
						_lottery.send(s, "/lottery help");
						_lottery.send(s, "/lottery reload");
					}
				}
			}
		}
		return false;
	}
    
	private boolean isInteger(String s) {
    	try {
    		Integer.parseInt(s);
    		if (Integer.valueOf(s) < 0) {
    			return false;
    		}
    	} catch (NumberFormatException e) {
    		return false;
    	}
    	return true;
    }
}
