package com.zhengzhengyiyimc;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.util.ActionResult;
import org.lwjgl.glfw.GLFW;

public class LazyboostClient implements ClientModInitializer {
    private long windowHandle = -1L;
    private boolean isFpsReduced = false;
    private int originalFpsLimit = 120; 

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        AutoConfig.getConfigHolder(ModConfig.class).registerSaveListener((manager, data) -> {
            return ActionResult.SUCCESS;
        });

        ClientTickEvents.START_CLIENT_TICK.register(mc -> {
            if (windowHandle == -1L && mc.getWindow() != null) {
                windowHandle = mc.getWindow().getHandle();
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(MinecraftClient client) {
		if (!AutoConfig.getConfigHolder(ModConfig.class).getConfig().enableAutoFpsReduction) return;

        if (client.player == null || client.world == null || windowHandle == -1L) {
            return;
        }

        boolean isFocused = GLFW.glfwGetWindowAttrib(windowHandle, GLFW.GLFW_FOCUSED) == 1;
        boolean isIconified = GLFW.glfwGetWindowAttrib(windowHandle, GLFW.GLFW_ICONIFIED) == 1;

        SimpleOption<Integer> fpsOption = client.options.getMaxFps();

        if (isFocused && !isIconified) {
            if (isFpsReduced) {
                fpsOption.setValue(originalFpsLimit);
                isFpsReduced = false;
            }
        } else {
            if (!isFpsReduced) {
                originalFpsLimit = fpsOption.getValue();
            }
            if (fpsOption.getValue() != 12) {
                fpsOption.setValue(12);
            }
            isFpsReduced = true;
        }
    }
}
