package com.zhengzhengyiyimc.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.annotation.ConfigEntry.Category;

@Config(name="lazyboost_configure")
public class ModConfig implements ConfigData {
    public boolean disablePartical = false;
    @ConfigEntry.Gui.Tooltip
    public boolean disableWeather = false;
    @ConfigEntry.Gui.Tooltip
    public boolean disableAnimation = false;

    @ConfigEntry.BoundedDiscrete(min = 0, max = 20 * 8)
    @ConfigEntry.Gui.Tooltip
    public int entity_render_distance = 48;

    @Category("Sound")
    @ConfigEntry.Gui.Tooltip
    public boolean disableAmbientCave = false;
    @Category("Sound")
    @ConfigEntry.Gui.Tooltip
    public boolean disableAmbientBasaltDeltas = false;
    @Category("Sound")
    @ConfigEntry.Gui.Tooltip
    public boolean disableAmbientCrimsonForest = false;
    @Category("Sound")
    @ConfigEntry.Gui.Tooltip
    public boolean disableAmbientNetherWastes = false;
    @Category("Sound")
    @ConfigEntry.Gui.Tooltip
    public boolean disableAmbientSoulSandValley = false;
    @Category("Sound")
    @ConfigEntry.Gui.Tooltip
    public boolean disableAmbientWarpedForest = false;
    @Category("Sound")
    @ConfigEntry.Gui.Tooltip
    public boolean disableAmbientUnderwater = false;

    @ConfigEntry.Gui.Tooltip
    public boolean enableAutoFpsReduction = true;

    @ConfigEntry.Gui.Tooltip
    public boolean smallhub = true;

    @ConfigEntry.Gui.Tooltip
    public boolean disablewater = false;

    @ConfigEntry.Gui.Tooltip
    public boolean renderHands = true;

    @ConfigEntry.Gui.Tooltip
    public double gamma = 1.0;
}
