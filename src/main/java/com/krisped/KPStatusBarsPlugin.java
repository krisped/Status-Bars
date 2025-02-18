package com.krisped;

import com.google.inject.Provides;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import javax.inject.Inject;

@PluginDescriptor(
        name = "[KP] Status Bars",
        description = "Viser en HP progress bar over chatboxen",
        tags = {"hp", "status", "bar", "health"}
)
public class KPStatusBarsPlugin extends Plugin {
    @Inject
    private OverlayManager overlayManager;

    @Inject
    private KPStatusBarsOverlay overlay;

    @Inject
    private KPStatusBarsConfig config;

    @Override
    protected void startUp() throws Exception {
        overlayManager.add(overlay);
    }

    @Override
    protected void shutDown() throws Exception {
        overlayManager.remove(overlay);
    }

    @Provides
    KPStatusBarsConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(KPStatusBarsConfig.class);
    }
}
