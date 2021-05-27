package com.gmail.val59000mc.commands;

import com.gmail.val59000mc.configuration.MainConfig;
import com.gmail.val59000mc.customitems.Kit;
import com.gmail.val59000mc.customitems.KitsManager;
import com.gmail.val59000mc.languages.Lang;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;
import com.gmail.val59000mc.scenarios.Scenario;
import com.gmail.val59000mc.scenarios.ScenarioManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ScenarioCommandExecutor implements CommandExecutor, TabCompleter {

    private final ScenarioManager scenarioManager;
    private final PlayerManager playerManager;
    private final MainConfig config;

    public ScenarioCommandExecutor(ScenarioManager scenarioManager, PlayerManager playerManager, MainConfig config) {
        this.scenarioManager = scenarioManager;
        this.playerManager = playerManager;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player p = ((Player) sender);
        if(args.length == 0) {
            p.openInventory(scenarioManager.getScenarioMainInventory(p.hasPermission("uhc-core.scenarios.edit")));
        } else if(args[0].equalsIgnoreCase("vote")) {
            UhcPlayer uhcPlayer = playerManager.getUhcPlayer(p);
            String[] argsCopy = Arrays.copyOfRange(args, 1, args.length);
            String scenarioName = String.join(" ", argsCopy);
            Scenario scenario = scenarioManager.getScenarioByName(scenarioName).orElse(null);

            if(scenario == null) {
                p.sendMessage(Lang.SCENARIO_DOESNT_EXIST);
                return true;
            }

            // toggle scenario
            if (uhcPlayer.getScenarioVotes().contains(scenario)) {
                p.sendMessage(Lang.SCENARIO_VOTE_REMOVED.replace("%scenario%", scenario.getInfo().getName()));
                uhcPlayer.getScenarioVotes().remove(scenario);
            }else{
                int maxVotes = config.get(MainConfig.MAX_SCENARIO_VOTES);
                if (uhcPlayer.getScenarioVotes().size() == maxVotes){
                    p.sendMessage(Lang.SCENARIO_GLOBAL_VOTE_MAX.replace("%max%", String.valueOf(maxVotes)));
                    return true;
                }

                p.sendMessage(Lang.SCENARIO_VOTE_ADDED.replace("%scenario%", scenario.getInfo().getName()));
                uhcPlayer.getScenarioVotes().add(scenario);
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> options = new ArrayList<>();

        if(args.length == 1) {
            options.add("vote");
        }

        if(args.length > 1 && args[0].equalsIgnoreCase("vote")) {
            if(!args[1].equalsIgnoreCase("")) {
                for(Scenario s : scenarioManager.getRegisteredScenarios()) {
                    if(s.getInfo().getName().toLowerCase().startsWith(args[1].toLowerCase())) options.add(s.getInfo().getName());
                }
            } else {
                options = scenarioManager.getRegisteredScenarios()
                            .stream().map(s -> s.getInfo().getName())
                            .collect(Collectors.toList());
            }
        }

        return options;
    }

}