package com.zhengzhengyiyimc;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.ActionResult;

public class LazyboostClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

		AutoConfig.getConfigHolder(ModConfig.class).registerSaveListener((manager, data) -> {
			return ActionResult.SUCCESS;
		});
	}
}
