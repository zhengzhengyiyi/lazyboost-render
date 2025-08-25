package com.zhengzhengyiyimc.mixin.client;

import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.zhengzhengyiyimc.config.ModConfig;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    private static final List<Box> BOXES_TO_RENDER = new ArrayList<>();

    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void disablerenderWeather(LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (AutoConfig.getConfigHolder(ModConfig.class).getConfig().disableWeather) ci.cancel();
    }

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At("HEAD"), cancellable = true)
    private void renderSkyCallback(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void onRenderStart(CallbackInfo ci) {
        BOXES_TO_RENDER.clear();
    }

    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        double distance = entity.getPos().distanceTo(client.player.getPos());
        if (distance >= config.entity_render_distance) {
            ci.cancel();
        }
    }

    @Inject(method = "renderLightSky", at = @At("HEAD"), cancellable = true)
    private void renderLightSky(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "renderDarkSky", at = @At("HEAD"), cancellable = true)
    private void renderDarkSky(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "renderLayer", at = @At("HEAD"), cancellable = true)
    private void renderLayer(RenderLayer renderLayer, MatrixStack matrices, double cameraX, double cameraY, double cameraZ, Matrix4f positionMatrix, CallbackInfo ci) {
        if (renderLayer == RenderLayer.getTranslucent() && AutoConfig.getConfigHolder(ModConfig.class).getConfig().disablewater) {
            ci.cancel();
        }
    }
}
