package the.max.lottery.placeholders;

import org.bukkit.entity.Player;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.clip.placeholderapi.external.EZPlaceholderHook;
import net.md_5.bungee.api.ChatColor;
import the.max.lottery.Lottery;
/*
public class Placeholder extends EZPlaceholderHook {
	
	private Lottery _pl;
	
	public Placeholder(Lottery pl) {
		super(pl, "lottery");
		_pl = pl;
	}

	@Override
	public String onPlaceholderRequest(Player p, String ph) {
		String placeholder = "";
		if (p != null) {
			if (ph.equalsIgnoreCase("keys")) {
				placeholder = "" + _pl.getAPI().getKeys(p.getName());
			}
		}
		return placeholder;
	}

}*/

public class Placeholder extends PlaceholderExpansion {
	
	private Lottery _pl;
	
	public Placeholder(Lottery l) {
		_pl = l;
	}

	@Override
	public String onPlaceholderRequest(Player p, String ph) {
		String placeholder = "";
		if (p != null) {
			if (ph.equalsIgnoreCase("keys")) {
				placeholder = "" + _pl.getAPI().getKeys(p.getName());
			}
		}
		return placeholder;
	}
	
	@Override
    public String getPlugin() {
        return "Lottery";
    }

	@Override
	public String getAuthor() {
		return "The_MaxCZ";
	}

	@Override
	public String getIdentifier() {
		return "lottery";
	}

	@Override
	public String getVersion() {
		return _pl.getDescription().getVersion();
	}
	
	@Override
    public boolean canRegister() {
        return true;
    }

}
