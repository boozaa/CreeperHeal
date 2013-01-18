package com.nitnelave.CreeperHeal.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.nitnelave.CreeperHeal.CreeperHeal;
import com.nitnelave.CreeperHeal.config.CreeperConfig;

import de.bananaco.bpermissions.api.ApiLayer;
import de.bananaco.bpermissions.api.util.CalculableType;

import net.milkbowl.vault.permission.Permission;

public class CreeperPermissionManager
{
	private static Permission vaultPerms = null;
	private static CreeperHeal plugin;
	private static boolean bPerms = false;

	public CreeperPermissionManager(CreeperHeal instance)
	{
		plugin = instance;
		PluginManager pm = plugin.getServer().getPluginManager(); 

		Plugin vaultPlugin = pm.getPlugin("Vault");
		if(vaultPlugin != null) {
			if(load_Vault())
				CreeperLog.logInfo("Successfully hooked in Vault",0);
			else
				CreeperLog.warning("[CreeperHeal] There was an error while hooking in Vault");
		}
		Plugin bPermissions = pm.getPlugin("bPermissions");
		if(bPermissions != null) {
			bPerms = true;
			CreeperLog.logInfo("Successfully hooked in bPermissions",0);
		}
	}


	private static boolean load_Vault()
	{
		RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);
		vaultPerms = rsp.getProvider();
		return vaultPerms != null;	    
	}


	public static boolean checkPermissions(Player player, boolean joker, String... nodes) {       //check permission for a given node for a given player
		if(bPerms)
		{
			if(joker && ApiLayer.hasPermission(player.getWorld().getName(), CalculableType.USER, player.getName(), "CreeperHeal.*"))
				return true;
			
			for(String node : nodes)
			{
				if(ApiLayer.hasPermission(player.getWorld().getName(), CalculableType.USER, player.getName(), "CreeperHeal." + node))
					return true;
			}
			if(CreeperConfig.opEnforce && joker)
				return player.isOp();

		}
		else if(vaultPerms != null)
		{
			if(joker && vaultPerms.has(player, "CreeperHeal.*"))
				return true;
			for(String node : nodes)
			{
				if(vaultPerms.has(player, "CreeperHeal." + node))
					return true;
			}
			
			if(CreeperConfig.opEnforce && joker)
				return player.isOp();
		}

		else
		{
			if(joker && player.hasPermission("CreeperHeal.*"))
				return true;
			for(String node : nodes)
			{
				if(player.hasPermission("CreeperHeal." + node))
					return true;
			}
			if(player.hasPermission("CreeperHeal.*"))
				return true;
			if(CreeperConfig.opEnforce && joker)
				return player.isOp();
		}
		return false;
	}




	/*public int getMaxTraps(Player player)
	{
		int max = 0;
		if(bPerms)
		{
			String s = ApiLayer.getValue(player.getWorld().getName(), CalculableType.USER, player.getName(), "CreeperHeal.trap.maxTraps");
			try{
				max = Integer.parseInt(s);
			}
			catch(NumberFormatException e)
			{
				return 0;
			}
			
		}
		
		else 
		{
			for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
				if (perm.getValue() && perm.getPermission().startsWith("CreeperHeal.maxTraps.")) {
					String num = perm.getPermission().substring("CreeperHeal.maxTraps.".length());
					if (!num.matches("\\d+"))
						continue;
					max = Math.max(max, Integer.parseInt(num));
				}
			}

		}
		return max;
	}*/

/*
	public float getTrapFee(Player player)
	{
		float fee = 0;
		if(bPerms)
		{
			de.bananaco.bpermissions.api.util.Permission[] perms = ApiLayer.getPermissions(player.getWorld().getName(), CalculableType.USER, player.getName());
			for(de.bananaco.bpermissions.api.util.Permission perm : perms)
			{
				if (perm.isTrue() && perm.name().startsWith("CreeperHeal.TrapFee.")) {
					String num = perm.name().substring(21);
					try{
						Float num2 = Float.parseFloat(num);
						fee = Math.min(fee, num2);
					}
					catch(NumberFormatException e){}
				}
			}
		}
		else 
		{
			for (PermissionAttachmentInfo perm : player.getEffectivePermissions()) {
				if (perm.getValue() && perm.getPermission().startsWith("CreeperHeal.TrapFee.")) {
					String num = perm.getPermission().substring("CreeperHeal.TrapFee.".length());
					try{
						Float num2 = Float.parseFloat(num);
						fee = Math.min(fee, num2);
					}
					catch(NumberFormatException e){}
				}
			}

		}
		return fee;
	}*/


}
