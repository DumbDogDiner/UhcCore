package com.gmail.val59000mc.threads;

import com.gmail.val59000mc.exceptions.UhcPlayerNotOnlineException;
import com.gmail.val59000mc.game.GameManager;
import com.gmail.val59000mc.languages.Lang;
import com.gmail.val59000mc.players.PlayerManager;
import com.gmail.val59000mc.players.UhcPlayer;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Objects;

public class FinalHealThread implements Runnable{

	private final GameManager gameManager;
	private final PlayerManager playerManager;

	public FinalHealThread(GameManager gameManager, PlayerManager playerManager){
		this.gameManager = gameManager;
		this.playerManager = playerManager;
	}
	
	@Override
	public void run() {
		for (UhcPlayer uhcPlayer : playerManager.getOnlinePlayingPlayers()){
			try {
				Player bukkitPlayer = uhcPlayer.getPlayer();
				double maxHealth = Objects.requireNonNull(bukkitPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue();

				bukkitPlayer.setHealth(maxHealth);

				// moving this announcement to be an individual message, so we can apply the maxhealth attribute to this
				// without huge modifications to the code base
				bukkitPlayer.sendMessage(Lang.GAME_FINAL_HEAL.replace("%maxhealth%", String.valueOf(maxHealth)));
			}catch (UhcPlayerNotOnlineException ex){
				// no heal for offline players
			}
		}
	}

}