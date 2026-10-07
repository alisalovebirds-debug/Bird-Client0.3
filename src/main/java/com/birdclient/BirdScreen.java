package com.birdclient;

import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

public final class BirdScreen extends Screen {

    private static final int BG = 0xA8050710;
    private static final int PANEL = 0xF50D1020;
    private static final int SIDEBAR = 0xF70A0E1B;
    private static final int CARD = 0xE9141A2C;
    private static final int CARD_HOVER = 0xF01C2440;
    private static final int BORDER = 0xFF242D48;
    private static final int ACCENT = 0xFF7566FF;
    private static final int TEXT = 0xFFF1F3FF;
    private static final int MUTED = 0xFF8F96B0;

    private enum TopTab {
        MODULES,
        FAVORITES,
        APPEARANCE
    }

    private final String[] categories = {
            "Combat",
            "Utility",
            "Movement",
            "Misc",
            "Tools",
            "Visual"
    };

    private final String[] general = {
            "Settings",
            "Theme",
            "Configs",
            "Socials",
            "Keybinds"
    };

    /*
     * Only real module currently added.
     *
     * More modules can be added here later.
     */
    private final String[] moduleNames = {
            "TriggerBot"
    };

    /*
     * Which category each module belongs to.
     *
     * 0 = Combat
     * 1 = Utility
     * 2 = Movement
     * 3 = Misc
     * 4 = Tools
     * 5 = Visual
     */
    private final int[] moduleCategories = {
            0
    };

    private final List<Boolean> enabled = new ArrayList<>();
    private final List<Boolean> favorites = new ArrayList<>();

    /*
     * TriggerBot settings.
     *
     * These are UI/settings values only.
     */
    private int triggerBotDelay = 100;
    private int triggerBotSlot = 1;
    private boolean triggerBotWeaponsOnly = false;

    private TopTab topTab = TopTab.MODULES;

    private int selectedCategory = 0;

    private int hoveredCategory = -1;
    private int hoveredGeneral = -1;
    private int hoveredModule = -1;

    private int selectedModule = -1;
    private boolean showingSettings = false;

    /*
     * Vertical scrolling for the module list.
     */
    private int moduleScroll = 0;

    public BirdScreen() {
        super(Text.literal("Bird Client"));

        for (int i = 0; i < moduleNames.length; i++) {
            enabled.add(false);
            favorites.add(false);
        }
    }

    @Override
    protected void init() {
        // Custom-drawn GUI.
    }

    @Override
    public void render(
            DrawContext ctx,
            int mouseX,
            int mouseY,
            float delta
    ) {
        hoveredCategory = -1;
        hoveredGeneral = -1;
        hoveredModule = -1;

        ctx.fill(
                0,
                0,
                width,
                height,
                BG
        );

        int panelW = Math.min(
                900,
                width - 40
        );

        int panelH = Math.min(
                560,
                height - 40
        );

        int left = (width - panelW) / 2;
        int top = (height - panelH) / 2;

        drawPanel(
                ctx,
                left,
                top,
                panelW,
                panelH
        );

        int sideW = 175;

        drawSidebar(
                ctx,
                left,
                top,
                sideW,
                panelH,
                mouseX,
                mouseY
        );

        drawHeader(
                ctx,
                left + sideW,
                top,
                panelW - sideW,
                mouseX,
                mouseY
        );

        drawMain(
                ctx,
                left + sideW,
                top + 82,
                panelW - sideW,
                panelH - 82,
                mouseX,
                mouseY
        );

        String version = "Bird Client 1.21.11";

        int versionW =
                textRenderer.getWidth(version);

        ctx.drawTextWithShadow(
                textRenderer,
                version,
                width - versionW - 12,
                height - 16,
                MUTED
        );

        super.render(
                ctx,
                mouseX,
                mouseY,
                delta
        );
    }

    private void drawPanel(
            DrawContext ctx,
            int x,
            int y,
            int w,
            int h
    ) {
        ctx.fill(
                x + 5,
                y + 7,
                x + w + 5,
                y + h + 7,
                0x70000000
        );

        ctx.fill(
                x,
                y,
                x + w,
                y + h,
                PANEL
        );

        ctx.fill(
                x,
                y,
                x + w,
                y + 1,
                BORDER
        );

        ctx.fill(
                x,
                y + h - 1,
                x + w,
                y + h,
                BORDER
        );

        ctx.fill(
                x,
                y,
                x + 1,
                y + h,
                BORDER
        );

        ctx.fill(
                x + w - 1,
                y,
                x + w,
                y + h,
                BORDER
        );
    }

    private void drawSidebar(
            DrawContext ctx,
            int x,
            int y,
            int w,
            int h,
            int mouseX,
            int mouseY
    ) {
        ctx.fill(
                x + 1,
                y + 1,
                x + w,
                y + h - 1,
                SIDEBAR
        );

        ctx.fill(
                x + w - 1,
                y + 1,
                x + w,
                y + h - 1,
                BORDER
        );

        ctx.drawTextWithShadow(
                textRenderer,
                "🐦",
                x + 16,
                y + 16,
                TEXT
        );

        ctx.drawTextWithShadow(
                textRenderer,
                "Bird Client",
                x + 42,
                y + 14,
                TEXT
        );

        ctx.drawText(
                textRenderer,
                "BETA RELEASE",
                x + 42,
                y + 28,
                MUTED,
                false
        );

        int cursorY = y + 74;

        ctx.drawText(
                textRenderer,
                "MODULES",
                x + 16,
                cursorY,
                MUTED,
                false
        );

        cursorY += 22;

        for (int i = 0; i < categories.length; i++) {

            int rowY = cursorY + i * 32;

            boolean hover =
                    mouseX >= x + 8 &&
                    mouseX <= x + w - 8 &&
                    mouseY >= rowY - 4 &&
                    mouseY < rowY + 25;

            boolean selected =
                    selectedCategory == i;

            if (hover) {
                hoveredCategory = i;
            }

            if (selected) {

                ctx.fill(
                        x + 9,
                        rowY - 4,
                        x + w - 9,
                        rowY + 25,
                        0xFF2B2853
                );

                ctx.fill(
                        x + 9,
                        rowY - 4,
                        x + 12,
                        rowY + 25,
                        ACCENT
                );

            } else if (hover) {

                ctx.fill(
                        x + 9,
                        rowY - 4,
                        x + w - 9,
                        rowY + 25,
                        0xFF171D31
                );
            }

            ctx.drawTextWithShadow(
                    textRenderer,
                    categoryIcon(categories[i]),
                    x + 18,
                    rowY + 5,
                    selected ? TEXT : MUTED
            );

            ctx.drawText(
                    textRenderer,
                    categories[i],
                    x + 40,
                    rowY + 5,
                    selected
                            ? TEXT
                            : 0xFFD2D6E7,
                    false
            );
        }

        int dividerY =
                cursorY +
                categories.length * 32 +
                5;

        ctx.fill(
                x + 16,
                dividerY,
                x + w - 16,
                dividerY + 1,
                BORDER
        );

        ctx.drawText(
                textRenderer,
                "GENERAL",
                x + 16,
                dividerY + 18,
                MUTED,
                false
        );

        int generalY =
                dividerY + 42;

        for (int i = 0; i < general.length; i++) {

            int rowY =
                    generalY + i * 31;

            boolean hover =
                    mouseX >= x + 8 &&
                    mouseX <= x + w - 8 &&
                    mouseY >= rowY - 4 &&
                    mouseY < rowY + 25;

            if (hover) {

                hoveredGeneral = i;

                ctx.fill(
                        x + 9,
                        rowY - 4,
                        x + w - 9,
                        rowY + 25,
                        0xFF171D31
                );
            }

            ctx.drawTextWithShadow(
                    textRenderer,
                    generalIcon(general[i]),
                    x + 18,
                    rowY + 5,
                    MUTED
            );

            ctx.drawText(
                    textRenderer,
                    general[i],
                    x + 40,
                    rowY + 5,
                    0xFFD2D6E7,
                    false
            );
        }
    }

    private void drawHeader(
            DrawContext ctx,
            int x,
            int y,
            int w,
            int mouseX,
            int mouseY
    ) {
        String[] tabs = {
                "Modules",
                "Favorites",
                "Appearance"
        };

        int tabX = x + 28;

        for (int i = 0; i < tabs.length; i++) {

            int tw =
                    textRenderer.getWidth(
                            tabs[i]
                    );

            boolean selected =
                    topTab.ordinal() == i;

            boolean hover =
                    mouseX >= tabX - 8 &&
                    mouseX <= tabX + tw + 8 &&
                    mouseY >= y + 12 &&
                    mouseY <= y + 38;

            if (hover && !selected) {

                ctx.fill(
                        tabX - 8,
                        y + 9,
                        tabX + tw + 8,
                        y + 39,
                        0xFF171D31
                );
            }

            ctx.drawText(
                    textRenderer,
                    tabs[i],
                    tabX,
                    y + 18,
                    selected ? TEXT : MUTED,
                    false
            );

            if (selected) {

                ctx.fill(
                        tabX,
                        y + 36,
                        tabX + tw,
                        y + 38,
                        ACCENT
                );
            }

            tabX += tw + 30;
        }

        int searchW =
                Math.min(
                        220,
                        Math.max(
                                0,
                                w - 330
                        )
                );

        int sx =
                x + w - searchW - 22;

        int sy = y + 11;

        if (searchW > 0) {

            ctx.fill(
                    sx,
                    sy,
                    sx + searchW,
                    sy + 30,
                    0xFF0B1020
            );

            ctx.fill(
                    sx,
                    sy,
                    sx + searchW,
                    sy + 1,
                    BORDER
            );

            ctx.fill(
                    sx,
                    sy + 29,
                    sx + searchW,
                    sy + 30,
                    BORDER
            );

            ctx.drawText(
                    textRenderer,
                    "⌕  Search modules",
                    sx + 10,
                    sy + 10,
                    MUTED,
                    false
            );
        }
    }

    private void drawMain(
            DrawContext ctx,
            int x,
            int y,
            int w,
            int h,
            int mouseX,
            int mouseY
    ) {
        if (
                showingSettings &&
                selectedModule >= 0
        ) {
            drawModuleSettings(
                    ctx,
                    x,
                    y,
                    w,
                    h,
                    mouseX,
                    mouseY
            );

            return;
        }

        String title;
        String subtitle;

        if (topTab == TopTab.MODULES) {

            title = categories[selectedCategory];

            int categoryCount =
                    countModulesInCategory(
                            selectedCategory
                    );

            subtitle =
                    categoryCount +
                    (categoryCount == 1
                            ? " module"
                            : " modules") +
                    " · " +
                    countEnabled() +
                    " enabled";

        } else if (topTab == TopTab.FAVORITES) {

            title = "Favorites";

            subtitle =
                    countFavorites() +
                    " favorite modules";

        } else {

            title = "Appearance";

            subtitle =
                    "Customize the Bird Client look";
        }

        ctx.drawTextWithShadow(
                textRenderer,
                title,
                x + 28,
                y + 8,
                TEXT
        );

        ctx.drawText(
                textRenderer,
                subtitle,
                x + 28,
                y + 27,
                MUTED,
                false
        );

        if (topTab == TopTab.APPEARANCE) {

            drawAppearance(
                    ctx,
                    x + 28,
                    y + 55,
                    w - 56,
                    mouseX,
                    mouseY
            );

            return;
        }

        /*
         * MODULE VIEW
         *
         * There is intentionally NO extra
         * "Combat Modules" / "Movement Modules"
         * text here.
         *
         * The sidebar already tells us the category.
         */

        int listTop = y + 54;
        int listBottom = y + h - 10;

        /*
         * Clip the module list so cards stay inside
         * the scrollable area.
         */
        ctx.enableScissor(
                x + 20,
                listTop,
                x + w - 20,
                listBottom
        );

        int cardY =
                listTop -
                moduleScroll;

        int cardH = 48;
        int gap = 7;

        int visibleIndex = 0;

        for (int i = 0; i < moduleNames.length; i++) {

            boolean shouldShow;

            if (topTab == TopTab.FAVORITES) {

                shouldShow =
                        favorites.get(i);

            } else {

                shouldShow =
                        moduleCategories[i]
                                ==
                                selectedCategory;
            }

            if (!shouldShow) {
                continue;
            }

            int cy =
                    cardY +
                    visibleIndex *
                            (cardH + gap);

            visibleIndex++;

            drawModuleCard(
                    ctx,
                    x,
                    w,
                    cy,
                    cardH,
                    i,
                    mouseX,
                    mouseY
            );
        }

        ctx.disableScissor();

        int shownCount =
                topTab == TopTab.FAVORITES
                        ? countFavorites()
                        : countModulesInCategory(
                                selectedCategory
                        );

        if (shownCount == 0) {

            ctx.drawText(
                    textRenderer,
                    topTab == TopTab.FAVORITES
                            ? "No favorite modules yet."
                            : "No modules in this category yet.",
                    x + 28,
                    listTop + 20,
                    MUTED,
                    false
            );
        }

        /*
         * Scroll bar.
         */
        drawScrollBar(
                ctx,
                x,
                y,
                w,
                h,
                shownCount
        );
    }

    private void drawModuleCard(
            DrawContext ctx,
            int x,
            int w,
            int cy,
            int cardH,
            int moduleIndex,
            int mouseX,
            int mouseY
    ) {
        int cardLeft = x + 28;
        int cardRight = x + w - 28;

        boolean hover =
                mouseX >= cardLeft &&
                mouseX <= cardRight &&
                mouseY >= cy &&
                mouseY <= cy + cardH;

        if (hover) {
            hoveredModule = moduleIndex;
        }

        ctx.fill(
                cardLeft,
                cy,
                cardRight,
                cy + cardH,
                hover
                        ? CARD_HOVER
                        : CARD
        );

        ctx.fill(
                cardLeft,
                cy,
                cardRight,
                cy + 1,
                BORDER
        );

        ctx.fill(
                cardLeft,
                cy + cardH - 1,
                cardRight,
                cy + cardH,
                BORDER
        );

        ctx.drawTextWithShadow(
                textRenderer,
                moduleNames[moduleIndex],
                cardLeft + 14,
                cy + 8,
                TEXT
        );

        ctx.drawText(
                textRenderer,
                description(moduleNames[moduleIndex]),
                cardLeft + 14,
                cy + 27,
                MUTED,
                false
        );

        /*
         * Favorite star.
         */
        ctx.drawText(
                textRenderer,
                favorites.get(moduleIndex)
                        ? "★"
                        : "☆",
                cardRight - 112,
                cy + 16,
                favorites.get(moduleIndex)
                        ? ACCENT
                        : MUTED,
                false
        );

        /*
         * Settings arrow.
         */
        ctx.drawText(
                textRenderer,
                "›",
                cardRight - 82,
                cy + 15,
                MUTED,
                false
        );

        /*
         * Enable toggle.
         */
        drawToggle(
                ctx,
                cardRight - 55,
                cy + 16,
                enabled.get(moduleIndex)
        );
    }

    private void drawScrollBar(
            DrawContext ctx,
            int x,
            int y,
            int w,
            int h,
            int itemCount
    ) {
        int visibleItems = 7;

        if (itemCount <= visibleItems) {
            return;
        }

        int trackTop = y + 54;
        int trackBottom = y + h - 10;
        int trackHeight =
                trackBottom - trackTop;

        ctx.fill(
                x + w - 14,
                trackTop,
                x + w - 10,
                trackBottom,
                0xFF151B2B
        );

        int maxScroll =
                getMaxScroll(
                        itemCount
                );

        float progress =
                maxScroll <= 0
                        ? 0
                        : (float) moduleScroll
                        / maxScroll;

        int thumbHeight =
                Math.max(
                        30,
                        trackHeight *
                                visibleItems /
                                itemCount
                );

        int thumbTravel =
                trackHeight -
                thumbHeight;

        int thumbY =
                trackTop +
                (int) (
                        thumbTravel *
                        progress
                );

        ctx.fill(
                x + w - 14,
                thumbY,
                x + w - 10,
                thumbY + thumbHeight,
                ACCENT
        );
    }

    private int getMaxScroll(
            int itemCount
    ) {
        int cardH = 48;
        int gap = 7;
        int visibleItems = 7;

        int contentHeight =
                itemCount *
                        (cardH + gap);

        int visibleHeight =
                visibleItems *
                        (cardH + gap);

        return Math.max(
                0,
                contentHeight -
                        visibleHeight
        );
    }

    private void drawModuleSettings(
            DrawContext ctx,
            int x,
            int y,
            int w,
            int h,
            int mouseX,
            int mouseY
    ) {
        String module =
                moduleNames[selectedModule];

        ctx.drawTextWithShadow(
                textRenderer,
                module + " Settings",
                x + 28,
                y + 8,
                TEXT
        );

        ctx.drawText(
                textRenderer,
                "Customize this module.",
                x + 28,
                y + 27,
                MUTED,
                false
        );

        /*
         * Back button.
         */
        int backX =
                x + w - 100;

        int backY =
                y + 5;

        boolean backHover =
                inside(
                        mouseX,
                        mouseY,
                        backX,
                        backY,
                        backX + 70,
                        backY + 30
                );

        ctx.fill(
                backX,
                backY,
                backX + 70,
                backY + 30,
                backHover
                        ? CARD_HOVER
                        : CARD
        );

        ctx.drawText(
                textRenderer,
                "Back",
                backX + 20,
                backY + 10,
                TEXT,
                false
        );

        int cardX = x + 28;
        int cardW = w - 56;

        /*
         * Enabled.
         */
        int enabledY = y + 58;

        ctx.fill(
                cardX,
                enabledY,
                cardX + cardW,
                enabledY + 70,
                CARD
        );

        ctx.drawTextWithShadow(
                textRenderer,
                "Module enabled",
                cardX + 15,
                enabledY + 12,
                TEXT
        );

        ctx.drawText(
                textRenderer,
                "Turn this module on or off.",
                cardX + 15,
                enabledY + 32,
                MUTED,
                false
        );

        drawToggle(
                ctx,
                cardX + cardW - 45,
                enabledY + 18,
                enabled.get(selectedModule)
        );

        /*
         * TRIGGERBOT SETTINGS
         */
        if (selectedModule == 0) {

            /*
             * Delay.
             */
            int delayY =
                    enabledY + 90;

            ctx.fill(
                    cardX,
                    delayY,
                    cardX + cardW,
                    delayY + 82,
                    CARD
            );

            ctx.drawTextWithShadow(
                    textRenderer,
                    "Delay",
                    cardX + 15,
                    delayY + 12,
                    TEXT
            );

            ctx.drawText(
                    textRenderer,
                    triggerBotDelay + " / 100",
                    cardX + 15,
                    delayY + 34,
                    MUTED,
                    false
            );

            int sliderLeft =
                    cardX + 110;

            int sliderRight =
                    cardX + cardW - 20;

            int sliderY =
                    delayY + 41;

            ctx.fill(
                    sliderLeft,
                    sliderY,
                    sliderRight,
                    sliderY + 4,
                    BORDER
            );

            float progress =
                    (triggerBotDelay - 1)
                            / 99.0f;

            int knobX =
                    sliderLeft +
                    (int) (
                            (sliderRight -
                                    sliderLeft)
                                    * progress
                    );

            ctx.fill(
                    sliderLeft,
                    sliderY,
                    knobX,
                    sliderY + 4,
                    ACCENT
            );

            ctx.fill(
                    knobX - 4,
                    sliderY - 5,
                    knobX + 4,
                    sliderY + 9,
                    ACCENT
            );

            /*
             * Slot restriction.
             */
            int slotY =
                    delayY + 94;

            ctx.fill(
                    cardX,
                    slotY,
                    cardX + cardW,
                    slotY + 70,
                    CARD
            );

            ctx.drawTextWithShadow(
                    textRenderer,
                    "Slot Restriction",
                    cardX + 15,
                    slotY + 12,
                    TEXT
            );

            ctx.drawText(
                    textRenderer,
                    "Hotbar slot: " +
                            triggerBotSlot,
                    cardX + 15,
                    slotY + 34,
                    MUTED,
                    false
            );

            /*
             * Slot - button.
             */
            int minusX =
                    cardX + cardW - 95;

            ctx.fill(
                    minusX,
                    slotY + 18,
                    minusX + 25,
                    slotY + 43,
                    0xFF20273A
            );

            ctx.drawText(
                    textRenderer,
                    "−",
                    minusX + 8,
                    slotY + 21,
                    TEXT,
                    false
            );

            /*
             * Slot + button.
             */
            int plusX =
                    cardX + cardW - 60;

            ctx.fill(
                    plusX,
                    slotY + 18,
                    plusX + 25,
                    slotY + 43,
                    0xFF20273A
            );

            ctx.drawText(
                    textRenderer,
                    "+",
                    plusX + 8,
                    slotY + 21,
                    TEXT,
                    false
            );

            /*
             * Weapons Only.
             */
            int weaponY =
                    slotY + 82;

            ctx.fill(
                    cardX,
                    weaponY,
                    cardX + cardW,
                    weaponY + 70,
                    CARD
            );

            ctx.drawTextWithShadow(
                    textRenderer,
                    "Weapons Only",
                    cardX + 15,
                    weaponY + 12,
                    TEXT
            );

            ctx.drawText(
                    textRenderer,
                    "Restrict this setting to weapon items.",
                    cardX + 15,
                    weaponY + 32,
                    MUTED,
                    false
            );

            drawToggle(
                    ctx,
                    cardX + cardW - 45,
                    weaponY + 18,
                    triggerBotWeaponsOnly
            );
        }
    }

    private void drawAppearance(
            DrawContext ctx,
            int x,
            int y,
            int w,
            int mouseX,
            int mouseY
    ) {
        String[] items = {
                "Dark theme",
                "Compact cards",
                "Blur background",
                "Accent color"
        };

        for (int i = 0; i < items.length; i++) {

            int cy =
                    y + i * 65;

            ctx.fill(
                    x,
                    cy,
                    x + w,
                    cy + 54,
                    CARD
            );

            ctx.drawTextWithShadow(
                    textRenderer,
                    items[i],
                    x + 15,
                    cy + 12,
                    TEXT
            );

            ctx.drawText(
                    textRenderer,
                    appearanceDescription(i),
                    x + 15,
                    cy + 31,
                    MUTED,
                    false
            );

            if (i < 3) {

                drawToggle(
                        ctx,
                        x + w - 52,
                        cy + 17,
                        i != 2
                );
            }
        }
    }

    private void drawToggle(
            DrawContext ctx,
            int x,
            int y,
            boolean on
    ) {
        int color =
                on
                        ? ACCENT
                        : 0xFF20273A;

        ctx.fill(
                x,
                y,
                x + 30,
                y + 16,
                color
        );

        ctx.fill(
                x + (on ? 17 : 1),
                y + 2,
                x + (on ? 28 : 12),
                y + 14,
                0xFFF2F4FF
        );
    }

    private int countEnabled() {
        int count = 0;

        for (boolean b : enabled) {
            if (b) {
                count++;
            }
        }

        return count;
    }

    private int countFavorites() {
        int count = 0;

        for (boolean b : favorites) {
            if (b) {
                count++;
            }
        }

        return count;
    }

    private int countModulesInCategory(
            int category
    ) {
        int count = 0;

        for (int i = 0; i < moduleNames.length; i++) {

            if (moduleCategories[i] == category) {
                count++;
            }
        }

        return count;
    }

    private String categoryIcon(
            String name
    ) {
        return switch (name) {
            case "Combat" -> "⚔";
            case "Utility" -> "✦";
            case "Movement" -> "↗";
            case "Misc" -> "⌁";
            case "Tools" -> "▣";
            case "Visual" -> "◉";
            default -> "•";
        };
    }

    private String generalIcon(
            String name
    ) {
        return switch (name) {
            case "Settings" -> "☰";
            case "Theme" -> "◐";
            case "Configs" -> "▣";
            case "Socials" -> "♟";
            case "Keybinds" -> "⌨";
            default -> "•";
        };
    }

    private String description(
            String name
    ) {
        return switch (name) {

            case "TriggerBot" ->
                    "TriggerBot settings and configuration.";

            default ->
                    "Bird Client module.";
        };
    }

    private String appearanceDescription(
            int index
    ) {
        return switch (index) {

            case 0 ->
                    "Use the Bird Client dark interface.";

            case 1 ->
                    "Use smaller spacing between module cards.";

            case 2 ->
                    "Adds a soft blur behind the client menu.";

            case 3 ->
                    "Choose the accent color used by the interface.";

            default ->
                    "";
        };
    }

    @Override
    public boolean mouseClicked(
            Click click,
            boolean doubled
    ) {
        double mouseX = click.x();
        double mouseY = click.y();

        int button = click.button();

        int panelW =
                Math.min(
                        900,
                        width - 40
                );

        int panelH =
                Math.min(
                        560,
                        height - 40
                );

        int left =
                (width - panelW) / 2;

        int top =
                (height - panelH) / 2;

        int sideW = 175;

        /*
         * SETTINGS SCREEN
         */
        if (
                showingSettings &&
                selectedModule >= 0
        ) {
            int mainX =
                    left + sideW;

            int mainY =
                    top + 82;

            int mainW =
                    panelW - sideW;

            int backX =
                    mainX + mainW - 100;

            int backY =
                    mainY + 5;

            if (
                    inside(
                            mouseX,
                            mouseY,
                            backX,
                            backY,
                            backX + 70,
                            backY + 30
                    )
            ) {
                showingSettings = false;
                selectedModule = -1;
                return true;
            }

            /*
             * Enable toggle.
             */
            int cardX =
                    mainX + 28;

            int enabledY =
                    mainY + 58;

            int cardW =
                    mainW - 56;

            if (
                    inside(
                            mouseX,
                            mouseY,
                            cardX,
                            enabledY,
                            cardX + cardW,
                            enabledY + 70
                    )
            ) {
                if (button == 0) {
                    enabled.set(
                            selectedModule,
                            !enabled.get(
                                    selectedModule
                            )
                    );
                }

                return true;
            }

            /*
             * TriggerBot settings.
             */
            if (selectedModule == 0) {

                /*
                 * Delay slider.
                 */
                int delayY =
                        enabledY + 90;

                int sliderLeft =
                        cardX + 110;

                int sliderRight =
                        cardX + cardW - 20;

                int sliderY =
                        delayY + 41;

                if (
                        inside(
                                mouseX,
                                mouseY,
                                sliderLeft - 5,
                                sliderY - 10,
                                sliderRight + 5,
                                sliderY + 15
                        )
                ) {
                    if (button == 0) {

                        double progress =
                                (mouseX -
                                        sliderLeft)
                                        /
                                        (double)
                                                (
                                                        sliderRight -
                                                        sliderLeft
                                                );

                        progress =
                                Math.max(
                                        0,
                                        Math.min(
                                                1,
                                                progress
                                        )
                                );

                        triggerBotDelay =
                                1 +
                                (int)
                                        Math.round(
                                                progress *
                                                        99
                                        );
                    }

                    return true;
                }

                /*
                 * Slot restriction -.
                 */
                int slotY =
                        delayY + 94;

                int minusX =
                        cardX + cardW - 95;

                if (
                        inside(
                                mouseX,
                                mouseY,
                                minusX,
                                slotY + 18,
                                minusX + 25,
                                slotY + 43
                        )
                ) {
                    if (button == 0) {
                        triggerBotSlot =
                                Math.max(
                                        1,
                                        triggerBotSlot - 1
                                );
                    }

                    return true;
                }

                /*
                 * Slot restriction +.
                 */
                int plusX =
                        cardX + cardW - 60;

                if (
                        inside(
                                mouseX,
                                mouseY,
                                plusX,
                                slotY + 18,
                                plusX + 25,
                                slotY + 43
                        )
                ) {
                    if (button == 0) {
                        triggerBotSlot =
                                Math.min(
                                        9,
                                        triggerBotSlot + 1
                                );
                    }

                    return true;
                }

                /*
                 * Weapons Only.
                 */
                int weaponY =
                        slotY + 82;

                if (
                        inside(
                                mouseX,
                                mouseY,
                                cardX,
                                weaponY,
                                cardX + cardW,
                                weaponY + 70
                        )
                ) {
                    if (button == 0) {
                        triggerBotWeaponsOnly =
                                !triggerBotWeaponsOnly;
                    }

                    return true;
                }
            }

            return true;
        }

        /*
         * TOP TABS
         */
        int tabX =
                left + sideW + 28;

        String[] tabs = {
                "Modules",
                "Favorites",
                "Appearance"
        };

        for (int i = 0; i < tabs.length; i++) {

            int tw =
                    textRenderer.getWidth(
                            tabs[i]
                    );

            if (
                    mouseX >= tabX - 8 &&
                    mouseX <= tabX + tw + 8 &&
                    mouseY >= top + 9 &&
                    mouseY <= top + 40
            ) {

                if (button == 0) {
                    topTab =
                            TopTab.values()[i];

                    moduleScroll = 0;
                }

                return true;
            }

            tabX += tw + 30;
        }

        /*
         * SIDEBAR CATEGORIES
         */
        int cursorY =
                top + 74 + 22;

        for (
                int i = 0;
                i < categories.length;
                i++
        ) {

            int rowY =
                    cursorY + i * 32;

            if (
                    inside(
                            mouseX,
                            mouseY,
                            left + 8,
                            rowY - 4,
                            left + sideW - 8,
                            rowY + 25
                    )
            ) {

                if (button == 0) {
                    selectedCategory = i;
                    topTab = TopTab.MODULES;
                    moduleScroll = 0;
                }

                return true;
            }
        }

        /*
         * MODULES
         */
        if (
                topTab == TopTab.MODULES ||
                topTab == TopTab.FAVORITES
        ) {

            int mainX =
                    left + sideW;

            int mainY =
                    top + 82;

            int listTop =
                    mainY + 54;

            int cardY =
                    listTop -
                    moduleScroll;

            int cardH = 48;
            int gap = 7;

            int visibleIndex = 0;

            for (
                    int i = 0;
                    i < moduleNames.length;
                    i++
            ) {

                boolean shouldShow;

                if (
                        topTab ==
                                TopTab.FAVORITES
                ) {
                    shouldShow =
                            favorites.get(i);
                } else {
                    shouldShow =
                            moduleCategories[i]
                                    ==
                                    selectedCategory;
                }

                if (!shouldShow) {
                    continue;
                }

                int cy =
                        cardY +
                        visibleIndex *
                                (cardH + gap);

                visibleIndex++;

                int cardLeft =
                        mainX + 28;

                int cardRight =
                        left + panelW - 28;

                if (
                        !inside(
                                mouseX,
                                mouseY,
                                cardLeft,
                                cy,
                                cardRight,
                                cy + cardH
                        )
                ) {
                    continue;
                }

                /*
                 * STAR
                 */
                int starLeft =
                        cardRight - 125;

                int starRight =
                        cardRight - 88;

                if (
                        mouseX >= starLeft &&
                        mouseX <= starRight
                ) {

                    if (button == 0) {
                        favorites.set(
                                i,
                                !favorites.get(i)
                        );
                    }

                    return true;
                }

                /*
                 * RIGHT CLICK = SETTINGS
                 */
                if (button == 1) {

                    selectedModule = i;
                    showingSettings = true;

                    return true;
                }

                /*
                 * LEFT CLICK = TOGGLE
                 */
                if (button == 0) {

                    enabled.set(
                            i,
                            !enabled.get(i)
                    );

                    return true;
                }
            }
        }

        return super.mouseClicked(
                click,
                doubled
        );
    }

    @Override
    public boolean mouseScrolled(
            double mouseX,
            double mouseY,
            double horizontalAmount,
            double verticalAmount
    ) {
        if (
                showingSettings ||
                topTab == TopTab.APPEARANCE
        ) {
            return super.mouseScrolled(
                    mouseX,
                    mouseY,
                    horizontalAmount,
                    verticalAmount
            );
        }

        int panelW =
                Math.min(
                        900,
                        width - 40
                );

        int panelH =
                Math.min(
                        560,
                        height - 40
                );

        int left =
                (width - panelW) / 2;

        int top =
                (height - panelH) / 2;

        int sideW = 175;

        int mainX =
                left + sideW;

        int mainY =
                top + 82;

        int mainW =
                panelW - sideW;

        int listTop =
                mainY + 54;

        int listBottom =
                mainY + panelH - 92;

        /*
         * Only scroll when mouse is over
         * the module list.
         */
        if (
                mouseX >= mainX + 20 &&
                mouseX <= mainX + mainW - 20 &&
                mouseY >= listTop &&
                mouseY <= listBottom
        ) {

            int itemCount;

            if (topTab == TopTab.FAVORITES) {
                itemCount = countFavorites();
            } else {
                itemCount =
                        countModulesInCategory(
                                selectedCategory
                        );
            }

            int maxScroll =
                    getMaxScroll(itemCount);

            moduleScroll -=
                    (int)
                            (verticalAmount * 25);

            moduleScroll =
                    Math.max(
                            0,
                            Math.min(
                                    maxScroll,
                                    moduleScroll
                            )
                    );

            return true;
        }

        return super.mouseScrolled(
                mouseX,
                mouseY,
                horizontalAmount,
                verticalAmount
        );
    }

    private boolean inside(
            double mx,
            double my,
            int x1,
            int y1,
            int x2,
            int y2
    ) {
        return mx >= x1 &&
                mx <= x2 &&
                my >= y1 &&
                my <= y2;
    }

    @Override
    public boolean keyPressed(
            KeyInput input
    ) {
        int keyCode =
                input.key();

        if (
                keyCode ==
                        GLFW.GLFW_KEY_ESCAPE
        ) {
            close();
            return true;
        }

        if (
                keyCode ==
                        GLFW.GLFW_KEY_RIGHT_SHIFT
        ) {
            close();
            return true;
        }

        return super.keyPressed(
                input
        );
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
    }
