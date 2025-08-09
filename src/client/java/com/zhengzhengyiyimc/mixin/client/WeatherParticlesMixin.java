package com.zhengzhengyiyimc.mixin.client;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.zhengzhengyiyimc.ModConfig;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;

@Mixin(ClientWorld.class)
public class WeatherParticlesMixin {
    @Inject(
        method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelParticles1(ParticleEffect parameters, 
                            double x, double y, double z,
                            double vx, double vy, double vz,
                            CallbackInfo ci) {
        if (AutoConfig.getConfigHolder(ModConfig.class).getConfig().disablePartical) ci.cancel();
    }

    @Inject(
        method = "addParticle(Lnet/minecraft/particle/ParticleEffect;ZDDDDDD)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void cancelParticles2(ParticleEffect parameters, boolean alwaysSpawn,
                            double x, double y, double z,
                            double vx, double vy, double vz,
                            CallbackInfo ci) {
        if (AutoConfig.getConfigHolder(ModConfig.class).getConfig().disablePartical) ci.cancel();
    }
}
