package de.rbs90.bukkit.plugins.biomeSounds;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.player.SpoutPlayer;

public class BiomeSoundListener extends PlayerListener
{
    public static BiomeSoundMain plugin;
    public static List<Player> playerList = new ArrayList<Player>();
    public static List<Location> locationList = new ArrayList<Location>();
    public BiomeSoundListener(BiomeSoundMain instance)
    {
        plugin = instance;
    }
    
	public void onPlayerQuit( PlayerQuitEvent event)
	{
		Player player = event.getPlayer();
		if(playerList.contains(player))
		{
			Integer pLocX = player.getLocation().getBlockX();
			Integer pLocY = player.getLocation().getBlockY();
			Integer pLocZ = player.getLocation().getBlockZ();
			Integer index = playerList.indexOf(player);
			World world = player.getWorld();
			Location loc = world.getBlockAt(pLocX + 25,pLocY,pLocZ +25).getLocation();
			locationList.set(index, loc);
		}		
	}
    
    public static Integer getDistance(Player player, int locX, int locY)
    {
		if(playerList.contains(player))
		{
			Integer listpos = playerList.indexOf(player);
			Location pos = locationList.get(listpos);
			Integer listX = (int) pos.getX();
			Integer listZ = (int) pos.getZ();
			Location loc = player.getLocation();
			Integer currentX = (int) loc.getX();
			Integer currentZ = (int) loc.getZ();
			Integer diffX = currentX - listX;
			Integer diffZ = currentZ - listZ;
			if(diffX < 0)diffX = -diffX;
			if(diffZ < 0)diffZ = -diffZ;
			Integer distance = (diffX + diffZ)/2;
			return distance;
		}
		else
		{
			return -1;
		}
    }
    
    public Location scanForLava(Player player)
    {
    	Integer locX = (int)player.getLocation().getX();
    	Integer locY = (int)player.getLocation().getY();
    	Integer locZ = (int)player.getLocation().getZ();
    	Integer radius = 10;
    	Integer minX = locX - (radius/2);
    	Integer maxX = locX + (radius/2);
    	Integer minY = locY - (radius/2);
    	Integer maxY = locY + (radius/2);
    	Integer minZ = locZ - (radius/2);
    	Integer maxZ = locZ + (radius/2);
    	World world = player.getWorld();
    	for(Integer X = minX; X < maxX; X++)
    	{
    		for(Integer Y = minY; Y < maxY; Y++)
    		{
    			for(Integer Z = minZ; Z < maxZ; Z++)
    			{
    				Block block = world.getBlockAt(X,Y,Z);
    				if((block.getTypeId() == 10) || (block.getTypeId() == 11))
    				{
    					return block.getLocation();
    				}
    			}
    		}
    	}
    	return null;
    }

    public void onPlayerMove(PlayerMoveEvent event)
    {
    	Location loc = event.getPlayer().getLocation();
    	Player player = event.getPlayer();
    	Integer stoneCount = 0;
    	Integer locX = (int)loc.getX();
   		Integer locZ = (int)loc.getZ();
   		Integer locY = (int)loc.getY();
   		World world = player.getWorld();
   		Boolean overWater = false;
   		
    	for(Integer X = locY; X < 129; X++)
		{
    		Integer typeID = world.getBlockAt(locX,X,locZ).getTypeId();
			if(typeID > 0 && typeID < 10 )
			{
				stoneCount++;				
			}
		}
    	for(Integer X = locY; X > (locY - 2);X--)
    	{
    		Integer typeID = world.getBlockAt(locX,X,locZ).getTypeId();
    		if(typeID == 8 || typeID == 9 || typeID == 79)
    		{
    			overWater = true;
    		}
    	}
    	Integer dist = getDistance(player,(int)loc.getX(),(int)loc.getZ());
    	Location lava = scanForLava(player);
    	if((lava != null) && (!player.isInsideVehicle()))
    	{    		
    		if((dist > plugin.soundNodeSeparation || dist < 0))
    		{
				if(!playerList.contains(player))
				{					
					playerList.add(player);
					locationList.add(loc);
				}
				Integer index = playerList.indexOf(player);
				locationList.set(index,loc);
				SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.lavaSound,false,lava,plugin.soundRange,plugin.soundVolume);
    		}
    	}
    	else if((stoneCount < 5) && (!player.isInsideVehicle()))
    	{
    		if((dist > plugin.soundNodeSeparation || dist < 0))
    		{
    			if(!playerList.contains(player))
    			{
    				playerList.add(player);
    				locationList.add(loc);    				
    			}
				Integer index = playerList.indexOf(player);
				locationList.set(index,loc);
				Biome biome = loc.getBlock().getBiome();
				if((biome == Biome.TUNDRA)
						|| (biome == Biome.EXTREME_HILLS))
				{
					SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.arcticSound,false,loc,plugin.soundRange,plugin.soundVolume);
				}
				else if (biome == Biome.SWAMPLAND && overWater != true)
				{
					if(event.getPlayer().getWorld().getTime() < 12000)
					{
						SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.swampDaySound,false,loc,plugin.soundRange,plugin.soundVolume);
					}
					else
					{
						SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.swampNightSound,false,loc,plugin.soundRange,plugin.soundVolume);
					}    					
				}
				else if ((biome == Biome.FOREST)
						|| (biome == Biome.TAIGA) && overWater != true)
				{
					if(event.getPlayer().getWorld().getTime() < 12000)
					{
						SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.forestDaySound,false,loc,plugin.soundRange,plugin.soundVolume);
					}
					else
					{
						System.out.println("playing Forest sound for " + player.getName());
						SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.forestNightSound,false,loc,plugin.soundRange,plugin.soundVolume);
					}				
				}
				else if ((biome == Biome.PLAINS) && overWater != true)
				{
					if(event.getPlayer().getWorld().getTime() < 12000)
					{
						SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.openDaySound,false,loc,plugin.soundRange,plugin.soundVolume);
					}
					else
					{
						SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.openNightSound,false,loc,plugin.soundRange,plugin.soundVolume);
					}		
				}
				else if (biome == Biome.DESERT)
				{
					SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.desertSound,false,loc,plugin.soundRange,plugin.soundVolume);
				}
				else if (biome == Biome.OCEAN)
				{
					SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.oceanSound,false,loc,plugin.soundRange,plugin.soundVolume);
				}
				else if (biome == Biome.HELL)
				{
					SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.netherSound,false,loc,plugin.soundRange,plugin.soundVolume);
				}
			}   		
    	}
		else if ((stoneCount > 15) && (!player.isInsideVehicle()))
		{
			if((dist > plugin.soundNodeSeparation || dist < 0))
			{
				if(!playerList.contains(player))
				{					
					playerList.add(player);
					locationList.add(loc);
				}
				Integer index = playerList.indexOf(player);
				locationList.set(index,loc);
				SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.undergroundSound,false,loc,plugin.soundRange,plugin.soundVolume);
			}
	    }
		else if (player.isInsideVehicle())
		{
			Vehicle v = player.getVehicle();
			if((dist > plugin.soundNodeSeparation || dist < 0))
			{
				if(!playerList.contains(player))
				{					
					playerList.add(player);
					locationList.add(loc);
				}
				Integer index = playerList.indexOf(player);
				locationList.set(index,loc);
				if (v instanceof Minecart)
				{
					SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.trainSound,false);
				}
				else if (v instanceof Boat)
				{
					SpoutManager.getSoundManager().playCustomSoundEffect(plugin,(SpoutPlayer) player,plugin.boatSound,false);
				}					
			}
		}
    }
}