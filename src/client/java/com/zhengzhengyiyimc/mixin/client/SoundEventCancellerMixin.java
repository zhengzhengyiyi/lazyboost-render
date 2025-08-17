package com.zhengzhengyiyimc.mixin.client;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.zhengzhengyiyimc.config.ModConfig;

@Mixin(SoundManager.class)
public class SoundEventCancellerMixin {

    @Inject(method = "play(Lnet/minecraft/client/sound/SoundInstance;)V", at = @At("HEAD"), cancellable = true)
    private void lazyboost_cancelAmbientSound(SoundInstance soundInstance, CallbackInfo ci) {
        ModConfig config = null;
        try {
            config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();
        } catch (Exception e) {
            return;
        }

        if (config == null) {
            return;
        }

        Sound soundEvent = soundInstance.getSound();
        if (soundEvent != null) {
            Identifier soundId = soundEvent.getIdentifier();
            String soundIdString = soundId.toString();

            boolean shouldCancel = false;
            switch (soundIdString) {
                case "minecraft:ambient.cave":
                    shouldCancel = config.disableAmbientCave;
                    break;
                case "minecraft:ambient.basalt_deltas.loop":
                    shouldCancel = config.disableAmbientBasaltDeltas;
                    break;
                case "minecraft:ambient.crimson_forest.loop":
                    shouldCancel = config.disableAmbientCrimsonForest;
                    break;
                case "minecraft:ambient.nether_wastes.loop":
                    shouldCancel = config.disableAmbientNetherWastes;
                    break;
                case "minecraft:ambient.soul_sand_valley.loop":
                    shouldCancel = config.disableAmbientSoulSandValley;
                    break;
                case "minecraft:ambient.warped_forest.loop":
                    shouldCancel = config.disableAmbientWarpedForest;
                    break;
                case "minecraft:ambient.underwater.loop":
                    shouldCancel = config.disableAmbientUnderwater;
                    break;
                default:
                    shouldCancel = false;
                    break;
            }

            if (shouldCancel) {
                System.out.println("[LazyBoost] Cancelling ambient sound: " + soundIdString);
                ci.cancel();
            }
        }
    }
}