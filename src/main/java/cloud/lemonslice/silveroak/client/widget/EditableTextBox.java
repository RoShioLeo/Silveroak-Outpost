package cloud.lemonslice.silveroak.client.widget;

import cloud.lemonslice.silveroak.network.SimpleNetworkHandler;
import cloud.lemonslice.silveroak.network.TextBoxEditMessage;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class EditableTextBox extends AbstractWidget
{
    private final Minecraft mc;
    private final Font font;

    private boolean isModified = false;
    private int updateCount = 0;
    private long lastClickTime = 0;
    private final int color;
    private final int spacing;

    private final TextFieldHelper textInputUtil;
    @Nullable
    private Page currentPage = null;
    private String page = "";

    private final ItemStack item;
    private final Player editingPlayer;
    private final InteractionHand hand;

    public EditableTextBox(ItemStack item, Player playerIn, InteractionHand handIn, int x, int y, int boxWidth, int boxHeight, int spacingPixel, int color, Component title)
    {
        super(x, y, boxWidth, boxHeight, title);
        this.mc = Minecraft.getInstance();
        this.font = mc.font;
        this.color = color;
        this.spacing = spacingPixel;
        this.item = item;
        this.editingPlayer = playerIn;
        this.hand = handIn;

        CompoundTag compoundnbt = item.getTag();
        if (compoundnbt != null)
        {
            Tag nbt = compoundnbt.get("Text");
            if (nbt != null)
            {
                this.page = nbt.copy().getAsString();
            }
        }

        this.textInputUtil = new TextFieldHelper(() -> page, this::setText, this::getClipboardText, this::setClipboardText, (text) -> text.length() < 1024 && this.font.wordWrapHeight(text, boxWidth) <= boxHeight * font.lineHeight / spacingPixel);
    }

    // Please link to Screen
    public void tick()
    {
        ++this.updateCount;
    }

    public void sendTextToServer()
    {
        if (this.isModified)
        {
            this.item.addTagElement("Text", StringTag.valueOf(this.page));
            int i = this.hand == InteractionHand.MAIN_HAND ? this.editingPlayer.getInventory().selected : 40;
            SimpleNetworkHandler.CHANNEL.sendToServer(new TextBoxEditMessage(item, i));
        }
    }

    private Page createPage()
    {
        if (page.isEmpty())
        {
            return Page.EMPTY;
        }
        else
        {
            int selectionEnd = this.textInputUtil.getCursorPos();
            int selectionStart = this.textInputUtil.getSelectionPos();
            IntList intlist = new IntArrayList();
            List<Line> lines = Lists.newArrayList();
            MutableInt mutableint = new MutableInt();
            MutableBoolean mutableboolean = new MutableBoolean();
            StringSplitter stringSplitter = this.font.getSplitter();
            stringSplitter.splitLines(page, width, Style.EMPTY, true, (style, lineStartPos, lineEndPos) ->
            {
                int lineCount = mutableint.getAndIncrement();
                String lineTextRaw = page.substring(lineStartPos, lineEndPos);
                mutableboolean.setValue(lineTextRaw.endsWith("\n"));
                String lineText = StringUtils.stripEnd(lineTextRaw, " \n");
                int y = lineCount * spacing;
                Point point = this.getPointPosInScreen(new Point(0, y));
                intlist.add(lineStartPos);
                lines.add(new Line(style, lineText, point.x, point.y));
            });
            int[] linesStartPos = intlist.toIntArray();
            boolean flag = selectionEnd == page.length();
            Point point;
            if (flag && mutableboolean.isTrue())
            {
                point = new Point(0, lines.size() * spacing);
            }
            else
            {
                int line = getCursorLine(linesStartPos, selectionEnd);
                point = new Point(this.font.width(page.substring(linesStartPos[line], selectionEnd)), line * spacing);
            }

            List<Rect2i> rectangleList = Lists.newArrayList();
            if (selectionEnd != selectionStart)
            {
                int selectionMin = Math.min(selectionEnd, selectionStart);
                int selectionMax = Math.max(selectionEnd, selectionStart);
                int selectionStartLine = getCursorLine(linesStartPos, selectionMin);
                int selectionEndLine = getCursorLine(linesStartPos, selectionMax);
                if (selectionStartLine == selectionEndLine)
                {
                    int linePosY = selectionStartLine * spacing;
                    int linePosX = linesStartPos[selectionStartLine];
                    rectangleList.add(this.getRectangle(page, stringSplitter, selectionMin, selectionMax, linePosY, linePosX));
                }
                else
                {
                    int i3 = selectionStartLine + 1 > linesStartPos.length ? page.length() : linesStartPos[selectionStartLine + 1];
                    rectangleList.add(this.getRectangle(page, stringSplitter, selectionMin, i3, selectionStartLine * spacing, linesStartPos[selectionStartLine]));

                    for (int j3 = selectionStartLine + 1; j3 < selectionEndLine; ++j3)
                    {
                        int j2 = j3 * spacing;
                        String s1 = page.substring(linesStartPos[j3], linesStartPos[j3 + 1]);
                        int k2 = (int) stringSplitter.stringWidth(s1);
                        rectangleList.add(this.getRectangle(new Point(0, j2), new Point(k2, j2 + 9)));
                    }

                    rectangleList.add(this.getRectangle(page, stringSplitter, linesStartPos[selectionEndLine], selectionMax, selectionEndLine * spacing, linesStartPos[selectionEndLine]));
                }
            }

            return new Page(page, point, flag, linesStartPos, lines.toArray(new Line[0]), rectangleList.toArray(new Rect2i[0]));
        }
    }

    private Rect2i getRectangle(String text, StringSplitter characterManager, int from, int to, int lineStart, int lineEnd)
    {
        String s = text.substring(lineEnd, from);
        String s1 = text.substring(lineEnd, to);
        Point pointFrom = new Point((int) characterManager.stringWidth(s), lineStart);
        Point pointTo = new Point((int) characterManager.stringWidth(s1), lineStart + spacing);
        return this.getRectangle(pointFrom, pointTo);
    }

    private Rect2i getRectangle(Point pointFromIn, Point pointToIn)
    {
        Point pointFrom = this.getPointPosInScreen(pointFromIn);
        Point pointTo = this.getPointPosInScreen(pointToIn);
        int i = Math.min(pointFrom.x, pointTo.x);
        int j = Math.max(pointFrom.x, pointTo.x);
        int k = Math.min(pointFrom.y, pointTo.y);
        int l = Math.max(pointFrom.y, pointTo.y);
        return new Rect2i(i, k, j - i, l - k);
    }

    private Page getPage()
    {
        if (this.currentPage == null)
        {
            this.currentPage = this.createPage();
        }

        return this.currentPage;
    }

    private void setText(String text)
    {
        this.page = text;
        this.isModified = true;
    }

    private String getClipboardText()
    {
        return TextFieldHelper.getClipboardContents(this.mc);
    }

    private void setClipboardText(String text)
    {
        TextFieldHelper.setClipboardContents(this.mc, text);
    }

    public void shouldRefresh()
    {
        this.currentPage = null;
    }

    private void getDownLine()
    {
        this.toMoveCursorLine(1);
    }

    private void getUpLine()
    {
        this.toMoveCursorLine(-1);
    }

    private void toMoveCursorLine(int lineAdded)
    {
        int i = this.textInputUtil.getCursorPos();
        int j = this.getPage().getLineToMove(i, lineAdded);
        this.textInputUtil.setCursorPos(j, Screen.hasShiftDown());
    }

    private static int getCursorLine(int[] linesLength, int cursorPos)
    {
        int i = Arrays.binarySearch(linesLength, cursorPos);
        return i < 0 ? -(i + 2) : i;
    }

    private void moveToLineHead()
    {
        int i = this.textInputUtil.getCursorPos();
        int j = this.getPage().getLineStartPos(i);
        this.textInputUtil.setCursorPos(j, Screen.hasShiftDown());
    }

    private void moveToLineEnd()
    {
        int i = this.textInputUtil.getCursorPos();
        int j = getPage().getLineEndPos(i);
        this.textInputUtil.setCursorPos(j, Screen.hasShiftDown());
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers)
    {
        if (super.keyPressed(keyCode, scanCode, modifiers))
        {
            return true;
        }
        else
        {
            boolean flag = this.keyPressedInBook(keyCode, scanCode, modifiers);
            if (flag)
            {
                this.shouldRefresh();
                return true;
            }
            else
            {
                return false;
            }
        }
    }

    private boolean keyPressedInBook(int keyCode, int scanCode, int modifiers)
    {
        if (Screen.isSelectAll(keyCode))
        {
            this.textInputUtil.selectAll();
            return true;
        }
        else if (Screen.isCopy(keyCode))
        {
            this.textInputUtil.copy();
            return true;
        }
        else if (Screen.isPaste(keyCode))
        {
            this.textInputUtil.paste();
            return true;
        }
        else if (Screen.isCut(keyCode))
        {
            this.textInputUtil.cut();
            return true;
        }
        else
        {
            switch (keyCode)
            {
                case 257:
                case 335:
                    this.textInputUtil.insertText("\n");
                    return true;
                case 259:
                    this.textInputUtil.removeCharsFromCursor(-1);
                    return true;
                case 261:
                    this.textInputUtil.removeCharsFromCursor(1);
                    return true;
                case 262:
                    this.textInputUtil.moveByChars(1, Screen.hasShiftDown());
                    return true;
                case 263:
                    this.textInputUtil.moveByChars(-1, Screen.hasShiftDown());
                    return true;
                case 264:
                    this.getDownLine();
                    return true;
                case 265:
                    this.getUpLine();
                    return true;
                case 268:
                    this.moveToLineHead();
                    return true;
                case 269:
                    this.moveToLineEnd();
                    return true;
                default:
                    return false;
            }
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers)
    {
        if (super.charTyped(codePoint, modifiers))
        {
            return true;
        }
        else if (SharedConstants.isAllowedChatCharacter(codePoint))
        {
            this.textInputUtil.insertText(Character.toString(codePoint));
            this.shouldRefresh();
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Page page = this.getPage();

        for (Line line : page.lines)
        {
            gui.drawString(this.font, line.lineTextComponent, line.x, line.y, color, false);
        }

        this.renderSelection(page.selection);
        this.renderCursor(gui, page.point, page.isInsert);
    }

    private void renderSelection(Rect2i[] selection)
    {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionShader);
        RenderSystem.setShaderColor(0.0F, 0.0F, 1.0F, 1.0F);
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION);

        for (Rect2i rectangle2d : selection)
        {
            int i = rectangle2d.getX();
            int j = rectangle2d.getY();
            int k = i + rectangle2d.getWidth();
            int l = j + rectangle2d.getHeight();
            bufferbuilder.vertex(i, l, 0.0D).endVertex();
            bufferbuilder.vertex(k, l, 0.0D).endVertex();
            bufferbuilder.vertex(k, j, 0.0D).endVertex();
            bufferbuilder.vertex(i, j, 0.0D).endVertex();
        }

        tesselator.end();
        RenderSystem.disableColorLogicOp();
    }

    private void renderCursor(GuiGraphics gui, Point point, boolean isInsert)
    {
        if (this.updateCount / 6 % 2 == 0 && height != 0)
        {
            point = this.getPointPosInScreen(point);
            if (!isInsert)
            {
                gui.fill(point.x, point.y - 1, point.x + 1, point.y + 9, color);
            }
            else
            {
                gui.drawString(this.font, "_", point.x, point.y, color, false);
            }
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (button == 0)
        {
            long i = Util.getMillis();
            int j = getPage().getMousePointPosInText(this.font, this.getPointPosInBox(new Point((int) mouseX, (int) mouseY)), spacing);
            if (j >= 0)
            {
                if (i - this.lastClickTime < 250L)
                {
                    if (!this.textInputUtil.isSelecting())
                    {
                        this.setSelection(j);
                    }
                    else
                    {
                        this.textInputUtil.selectAll();
                    }
                }
                else
                {
                    this.textInputUtil.setCursorPos(j, Screen.hasShiftDown());
                }

                this.shouldRefresh();
            }

            this.lastClickTime = i;
        }

        return true;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        if (button == 0)
        {
            int i = getPage().getMousePointPosInText(this.font, this.getPointPosInBox(new Point((int) mouseX, (int) mouseY)), spacing);
            this.textInputUtil.setCursorPos(i, true);
            this.shouldRefresh();
        }
        return true;
    }

    private void setSelection(int cursorPos)
    {
        this.textInputUtil.setSelectionRange(StringSplitter.getWordPosition(page, -1, cursorPos, false), StringSplitter.getWordPosition(page, 1, cursorPos, false));
    }

    private Point getPointPosInBox(Point pointIn)
    {
        return new Point(pointIn.x - this.getX(), pointIn.y - this.getY());
    }

    private Point getPointPosInScreen(Point pointIn)
    {
        return new Point(pointIn.x + this.getX(), pointIn.y + this.getY());
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput)
    {

    }

    static class Page
    {
        protected static final Page EMPTY = new Page("", new Point(0, 0), true, new int[]{0}, new Line[]{new Line(Style.EMPTY, "", 0, 0)}, new Rect2i[0]);
        private final String text;
        private final Point point;
        private final boolean isInsert;
        private final int[] linesStartPos;
        protected final Line[] lines;
        private final Rect2i[] selection;

        public Page(String text, Point point, boolean isInsert, int[] linesStartPos, Line[] lines, Rect2i[] selection)
        {
            this.text = text;
            this.point = point;
            this.isInsert = isInsert;
            this.linesStartPos = linesStartPos;
            this.lines = lines;
            this.selection = selection;
        }

        public int getMousePointPosInText(Font font, Point point, int spacingPixel)
        {
            int linePos = point.y / spacingPixel;
            if (linePos < 0)
            {
                return 0;
            }
            else if (linePos >= this.lines.length)
            {
                return this.text.length();
            }
            else
            {
                Line line = this.lines[linePos];
                return this.linesStartPos[linePos] + font.getSplitter().plainIndexAtWidth(line.lineText, point.x, line.style);
            }
        }

        public int getLineToMove(int cursorPos, int lineAdded)
        {
            int i = getCursorLine(this.linesStartPos, cursorPos);
            int j = i + lineAdded;
            int k;
            if (0 <= j && j < this.linesStartPos.length)
            {
                int l = cursorPos - this.linesStartPos[i];
                int i1 = this.lines[j].lineText.length();
                k = this.linesStartPos[j] + Math.min(l, i1);
            }
            else
            {
                k = cursorPos;
            }

            return k;
        }

        public int getLineStartPos(int cursorPos)
        {
            int i = getCursorLine(this.linesStartPos, cursorPos);
            return this.linesStartPos[i];
        }

        public int getLineEndPos(int cursorPos)
        {
            int i = getCursorLine(this.linesStartPos, cursorPos);
            return this.linesStartPos[i] + this.lines[i].lineText.length();
        }
    }

    static class Line
    {
        private final Style style;
        private final String lineText;
        protected final Component lineTextComponent;
        protected final int x;
        protected final int y;

        public Line(Style style, String text, int x, int y)
        {
            this.style = style;
            this.lineText = text;
            this.x = x;
            this.y = y;
            this.lineTextComponent = (Component.literal(text)).setStyle(style);
        }
    }

    static class Point
    {
        public final int x;
        public final int y;

        Point(int x, int y)
        {
            this.x = x;
            this.y = y;
        }
    }
}
