package lowbrain.core.main;

import lowbrain.core.commun.Helper;
import lowbrain.core.commun.Settings;
import lowbrain.core.config.Classes;
import lowbrain.core.config.Internationalization;
import lowbrain.core.config.Races;
import lowbrain.core.rpg.*;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CommandHandler implements CommandExecutor{
	private final LowbrainCore plugin;
	
	public CommandHandler(LowbrainCore plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Called when the plugin receice a command
	 */
	@Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("lbcore")) {
			if(args.length > 0){
				if(sender instanceof Player){
					LowbrainPlayer rp = LowbrainCore.getInstance().getPlayerHandler().getList().get(((Player) sender).getUniqueId());
					if(rp == null)return false;
					switch (args[0].toLowerCase()){
						case "xp":
							double xp = rp.getNextLvl() - rp.getExperience();
							rp.sendMessage(Internationalization.getInstance().getString("you_will_reach_level") + " "
									+ (rp.getLvl()+1)
									+ " " + Internationalization.getInstance().getString("in") + " "
									+ xp + " xp");
							return true;
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
									rp.sendMessage(Internationalization.getInstance().getString("use_help"));
								}
							}
							if(amount <= 0){
								rp.sendMessage(Internationalization.getInstance().getString("number_higher_then_zero"));
							}
							switch (args[1].toLowerCase()){
								case "defence":
								case "def":
									rp.addDefence(amount,true,true);
									break;
								case "vitality":
								case "vit":
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
									rp.sendMessage(Internationalization.getInstance().getString("invalid_attribute"));
							}
							return true;
						case "+defence":
						case "+def":
							rp.addDefence(1,true,true);
							return true;
						case "+strength":
						case "+str":
							rp.addStrength(1,true,true);
							return true;
						case "+intelligence":
						case "+int":
							rp.addIntelligence(1,true,true);
							return true;
						case "+health":
						case "+hp":
							rp.addHealth(1,true,true);
							return true;
						case "+magicresistance":
						case "+mr":
							rp.addMagicResistance(1,true,true);
							return true;
						case "+dexterity":
						case "+dext":
							rp.addDexterity(1,true,true);
							return true;
						case "+agility":
						case "+agi":
							rp.addAgility(1,true,true);
							return true;
						case "stats":
							if(args.length == 2 && senderHasPermission(sender,"core.stats-others")){
								Player p = plugin.getServer().getPlayer(args[1]);
								if(p != null){
									LowbrainPlayer rp2 = LowbrainCore.getInstance().getPlayerHandler().getList().get(p.getUniqueId());
									if(rp2 != null){
										rp.sendMessage(rp2.toString());
									}else{
										rp.sendMessage(Internationalization.getInstance().getString("player_not_connected"));
									}
								}
								else{
									rp.sendMessage(Internationalization.getInstance().getString("player_not_connected"));
								}
							}
							else{
								rp.sendMessage(rp.toString());
							}
							return true;
						case "classes":
							String cls = "";
							for (String s :
									Classes.getInstance().getKeys(false)) {
								LowbrainClass rc = new LowbrainClass(s);
								cls += "-----------------------------" + "\n";
								cls += rc.toString() + "\n";
							}
							sender.sendMessage(ChatColor.GREEN +cls);
							return true;
						case "races":
							String rcs = "";
							for (String s :
									Races.getInstance().getKeys(false)) {
								LowbrainRace rr = new LowbrainRace(s);
								rcs += "-----------------------------" + "\n";
								rcs += rr.toString() + "\n";
							}
							sender.sendMessage(ChatColor.GREEN +rcs);
							return true;
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
									rp.sendMessage(Internationalization.getInstance().getString("invalid_class"));
								}
							}
							return true;
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
									rp.sendMessage(Internationalization.getInstance().getString("invalid_race"));
								}
							}
							return true;
						case "reset":
							if(args.length == 2){
								if(Classes.getInstance().getKeys(false).contains(args[1])){
									rp.reset(args[1],null);
								}
								else{
									rp.sendMessage(Internationalization.getInstance().getString("no_such_class"));
								}
							}
							else if(args.length == 3){
								if(Classes.getInstance().getKeys(false).contains(args[1]) && Classes.getInstance().getKeys(false).contains(args[2])){
									rp.reset(args[1],args[2]);
								}
								else{
									sender.sendMessage(ChatColor.RED + Internationalization.getInstance().getString("no_such_class_or_race"));
								}
							}
							else{
								rp.reset(rp.getClassName(),rp.getRaceName());
							}
							return true;
						case "completereset":
							if(args.length == 2 && args[1].toLowerCase().equals("yes")){
								rp.resetAll(false);
							}
							else{
								rp.sendMessage(Internationalization.getInstance().getString("validate_complete_reset"));
							}
							return true;
						case "nextlvl":
							rp.sendMessage(Internationalization.getInstance().getString("next_level_at") + " " + rp.getNextLvl() + " xp");
							return true;
						case "save":
							if(senderHasPermission(sender,"core.save")){
								rp.saveData();
								rp.sendMessage(Internationalization.getInstance().getString("stats_saved"));
							}
							return true;
						case "cast":
							if(args.length == 2){
								rp.castSpell(args[1],null);
							}
							else if(args.length == 3){
								Player p = plugin.getServer().getPlayer(args[2]);
								if(p == null){
									rp.sendMessage(Internationalization.getInstance().getString("player_not_connected"));
								}
								else {
									LowbrainPlayer to = LowbrainCore.getInstance().getPlayerHandler().getList().get(p.getUniqueId());
									if(to == null){
										rp.sendMessage(Internationalization.getInstance().getString("player_not_connected"));
									}
									else{
										rp.castSpell(args[1],to);
									}
								}
							}
							return true;
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
							return true;
						case "mobkills":
							String msg = "";
							if(args.length == 1){
								for(Map.Entry<String, Integer> s : rp.getMobKills().entrySet()) {
									String n = s.getKey();
									int v = s.getValue();
									msg += n + " : " + v + "\n";
								}
								rp.sendMessage(msg);
							}
							else if(args.length == 2){
								if(rp.getMobKills().containsKey(args[1].toLowerCase()));
								msg = args[1] + " : " + rp.getMobKills().get(args[1].toLowerCase());
								rp.sendMessage(msg);
							}
							return true;
						case "setskill":
							if(args.length == 2){
								rp.setCurrentSkill(args[1].toLowerCase());
							}
							else return false;
							return true;
						case "getskill":
							if(rp.getCurrentSkill() != null){
								rp.sendMessage(Internationalization.getInstance().getString("current_skill") + " " + rp.getCurrentSkill().getName());
							}
							else{
								rp.sendMessage(Internationalization.getInstance().getString("no_current_skill"));
							}
							return true;
						case "upskill":
							if(args.length == 2){
								rp.upgradeSkill(args[1].toLowerCase());
							}
							else return false;
							return true;
						case "skills":
							String sk = "";
							for(Map.Entry<String, LowbrainSkill> s : rp.getSkills().entrySet()) {
								String n = s.getValue().getName();
								int v = s.getValue().getCurrentLevel();
								sk += n + " : " + v + "\n";
							}
							rp.sendMessage(sk);
							return true;
						case "myskill":
							if(args.length == 2){
								LowbrainSkill s = rp.getSkills().get(args[1]);
								if(s != null){
									rp.sendMessage(s.info(),ChatColor.LIGHT_PURPLE);
								}
								else{
									rp.sendMessage(Internationalization.getInstance().getString("no_such_skill"),ChatColor.RED);
								}
							}
							return true;
						case "skill":
							if(args.length == 2){
								LowbrainSkill s = rp.getSkills().get(args[1]);
								if(s != null){
									rp.sendMessage(s.toString(),ChatColor.LIGHT_PURPLE);
								}
								else{
									rp.sendMessage(Internationalization.getInstance().getString("no_such_skill"),ChatColor.RED);
								}
							}
							return true;
						case "mypowers":
							String pws = "";
							for (LowbrainPower power :
									rp.getPowers().values()) {
								pws += power.getName() + ", ";
							}
							rp.sendMessage(Helper.StringIsNullOrEmpty(pws) ? Internationalization.getInstance().getString("no_powers_available") : pws);
							return true;
					}
				}

				switch (args[0].toLowerCase()){
					case "save-all":
						if(senderHasPermission(sender,"core.save-all")) {
							plugin.saveData();
							sender.sendMessage(Internationalization.getInstance().getString("all_stats_saved"));
						}
						return true;
					case "reload":
						if(senderHasPermission(sender,"core.reload")){
							plugin.reloadConfig();
							sender.sendMessage(Internationalization.getInstance().getString("config_file_reloaded"));
						}
						return true;
					case "set":
						if(args.length == 4 && senderHasPermission(sender,"core.setattributes-others")){
							String pName = args[1];
							String attribute = args[2].toLowerCase();
							String sValue = args[3];

							Player p = plugin.getServer().getPlayer(pName);
							LowbrainPlayer rp = p != null ? LowbrainCore.getInstance().getPlayerHandler().getList().get(p.getUniqueId()) : null;
							if(rp == null){
								sender.sendMessage(Internationalization.getInstance().getString("player_not_connected"));
							}
							else{
								int value = Helper.intTryParse(sValue,-1);
								if(value < 0){
									sender.sendMessage(Internationalization.getInstance().getString("invalid_value"));
								}
								else{
									switch (attribute){
										default:
											sender.sendMessage(Internationalization.getInstance().getString("invalid_attribute"));
											break;
										case "intelligence":
										case "intel":
										case "int":
											rp.setIntelligence(value);
											break;
										case "dexterity":
										case "dext":
											rp.setDexterity(value);
											break;
										case "level":
										case "lvl":
											rp.setLvl(value,true);
											break;
										case "agility":
										case "agi":
											rp.setAgility(value);
											break;
										case "health":
										case "hp":
											rp.setVitality(value);
											break;
										case "strength":
										case "str":
											rp.setStrength(value);
											break;
										case "defence":
										case "def":
											rp.setDefence(value);
											break;
										case "magicresistance":
										case "mr":
										case "magicresist":
										case "magic_resistance":
											rp.setMagicResistance(value);
											break;
										case "kills":
										case "kill":
											rp.setKills(value);
											break;
										case "deaths":
										case "death":
											rp.setDeaths(value);
											break;
										case "experience":
										case "xp":
										case "exp":
											rp.setExperience(value);
											break;
										case "points":
										case "point":
										case "pts":
											rp.setPoints(value);
											break;
										case "skillpoints":
										case "skillpoint":
										case "skp":
											rp.setSkillPoints(value);
											break;
									}
									sender.sendMessage("Done !");
								}
							}
						}
						else{
							return false;
						}
						return true;
				}
			}
			else{
				return false;
			}
		}
		return false;
    }

	/**
	 * check sender pemission
	 * @param sender
	 * @param permission
	 * @return
	 */
    private boolean senderHasPermission(CommandSender sender, String permission){
    	if(sender.isOp() && Settings.getInstance().op_bypass_permission){
    		return true;
		}
		if(sender instanceof  ConsoleCommandSender){
			return true;
		}
		if(sender.hasPermission(permission)){
			return true;
		}
		sender.sendMessage(ChatColor.RED + Internationalization.getInstance().getString("insufficient_permission"));
		return false;
	}
}
