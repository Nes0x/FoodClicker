package me.nes0x;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;

public class DiscordIntegration {
    private DiscordRPC rpc = DiscordRPC.INSTANCE;

    public void startRPC() {
        String applicationId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        rpc.Discord_Initialize(applicationId, handlers, true, null);
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() / 1000;
        presence.largeImageKey = "";
        presence.largeImageText = "FoodClicker <3";
        rpc.Discord_UpdatePresence(presence);

        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                rpc.Discord_RunCallbacks();
                presence.details = "Money: " + SaveGlobalVariables.getMoney();
                presence.state = "$ for click: " + SaveGlobalVariables.getEarn() + " | on second: " + SaveGlobalVariables.getAutoClick();
                rpc.Discord_UpdatePresence(presence);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ignored) {}
            }
        }, "RPC-Callback-Handler").start();
    }

    public void stopRPC() {
        rpc.Discord_Shutdown();
        rpc.Discord_ClearPresence();
    }

}
