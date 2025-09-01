package com.zhengzhengyiyimc.mixin.client;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.io.File;

import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.gson.Gson;

import com.zhengzhengyiyimc.config.ModConfig;

import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.SharedConstants;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.util.VideoMode;
import net.minecraft.client.MinecraftClient;

@Mixin(GameOptions.class)
public class GameOptionsMixin {
    @Shadow
    static Logger LOGGER;
    @Shadow
    static Gson GSON;
    @Shadow
    public void sendClientSettings() {}
    @Shadow
    public File optionsFile;
	
	public interface Visitor {
	    int visitInt(String key, int current);
	    boolean visitBoolean(String key, boolean current);
	    float visitFloat(String key, float current);
	    String visitString(String key, String current);
	    <T> T visitObject(String key, T current, java.util.function.Function<String, T> decoder, java.util.function.Function<T, String> encoder);
	}

    @Inject(at = @At("HEAD"), cancellable = true, method = "Lnet/minecraft/client/option/GameOptions;write()V")
    private void write(CallbackInfo ci) {
//      ci.cancel();
    	
    	MinecraftClient client = MinecraftClient.getInstance();

        try {
			final PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.optionsFile), StandardCharsets.UTF_8));

			try {
				printWriter.println("version:" + SharedConstants.getGameVersion().getSaveVersion().getId());
//				this.accept(new Visitor() {
//					public void print(String key) {
//						printWriter.print(key);
//						printWriter.print(':');
//					}
//
//					public <T> void accept(String key, SimpleOption<T> option) {
//						DataResult<JsonElement> dataResult = option.getCodec().encodeStart(JsonOps.INSTANCE, option.getValue());
//						// dataResult.error().ifPresent((partialResult) -> {
//						// 	LOGGER.error("Error saving option " + option + ": " + partialResult);
//						// });
//						dataResult.result().ifPresent((json) -> {
//							this.print(key);
//							printWriter.println(GSON.toJson(json));
//						});
//					}
//
//					public int visitInt(String key, int current) {
//						this.print(key);
//						printWriter.println(current);
//						return current;
//					}
//
//					public boolean visitBoolean(String key, boolean current) {
//						this.print(key);
//						printWriter.println(current);
//						return current;
//					}
//
//					public String visitString(String key, String current) {
//						this.print(key);
//						printWriter.println(current);
//						return current;
//					}
//
//					public float visitFloat(String key, float current) {
//						this.print(key);
//						printWriter.println(current);
//						return current;
//					}
//
//					public <T> T visitObject(String key, T current, Function<String, T> decoder, Function<T, String> encoder) {
//						this.print(key);
//						printWriter.println((String)encoder.apply(current));
//						return current;
//					}
//				});
				writeOption(printWriter, "gamma", AutoConfig.getConfigHolder(ModConfig.class).getConfig().gamma);
				if (client.getWindow().getVideoMode().isPresent()) {
					printWriter.println("fullscreenResolution:" + ((VideoMode)client.getWindow().getVideoMode().get()).asString());
				}
				this.sendClientSettings();
			} catch (Throwable var5) {
				try {
					printWriter.close();
				} catch (Throwable var4) {
					var5.addSuppressed(var4);
				}

				throw var5;
			}

			printWriter.close();
		} catch (Exception var6) {
			LOGGER.error((String)"Failed to save options", (Throwable)var6);
		}

		this.sendClientSettings();
    }
    
    private void writeOption(PrintWriter writer, String key, Object value) {
        writer.print(key);
        writer.print(':');
        writer.println(value);
    }
}
