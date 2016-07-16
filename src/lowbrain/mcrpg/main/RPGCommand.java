package lowbrain.mcrpg.main;

import lowbrain.mcrpg.commun.RPGClass;
import lowbrain.mcrpg.commun.RPGPlayer;
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
                    case "xpneeded":
                        double xp = rp.getNextLvl() - rp.getExperience();
                        sender.sendMessage("You will reach lvl " + (rp.getLvl()+1) + " in " + xp + " xp");
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
                                sender.sendMessage("Invalid arguments !. Please use help command");
                                return false;
                            }
                        }
                        if(amount <= 0){
                            sender.sendMessage("Please use a number higher then 0!");
                            return false;
                        }
                        switch (args[1].toLowerCase()){
                            case "defence":
                                rp.addDefence(amount,true);
                                break;
                            case "health":
                                rp.addHealth(amount,true);
                                break;
                            case "dexterity":
                                rp.addDexterity(amount,true);
                                break;
                            case "strength":
                                rp.addStrength(amount,true);
                                break;
                            case "intelligence":
                                rp.addIntelligence(amount,true);
                                break;
                            case "magicresistance":
                                rp.addMagicResistance(amount,true);
                                break;
                            default:
                                sender.sendMessage("There is no such attributes !");
                                return false;
                        }
                        return true;
					case "+defence":
						rp.addDefence(1,true);
						break;
					case "+strength":
						rp.addStrength(1,true);
						break;
					case "+intelligence":
						rp.addIntelligence(1,true);
						break;
					case "+health":
						rp.addHealth(1,true);
						break;
					case "+magicresistance":
						rp.addMagicResistance(1,true);
						break;
					case "+dexterity":
						rp.addDexterity(1,true);
						break;
					case "stats":
						if(args.length == 2){
							Player p = plugin.getServer().getPlayer(args[1]);
							if(p != null){
								RPGPlayer rp2 = plugin.connectedPlayers.get(p.getUniqueId());
								if(rp2 != null){
                                    sender.sendMessage(rp2.toString());
								}else{
									sender.sendMessage("This player is not connected !");
								}
							}
							else{
								sender.sendMessage("This player is not connected !");
							}
						}
						else{
						    sender.sendMessage(rp.toString());
                        }
						break;
					case "class":
						RPGClass k = new RPGClass(1);
						RPGClass pa = new RPGClass(2);
						RPGClass a = new RPGClass(3);
						RPGClass m = new RPGClass(4);


						String msg = "-----------------------------" + "\n";
                        msg += k.toString() + "\n";
                        msg += "-----------------------------" + "\n";
						msg += pa.toString() + "\n";
                        msg += "-----------------------------" + "\n";
						msg += a.toString() + "\n";
                        msg += "-----------------------------" + "\n";
						msg += m.toString() + "\n";

						sender.sendMessage(msg);
						break;
					case "setclass":
						if(args.length == 2){
							try {
								int idClass = Integer.parseInt(args[1]);
								if(idClass >= 1 && idClass <= 4){
									rp.SetClass(idClass, false);
								}
								else{
									sender.sendMessage("Wrong class id !: Use \"/mcrpg class\" to show all classes");
								}
							}catch (Exception e){
								sender.sendMessage("Wrong arguments type ! Must be a number between 1 and 4 include");
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
								sender.sendMessage("Wrong arguments type ! Must be a number between 1 and 4 include");
                                return false;
							}
						}
						else{
							rp.reset(rp.getIdClass());
						}
						break;
					case "nextlvl":
						sender.sendMessage("Next level achieved at " + rp.getNextLvl() + " xp");
						break;
					case "save":
						rp.SaveData();
						sender.sendMessage("Stats saved !");
						break;
					case "save-all":
						plugin.SaveData();
						sender.sendMessage("All stats saved !");
						break;
				}
				return true;
			}
		}
		return false;
    }
}
