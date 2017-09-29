package lowbrain.core.handlers;

import lowbrain.core.main.LowbrainCore;
import lowbrain.core.rpg.*;
import lowbrain.library.command.Command;
import lowbrain.library.fn;
import lowbrain.library.main.LowbrainLibrary;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class CommandHandler extends Command {
    private final LowbrainCore plugin;

    private Command onAdd;
    private Command onCast;
    private Command onClasses;
    private Command onCompleteReset;
    private Command onMobKills;
    private Command onMyPowers;
    private Command onMySkill;
    private Command onRaces;
    private Command onSetClass;
    private Command onSetRace;
    private Command onSkill;
    private Command onSkills;
    private Command onStats;
    private Command onToggleStats;
    private Command onShowStats;
    private Command onHideStats;
    private Command onXp;
    private Command onSetSkill;
    private Command onGetSkill;
    private Command onUpSkill;
    private Command onSave;
    private Command onNextLvl;

    private Command onSet;
    private Command onReload;
    private Command onSaveAll;
    

    public CommandHandler(LowbrainCore plugin) {
        super("core");
        this.plugin = plugin;

        this.subs();

        LowbrainLibrary.getInstance().getBaseCmdHandler().register("core", this);
    }

    @Override
    public boolean beforeEach(CommandSender who, String[] args, String cmd) {
        if (who instanceof Player && !plugin.getPlayerHandler().getList().containsKey(((Player) who).getUniqueId()))
            return false;

        return true;
    }

    private void subs() {
        /**
         * only player command section
         */

        this.register(new String[]{"add", "+"}, onAdd = new Command("add") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                int amount = 0;

                if(args.length == 1){
                    amount = 1;
                } else if(args.length == 2){
                    try {
                        amount = Integer.parseInt(args[1]);
                    }
                    catch (Exception e){
                        rp.sendMessage(plugin.getConfigHandler().localization().format("use_help"));
                    }
                }
                
                if(amount <= 0) {
                    rp.sendMessage(plugin.getConfigHandler().localization().format("number_higher_then_zero"));
                    return CommandStatus.VALID;
                }
                    

                switch (args[0].toLowerCase()){
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
                        rp.sendMessage(plugin.getConfigHandler().localization().format("invalid_attribute"));
                }
                
                return CommandStatus.VALID;
            }
        });

        this.register("cast", onCast = new Command("cast") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);
                
                if(args.length == 1){
                    rp.castSpell(args[0],null);
                } else if(args.length == 2){
                    Player p = plugin.getServer().getPlayer(args[1]);
                    if (p == null) {
                        rp.sendMessage(plugin.getConfigHandler().localization().format("player_not_connected"));
                    } else {
                        LowbrainPlayer to = LowbrainCore.getInstance().getPlayerHandler().getList().get(p.getUniqueId());
                        if(to == null)
                            rp.sendMessage(plugin.getConfigHandler().localization().format("player_not_connected"));
                        else
                            rp.castSpell(args[0],to);

                    }
                } else {
                    return CommandStatus.INVALID;
                }
                return CommandStatus.VALID;
            }
        });

        this.register("classes", onClasses = new Command("classes") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                String cls = "";
                for (String s :
                        plugin.getConfigHandler().classes().getKeys(false)) {
                    LowbrainClass rc = new LowbrainClass(s);
                    cls += "-----------------------------" + "\n";
                    cls += rc.toString() + "\n";
                }
                rp.sendMessage(ChatColor.GREEN +cls);
                return CommandStatus.VALID;
            }
        });

        this.register("completereset", onCompleteReset = new Command("completereset") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                if(args.length == 1 && args[0].toLowerCase().equals("yes"))
                    rp.resetAll(false);

                else
                    rp.sendMessage(plugin.getConfigHandler().localization().format("validate_complete_reset"));
                
                return CommandStatus.VALID;
            }
        });

        this.register("getskill", onGetSkill = new Command("getskill") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                if(rp.getCurrentSkill() != null)
                    rp.sendMessage(plugin.getConfigHandler().localization().format("current_skill", rp.getCurrentSkill().getName()));
                else
                    rp.sendMessage(plugin.getConfigHandler().localization().format("no_current_skill"));
                
                return CommandStatus.VALID;
            }
        });

        this.register("hidestats", onHideStats = new Command("hidestats") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);
                rp.getStatsBoard().hide();
                return CommandStatus.VALID;
            }
        });

        this.register("showstats", onShowStats = new Command("showstats") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);
                rp.getStatsBoard().show();
                return CommandStatus.VALID;
            }
        });

        this.register("togglestats", onToggleStats = new Command("togglestats") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                if (args.length == 0) {
                    rp.getStatsBoard().toggle();
                } else if(args.length == 1){
                    switch (args[0].toLowerCase()){
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
                    return CommandStatus.INVALID;
                }
                return CommandStatus.VALID;
            }
        });

        this.register("stats", onStats = new Command("stats") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);
                
                if(args.length == 1 && who.hasPermission("lb.core.stats-others")){
                    Player p = plugin.getServer().getPlayer(args[0]);
                    if(p != null){
                        LowbrainPlayer rp2 = LowbrainCore.getInstance().getPlayerHandler().getList().get(p.getUniqueId());
                        if (rp2 != null)
                            rp.sendMessage(rp2.toString());
                        else
                            rp.sendMessage(plugin.getConfigHandler().localization().format("player_not_connected"));
                    }
                    else{
                        rp.sendMessage(plugin.getConfigHandler().localization().format("player_not_connected"));
                    }
                }
                else{
                    rp.sendMessage(rp.toString());
                }
                
                return CommandStatus.VALID;
            }
        });

        this.register("mobkills", onMobKills = new Command("mobkills") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                String msg = "";
                if (args.length == 0) {
                    for(Map.Entry<String, Integer> s : rp.getMobKills().entrySet()) {
                        String n = s.getKey();
                        int v = s.getValue();
                        msg += n + " : " + v + "\n";
                    }
                    rp.sendMessage(msg);
                } else if (args.length == 1 && rp.getMobKills().containsKey(args[0].toLowerCase())) {
                    msg = args[0] + " : " + rp.getMobKills().get(args[0].toLowerCase());
                    rp.sendMessage(msg);
                } else {
                    return CommandStatus.INVALID;
                }
                
                return CommandStatus.VALID;
            }
        });
        
        this.register("mypowers", onMyPowers = new Command("mypowers") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                String pws = "";
                for (LowbrainPower power :
                        rp.getPowers().values()) {
                    pws += power.getName() + ", ";
                }
                rp.sendMessage(fn.StringIsNullOrEmpty(pws) ? plugin.getConfigHandler().localization().format("no_powers_available") : pws);
                
                return CommandStatus.VALID;
            }
        });

        this.register("myskill", onMySkill = new Command("myskill") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                if (args.length != 1)
                    return CommandStatus.INVALID;

                LowbrainSkill s = rp.getSkills().get(args[0]);
                if(s != null)
                    rp.sendMessage(s.info());
                else
                    rp.sendMessage(plugin.getConfigHandler().localization().format("no_such_skill"));
                
                return CommandStatus.VALID;
            }
        });

        this.register("setclass", onSetClass = new Command("setclass") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                if (args.length != 1)
                    return CommandStatus.INVALID;

                if (plugin.getConfigHandler().classes().getKeys(false).contains(args[0])) {
                    rp.setClass(args[0], false);
                } else if (args[0].equalsIgnoreCase("rdm") || args[0].equalsIgnoreCase("random")) {
                    int max = plugin.getConfigHandler().classes().getKeys(false).size() - 1;
                    int rdm = fn.randomInt(0,max);
                    rp.setClass((String)plugin.getConfigHandler().classes().getKeys(false).toArray()[rdm],false);
                } else {
                    rp.sendMessage(plugin.getConfigHandler().localization().format("invalid_class"));
                }
                
                return CommandStatus.VALID;
            }
        });

        this.register("setrace", onSetRace = new Command("setrace") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                if (args.length != 1)
                    return CommandStatus.INVALID;

                if (plugin.getConfigHandler().races().getKeys(false).contains(args[0])) {
                    rp.setRace(args[0], false);
                } else if (args[0].equalsIgnoreCase("rdm") || args[0].equalsIgnoreCase("random")) {
                    int max = plugin.getConfigHandler().races().getKeys(false).size() - 1;
                    int rdm = fn.randomInt(0,max);
                    rp.setRace((String)plugin.getConfigHandler().races().getKeys(false).toArray()[rdm],false);
                } else {
                    rp.sendMessage(plugin.getConfigHandler().localization().format("invalid_race"));
                }
                return CommandStatus.VALID;
            }
        });

        this.register("races", onRaces = new Command("races") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                String rcs = "";
                for (String s :
                        plugin.getConfigHandler().races().getKeys(false)) {
                    LowbrainRace rr = new LowbrainRace(s);
                    rcs += "-----------------------------" + "\n";
                    rcs += rr.toString() + "\n";
                }
                rp.sendMessage(ChatColor.GREEN +rcs);
                
                return CommandStatus.VALID;
            }
        });

        this.register("skill", onSkill = new Command("skill") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                if(args.length != 1)
                    return CommandStatus.INVALID;

                LowbrainSkill s = rp.getSkills().get(args[0]);
                if(s != null)
                    rp.sendMessage(s.toString());

                else
                    rp.sendMessage(plugin.getConfigHandler().localization().format("no_such_skill"));
                
                return CommandStatus.VALID;
            }
        });

        this.register("skills", onSkills = new Command("skills") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                String sk = "";
                for(Map.Entry<String, LowbrainSkill> s : rp.getSkills().entrySet()) {
                    String n = s.getValue().getName();
                    int v = s.getValue().getCurrentLevel();
                    sk += n + " : " + v + "\n";
                }
                rp.sendMessage(sk);
                
                return CommandStatus.VALID;
            }
        });

        this.register("xp", onXp = new Command("xp") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);
                double xp = rp.getNextLvl() - rp.getExperience();
                rp.sendMessage(plugin.getConfigHandler().localization().format("you_will_reach_level", new Object[]{rp.getLvl()+1, xp}));
                return CommandStatus.VALID;
            }
        });

        this.register("setskill", onSetSkill = new Command("setskill") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);
                
                if (args.length != 1)
                    return CommandStatus.INVALID;
                
                rp.setCurrentSkill(args[0].toLowerCase());
                
                return CommandStatus.VALID;
            }
        });

        this.register("upskill", onUpSkill = new Command("upskill") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);

                if (args.length != 1)
                    return CommandStatus.INVALID;
                
                rp.upgradeSkill(args[0].toLowerCase());
                
                return CommandStatus.VALID;
            }
        });
        
        this.register("save", onSave = new Command("save") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);
                if(who.hasPermission("lb.core.save")){
                    rp.saveData();
                    rp.sendMessage(plugin.getConfigHandler().localization().format("stats_saved"));
                    return CommandStatus.VALID;
                }
                return CommandStatus.INVALID;
            }
        });

        this.register("nextlvl", onNextLvl = new Command("nextlvl") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                LowbrainPlayer rp = plugin.getPlayerHandler().get((Player)who);
                rp.sendMessage(plugin.getConfigHandler().localization().format("next_level_at", rp.getNextLvl()));
                return CommandStatus.VALID;
            }
        });
        
        onAdd.onlyPlayer(true);
        onCast.onlyPlayer(true);
        onClasses.onlyPlayer(true);
        onCompleteReset.onlyPlayer(true);
        onMobKills.onlyPlayer(true);
        onMyPowers.onlyPlayer(true);
        onMySkill.onlyPlayer(true);
        onRaces.onlyPlayer(true);
        onSetClass.onlyPlayer(true);
        onSetRace.onlyPlayer(true);
        onSkill.onlyPlayer(true);
        onSkills.onlyPlayer(true);
        onStats.onlyPlayer(true);
        onToggleStats.onlyPlayer(true);
        onShowStats.onlyPlayer(true);
        onHideStats.onlyPlayer(true);
        onXp.onlyPlayer(true);
        onSetSkill.onlyPlayer(true);
        onGetSkill.onlyPlayer(true);
        onUpSkill.onlyPlayer(true);
        onSave.onlyPlayer(true);
        onNextLvl.onlyPlayer(true);

        this.register("set", onSet = new Command("set") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                if (args.length != 3)
                    return CommandStatus.INVALID;

                String pName = args[0];
                String attribute = args[1].toLowerCase();
                String sValue = args[2];

                Player p = plugin.getServer().getPlayer(pName);
                LowbrainPlayer rp = p != null ? LowbrainCore.getInstance().getPlayerHandler().getList().get(p.getUniqueId()) : null;

                if(rp == null){
                    who.sendMessage(plugin.getConfigHandler().localization().format("player_not_connected"));
                    return CommandStatus.VALID;
                }

                int value = fn.toInteger(sValue,-1);
                if(value < 0){
                    who.sendMessage(plugin.getConfigHandler().localization().format("invalid_value"));
                    return CommandStatus.VALID;
                }

                switch (attribute){
                    default:
                        who.sendMessage(plugin.getConfigHandler().localization().format("invalid_attribute"));
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
                who.sendMessage(plugin.getConfigHandler().localization().format("attribute_set_done"));

                return CommandStatus.VALID;
            }
        });
        onSet.addPermission("lb.core.setattributes-others");

        this.register("save-all", onSaveAll = new Command("save-all") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                plugin.saveData();
                who.sendMessage(plugin.getConfigHandler().localization().format("all_stats_saved"));
                return CommandStatus.VALID;
            }
        });
        onSaveAll.addPermission("lb.core.save-all");


        this.register("reload", onReload = new Command("reload") {
            @Override
            public CommandStatus execute(CommandSender who, String[] args, String cmd) {
                plugin.reloadConfig();
                who.sendMessage(plugin.getConfigHandler().localization().format("config_file_reloaded"));
                return CommandStatus.VALID;
            }
        });
        onReload.addPermission("lb.core.reload");
        
    }

    @Override
    public CommandStatus execute(CommandSender who, String[] args, String cmd) {
        String msg = "";
        msg += "<arg> required, <[arg]> optional argument";
        msg += "\n/lb core stats - show your player stats";
        msg += "\n/lb core stats <player> - show stats of player. requires special permission";
        msg += "\n/lb core classes - show all class basic attributes";
        msg += "\n/lb core setclass <name> - set your class";
        msg += "\n/lb core setclass rdm/random - set a random class";
        msg += "\n/lb core races - show all races basic attributes";
        msg += "\n/lb core setrace <name> - set your race";
        msg += "\n/lb core setrace rdm/random - set a random race";
        msg += "\n/lb core cast <spell name> - cast a spell to yourself";
        msg += "\n/lb core cast <spell name> <player name> - cast a spell to someone";
        msg += "\n/lb core togglestats <false or true> - hide or show stats slbcoreboard";
        msg += "\n/lb core togglestats <0 or 1> - hide or show stats slbcoreboard";
        msg += "\n/lb core togglestats - toggle stats slbcoreboard";
        msg += "\n/lb core showstats - toggle on stats slbcoreboard";
        msg += "\n/lb core hidestats - toggle off stats slbcoreboard";
        msg += "\n/lb core set <player-name> <attribute> <value> - set attribute value of a player. requires special permission";
        
        msg += "\n/lb core mobkills <name> - show how many time you killed this mob";
        msg += "\n/lb core mobkills - show the number of kills you got for each mob";
        
        msg += "\n/lb core skills - show the list of skills and their current level";
        msg += "\n/lb core setskill <name> - set the current skill to be used";
        msg += "\n/lb core upskill <name> - upgrade skill";
        msg += "\n/lb core getskill - get current skill";
        msg += "\n/lb core skill <name> - get the skill description";
        msg += "\n/lb core myskill <name> - get the skill info (level)";
        msg += "\n/lb core mypowers - show your available powers";
        
        msg += "\n/lb core add <attribute> <[number]>  - add <number or 1> to <attribute>";
        msg += "\n/lb core + <attribute> <[number]>  - add <number or 1> to <attribute>";
        
        msg += "\n/lb core reset - reset your attributes to your default class";
        msg += "\n/lb core completereset true - reset you whole character (incluing level gained and xp)";
        msg += "\n/lb core reset <class> - reset your attributes to your new class";
        msg += "\n/lb core reset <class> <race>  - reset your attributes to your new class and new race";
        msg += "\n/lb core nextlvl - show how much xp is need for next lvl";
        msg += "\n/lb core save-all - save all player stats. requires special permission";
        msg += "\n/lb core reload - reload plugin. requires special permission";
        msg += "\n/lb core save - save your stats. requires special permission";
        msg += "\n/lb core xp - amount of xp needed left to achieve next level";

        who.sendMessage(ChatColor.WHITE + msg);
                
        return CommandStatus.VALID;
    }
}
