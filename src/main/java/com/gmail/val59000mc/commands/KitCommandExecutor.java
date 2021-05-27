package com.gmail.val59000mc.commands;

import com.gmail.val59000mc.configuration.MainConfig;
import com.gmail.val59000mc.customitems.Kit;
import com.gmail.val59000mc.customitems.KitsManager;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.game.GameState;
import com.gmail.val59000mc.languages.Lang;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class KitCommandExecutor implements CommandExecutor, TabCompleter {

    private final GameManager gameManager;
    private final PlayerManager playerManager;
    private final MainConfig config;

    public KitCommandExecutor(GameManager gameManager, PlayerManager playerManager, MainConfig config) {
        this.config = config;
        this.gameManager = gameManager;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        Player player = (Player) sender;

        if(gameManager.getGameState().equals(GameState.WAITING)) {
            String kitName = String.join(" ", args);
            Kit kit = KitsManager.getKitByArgument(kitName);
            if(kit == null) {
                player.sendMessage("§cThis Kit does not exist!");
                return true;
            }

            UhcPlayer uhcPlayer = playerManager.getUhcPlayer(player);

            if(kit.canBeUsedBy(player, config)) {
                uhcPlayer.setKit(kit);
                uhcPlayer.sendMessage(Lang.ITEMS_KIT_SELECTED.replace("%kit%", kit.getName()));
            } else {
                uhcPlayer.sendMessage(Lang.ITEMS_KIT_NO_PERMISSION);
            }

        }

        return true;
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> options = new ArrayList<>();

        if(args.length == 1) {
            if(!args[0].equals("")) {
                for(Kit kit : KitsManager.getKits()) {
                    if(kit.getName().toLowerCase().startsWith(args[0].toLowerCase())) options.add(kit.getName());
                }
            } else {
                options = KitsManager.getKits().stream().map(Kit::getName).collect(Collectors.toList());
            }
        }

        return options;
    }
}
