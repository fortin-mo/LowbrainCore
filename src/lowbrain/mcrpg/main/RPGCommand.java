package lowbrain.mcrpg.main;

import lowbrain.mcrpg.commun.RPGClass;
import lowbrain.mcrpg.commun.RPGPlayer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
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

			if(args.length > 0){

				switch (args[0].toLowerCase()){
                    case "xp":
                        double xp = rp.getNextLvl() - rp.getExperience();
                        sender.sendMessage(ChatColor.GREEN +"You will reach lvl " + (rp.getLvl()+1) + " in " + xp + " xp");
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
                                sender.sendMessage(ChatColor.GREEN +"Invalid arguments !. Please use help command");
                                return false;
                            }
                        }
                        if(amount <= 0){
                            sender.sendMessage(ChatColor.GREEN +"Please use a number higher then 0!");
                            return false;
                        }
                        switch (args[1].toLowerCase()){
                            case "defence":
							case "def":
                                rp.addDefence(amount,true);
                                break;
                            case "health":
							case "hp":
                                rp.addHealth(amount,true);
                                break;
                            case "dexterity":
							case "dext":
                                rp.addDexterity(amount,true);
                                break;
                            case "strength":
							case "str":
                                rp.addStrength(amount,true);
                                break;
                            case "intelligence":
							case "int":
                                rp.addIntelligence(amount,true);
                                break;
                            case "magicresistance":
							case "mr":
                                rp.addMagicResistance(amount,true);
                                break;
							case "agility":
							case "agi":
								rp.addAgility(amount,true);
								break;
                            default:
                                sender.sendMessage(ChatColor.GREEN +"There is no such attributes !");
                                return false;
                        }
                        return true;
					case "+defence":
					case "+def":
						rp.addDefence(1,true);
						break;
					case "+strength":
					case "+str":
						rp.addStrength(1,true);
						break;
					case "+intelligence":
					case "+int":
						rp.addIntelligence(1,true);
						break;
					case "+health":
					case "+hp":
						rp.addHealth(1,true);
						break;
					case "+magicresistance":
					case "+mr":
						rp.addMagicResistance(1,true);
						break;
					case "+dexterity":
					case "+dext":
						rp.addDexterity(1,true);
						break;
					case "+agility":
					case "+agi":
						rp.addAgility(1,true);
						break;
					case "stats":
						if(args.length == 2){
							Player p = plugin.getServer().getPlayer(args[1]);
							if(p != null){
								RPGPlayer rp2 = plugin.connectedPlayers.get(p.getUniqueId());
								if(rp2 != null){
                                    sender.sendMessage(ChatColor.GREEN +rp2.toString());
								}else{
									sender.sendMessage(ChatColor.GREEN +"This player is not connected !");
								}
							}
							else{
								sender.sendMessage(ChatColor.GREEN +"This player is not connected !");
							}
						}
						else{
						    sender.sendMessage(ChatColor.GREEN +rp.toString());
                        }
						break;
					case "classes":
						String msg = "";

                        for (int id :
                                plugin.settings.lstClassId) {
                            RPGClass rc = new RPGClass(id);
                            msg += "-----------------------------" + "\n";
                            msg += rc.toString() + "\n";
                        }
						sender.sendMessage(ChatColor.GREEN +msg);
						break;
					case "setclass":
						if(args.length == 2){
							try {
								int idClass = Integer.parseInt(args[1]);
								if(plugin.settings.lstClassId.contains(idClass)){
									rp.SetClass(idClass, false);
								}
								else{
									sender.sendMessage(ChatColor.GREEN + "Wrong class id !: Use \"/mcrpg class\" to show all classes");
								}
							}catch (Exception e){
								sender.sendMessage(ChatColor.GREEN +"Invalid arguments !");
                                return false;
							}
						}
						break;
					case "reset":
						if(args.length >= 2){
							try {
								int idClass = Integer.parseInt(args[1]);
								rp.reset(idClass);
							}catch (Exception e){
								sender.sendMessage(ChatColor.GREEN +"Wrong arguments type ! Must be a number between 1 and 4 include");
                                return false;
							}
						}
						else{
							rp.reset(rp.getIdClass());
						}
						break;
					case "nextlvl":
						sender.sendMessage(ChatColor.GREEN +"Next level achieved at " + rp.getNextLvl() + " xp");
						break;
					case "save":
						rp.SaveData();
						sender.sendMessage(ChatColor.GREEN +"Stats saved !");
						break;
					case "save-all":
					    if(sender.isOp()) {
                            plugin.SaveData();
                            sender.sendMessage(ChatColor.GREEN +"All stats saved !");
                        }
						break;
				}
				return true;
			}
		}
		return false;
    }
}
