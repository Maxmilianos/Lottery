package the.max.lottery.menu;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import the.max.lottery.Lottery;
import the.max.lottery.checker.Checker;
import the.max.lottery.config.Config;
import the.max.lottery.random.RandomCollection;

@SuppressWarnings({ "unchecked", "rawtypes" })
public class Menu {
	
	private Player _owner;
	
	private RandomCollection _rewards = new RandomCollection();
	
	private Inventory _inv;
	
	private Long _start;
	
	private Checker _checker;
	
	private Integer _step;
	
	private Boolean _close;
	
	public Menu(Player owner) {
		_owner = owner;
		_inv = Bukkit.createInventory(null, 27, ChatColor.translateAlternateColorCodes('&', Lottery.getLottery()._config.getString("message.inventory.name")));
		for (String reward : Lottery.getLottery()._rewards.getKeys(false)) {
			ConfigurationSection config = Lottery.getLottery()._rewards.getConfigurationSection(reward);
			ItemStack it = new ItemStack(Material.valueOf(config.getString("material")));
			ItemMeta im = it.getItemMeta();
			im.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("display-name")));
			ArrayList<String> lore = new ArrayList<String>();
			for (String s : config.getStringList("lore")) {
				lore.add(ChatColor.translateAlternateColorCodes('&', s));
			}
			im.setLore(lore);
			it.setItemMeta(im);
			_rewards.add(config.getDouble("chance"), it);
		}
		_start = System.currentTimeMillis();
		_checker = new Checker();
		_step = 0;
		_close = false;
		_inv.setItem(4, get(Lottery.getLottery()._config.getString("message.prize"), Material.REDSTONE_TORCH_ON, 1, (byte)0));
		_inv.setItem(10, (ItemStack)_rewards.next());
		_inv.setItem(11, (ItemStack)_rewards.next());
		_inv.setItem(12, (ItemStack)_rewards.next());
		_inv.setItem(13, (ItemStack)_rewards.next());
		_inv.setItem(14, (ItemStack)_rewards.next());
		_inv.setItem(15, (ItemStack)_rewards.next());
		_inv.setItem(16, (ItemStack)_rewards.next());
		_inv.setItem(22, get(Lottery.getLottery()._config.getString("message.prize"), Material.REDSTONE_TORCH_ON, 1, (byte)0));
	}
	
	public Inventory getInventory() {
		return _inv;
	}
	
	public Boolean canClose() {
		return _close;
	}
	
	public Boolean isInventory(Inventory inv) {
		return inv.equals(_inv);
	}
	
	public void close() {
		_inv.clear();
	}
	
	public void update() {
		Long time = System.currentTimeMillis() - _start;
		if (_step == 0) {
			if (time < 6000L) {
				glassRainbow(true);
				_checker.check();
				if (_checker.should()) {
					for (int i = 10; i < 17 - 1; i++) {
						_inv.setItem(i, _inv.getItem(i + 1));
				    }
					_inv.setItem(16, (ItemStack)_rewards.next());
				}
				Config c = Lottery.getLottery()._config;
				if (c.getBoolean("settings.sound.enable"))
					_owner.playSound(_owner.getLocation(), Sound.valueOf(c.getString("settings.sound.type")), Float.valueOf(c.getString("settings.sound.float1")), Float.valueOf(c.getString("settings.sound.float2")));
			} else if (time >= 6500L) {
				_inv.setItem(10, new ItemStack(Material.AIR));
				_inv.setItem(11, new ItemStack(Material.AIR));
				_inv.setItem(12, new ItemStack(Material.AIR));
				_inv.setItem(14, new ItemStack(Material.AIR));
				_inv.setItem(15, new ItemStack(Material.AIR));
				_inv.setItem(16, new ItemStack(Material.AIR));
				_step += 1;
			}
		} else if (_step == 1) {
			if (time >= 9000L) {
				ItemStack it = _inv.getItem(13).clone();
				ItemMeta im = it.getItemMeta();
				Material m = it.getType();
				String displayname = im.getDisplayName().replace("§", "&");
				for (String reward : Lottery.getLottery()._rewards.getKeys(false)) {
					ConfigurationSection config = Lottery.getLottery()._rewards.getConfigurationSection(reward);
					ArrayList<String> lore = new ArrayList<String>();
					for (String s : config.getStringList("lore")) {
						lore.add(ChatColor.translateAlternateColorCodes('&', s));
					}
					if (m == Material.valueOf(config.getString("material")) && displayname.equalsIgnoreCase(config.getString("display-name"))) {
						for (String cmd : config.getStringList("commands")) {
							cmd = cmd.replace("%playername%", _owner.getName());
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
						}
					}
				}
				_step += 1;
				_close = true;
				_owner.closeInventory();
				Lottery.getLottery()._invs.remove(_owner);
				close();
			} else {
				glassRainbow(false);
			}
		}
		_owner.updateInventory();
	}
	
	private void glassRainbow(boolean paramBoolean) {
		RandomCollection localRandomCollection = new RandomCollection();
	    localRandomCollection.add(50.0D, 2);
	    localRandomCollection.add(50.0D, 4);
	    localRandomCollection.add(50.0D, 5);
	    localRandomCollection.add(50.0D, 1);
	    localRandomCollection.add(50.0D, 3);
	    localRandomCollection.add(50.0D, 6);
	    int i = ((Integer)localRandomCollection.next()).intValue();
	    for (int j = 0; j < _inv.getSize(); j++) {
	    	if ((j != 10) && (j != 11) && (j != 12) && (j != 13) && (j != 14) && (j != 15) && (j != 16) && (j != 4) && (j != 22)) {
	    		if (paramBoolean) {
	    			int k = ((Integer)localRandomCollection.next()).intValue();
	    			_inv.setItem(j, getGlass(" ", 1, (byte)k));
	    		} else {
	    			_inv.setItem(j, getGlass(" ", 1, (byte)i));
	    		}
	    	}
	    }
	}
	
	private ItemStack getWithLore(String name, Material material, Integer number, byte byt, String[] lore){
		ItemStack it=new ItemStack(material, number, byt);
		ItemMeta im=it.getItemMeta();
		im.setDisplayName(name);
		im.setLore(Arrays.asList(lore));
		it.setItemMeta(im);
		return it;
	}
	
	private ItemStack get(String name, Material material, Integer number, byte byt){
		ItemStack it=new ItemStack(material, number, byt);
		ItemMeta im=it.getItemMeta();
		im.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
		it.setItemMeta(im);
		return it;
	}
	
	private ItemStack getGlass(String name, Integer number, byte by) {
		ItemStack it=new ItemStack(Material.STAINED_GLASS_PANE, number, by);
		ItemMeta im=it.getItemMeta();
		im.setDisplayName(name);
		it.setItemMeta(im);
		return it;
	}

}
