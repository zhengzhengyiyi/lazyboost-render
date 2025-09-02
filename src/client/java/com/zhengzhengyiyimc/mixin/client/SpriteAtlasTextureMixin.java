// 文件：DisableAnimationMixin.java
package com.zhengzhengyiyimc.mixin.client;

import com.zhengzhengyiyimc.config.ModConfig;
import me.shedaniel.autoconfig.AutoConfig;

import net.minecraft.client.texture.Sprite;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.texture.AbstractTexture;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SpriteAtlasTexture.class)
public abstract class SpriteAtlasTextureMixin extends AbstractTexture {
    @Redirect(
        method = "upload",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/texture/Sprite;createAnimation()Lnet/minecraft/client/texture/Sprite$TickableAnimation;"
        )
    )
    private Sprite.TickableAnimation disableAnimations(Sprite instance) {
        ModConfig config = AutoConfig.getConfigHolder(ModConfig.class).getConfig();

        if (config.disableAnimation) {
            return null;
        }

        return instance.createAnimation();
    }
}