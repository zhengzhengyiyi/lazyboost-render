package com.zhengzhengyiyimc;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name="lazyboost_configure")
public class ModConfig implements ConfigData {
    public boolean disablePartical = false;
    public boolean disableWeather = false;

    @me.shedaniel.autoconfig.annotation.ConfigEntry.BoundedDiscrete(min = 0, max = 20 * 8)
    public int entity_render_distance = 48;

    public boolean enableAutoFpsReduction = true;
}
