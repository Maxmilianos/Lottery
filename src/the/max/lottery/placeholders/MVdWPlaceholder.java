package the.max.lottery.placeholders;

import be.maximvdw.placeholderapi.PlaceholderAPI;
import be.maximvdw.placeholderapi.PlaceholderReplaceEvent;
import be.maximvdw.placeholderapi.PlaceholderReplacer;
import net.md_5.bungee.api.ChatColor;
import the.max.lottery.Lottery;

public class MVdWPlaceholder {
	
	private Lottery _pl;
	
	public MVdWPlaceholder(Lottery pl) {
		_pl = pl;
	}
	
	public void start() {
		PlaceholderAPI.registerPlaceholder(_pl, "lottery_keys", new PlaceholderReplacer() {
			@Override
			public String onPlaceholderReplace(PlaceholderReplaceEvent e) {
				return "" + _pl.getAPI().getKeys(e.getPlayer().getName());
			}
		});
	}
	
	
}
