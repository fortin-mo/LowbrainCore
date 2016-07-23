package lowbrain.mcrpg.main;

import lowbrain.mcrpg.commun.RPGClass;
import lowbrain.mcrpg.commun.RPGPlayer;
import lowbrain.mcrpg.commun.RPGRace;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RPGCommand implements CommandExecutor{
	private final Main plugin;
	
	public RPGCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Called when the plugin receice a command
	 */
	@Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("mcrpg")&& sender instanceof Player) {

			RPGPlayer rp = plugin.connectedPlayers.get(((Player) sender).getUniqueId());
			if(rp == null)return false;

			if(args.length > 0){

				switch (args[0].toLowerCase()){
                    case "xp":
                        double xp = rp.getNextLvl() - rp.getExperience();
						rp.SendMessage("You will reach lvl " + (rp.getLvl()+1) + " in " + xp + " xp");
                        break;
					case "add":
					    int amount = 0;
					    if(args.length == 2){
					        amount = 1;
                        }
                        else if(args.length == 3){
                            try {
                                amount = Integer.parseInt(args[2]);
                            }
                            catch (Exception e){
								rp.SendMessage("Invalid arguments !. Please use help command");
                            }
                        }
                        if(amount <= 0){
							rp.SendMessage("Please use a number higher then 0!");
                        }
                        switch (args[1].toLowerCase()){
                            case "defence":
							case "def":
                                rp.addDefence(amount,true,true);
                                break;
                            case "health":
							case "hp":
                                rp.addHealth(amount,true,true);
                                break;
                            case "dexterity":
							case "dext":
                                rp.addDexterity(amount,true,true);
                                break;
                            case "strength":
							case "str":
                                rp.addStrength(amount,true,true);
                                break;
                            case "intelligence":
							case "int":
                                rp.addIntelligence(amount,true,true);
                                break;
                            case "magicresistance":
							case "mr":
                                rp.addMagicResistance(amount,true,true);
                                break;
							case "agility":
							case "agi":
								rp.addAgility(amount,true,true);
								break;
                            default:
								rp.SendMessage("There is no such attributes !");
                        }
                        return true;
					case "+defence":
					case "+def":
						rp.addDefence(1,true,true);
						break;
					case "+strength":
					case "+str":
						rp.addStrength(1,true,true);
						break;
					case "+intelligence":
					case "+int":
						rp.addIntelligence(1,true,true);
						break;
					case "+health":
					case "+hp":
						rp.addHealth(1,true,true);
						break;
					case "+magicresistance":
					case "+mr":
						rp.addMagicResistance(1,true,true);
						break;
					case "+dexterity":
					case "+dext":
						rp.addDexterity(1,true,true);
						break;
					case "+agility":
					case "+agi":
						rp.addAgility(1,true,true);
						break;
					case "stats":
						if(args.length == 2){
							Player p = plugin.getServer().getPlayer(args[1]);
							if(p != null){
								RPGPlayer rp2 = plugin.connectedPlayers.get(p.getUniqueId());
								if(rp2 != null){
									rp.SendMessage(rp2.toString());
								}else{
									rp.SendMessage("This player is not connected !");
								}
							}
							else{
								rp.SendMessage("This player is not connected !");
							}
						}
						else{
							rp.SendMessage(rp.toString());
                        }
						break;
					case "classes":
						String cls = "";
						for (String s :
								plugin.classesConfig.getKeys(false)) {
							RPGClass rc = new RPGClass(s);
							cls += "-----------------------------" + "\n";
							cls += rc.toString() + "\n";
						}
						sender.sendMessage(ChatColor.GREEN +cls);
						break;
					case "races":
						String rcs = "";
						for (String s :
								plugin.racesConfig.getKeys(false)) {
							RPGRace rr = new RPGRace(s);
							rcs += "-----------------------------" + "\n";
							rcs += rr.toString() + "\n";
						}
						sender.sendMessage(ChatColor.GREEN +rcs);
						break;
					case "setclass":
						if(args.length == 2){
							if(plugin.classesConfig.getKeys(false).contains(args[1])){
								rp.setClass(args[1], false);
							}
							else if(args[1].equalsIgnoreCase("rdm") || args[1].equalsIgnoreCase("random")){
								int max = plugin.classesConfig.getKeys(false).size() - 1;
								int rdm = 0 + ((int)Math.random() * max);
								rp.setClass((String)plugin.classesConfig.getKeys(false).toArray()[rdm],false);
							}
							else{
								rp.SendMessage("Wrong class !: Use \"/mcrpg class\" to show all classes");
							}
						}
						break;
					case "setrace":
						if(args.length == 2){
							if(plugin.racesConfig.getKeys(false).contains(args[1])){
								rp.setRace(args[1], false);
							}
							else if(args[1].equalsIgnoreCase("rdm") || args[1].equalsIgnoreCase("random")){
								int max = plugin.racesConfig.getKeys(false).size() - 1;
								int rdm = 0 + ((int)Math.random() * max);
								rp.setRace((String)plugin.racesConfig.getKeys(false).toArray()[rdm],false);
							}
							else{
								rp.SendMessage("Wrong race !: Use \"/mcrpg races\" to show all races");
							}
						}
						break;
					case "reset":
						if(args.length == 2){
								if(plugin.classesConfig.getKeys(false).contains(args[1])){
									rp.reset(args[1],null);
								}
								else{
									rp.SendMessage("There is no such class !");
								}
						}
						else if(args.length == 3){
							if(plugin.classesConfig.getKeys(false).contains(args[1]) && plugin.racesConfig.getKeys(false).contains(args[2])){
								rp.reset(args[1],args[2]);
							}
							else{
								sender.sendMessage(ChatColor.GREEN + "There is no such class or race !");
							}
						}
						else{
							rp.reset(rp.getClassName(),rp.getRaceName());
						}
						break;
					case "completereset":
							if(args.length == 2 && args[1].toLowerCase().equals("yes")){
								rp.resetAll();
							}
							else{
								rp.SendMessage("You must validate your choice with yes! : /mcrpg completereset yes");
							}
						break;
					case "nextlvl":
						rp.SendMessage("Next level achieved at " + rp.getNextLvl() + " xp");
						break;
					case "save":
						rp.SaveData();
						rp.SendMessage("Stats saved !");
						break;
					case "save-all":
					    if(sender.isOp()) {
                            plugin.SaveData();
							rp.SendMessage("All stats saved !");
                        }
						break;
					case "cast":
						if(args.length == 2){
						 	rp.castSpell(args[1],null);
						}
						else if(args.length == 3){
							Player p = plugin.getServer().getPlayer(args[2]);
							if(p == null){
								rp.SendMessage("Player not available !");
							}
							else {
								RPGPlayer to = plugin.connectedPlayers.get(p.getUniqueId());
								if(to == null){
									rp.SendMessage("Player not available !");
								}
								else{
									rp.castSpell(args[1],to);
								}
							}
						}
						break;
				}
				return true;
			}
		}
		return false;
    }
}
