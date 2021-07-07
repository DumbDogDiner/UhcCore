package com.gmail.val59000mc.commands;

import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.game.GameState;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.TeamManager;
import com.gmail.val59000mc.players.UhcPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReadyCommandExecutor implements CommandExecutor {

    private final TeamManager teamManager;
    private final GameManager gameManager;

    public ReadyCommandExecutor(GameManager gameManager, TeamManager teamManager) {
        this.teamManager = teamManager;
        this.gameManager = gameManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command!");
            return true;
        }

        if(gameManager.getGameState().equals(GameState.WAITING)) {
            UhcPlayer player = gameManager.getPlayerManager().getUhcPlayer((Player) sender);
            player.getTeam().changeReadyState();
        }

        return true;
    }
}
