package com.zhengzhengyiyimc.mixin.client;

import java.util.Objects;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.SimpleOption;

import java.util.function.Consumer;

@Mixin(SimpleOption.class)
public class SinpleOptionMixin<T> {
    @Shadow()
    private T value;

    @Shadow
    private Consumer<T> changeCallback;

    @Inject(cancellable = true, at = @At("HEAD"), method = "setValue")
    private void setValue(T v, CallbackInfo ci) {
        ci.cancel();
        if (!MinecraftClient.getInstance().isRunning()) {
            this.value = v;
        } else {
            if (!Objects.equals(this.value, v)) {
                this.value = v;
                this.changeCallback.accept(this.value);
            }
        }
    }
}
