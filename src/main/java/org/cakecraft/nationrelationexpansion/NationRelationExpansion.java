package org.cakecraft.nationrelationexpansion;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.exceptions.NotRegisteredException;
import com.palmergames.bukkit.towny.object.Nation;
import com.palmergames.bukkit.towny.object.Resident;
import me.clip.placeholderapi.expansion.Cacheable;
import me.clip.placeholderapi.expansion.Configurable;
import me.clip.placeholderapi.expansion.Relational;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class NationRelationExpansion extends PlaceholderExpansion implements Relational, Configurable, Cacheable {

    private String own;
    private String neutral;
    private String enemy;
    private String ally;

    @Override
    public boolean canRegister(){
        return (Bukkit.getPluginManager().getPlugin(getRequiredPlugin()) != null);
    }

    @Override
    public Map<String, Object> getDefaults() {
        Map<String, Object> defaults = new HashMap<>();
        defaults.put("colors.own", "&a");
        defaults.put("colors.neutral", "&f");
        defaults.put("colors.enemy", "&c");
        defaults.put("colors.ally", "&d");
        return defaults;
    }

    @Override
    public boolean register() {
        this.own = getString("colors.own", "&a");
        this.neutral = getString("colors.neutral", "&f");
        this.enemy = getString("colors.enemy", "&c");
        this.ally = getString("colors.ally", "&d");
        return super.register();
    }

    @Override
    public void clear() {

    }

    @Override
    public String getAuthor(){
        return "LittleCakeMan";
    }

    @Override
    public String getIdentifier(){
        return "nationrelation";
    }

    @Override
    public String getRequiredPlugin() {
        return "Towny";
    }

    @Override
    public String getVersion(){
        return "1.0.0";
    }

    @Override
    public String onPlaceholderRequest(Player playerOne, Player playerTwo, String identifier) {
        Resident residentOne = getResident(playerOne);
        Resident residentTwo = getResident(playerTwo);
        if (residentOne == null || residentTwo == null ) {
            return "";
        }
        Nation nationOne = getNation(residentOne);
        Nation nationTwo = getNation(residentTwo);
        if (nationOne == null || nationTwo == null ) {
            return "";
        }

        if (identifier.equals("color")) {
            return getRelation(nationOne, nationTwo);
        }
        if (identifier.equals("colored_nation")) {
            return getRelation(nationOne, nationTwo) + nationOne.getName();
        }
        return null;
    }

    private Resident getResident(Player player) {
        try {
            return TownyAPI.getInstance().getDataSource().getResident(player.getName());
        } catch (NotRegisteredException e) {
            return null;
        }
    }

    private Nation getNation(Resident resident) {
        try {
            return resident.getTown().getNation();
        } catch (NotRegisteredException e) {
            return null;
        }
    }

    private String getRelation(Nation nationOne, Nation nationTwo) {
        if (nationTwo == nationOne) {
            return own;
        }
        if (nationTwo.hasAlly(nationOne)) {
            return ally;
        }
        if (nationTwo.hasEnemy(nationOne)) {
            return enemy;
        }
        if (nationOne.isNeutral()) {
            return neutral;
        }
        return "";
    }
}