package com.zhengzhengyiyimc;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.option.SimpleOption;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.ActionResult;

import org.lwjgl.glfw.GLFW;

import com.zhengzhengyiyimc.config.ModConfig;
import com.zhengzhengyiyimc.hub.HubRenderer;

public class LazyboostClient implements ClientModInitializer {
    private long windowHandle = -1L;
    private boolean isFpsReduced = false;
    private int originalFpsLimit = 120;

	private KeyBinding openConfigKey;

    @Override
    public void onInitializeClient() {
        AutoConfig.register(ModConfig.class, GsonConfigSerializer::new);

        AutoConfig.getConfigHolder(ModConfig.class).registerSaveListener((manager, config) -> {
            MinecraftClient.getInstance().gameRenderer.setRenderHand(config.renderHands);

            return ActionResult.SUCCESS;
        });

		openConfigKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.lazyboost.opengui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_UNKNOWN,
                "key.lazyboost.key"
        ));

		HubRenderer.init();

        ClientTickEvents.START_CLIENT_TICK.register(mc -> {
            if (windowHandle == -1L && mc.getWindow() != null) {
                windowHandle = mc.getWindow().getHandle();
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(this::onClientTick);
    }

    private void onClientTick(MinecraftClient client) {
		if (openConfigKey.isPressed()) {
			client.setScreen(AutoConfig.getConfigScreen(ModConfig.class, client.currentScreen).get());
		}

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
