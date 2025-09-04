package com.shim.celestiallib.capabilities;

import com.shim.celestiallib.CelestialLib;
import com.shim.celestiallib.api.world.galaxy.Galaxy;
import com.shim.celestiallib.api.world.planet.Planet;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class PlanetCooldown {
    final Planet planet;
    int currentCooldown;
    int numTimesReset;
    boolean hasVisited = false;

    public PlanetCooldown(Planet planet) {
        this.planet = planet;
        Galaxy galaxy = this.planet.getGalaxy();
        if (!galaxy.areCooldownsEnabled())
            return;
        this.currentCooldown = galaxy.getMaxCooldown().get();
        this.numTimesReset = 0;
    }

    public void setHasVisited() {
        this.hasVisited = true;
    }

    public boolean hasVisited() {
        return this.hasVisited;
    }

    public void resetCooldown() {
//            this.numTimesReset = 0;
        Galaxy galaxy = this.planet.getGalaxy();
        if (!galaxy.areCooldownsEnabled())
            return;
        int maxCooldown = galaxy.getMaxCooldown().get();
        int minCooldown = galaxy.getMinCooldown().get();
        if (maxCooldown < minCooldown)
            throw new IllegalStateException("maximum light speed cooldown is less than minimum");

        int decrement = galaxy.getCooldownDecrement().get();
        this.currentCooldown = maxCooldown - (decrement * this.numTimesReset);
        if (this.currentCooldown < minCooldown)
            this.currentCooldown = minCooldown;
        this.numTimesReset++;
    }

    public int getCurrentCooldown() {
        return this.currentCooldown;
    }

    public boolean isCooldownEnded() {
        return this.currentCooldown == 0;
    }

    public void decrementCooldown() {
        if (this.currentCooldown > 0) {
            this.currentCooldown--;
//                CelestialLib.LOGGER.debug("planet: " + planet.getDimension().location().toString() + ", cooldown: " + this.currentCooldown + " m:" + ((this.currentCooldown / 20) / 60) + ", s: " + ((this.currentCooldown / 20) % 60));
        }
    }

    public String getFormattedCooldown() {
        int minutes;
        int seconds;
        String cooldown;

        minutes = (this.currentCooldown / 20) / 60;
        seconds = (this.currentCooldown / 20) % 60;

        cooldown = minutes + "m " + seconds + "s";
//            CelestialLib.LOGGER.debug("planet: " + planet.getDimension().location().toString() + ", cooldown: " + this.currentCooldown + " m:" + ((this.currentCooldown / 20) / 60) + ", minutes: " + minutes +
//                    ", s: " + ((this.currentCooldown / 20) % 60) + ", seconds: " + seconds);

        return cooldown;
    }

    public Component getCooldownComponent() {
        if (this.currentCooldown > 0)
            return new TranslatableComponent("menu.celestiallib.light_speed_travel.cooldown").append(getFormattedCooldown()).withStyle(ChatFormatting.RED);
        else {
            return new TranslatableComponent("menu.celestiallib.light_speed_travel.cooldown").append(getFormattedCooldown()).withStyle(ChatFormatting.WHITE);
        }
    }

    public static PlanetCooldown createFromTag(CompoundTag nbt, Planet planet) {
        PlanetCooldown cooldown = new PlanetCooldown(planet);

        if (nbt.contains("cooldown")) cooldown.currentCooldown = nbt.getInt("cooldown");
        if (nbt.contains("timesReset")) cooldown.numTimesReset = nbt.getInt("timesReset");
        if (nbt.contains("hasVisited")) cooldown.hasVisited = nbt.getBoolean("hasVisited");

        return cooldown;

    }

    public void load(CompoundTag nbt) {
        if (nbt.contains("cooldown")) this.currentCooldown = nbt.getInt("cooldown");
        if (nbt.contains("timesReset")) this.numTimesReset = nbt.getInt("timesReset");
        if (nbt.contains("hasVisited")) this.hasVisited = nbt.getBoolean("hasVisited");
    }

    public CompoundTag save() {
        CompoundTag nbt = new CompoundTag();

        nbt.putInt("cooldown", this.currentCooldown);
        nbt.putInt("timesReset", this.numTimesReset);
        nbt.putBoolean("hasVisited", this.hasVisited);

        return nbt;
    }
}