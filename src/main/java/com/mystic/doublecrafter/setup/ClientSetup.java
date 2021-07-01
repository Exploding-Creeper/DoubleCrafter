package com.mystic.doublecrafter.setup;

import com.mystic.doublecrafter.DoubleCrafter;
import com.mystic.doublecrafter.gui.DoubleCrafterScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;

public class ClientSetup implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ScreenRegistry.register(DoubleCrafter.DOUBLE_CRAFTER_SCREEN_HANDLER, DoubleCrafterScreen::new);
    }
}
