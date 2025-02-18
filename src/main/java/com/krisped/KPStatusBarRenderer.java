package com.krisped;

import java.awt.Rectangle;

public class KPStatusBarRenderer
{
    public static class BarDimensions
    {
        public final int x;
        public final int y;
        public final int width;
        public final int height;
        /**
         * True hvis baren er horisontal (top/bottom), false hvis vertikal (left/right)
         */
        public final boolean horizontal;

        public BarDimensions(int x, int y, int width, int height, boolean horizontal)
        {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.horizontal = horizontal;
        }
    }

    /**
     * For top- og bottom-barer reduserer vi bredden med ekstraMarginX på hver side.
     */
    public static BarDimensions getTopBarDimensions(Rectangle chatboxRect, int margin, int thickness)
    {
        int extraMarginX = 6; // ekstra margin på hver side
        int x = chatboxRect.x + margin + extraMarginX;
        int y = chatboxRect.y + margin;
        int width = chatboxRect.width - (margin * 2) - (extraMarginX * 2);
        int height = thickness;
        return new BarDimensions(x, y, width, height, true);
    }

    public static BarDimensions getBottomBarDimensions(Rectangle chatboxRect, int margin, int thickness)
    {
        int extraMarginX = 6; // ekstra margin på hver side
        int x = chatboxRect.x + margin + extraMarginX;
        int height = thickness;
        int y = chatboxRect.y + chatboxRect.height - thickness - margin;
        int width = chatboxRect.width - (margin * 2) - (extraMarginX * 2);
        return new BarDimensions(x, y, width, height, true);
    }

    /**
     * For venstre- og høyre-barer reduserer vi høyden med ekstraMarginY på toppen og bunnen.
     */
    public static BarDimensions getLeftBarDimensions(Rectangle chatboxRect, int margin, int thickness)
    {
        int extraMarginY = 6; // ekstra margin på toppen og bunnen
        int x = chatboxRect.x + margin;
        int y = chatboxRect.y + margin + extraMarginY;
        int width = thickness;
        int height = chatboxRect.height - (margin * 2) - (extraMarginY * 2);
        return new BarDimensions(x, y, width, height, false);
    }

    public static BarDimensions getRightBarDimensions(Rectangle chatboxRect, int margin, int thickness)
    {
        int extraMarginY = 6; // ekstra margin på toppen og bunnen
        int width = thickness;
        int x = chatboxRect.x + chatboxRect.width - width - margin;
        int y = chatboxRect.y + margin + extraMarginY;
        int height = chatboxRect.height - (margin * 2) - (extraMarginY * 2);
        return new BarDimensions(x, y, width, height, false);
    }
}
