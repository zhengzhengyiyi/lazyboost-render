package com.zhengzhengyiyimc.mixin.client;

import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.zhengzhengyiyimc.config.ModConfig;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "renderWeather", at = @At("HEAD"), cancellable = true)
    private void disablerenderWeather(LightmapTextureManager manager, float tickDelta, double cameraX, double cameraY, double cameraZ, CallbackInfo ci) {
        if (AutoConfig.getConfigHolder(ModConfig.class).getConfig().disableWeather) ci.cancel();
    }

    @Inject(method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;Lorg/joml/Matrix4f;FLnet/minecraft/client/render/Camera;ZLjava/lang/Runnable;)V", at = @At("HEAD"), cancellable = true)
    private void renderSkyCallback(MatrixStack matrices, Matrix4f projectionMatrix, float tickDelta, Camera camera, boolean thickFog, Runnable fogCallback, CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "renderEntity", at = @At("HEAD"), cancellable = true)
    private void renderEntity(Entity entity, double cameraX, double cameraY, double cameraZ, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (Math.sqrt(entity.getBlockPos().getSquaredDistance(client.player.getBlockPos())) >= AutoConfig.getConfigHolder(ModConfig.class).getConfig().entity_render_distance) ci.cancel();
    }

    @Inject(method = "renderLightSky", at = @At("HEAD"), cancellable = true)
    private void renderLightSky(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "renderDarkSky", at = @At("HEAD"), cancellable = true)
    private void renderDarkSky(CallbackInfo ci) {
        ci.cancel();
    }
}
