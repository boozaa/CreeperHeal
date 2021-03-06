package com.nitnelave.CreeperHeal.block;

import java.lang.reflect.Method;
import java.util.Random;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.inventory.ItemStack;

public class CreeperDrop
{

	private static Random random = new Random(System.currentTimeMillis());

	/*private static YamlConfiguration fileconf;


	public CreeperDrop(CreeperHeal instance){
		File file = new File(instance.getDataFolder() + "/drops.yml");		//get the trap file
		fileconf = new YamlConfiguration();



		try
		{
			fileconf.load(file);
		}
		catch (FileNotFoundException e)
		{
			try {
				createNewFile(file);
			}
			catch (IOException ex) {
				CreeperLog.warning("[CreeperHeal] Cannot create file "+file.getPath());
			}
		}
		catch (IOException e)
		{
			CreeperLog.warning("[CreeperHeal] Cannot load file "+file.getPath());
		}
		catch (InvalidConfigurationException e)
		{
			CreeperLog.warning("[CreeperHeal] Invalid configuration file "+file.getPath());
		}





	}



	private static void createNewFile(File file) throws IOException
	{
		file.createNewFile();
		fileconf = new YamlConfiguration();
		int[] ids = {1,2,3,4,5,6,12,13,14,15,16,17,19,21,22,23,24,25,26,27,28,29,30,31,32,33,35,37,38,39,40,41,42,43,44,45,46,47,48,49,50,
				53,54,55,56,57,58,59,60,61,62,63,64,65,66,67,68,69,70,71,72,73,74,75,76,77,80,81,82,83,84,85,86,87,88,89,91,93,94,
				95,96,97,98,99,100,101,103,104,105,107,108,109,110,111,112,113,114,115,116,117,118,121,122,123,124};
		int[] drops = {4,3,3,4,5,6,12,13,14,15,263,17,19,351,22,23,24,25,355,27,28,29,30,31,32,33,35,37,38,39,40,41,42,43,44,45,46,47,48,49,
				50,53,54,331,264,57,58,295,3,61,61,323,324,65,66,67,323,69,70,330,72,331,331,76,76,77,80,81,82,338,84,85,86,87,88,348,
				91,356,356,95,96,4,98,39,40,101,103,361,362,107,108,109,3,111,112,113,114,372,116,379,380,4,122,123,123};
		for (int i = 0; i< ids.length; i++) {
			fileconf.set(Material.getMaterial(ids[i]).name(), Material.getMaterial(drops[i]).name());
		}
		fileconf.save(file);
	}*/



	/*public static ItemStack getDrop(BlockState blockState) {         //drops the resource associated with the given blockState exploded

		int type_id = blockState.getTypeId();
		byte data = blockState.getRawData();
		int type_drop = getDropId(type_id);
		if(type_drop != 0){
			int number_drops = 1;
			Random generator = new Random();
			if(type_drop == 351 && data == 4)
				number_drops = generator.nextInt(5) + 4;
			else if(type_drop == 331)
				number_drops = generator.nextInt(2) + 4;
			else if(type_drop == 337)
				number_drops = 4;
			else if(type_drop == 348)
				number_drops = generator.nextInt(3) + 2;
			else if(type_id == 99 || type_id == 100)
				number_drops = generator.nextInt(3);

			return new ItemStack(type_drop, number_drops, data);
		}

		return null;


	}

	private static int getDropId(int id)
	{
		String value = (String) fileconf.get(Material.getMaterial(id).name());
		if(value != null)
			return Material.getMaterial(value).getId();
		else return 0;
	}*/

	public static ItemStack getDrop(BlockState block) {
		return getDrop(block.getTypeId(), block.getRawData());
	}

	public static ItemStack getDrop(Block block) throws RuntimeException {
		return getDrop(block.getTypeId(), block.getData());

	}
	
	public static ItemStack getDrop(int blockTypeId, byte data) {
		if (blockTypeId < 1 || blockTypeId > 255) return null;
		try {
			net.minecraft.server.v1_4_R1.Block b = net.minecraft.server.v1_4_R1.Block.byId[blockTypeId];

			int typeId = b.getDropType(blockTypeId, random, 0);
			if (typeId < 1) return null;

			int dropCount = b.getDropCount(0, random);
			if (dropCount < 1) return null;

			Method m = getMethod(b.getClass(), "getDropData", new Class[] {int.class});
			m.setAccessible(true);
			byte dropData = ((Integer)m.invoke(b, data)).byteValue();

			return new ItemStack(typeId, dropCount, dropData);
		} catch (Exception e) {
			throw new RuntimeException("A severe error occured while retreiving the data dropped.", e);
		}
	}

	/**
	 * Equivalent to java.lang.Class.getDeclaredMethod, except also searches through the <i>clazz</i>'s superclasses.
	 *
	 * @param clazz The class to get the method from.
	 * @param methodName the name of the method
	 * @param parameters the parameter array
	 * @return the Method object for the method of this class matching the specified name and parameters
	 * @throws NoSuchMethodException if a matching method is not found
	 * @throws NullPointerException if methodName is null
	 */
	private static Method getMethod(Class<?> clazz, String methodName, Class<?>[] parameters) throws NoSuchMethodException, NullPointerException {
		if (methodName == null) throw new NullPointerException();
		if (clazz == null) throw new NoSuchMethodException();
		try {
			return clazz.getDeclaredMethod(methodName, parameters);
		} catch (Exception e) {
			return getMethod(clazz.getSuperclass(), methodName, parameters);
		}

	}
}
