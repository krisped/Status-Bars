package com.krisped;

import net.runelite.client.config.*;

@ConfigGroup("kpstatusbars")
public interface KPStatusBarsConfig extends Config
{
    @ConfigItem(
            keyName = "topBarType",
            name = "Top Bar",
            description = "Velg type for top bar",
            position = 1
    )
    default BarType topBarType()
    {
        return BarType.DISABLED;
    }

    @ConfigItem(
            keyName = "bottomBarType",
            name = "Bottom Bar",
            description = "Velg type for bottom bar",
            position = 2
    )
    default BarType bottomBarType()
    {
        return BarType.HITPOINTS;
    }

    @ConfigItem(
            keyName = "leftBarType",
            name = "Left Bar",
            description = "Velg type for left bar",
            position = 3
    )
    default BarType leftBarType()
    {
        return BarType.DISABLED;
    }

    @ConfigItem(
            keyName = "rightBarType",
            name = "Right Bar",
            description = "Velg type for right bar",
            position = 4
    )
    default BarType rightBarType()
    {
        return BarType.PRAYER;
    }

    @Range(
            min = 0,
            max = 100
    )
    @ConfigItem(
            keyName = "barTransparency",
            name = "Bar Transparency",
            description = "Juster synligheten til barene (0 = usynlig, 100 = fullt synlig)",
            position = 5
    )
    default int barTransparency()
    {
        return 100;
    }

    @ConfigItem(
            keyName = "fillDirection",
            name = "Fill Direction",
            description = "Velg retningen for nedtellingen (Standard: fra venstre/bunnen, Inverted: fra høyre/toppen)",
            position = 6
    )
    default FillDirection fillDirection()
    {
        return FillDirection.STANDARD;
    }

    enum FillDirection
    {
        STANDARD,
        INVERTED
    }

    @Range(
            min = 0,
            max = 100
    )
    @ConfigItem(
            keyName = "hpBlinkThreshold",
            name = "HP Blink Threshold",
            description = "Angi % HP under hvilket HP-bar skal blinke (0 for deaktivert)",
            position = 7
    )
    default int hpBlinkThreshold()
    {
        return 0;
    }

    @Range(
            min = 0,
            max = 100
    )
    @ConfigItem(
            keyName = "prayerBlinkThreshold",
            name = "Prayer Blink Threshold",
            description = "Angi % Prayer under hvilket Prayer-bar skal blinke (0 for deaktivert)",
            position = 8
    )
    default int prayerBlinkThreshold()
    {
        return 0;
    }

    enum BarType
    {
        DISABLED,
        HITPOINTS,
        PRAYER
    }
}
