package lowbrain.mcrpg.main;

import lowbrain.mcrpg.commun.Helper;
import lowbrain.mcrpg.config.Classes;
import lowbrain.mcrpg.config.Races;
import lowbrain.mcrpg.rpg.RPGClass;
import lowbrain.mcrpg.rpg.RPGPlayer;
import lowbrain.mcrpg.rpg.RPGRace;
import lowbrain.mcrpg.rpg.RPGSkill;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

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
					default:
						return false;
					case "reload":
						if(rp.getPlayer().isOp() || rp.getPlayer().hasPermission("mcrpg.reload")){
							plugin.reloadConfig();
							rp.SendMessage("Configuration files reloaded !", ChatColor.GRAY);
						}
						break;
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
						if(args.length == 2 && (sender.isOp() || sender.hasPermission("mcrpg.stats-others"))){
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
								Classes.getInstance().getKeys(false)) {
							RPGClass rc = new RPGClass(s);
							cls += "-----------------------------" + "\n";
							cls += rc.toString() + "\n";
						}
						sender.sendMessage(ChatColor.GREEN +cls);
						break;
					case "races":
						String rcs = "";
						for (String s :
								Races.getInstance().getKeys(false)) {
							RPGRace rr = new RPGRace(s);
							rcs += "-----------------------------" + "\n";
							rcs += rr.toString() + "\n";
						}
						sender.sendMessage(ChatColor.GREEN +rcs);
						break;
					case "setclass":
						if(args.length == 2){
							if(Classes.getInstance().getKeys(false).contains(args[1])){
								rp.setClass(args[1], false);
							}
							else if(args[1].equalsIgnoreCase("rdm") || args[1].equalsIgnoreCase("random")){
								int max = Classes.getInstance().getKeys(false).size() - 1;
								int rdm = Helper.randomInt(0,max);
								rp.setClass((String)Classes.getInstance().getKeys(false).toArray()[rdm],false);
							}
							else{
								rp.SendMessage("Wrong class !: Use \"/mcrpg class\" to show all classes");
							}
						}
						break;
					case "setrace":
						if(args.length == 2){
							if(Races.getInstance().getKeys(false).contains(args[1])){
								rp.setRace(args[1], false);
							}
							else if(args[1].equalsIgnoreCase("rdm") || args[1].equalsIgnoreCase("random")){
								int max = Races.getInstance().getKeys(false).size() - 1;
								int rdm = Helper.randomInt(0,max);
								rp.setRace((String)Races.getInstance().getKeys(false).toArray()[rdm],false);
							}
							else{
								rp.SendMessage("Wrong race !: Use \"/mcrpg races\" to show all races");
							}
						}
						break;
					case "reset":
						if(args.length == 2){
								if(Classes.getInstance().getKeys(false).contains(args[1])){
									rp.reset(args[1],null);
								}
								else{
									rp.SendMessage("There is no such class !");
								}
						}
						else if(args.length == 3){
							if(Classes.getInstance().getKeys(false).contains(args[1]) && Classes.getInstance().getKeys(false).contains(args[2])){
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
						if(sender.isOp() || sender.hasPermission("mcrpg.save")){
							rp.SaveData();
							rp.SendMessage("Stats saved !");
						}
						break;
					case "save-all":
					    if(sender.isOp() || sender.hasPermission("mcrpg.save-all")) {
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
					case "togglestats":
						if(args.length == 1){
							rp.toggleScoreboard();
						}
						else if(args.length == 2){
							switch (args[2].toLowerCase()){
								case "true":
								case "1":
									rp.toggleScoreboard(true);
									break;
								case "false":
								case "0":
									rp.toggleScoreboard(false);
									break;
							}
						}
						break;
					case "mobkills":
						String msg = "";
						if(args.length == 1){
							for(Map.Entry<String, Integer> s : rp.getMobKills().entrySet()) {
								String n = s.getKey();
								int v = s.getValue();
								msg += n + " : " + v + "\n";
							}
							rp.SendMessage(msg);
						}
						else if(args.length == 2){
							if(rp.getMobKills().containsKey(args[1].toLowerCase()));
							msg = args[1] + " : " + rp.getMobKills().get(args[1].toLowerCase());
							rp.SendMessage(msg);
						}
						break;
					case "setskill":
						if(args.length == 2){
							rp.setCurrentSkill(args[1].toLowerCase());
						}
						else return false;
						break;
					case "getskill":
						if(rp.getCurrentSkill() != null){
							rp.SendMessage("Current skill : " + rp.getCurrentSkill().getName());
						}
						else{
							rp.SendMessage("Current skill : none");
						}
						break;
					case "upskill":
						if(args.length == 2){
							rp.upgradeSkill(args[1].toLowerCase());
						}
						else return false;
						break;
					case "skills":
						String sk = "";
						for(Map.Entry<String, RPGSkill> s : rp.getSkills().entrySet()) {
							String n = s.getValue().getName();
							int v = s.getValue().getCurrentLevel();
							sk += n + " : " + v + "\n";
						}
						rp.SendMessage(sk);
						break;
				}
				return true;
			}
		}
		return false;
    }
}
