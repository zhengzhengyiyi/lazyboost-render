package com.zhengzhengyiyimc.hub;

import me.shedaniel.autoconfig.AutoConfig;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import java.text.DecimalFormat;
import java.util.Optional;

import com.zhengzhengyiyimc.Lazyboost;
import com.zhengzhengyiyimc.config.ModConfig;

public class HubRenderer {
    private static HubRenderer INSTANCE;
    private final DecimalFormat df = new DecimalFormat("#.##");
    private int currentFPS = 0;
    private float currentTPS = 20.0f;
    private int frameCount = 0;
    private long lastFpsUpdate = 0;
    private long lastTpsUpdate = 0;
    private long lastBiomeUpdate = 0;
    private String cachedBiomeText = "Biome: Unknown";
    private static final long FPS_UPDATE_INTERVAL = 500_000_000L;
    private static final long TPS_UPDATE_INTERVAL = 1_000_000_000L;
    private static final long BIOME_UPDATE_INTERVAL_MS = 2000;

    private HubRenderer() {}

    public static void init() {
        if (INSTANCE == null) {
            INSTANCE = new HubRenderer();
            HudRenderCallback.EVENT.register(INSTANCE::onHudRender);
        }
    }

    private void onHudRender(DrawContext drawContext, float tickDelta) {
        renderCustomHud(drawContext, tickDelta);
    }

    private void renderCustomHud(DrawContext drawContext, float tickDelta) {
        MinecraftClient client = MinecraftClient.getInstance();
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        if (!config.smallhub || client.player == null || client.world == null) {
            return;
        }

        updateFPS();
        updateTPS(client);
        updateBiomeText(client);

        TextRenderer textRenderer = client.textRenderer;
        BlockPos playerPos = client.player.getBlockPos();
        String positionText = String.format("X: %d Y: %d Z: %d", playerPos.getX(), playerPos.getY(), playerPos.getZ());
        String fpsText = "FPS: " + currentFPS;
        String tpsText = "TPS: " + df.format(currentTPS);
        int lineHeight = textRenderer.fontHeight + 1;
        int startX = 4;
        int startY = 4;

        drawContext.drawText(textRenderer, positionText, startX, startY, 0xFFFFFF, true);
        drawContext.drawText(textRenderer, fpsText, startX, startY + lineHeight, 0xFFFF00, true);
        drawContext.drawText(textRenderer, tpsText, startX, startY + 2 * lineHeight, 0x00FF00, true);
        drawContext.drawText(textRenderer, cachedBiomeText, startX, startY + 3 * lineHeight, 0x00FFFF, true);
    }

    private void updateFPS() {
        long currentTime = System.nanoTime();
        frameCount++;
        if (currentTime - lastFpsUpdate >= FPS_UPDATE_INTERVAL) {
            long timeElapsed = currentTime - lastFpsUpdate;
            currentFPS = (int) ((long) frameCount * 1_000_000_000L / timeElapsed);
            frameCount = 0;
            lastFpsUpdate = currentTime;
        }
    }

    private void updateTPS(MinecraftClient client) {
    	try {
	        long currentTime = System.nanoTime();
	        if (currentTime - lastTpsUpdate >= TPS_UPDATE_INTERVAL) {
	            if (client.isIntegratedServerRunning() && client.getServer() != null) {
	                // currentTPS = client.getServer().getTickManager().getTickRate();
	            	currentTPS = client.getServer().getAverageTickTime();
	//                currentTPS = 20.0f;
	            } else {
	                currentTPS = 20.0f;
	            }
	            lastTpsUpdate = currentTime;
	        }
    	} catch (Exception e) {
    		Lazyboost.LOGGER.warn(e.toString());
    	}
    }

    private void updateBiomeText(MinecraftClient client) {
        long currentMillis = System.currentTimeMillis();
        if (currentMillis - lastBiomeUpdate >= BIOME_UPDATE_INTERVAL_MS) {
            cachedBiomeText = "Biome: Unknown";
            BlockPos playerPos = client.player.getBlockPos();
            if (client.world.getRegistryManager() != null) {
                Optional<RegistryKey<Biome>> biomeKey = client.world.getRegistryManager().get(RegistryKeys.BIOME)
                        .getKey(client.world.getBiome(playerPos).value());
                if (biomeKey.isPresent()) {
                    Identifier id = biomeKey.get().getValue();
                    cachedBiomeText = "Biome: " + id.getPath();
                }
            }
            lastBiomeUpdate = currentMillis;
        }
    }
}