package com.zhengzhengyiyimc.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name="lazyboost_configure")
public class ModConfig implements ConfigData {
    public boolean disablePartical = false;
    public boolean disableWeather = false;

    @me.shedaniel.autoconfig.annotation.ConfigEntry.BoundedDiscrete(min = 0, max = 20 * 8)
    public int entity_render_distance = 48;

    public boolean disableAmbientCave = false;
    public boolean disableAmbientBasaltDeltas = false;
    public boolean disableAmbientCrimsonForest = false;
    public boolean disableAmbientNetherWastes = false;
    public boolean disableAmbientSoulSandValley = false;
    public boolean disableAmbientWarpedForest = false;
    public boolean disableAmbientUnderwater = false;

    public boolean enableAutoFpsReduction = true;

    public boolean smallhub = true;

    public boolean disablewater = false;
}
