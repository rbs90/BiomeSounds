package de.rbs90.bukkit.plugins.biomeSounds;

import java.util.List;

import java.util.logging.Logger;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.PluginManager;

public class BiomeSoundMain extends JavaPlugin
{
	public String arcticSound = "http://your.site.here/arcticwind.ogg";
	public String desertSound = "http://your.site.here/desertwind.ogg";
	public String netherSound = "http://your.site.here/nether.ogg";
	public String swampDaySound = "http://your.site.here/swamp_day.ogg";
	public String swampNightSound = "http://your.site.here/swamp_night.ogg";
	public String forestDaySound = "http://your.site.here/forest_day.ogg";
	public String forestNightSound = "http://your.site.here/forest_night.ogg";
	public String openDaySound = "http://your.site.here/daytime.ogg";
	public String openNightSound = "http://your.site.here/nighttime.ogg";
	public String oceanSound = "http://your.site.here/ocean.ogg";
	public String undergroundSound = "http://your.site.here/underground.ogg";
	public String boatSound = "http://your.site.here/rowing.ogg";
	public String trainSound = "http://your.site.here/train.ogg";
	public String lavaSound = "http://your.site.here/lava.ogg";
	public Integer soundRange = 45;
	public Integer soundVolume = 70;
	public Integer soundNodeSeparation = 20;
	public static final Logger log = Logger.getLogger("Minecraft");
    private final BiomeSoundListener playerListener = new BiomeSoundListener(this);

	public void onDisable()
	{
		PluginDescriptionFile pdfFile = this.getDescription();
        log.info(pdfFile.getName()+" version "+pdfFile.getVersion()+" is disabled!");
	}

	public void onEnable() 
	{
		PluginManager pm = getServer().getPluginManager();
	    pm.registerEvent(Event.Type.PLAYER_MOVE, playerListener, Event.Priority.Normal, this);
	    pm.registerEvent(Event.Type.PLAYER_QUIT, playerListener, Event.Priority.Normal, this);
		PluginDescriptionFile pdfFile = this.getDescription();

		log.info(pdfFile.getName()+" version "+pdfFile.getVersion()+" is enabled!");

		reload();
	}

    @SuppressWarnings("deprecation")
	public void reload()
    {
    	getConfiguration().load();
    	arcticSound = getConfiguration().getString("sound.arctic","http://your.site.here/arcticwind.ogg");
    	desertSound = getConfiguration().getString("sound.desert","http://your.site.here/desertwind.ogg");
    	netherSound = getConfiguration().getString("sound.nether","http://your.site.here/nether.ogg");
    	swampDaySound = getConfiguration().getString("sound.swamp.day","http://your.site.here/swamp_day.ogg");
    	swampNightSound = getConfiguration().getString("sound.swamp.night","http://your.site.here/swamp_night.ogg");
    	forestDaySound = getConfiguration().getString("sound.forest.day","http://your.site.here/forest_day.ogg");
    	forestNightSound = getConfiguration().getString("sound.forest.night","http://your.site.here/forest_night.ogg");
    	openDaySound = getConfiguration().getString("sound.open.day","http://your.site.here/daytime.ogg");
    	openNightSound = getConfiguration().getString("sound.open.night","http://your.site.here/nighttime.ogg");
    	undergroundSound = getConfiguration().getString("sound.underground","http://your.site.here/underground.ogg");
    	oceanSound = getConfiguration().getString("sound.ocean","http://your.site.here/ocean.ogg");
    	boatSound = getConfiguration().getString("sound.boat","http://your.site.here/rowing.ogg");
    	trainSound = getConfiguration().getString("sound.train","http://your.site.here/train.ogg");
    	lavaSound = getConfiguration().getString("sound.lava","http://your.site.here/lava.ogg");
    	soundRange = getConfiguration().getInt("sound.range", 45);
    	soundVolume = getConfiguration().getInt("sound.volume", 70);
    	soundNodeSeparation = getConfiguration().getInt("sound.nodeseparation", 20);
    	getConfiguration().save();
    }

	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args)
	{
        if (command.getName().equalsIgnoreCase("biome"))
        {
            if (!(sender instanceof Player))
            {
                if (args.length >= 1)
                {
                	List<Player> players = getServer().matchPlayer(args[0]);

                	if (players.isEmpty())
                	{
                		log.info("Cannot use biome. "+args[0]+" is not online!");
                	}
                	else
                    {
                		log.info(players.get(0).getName()+" is in a "+players.get(0).getLocation().getBlock().getBiome().name().toLowerCase().replace("_"," ")+" biome!");
                    }
                }
                else
                {
                	log.info("Usage: "+commandLabel+" <player>");
                }
                return true;
            }

            Player player = (Player)sender;

            if (args.length >= 1)
            {
           	    List<Player> players = getServer().matchPlayer(args[0]);

           	    if (players.isEmpty())
           	    {
               		player.sendMessage("�cCannot use that command. "+args[0]+" is not online!");
               	}
               	else
                {
               		if (player.getName().equalsIgnoreCase(players.get(0).getName()))
           	    	{
           		        player.sendMessage("�7You are in a "+players.get(0).getLocation().getBlock().getBiome().name().replace("_"," ")+" biome.");
           	    	}
            	    else
            		{
            	    	if(player.isOp())
            	    	{
            	    		player.sendMessage(players.get(0).getName()+" is in a "+players.get(0).getLocation().getBlock().getBiome().name().replace("_"," ")+" biome.");
            	    	}
            	    	else
            	    	{
            	    		player.sendMessage("You are not allowed to check the biome of others!");
            	    	}
                    }
            	}
            }
            else
            {
                player.sendMessage("You are in a "+player.getLocation().getBlock().getBiome().name().replace("_"," ")+" biome.");
            }
            return true;
        }
        return false;
    }
}