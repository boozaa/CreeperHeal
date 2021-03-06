package com.nitnelave.CreeperHeal.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.EnderDragon;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.Wither;

import com.nitnelave.CreeperHeal.block.BlockId;
import com.nitnelave.CreeperHeal.utils.CreeperLog;


public class WorldConfig {

	public boolean enderman, replaceAbove, blockLava, blockTNT, blockIgnite, blockBlackList, 
	blockSpawnEggs, blockPvP, warnLava, warnTNT, warnIgnite, warnBlackList, warnSpawnEggs, warnPvP, preventFireSpread, preventFireLava,
	creepers, tnt, fire, ghast, magical, dragons, wither, spawnWither, ignoreFactionsWilderness, ignoreFactionsTerritory, whiteBlockList, whitePlaceList;
	public String name;
	public int repairTime, replaceLimit;
	public HashSet<BlockId> blockList = new HashSet<BlockId>(), placeList = new HashSet<BlockId>();
	private File pluginFolder;
	private YamlConfiguration config;





	public WorldConfig(String name, File folder) throws FileNotFoundException, IOException, InvalidConfigurationException {
		this.name = name;
		if(folder != null)
		{
			pluginFolder = folder;
			load();
		}
		else
		{
			creepers = tnt = ghast = fire = wither = spawnWither = true;
			magical = dragons = replaceAbove = enderman = blockLava = blockTNT = blockIgnite = blockBlackList = blockSpawnEggs = blockPvP = 
					warnLava = warnTNT = warnIgnite = warnBlackList = warnSpawnEggs = warnPvP = preventFireSpread = preventFireLava =
					ignoreFactionsWilderness = ignoreFactionsTerritory = whiteBlockList = whitePlaceList = false;
			replaceLimit = 60;
			repairTime = -1;
			blockList = new HashSet<BlockId>();        //sample whitelist
			int[] tmp_list = { 1,2,3,9,11,12,13,14,15,16,17,18,21,24,31,32,37,38,39,40,48,49,56,73,79,81,82,86,87,88,89 };
			for(int k : tmp_list)
				blockList.add(new BlockId(k));
		}
	}

	
	@SuppressWarnings("unchecked")
	public WorldConfig(String name, Object... l) {
		this.name = name;
		creepers =  (Boolean) l[0];
		tnt = (Boolean) l[1];
		ghast = (Boolean) l[2];
		dragons = (Boolean) l[3];
		magical = (Boolean) l[4];
		fire = (Boolean) l[5];
		enderman = (Boolean) l[6];
		replaceAbove = (Boolean) l[7];
		replaceLimit = (Integer) l[8]; 
		blockList = (HashSet<BlockId>) l[9];
		repairTime = (Integer) l[10];
		blockLava = blockTNT = blockIgnite = blockBlackList = blockSpawnEggs = blockPvP = warnLava = 
				warnTNT = warnIgnite = warnBlackList = warnSpawnEggs = warnPvP = preventFireSpread = preventFireLava =
				ignoreFactionsWilderness = whiteBlockList = whitePlaceList = ignoreFactionsTerritory = false;
		wither = spawnWither = true;
		placeList = new HashSet<BlockId>();
	}



	public String getName() {
		return name;
	}

	public String formatList(HashSet<BlockId> list)
	{
		StringBuilder b = new StringBuilder();
		for(BlockId block : list)
		{
			b.append(block.toString());
			b.append(", ");
		}

		String blocklist = b.toString();
		return blocklist.substring(0, blocklist.length() - 2);

	}

	public boolean isRepairTimed()
	{
		return repairTime > -1;
	}

	public void load() throws FileNotFoundException, IOException, InvalidConfigurationException
	{
		File configFile = new File(pluginFolder.getPath() + "/" + name + ".yml");
		if(!configFile.exists())
			CreeperConfig.copyJarConfig(configFile, "world.yml");
		config = new YamlConfiguration();
		config.load(configFile);
		
		
		creepers = getBoolean("replace.Creepers", true);
		tnt = getBoolean("replace.TNT", true);
		ghast = getBoolean("replace.Ghast", true);
		dragons = getBoolean("replace.Dragons", false);
		magical = getBoolean("replace.Magical", false);
		fire = getBoolean("replace.Fire", true);
		enderman = getBoolean("replace.Enderman", true);
		replaceAbove = getBoolean("replace.replace-above-limit-only", false);
		replaceLimit = getInt("replace.replace-limit", 64);
		blockList = loadList("replace.restrict-list");
		repairTime = getInt("replace.repair-time-of-day", -1);
		blockLava = getBoolean("grief.block.lava", false);
		blockTNT = getBoolean("grief.block.TNT", false);
		blockIgnite = getBoolean("grief.block.flint-and-steel", false);
		blockBlackList = getBoolean("grief.block.blacklist", false);
		blockSpawnEggs = getBoolean("grief.block.spawn-eggs", false);
		blockPvP = getBoolean("grief.block.PvP", false);
		warnLava = getBoolean("grief.warn.lava", false);
		warnTNT = getBoolean("grief.warn.TNT", false);
		warnIgnite = getBoolean("grief.warn.flint-and-steel", false);
		warnBlackList = getBoolean("grief.warn.blacklist", false);
		warnSpawnEggs = getBoolean("grief.warn.spawn-eggs", false);
		warnPvP = getBoolean("grief.warn.PvP", false);
		preventFireSpread = getBoolean("grief.prevent-fire-spread.fire", false);
		preventFireLava = getBoolean("grief.prevent-fire-spread.lava", false);
		placeList = loadList("grief.blacklist"); 
		ignoreFactionsWilderness = getBoolean("replace.factions.ignore-wilderness", false);
		ignoreFactionsTerritory = getBoolean("replace.factions.ignore-territory", false);
		whiteBlockList = getBoolean("replace.white-restrict-list", false);
		whitePlaceList = getBoolean("grief.white-list", false);
		wither = getBoolean("replace.Wither", true);
		spawnWither = getBoolean("grief.allow-spawn-wither", true);
		
	}
	
	public void save() throws IOException
	{
		set("replace.Creepers", creepers);
		set("replace.TNT", tnt);
		set("replace.Ghast", ghast);
		set("replace.Dragons", dragons);
		set("replace.Magical", magical);
		set("replace.Fire", fire);
		set("replace.Enderman", enderman);
		set("replace.replace-above-limit-only", replaceAbove);
		set("replace.replace-limit", replaceLimit);
		set("replace.restrict-list", formatList(blockList));
		set("replace.white-restrict-list", whiteBlockList);
		set("replace.repair-time-of-day", repairTime);
		set("replace.factions.ignore-wilderness", ignoreFactionsWilderness);
		set("replace.factions.ignore-territory", ignoreFactionsTerritory);
		set("replace.Wither", wither);
		set("grief.block.lava", blockLava);
		set("grief.block.TNT", blockTNT);
		set("grief.block.flint-and-steel", blockIgnite);
		set("grief.block.blacklist", blockBlackList);
		set("grief.block.spawn-eggs", blockSpawnEggs);
		set("grief.block.PvP", blockPvP);
		set("grief.warn.lava", warnLava);
		set("grief.warn.TNT", warnTNT);
		set("grief.warn.flint-and-steel", warnIgnite);
		set("grief.warn.blacklist", warnBlackList);
		set("grief.warn.spawn-eggs", warnSpawnEggs);
		set("grief.warn.PvP", warnPvP);
		set("grief.prevent-fire-spread.fire", preventFireSpread);
		set("grief.prevent-fire-spread.lava", preventFireLava);
		set("grief.blacklist", formatList(placeList)); 
		set("grief.white-list", whitePlaceList);
		set("grief.allow-spawn-wither", spawnWither);
		config.save(pluginFolder.getPath() + "/" + name + ".yml");
	}

	private void set(String path, Object value)
    {
		config.set(path, value);
    }

	private HashSet<BlockId> loadList(String path)
    {
		HashSet<BlockId> returnList  = new HashSet<BlockId>();
		try{
			String tmp_str1 = config.getString(path, "").trim();
			String[] split = tmp_str1.split(",");
			for(String elem : split) {
				returnList.add(new BlockId(elem));
			}
		}
		catch (NumberFormatException e) {
			CreeperLog.warning("[CreeperHeal] Wrong values for " + path + " field for world " + name);
			returnList.clear();
			returnList.add(new BlockId(0));
		}
		return returnList;
    }

	private int getInt(String path, int def)
    {
	    int tmp;
		try {
			tmp = config.getInt(path, def);
		}
		catch(Exception e) {
			CreeperLog.warning("[CreeperHeal] Wrong value for " + path + " field in world " + name + ". Defaulting to " + Integer.toString(def));
			tmp = def;
		}
		return tmp;
    }

	private boolean getBoolean(String path, boolean def)
    {
		boolean tmp;
		try {
			tmp = config.getBoolean(path, def);
		}
		catch(Exception e) {
			CreeperLog.warning("[CreeperHeal] Wrong value for " + path + " field in world " + name + ". Defaulting to " + Boolean.toString(def));
			tmp = def;
		}
		return tmp;
    }
	
	

	public boolean shouldReplace(Entity entity)
	{

		if(entity != null) {

			if( entity instanceof Creeper)         //if it's a creeper, and creeper explosions are recorded
			{
				if(replaceAbove)
				{
					if(isAbove(entity))
						return creepers;
					return false;
				}
				return creepers;
			}
			else if(entity instanceof TNTPrimed)                 //tnt -- it checks if it's a trap.
			{
				if(replaceAbove){
					if(isAbove(entity))
						return tnt;
					return false;
				}
				else
					return tnt;
			}
			else if(entity instanceof Fireball)         //fireballs (shot by ghasts)
			{
				if(replaceAbove){
					if(isAbove(entity))
						return ghast;
					return false;
				}
				else
					return ghast;
			}
			else if(entity instanceof EnderDragon)
				return dragons;
			else if(entity instanceof Wither)
				return wither;
			else        //none of it, another custom entity
				return magical;

		}
		else
			return magical;
	}



	public boolean isAbove(Entity entity) {       //the entity that exploded was above the limit
		return entity.getLocation().getBlockY()>= replaceLimit;
	}
}