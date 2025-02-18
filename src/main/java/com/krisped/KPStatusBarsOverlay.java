package com.krisped;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.VarPlayer;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayPriority;

public class KPStatusBarsOverlay extends Overlay
{
    private final Client client;
    private final KPStatusBarsPlugin plugin;
    private final KPStatusBarsConfig config;

    // Fargekonstanter
    private static final Color HEALTH_COLOR = new Color(225, 35, 0, 125);
    private static final Color POISONED_COLOR = new Color(0, 145, 0, 150);
    private static final Color VENOMED_COLOR = new Color(0, 65, 0, 150);
    private static final Color PRAYER_COLOR = new Color(50, 200, 200, 175);
    private static final Color ACTIVE_PRAYER_COLOR = new Color(57, 255, 186, 225);

    @Inject
    public KPStatusBarsOverlay(Client client, KPStatusBarsPlugin plugin, KPStatusBarsConfig config)
    {
        this.client = client;
        this.plugin = plugin;
        this.config = config;
        setPosition(OverlayPosition.DYNAMIC);
        setPriority(OverlayPriority.HIGH);
        setLayer(OverlayLayer.ABOVE_WIDGETS);
    }

    @Override
    public Dimension render(Graphics2D graphics)
    {
        Widget chatbox = client.getWidget(WidgetInfo.CHATBOX);
        if (chatbox == null || chatbox.isHidden())
        {
            return null;
        }

        // Hent chatboxens rektangel
        Rectangle chatboxRect = new Rectangle(
                chatbox.getCanvasLocation().getX(),
                chatbox.getCanvasLocation().getY(),
                chatbox.getWidth(),
                chatbox.getHeight()
        );

        int transparency = (int)(255 * (config.barTransparency() / 100.0));
        transparency = Math.max(0, Math.min(transparency, 255));

        // Fast margin og bar-tykkelse
        int margin = 2;
        int barThickness = 5;

        // Top Bar
        if (config.topBarType() != KPStatusBarsConfig.BarType.DISABLED)
        {
            KPStatusBarRenderer.BarDimensions dims = KPStatusBarRenderer.getTopBarDimensions(chatboxRect, margin, barThickness);
            if (config.topBarType() == KPStatusBarsConfig.BarType.HITPOINTS)
            {
                renderHpBar(graphics, dims, transparency);
            }
            else if (config.topBarType() == KPStatusBarsConfig.BarType.PRAYER)
            {
                renderPrayerBar(graphics, dims, transparency);
            }
        }

        // Bottom Bar
        if (config.bottomBarType() != KPStatusBarsConfig.BarType.DISABLED)
        {
            KPStatusBarRenderer.BarDimensions dims = KPStatusBarRenderer.getBottomBarDimensions(chatboxRect, margin, barThickness);
            if (config.bottomBarType() == KPStatusBarsConfig.BarType.HITPOINTS)
            {
                renderHpBar(graphics, dims, transparency);
            }
            else if (config.bottomBarType() == KPStatusBarsConfig.BarType.PRAYER)
            {
                renderPrayerBar(graphics, dims, transparency);
            }
        }

        // Left Bar (vertikal)
        if (config.leftBarType() != KPStatusBarsConfig.BarType.DISABLED)
        {
            KPStatusBarRenderer.BarDimensions dims = KPStatusBarRenderer.getLeftBarDimensions(chatboxRect, margin, barThickness);
            if (config.leftBarType() == KPStatusBarsConfig.BarType.HITPOINTS)
            {
                renderHpBar(graphics, dims, transparency);
            }
            else if (config.leftBarType() == KPStatusBarsConfig.BarType.PRAYER)
            {
                renderPrayerBar(graphics, dims, transparency);
            }
        }

        // Right Bar (vertikal)
        if (config.rightBarType() != KPStatusBarsConfig.BarType.DISABLED)
        {
            KPStatusBarRenderer.BarDimensions dims = KPStatusBarRenderer.getRightBarDimensions(chatboxRect, margin, barThickness);
            if (config.rightBarType() == KPStatusBarsConfig.BarType.HITPOINTS)
            {
                renderHpBar(graphics, dims, transparency);
            }
            else if (config.rightBarType() == KPStatusBarsConfig.BarType.PRAYER)
            {
                renderPrayerBar(graphics, dims, transparency);
            }
        }

        return chatboxRect.getSize();
    }

    private void renderHpBar(Graphics2D graphics, KPStatusBarRenderer.BarDimensions dims, int transparency)
    {
        int currentHp = Math.min(client.getBoostedSkillLevel(Skill.HITPOINTS), client.getRealSkillLevel(Skill.HITPOINTS));
        int maxHp = client.getRealSkillLevel(Skill.HITPOINTS);
        if (maxHp == 0)
        {
            return;
        }

        float percentage = (float) currentHp / maxHp;
        int totalLength = dims.horizontal ? dims.width : dims.height;
        int fillLength = (int)(totalLength * percentage);

        // Blink-effekt for HP
        int blinkThreshold = config.hpBlinkThreshold();
        boolean blink = false;
        if (blinkThreshold > 0 && percentage <= (blinkThreshold / 100f))
        {
            blink = (System.currentTimeMillis() % 1000) < 500;
        }

        Color baseColor = HEALTH_COLOR;
        if (isVenomed())
        {
            baseColor = VENOMED_COLOR;
        }
        else if (isPoisoned())
        {
            baseColor = POISONED_COLOR;
        }
        Color barColor = blink
                ? new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), (int)(transparency * 0.3))
                : new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), transparency);

        // Tegn bakgrunn
        graphics.setColor(new Color(30, 30, 30, transparency));
        graphics.fillRect(dims.x, dims.y, dims.width, dims.height);

        // Tegn fyll basert på fill-retning
        KPStatusBarsConfig.FillDirection fillDir = config.fillDirection();
        if (dims.horizontal)
        {
            if (fillDir == KPStatusBarsConfig.FillDirection.STANDARD)
            {
                graphics.setColor(barColor);
                graphics.fillRect(dims.x, dims.y, fillLength, dims.height);
            }
            else
            {
                graphics.setColor(barColor);
                graphics.fillRect(dims.x + dims.width - fillLength, dims.y, fillLength, dims.height);
            }
        }
        else
        {
            if (fillDir == KPStatusBarsConfig.FillDirection.STANDARD)
            {
                graphics.setColor(barColor);
                graphics.fillRect(dims.x, dims.y + (dims.height - fillLength), dims.width, fillLength);
            }
            else
            {
                graphics.setColor(barColor);
                graphics.fillRect(dims.x, dims.y, dims.width, fillLength);
            }
        }
    }

    private void renderPrayerBar(Graphics2D graphics, KPStatusBarRenderer.BarDimensions dims, int transparency)
    {
        int currentPrayer = Math.min(client.getBoostedSkillLevel(Skill.PRAYER), client.getRealSkillLevel(Skill.PRAYER));
        int maxPrayer = client.getRealSkillLevel(Skill.PRAYER);
        if (maxPrayer == 0)
        {
            return;
        }

        float percentage = (float) currentPrayer / maxPrayer;
        int totalLength = dims.horizontal ? dims.width : dims.height;
        int fillLength = (int)(totalLength * percentage);

        // Blink-effekt for Prayer
        int blinkThreshold = config.prayerBlinkThreshold();
        boolean blink = false;
        if (blinkThreshold > 0 && percentage <= (blinkThreshold / 100f))
        {
            blink = (System.currentTimeMillis() % 1000) < 500;
        }

        Color baseColor = isPrayerActive() ? ACTIVE_PRAYER_COLOR : PRAYER_COLOR;
        Color barColor = blink
                ? new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), (int)(transparency * 0.3))
                : new Color(baseColor.getRed(), baseColor.getGreen(), baseColor.getBlue(), transparency);

        // Tegn bakgrunn
        graphics.setColor(new Color(30, 30, 30, transparency));
        graphics.fillRect(dims.x, dims.y, dims.width, dims.height);

        KPStatusBarsConfig.FillDirection fillDir = config.fillDirection();
        if (dims.horizontal)
        {
            if (fillDir == KPStatusBarsConfig.FillDirection.STANDARD)
            {
                graphics.setColor(barColor);
                graphics.fillRect(dims.x, dims.y, fillLength, dims.height);
            }
            else
            {
                graphics.setColor(barColor);
                graphics.fillRect(dims.x + dims.width - fillLength, dims.y, fillLength, dims.height);
            }
        }
        else
        {
            if (fillDir == KPStatusBarsConfig.FillDirection.STANDARD)
            {
                graphics.setColor(barColor);
                graphics.fillRect(dims.x, dims.y + (dims.height - fillLength), dims.width, fillLength);
            }
            else
            {
                graphics.setColor(barColor);
                graphics.fillRect(dims.x, dims.y, dims.width, fillLength);
            }
        }
    }

    // --- Sjekk for poison, venom og aktiv prayer ---

    private boolean isPoisoned()
    {
        return client.getVar(VarPlayer.POISON) > 0;
    }

    private boolean isVenomed()
    {
        return client.getVar(VarPlayer.POISON) >= 1000000;
    }

    private boolean isPrayerActive()
    {
        for (Prayer pray : Prayer.values())
        {
            if (client.isPrayerActive(pray))
            {
                return true;
            }
        }
        return false;
    }
}
