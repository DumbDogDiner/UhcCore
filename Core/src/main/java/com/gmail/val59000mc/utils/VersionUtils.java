package com.gmail.val59000mc.utils;

import com.gmail.val59000mc.UhcCore;
import com.gmail.val59000mc.exceptions.ParseException;
import com.gmail.val59000mc.players.UhcPlayer;
import com.gmail.val59000mc.schematics.SchematicHandler;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.Skull;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public abstract class VersionUtils{

    private static VersionUtils versionUtils = null;

    public static VersionUtils getVersionUtils(){
        if (versionUtils == null){
            int version = UhcCore.getVersion();
            int utilsVersion = 15;
            if (version < 12){
                utilsVersion = 8;
            }else if (version == 12){
                utilsVersion = 12;
            }else if (version == 13){
                utilsVersion = 13;
            }else if (version == 14){
                utilsVersion = 14;
            }

            try {
                Class c = Class.forName("com.gmail.val59000mc.utils.VersionUtils_1_" + utilsVersion);
                versionUtils = (VersionUtils) c.newInstance();
            }catch (ReflectiveOperationException ex){
                ex.printStackTrace();
            }
        }
        return versionUtils;
    }

    public abstract SchematicHandler getSchematicHandler();

    public abstract ShapedRecipe createShapedRecipe(ItemStack craft, String craftKey);

    public abstract ItemStack createPlayerSkull(String name, UUID uuid);

    public abstract void setSkullOwner(Skull skull, UhcPlayer player);

    public abstract Objective registerObjective(Scoreboard scoreboard, String name, String criteria);

    public abstract void setPlayerMaxHealth(Player player, double maxHealth);

    public abstract void replaceOceanBiomes();

    public abstract void setGameRuleValue(World world, String gameRule, Object value);

    public abstract boolean hasEye(Block block);

    public abstract void setEye(Block block, boolean eye);

    public abstract void setEndPortalFrameOrientation(Block block, BlockFace blockFace);

    public abstract void setTeamNameTagVisibility(Team team, boolean value);

    public abstract void setChestName(Chest chest, String name);

    @Nullable
    public abstract JsonObject getBasePotionEffect(PotionMeta potionMeta);

    public abstract PotionMeta setBasePotionEffect(PotionMeta potionMeta, PotionType type, boolean extended, boolean upgraded);

    @Nullable
    public abstract Color getPotionColor(PotionMeta potionMeta);

    public abstract PotionMeta setPotionColor(PotionMeta potionMeta, Color color);

    /**
     * Sets the side of a chest. (Used for double chests)
     * @param chest The chest you want to change the side for.
     * @param left If true it's changed to a left chest, false means it will be changed to a right chest.
     */
    public abstract void setChestSide(Chest chest, boolean left);

    public abstract void removeRecipeFor(ItemStack item);

    public abstract void handleNetherPortalEvent(PlayerPortalEvent event);

    @Nullable
    public abstract JsonObject getItemAttributes(ItemMeta meta);

    public abstract ItemMeta applyItemAttributes(ItemMeta meta, JsonObject attributes);

    public abstract String getEnchantmentKey(Enchantment enchantment);

    @Nullable
    public abstract Enchantment getEnchantmentFromKey(String key);

    public abstract void setEntityAI(LivingEntity entity, boolean b);

    public abstract List<Material> getItemList();

    @Nullable
    public abstract JsonArray getSuspiciousStewEffects(ItemMeta meta);

    public abstract ItemMeta applySuspiciousStewEffects(ItemMeta meta, JsonArray effects) throws ParseException;

    public abstract void setItemUnbreakable(ItemMeta meta, boolean b);

}