package com.zhengzhengyiyimc.util;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

public class CommandUtil {
    public static void sendClientCommand(ClientPlayerEntity player, String command) {
        player.sendMessage(Text.literal("/" + command));
    }
}
