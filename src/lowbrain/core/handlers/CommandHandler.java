package lowbrain.core.handlers;

import lowbrain.core.commun.Settings;
import lowbrain.core.main.LowbrainCore;
import lowbrain.core.rpg.*;
import lowbrain.library.fn;
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
        if (!cmd.getName().equalsIgnoreCase("lbcore"))
            return false;

        if (args.length <= 0)
            return false;

        if(sender instanceof Player){
            LowbrainPlayer rp = LowbrainCore.getInstance().getPlayerHandler().getList().get(((Player) sender).getUniqueId());

            if(rp == null)
                return false;

            switch (args[0].toLowerCase()){
                case "xp":
                    return onXp(rp, args);
                case "add":
                    return onAdd(rp, args);
                case "+":
                    return onAdd(rp, args);
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
                case "+vitality":
                case "+vit":
                    rp.addVitality(1,true,true);
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
                    return onStats(rp, args, sender);
                case "classes":
                    return onClasses(rp, args);
                case "races":
                    return onRaces(rp, args);
                case "setclass":
                    return onSetClass(rp, args);
                case "setrace":
                    return onSetRace(rp, args);
                case "reset":
                    return onReset(rp, args);
                case "completereset":
                    return onCompleteReset(rp, args);
                case "nextlvl":
                    rp.sendMessage(plugin.getConfigHandler().internationalization().format("next_level_at", rp.getNextLvl()));
                    return true;
                case "save":
                    if(senderHasPermission(sender,"lb.core.save")){
                        rp.saveData();
                        rp.sendMessage(plugin.getConfigHandler().internationalization().format("stats_saved"));
                    }
                    return true;
                case "cast":
                   return onCast(rp, args);
                case "togglestats":
                    return onToggleStats(rp, args);
                case "showstats":
                    return onShowStats(rp);
                case "hidestats":
                    return onHideStats(rp);
                case "mobkills":
                    return onMobKills(rp, args);
                case "setskill":
                    if (args.length != 2)
                        return false;
                    rp.setCurrentSkill(args[1].toLowerCase());
                    return true;
                case "getskill":
                    if(rp.getCurrentSkill() != null)
                        rp.sendMessage(plugin.getConfigHandler().internationalization().format("current_skill", rp.getCurrentSkill().getName()));
                    else
                        rp.sendMessage(plugin.getConfigHandler().internationalization().format("no_current_skill"));

                    return true;
                case "upskill":
                    if (args.length != 2)
                        return false;
                    rp.upgradeSkill(args[1].toLowerCase());
                    return true;
                case "skills":
                    return onSkills(rp, args);
                case "myskill":
                    return onMySkill(rp, args);
                case "skill":
                    return onSkill(rp, args);
                case "mypowers":
                    return onMyPowers(rp, args);
            }
        }

        switch (args[0].toLowerCase()){
            case "save-all":
                return onSaveAll(sender, args);
            case "reload":
                return onReload(sender, args);
            case "set":
                return onSet(sender, args);
        }
        return false;
    }

    private boolean onSet(CommandSender sender, String[] args){
	    if (args.length != 4)
	        return false;

        if(!senderHasPermission(sender,"lb.core.setattributes-others"))
            return true;

        String pName = args[1];
        String attribute = args[2].toLowerCase();
        String sValue = args[3];

        Player p = plugin.getServer().getPlayer(pName);
        LowbrainPlayer rp = p != null ? LowbrainCore.getInstance().getPlayerHandler().getList().get(p.getUniqueId()) : null;

        if(rp == null){
            sender.sendMessage(plugin.getConfigHandler().internationalization().format("player_not_connected"));
            return true;
        }

        int value = fn.toInteger(sValue,-1);
        if(value < 0){
            sender.sendMessage(plugin.getConfigHandler().internationalization().format("invalid_value"));
            return true;
        }

        switch (attribute){
            default:
                sender.sendMessage(plugin.getConfigHandler().internationalization().format("invalid_attribute"));
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

        return true;
    }

    private boolean onReload (CommandSender sender, String[] args){
        if(senderHasPermission(sender,"lb.core.reload")){
            plugin.reloadConfig();
            sender.sendMessage(plugin.getConfigHandler().internationalization().format("config_file_reloaded"));
        }
        return true;
    }

    private boolean onSaveAll(CommandSender sender, String[] args){
        if(senderHasPermission(sender,"lb.core.save-all")) {
            plugin.saveData();
            sender.sendMessage(plugin.getConfigHandler().internationalization().format("all_stats_saved"));
        }
        return true;
    }

    //private boolean onMinus(LowbrainPlayer rp, String[] args){}

    private boolean onSetRace(LowbrainPlayer rp, String[] args){
        if (args.length != 2)
            return false;

        if (plugin.getConfigHandler().races().getKeys(false).contains(args[1])) {
            rp.setRace(args[1], false);
        } else if (args[1].equalsIgnoreCase("rdm") || args[1].equalsIgnoreCase("random")) {
            int max = plugin.getConfigHandler().races().getKeys(false).size() - 1;
            int rdm = fn.randomInt(0,max);
            rp.setRace((String)plugin.getConfigHandler().races().getKeys(false).toArray()[rdm],false);
        } else {
            rp.sendMessage(plugin.getConfigHandler().internationalization().format("invalid_race"));
        }

        return true;
    }

    private boolean onSetClass(LowbrainPlayer rp, String[] args){
        if (args.length != 2)
            return false;

        if (plugin.getConfigHandler().classes().getKeys(false).contains(args[1])) {
            rp.setClass(args[1], false);
        } else if (args[1].equalsIgnoreCase("rdm") || args[1].equalsIgnoreCase("random")) {
            int max = plugin.getConfigHandler().classes().getKeys(false).size() - 1;
            int rdm = fn.randomInt(0,max);
            rp.setClass((String)plugin.getConfigHandler().classes().getKeys(false).toArray()[rdm],false);
        } else {
            rp.sendMessage(plugin.getConfigHandler().internationalization().format("invalid_class"));
        }
        return true;
    }

    private boolean onXp(LowbrainPlayer rp, String[] args){
        double xp = rp.getNextLvl() - rp.getExperience();
        rp.sendMessage(plugin.getConfigHandler().internationalization().format("you_will_reach_level", new Object[]{rp.getLvl()+1, xp}));
        return true;
    }

    private boolean onStats(LowbrainPlayer rp, String[] args, CommandSender sender){
        if(args.length == 2 && senderHasPermission(sender,"lb.core.stats-others")){
            Player p = plugin.getServer().getPlayer(args[1]);
            if(p != null){
                LowbrainPlayer rp2 = LowbrainCore.getInstance().getPlayerHandler().getList().get(p.getUniqueId());
                if(rp2 != null){
                    rp.sendMessage(rp2.toString());
                }else{
                    rp.sendMessage(plugin.getConfigHandler().internationalization().format("player_not_connected"));
                }
            }
            else{
                rp.sendMessage(plugin.getConfigHandler().internationalization().format("player_not_connected"));
            }
        }
        else{
            rp.sendMessage(rp.toString());
        }
        return true;
    }

    private boolean onReset(LowbrainPlayer rp, String[] args){
        if(args.length == 2){
            if(plugin.getConfigHandler().classes().getKeys(false).contains(args[1]))
                rp.reset(args[1],null);
            else
                rp.sendMessage(plugin.getConfigHandler().internationalization().format("no_such_class"));

        } else if(args.length == 3){
            if(plugin.getConfigHandler().classes().getKeys(false).contains(args[1]) && plugin.getConfigHandler().classes().getKeys(false).contains(args[2]))
                rp.reset(args[1],args[2]);
            else
                rp.sendMessage(ChatColor.RED + plugin.getConfigHandler().internationalization().format("no_such_class_or_race"));

        } else{
            rp.reset(rp.getClassName(),rp.getRaceName());
        }
        return true;
    }

    private boolean onCompleteReset(LowbrainPlayer rp, String[] args){
        if(args.length == 2 && args[1].toLowerCase().equals("yes"))
            rp.resetAll(false);

        else
            rp.sendMessage(plugin.getConfigHandler().internationalization().format("validate_complete_reset"));

        return true;
    }

    private boolean onSkill(LowbrainPlayer rp, String[] args){
        if(args.length != 2)
            return false;

        LowbrainSkill s = rp.getSkills().get(args[1]);
        if(s != null)
            rp.sendMessage(s.toString(),ChatColor.LIGHT_PURPLE);

        else
            rp.sendMessage(plugin.getConfigHandler().internationalization().format("no_such_skill"),ChatColor.RED);

        return true;
    }

    private boolean onSkills(LowbrainPlayer rp, String[] args){
        String sk = "";
        for(Map.Entry<String, LowbrainSkill> s : rp.getSkills().entrySet()) {
            String n = s.getValue().getName();
            int v = s.getValue().getCurrentLevel();
            sk += n + " : " + v + "\n";
        }
        rp.sendMessage(sk);
        return true;
    }

    private boolean onCast(LowbrainPlayer rp, String[] args){
        if(args.length == 2){
            rp.castSpell(args[1],null);
        } else if(args.length == 3){
            Player p = plugin.getServer().getPlayer(args[2]);
            if(p == null){
                rp.sendMessage(plugin.getConfigHandler().internationalization().format("player_not_connected"));
            }
            else {
                LowbrainPlayer to = LowbrainCore.getInstance().getPlayerHandler().getList().get(p.getUniqueId());
                if(to == null)
                    rp.sendMessage(plugin.getConfigHandler().internationalization().format("player_not_connected"));
                else
                    rp.castSpell(args[1],to);

            }
        } else {
            return false;
        }
        return true;
    }

    private boolean onMyPowers(LowbrainPlayer rp, String[] args){
        String pws = "";
        for (LowbrainPower power :
                rp.getPowers().values()) {
            pws += power.getName() + ", ";
        }
        rp.sendMessage(fn.StringIsNullOrEmpty(pws) ? plugin.getConfigHandler().internationalization().format("no_powers_available") : pws);
        return true;
    }

    private boolean onMySkill(LowbrainPlayer rp, String[] args){
        if (args.length != 2)
            return false;

        LowbrainSkill s = rp.getSkills().get(args[1]);
        if(s != null)
            rp.sendMessage(s.info(),ChatColor.LIGHT_PURPLE);
        else
            rp.sendMessage(plugin.getConfigHandler().internationalization().format("no_such_skill"),ChatColor.RED);

        return true;
    }

    private boolean onAdd(LowbrainPlayer rp, String[] args){
        int amount = 0;

        if(args.length == 2){
            amount = 1;
        } else if(args.length == 3){
            try {
                amount = Integer.parseInt(args[2]);
            }
            catch (Exception e){
                rp.sendMessage(plugin.getConfigHandler().internationalization().format("use_help"));
            }
        }
        if(amount <= 0)
            rp.sendMessage(plugin.getConfigHandler().internationalization().format("number_higher_then_zero"));

        switch (args[1].toLowerCase()){
            case "defence":
            case "def":
                rp.addDefence(amount,true,true);
                break;
            case "vitality":
            case "vit":
                rp.addVitality(amount,true,true);
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
                rp.sendMessage(plugin.getConfigHandler().internationalization().format("invalid_attribute"));
        }
        return true;
    }

    private boolean onMobKills(LowbrainPlayer rp, String[] args) {
        String msg = "";
        if (args.length == 1) {
            for(Map.Entry<String, Integer> s : rp.getMobKills().entrySet()) {
                String n = s.getKey();
                int v = s.getValue();
                msg += n + " : " + v + "\n";
            }
            rp.sendMessage(msg);
        } else if (args.length == 2) {
            if(rp.getMobKills().containsKey(args[1].toLowerCase()));
            msg = args[1] + " : " + rp.getMobKills().get(args[1].toLowerCase());
            rp.sendMessage(msg);
        } else {
            return false;
        }
        return true;
    }

    private boolean onHideStats(LowbrainPlayer rp) {
        rp.getStatsBoard().hide();
        return true;
    }

    private boolean onShowStats(LowbrainPlayer rp) {
        rp.getStatsBoard().show();
        return true;
    }

    private boolean onToggleStats(LowbrainPlayer rp, String[] args) {
        if (args.length == 1) {
            rp.getStatsBoard().toggle();
        } else if(args.length == 2){
            switch (args[2].toLowerCase()){
                case "true":
                case "1":
                    rp.getStatsBoard().show();
                    break;
                case "false":
                case "0":
                    rp.getStatsBoard().hide();
                    break;
            }
        } else {
            return false;
        }
        return true;
    }

    private boolean onClasses(LowbrainPlayer rp, String[] args) {
        String cls = "";
        for (String s :
                plugin.getConfigHandler().classes().getKeys(false)) {
            LowbrainClass rc = new LowbrainClass(s);
            cls += "-----------------------------" + "\n";
            cls += rc.toString() + "\n";
        }
        rp.sendMessage(ChatColor.GREEN +cls);
        return true;
    }

    private boolean onRaces(LowbrainPlayer rp, String[] args) {
        String rcs = "";
        for (String s :
                plugin.getConfigHandler().races().getKeys(false)) {
            LowbrainRace rr = new LowbrainRace(s);
            rcs += "-----------------------------" + "\n";
            rcs += rr.toString() + "\n";
        }
        rp.sendMessage(ChatColor.GREEN +rcs);
        return true;
    }

	/**
	 * check sender pemission
	 * @param sender
	 * @param permission
	 * @return
	 */
    private boolean senderHasPermission(CommandSender sender, String permission){
    	if(sender.isOp() && Settings.getInstance().isOpBypassPermission()){
    		return true;
		}
		if(sender instanceof  ConsoleCommandSender){
			return true;
		}
		if(sender.hasPermission(permission)){
			return true;
		}
		sender.sendMessage(ChatColor.RED + plugin.getConfigHandler().internationalization().format("insufficient_permission"));
		return false;
	}
}
