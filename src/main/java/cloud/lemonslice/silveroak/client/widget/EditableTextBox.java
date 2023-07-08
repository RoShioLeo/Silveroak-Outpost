package cloud.lemonslice.silveroak.client.widget;

import cloud.lemonslice.silveroak.network.TextBoxEditMessage;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.SharedConstants;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtString;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class EditableTextBox extends ClickableWidget
{
    private final MinecraftClient mc;
    private final TextRenderer font;

    private boolean isModified = false;
    private int updateCount = 0;
    private long lastClickTime = 0;
    private final int color;
    private final int spacing;

    private final SelectionManager textInputUtil;
    @Nullable
    private Page currentPage = null;
    private String page = "";

    private final ItemStack item;
    private final PlayerEntity editingPlayer;
    private final Hand hand;

    public EditableTextBox(ItemStack item, PlayerEntity playerIn, Hand handIn, int x, int y, int boxWidth, int boxHeight, int spacingPixel, int color, Text title)
    {
        super(x, y, boxWidth, boxHeight, title);
        this.mc = MinecraftClient.getInstance();
        this.font = mc.textRenderer;
        this.color = color;
        this.spacing = spacingPixel;
        this.item = item;
        this.editingPlayer = playerIn;
        this.hand = handIn;

        NbtCompound compoundnbt = item.getNbt();
        if (compoundnbt != null)
        {
            NbtElement nbt = compoundnbt.get("Text");
            if (nbt != null)
            {
                this.page = nbt.copy().asString();
            }
        }

        this.textInputUtil = new SelectionManager(() -> page, this::setText, this::getClipboardText, this::setClipboardText, (text) -> text.length() < 1024 && this.font.getWrappedLinesHeight(text, boxWidth) <= boxHeight * font.fontHeight / spacingPixel);
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
            this.item.getOrCreateNbt().put("Text", NbtString.of(this.page));
            int i = this.hand == Hand.MAIN_HAND ? this.editingPlayer.getInventory().selectedSlot : 40;
            ClientPlayNetworking.send(TextBoxEditMessage.getID(), TextBoxEditMessage.create(item, i).toBytes());
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
            int selectionStart = this.textInputUtil.getSelectionStart();
            int selectionEnd = this.textInputUtil.getSelectionEnd();
            IntList intlist = new IntArrayList();
            List<Line> lines = Lists.newArrayList();
            MutableInt mutableint = new MutableInt();
            MutableBoolean mutableboolean = new MutableBoolean();
            TextHandler stringSplitter = this.font.getTextHandler();
            stringSplitter.wrapLines(page, width, Style.EMPTY, true, (style, lineStartPos, lineEndPos) ->
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
            boolean flag = selectionStart == page.length();
            Point point;
            if (flag && mutableboolean.isTrue())
            {
                point = new Point(0, lines.size() * spacing);
            }
            else
            {
                int line = getCursorLine(linesStartPos, selectionStart);
                point = new Point(this.font.getWidth(page.substring(linesStartPos[line], selectionStart)), line * spacing);
            }

            List<Rect2i> rectangleList = Lists.newArrayList();
            if (selectionStart != selectionEnd)
            {
                int selectionMin = Math.min(selectionStart, selectionEnd);
                int selectionMax = Math.max(selectionStart, selectionEnd);
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
                        int k2 = (int) stringSplitter.getWidth(s1);
                        rectangleList.add(this.getRectangle(new Point(0, j2), new Point(k2, j2 + 9)));
                    }

                    rectangleList.add(this.getRectangle(page, stringSplitter, linesStartPos[selectionEndLine], selectionMax, selectionEndLine * spacing, linesStartPos[selectionEndLine]));
                }
            }

            return new Page(page, point, flag, linesStartPos, lines.toArray(new Line[0]), rectangleList.toArray(new Rect2i[0]));
        }
    }

    private Rect2i getRectangle(String text, TextHandler characterManager, int from, int to, int lineStart, int lineEnd)
    {
        String s = text.substring(lineEnd, from);
        String s1 = text.substring(lineEnd, to);
        Point pointFrom = new Point((int) characterManager.getWidth(s), lineStart);
        Point pointTo = new Point((int) characterManager.getWidth(s1), lineStart + spacing);
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
        return this.mc != null ? SelectionManager.getClipboard(this.mc) : "";
    }

    private void setClipboardText(String text)
    {
        if (this.mc != null)
        {
            SelectionManager.setClipboard(this.mc, text);
        }
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
        int i = this.textInputUtil.getSelectionStart();
        int j = this.getPage().getLineToMove(i, lineAdded);
        this.textInputUtil.moveCursorTo(j, Screen.hasShiftDown());
    }

    private static int getCursorLine(int[] linesLength, int cursorPos)
    {
        int i = Arrays.binarySearch(linesLength, cursorPos);
        return i < 0 ? -(i + 2) : i;
    }

    private void moveToLineHead()
    {
        if (Screen.hasControlDown())
        {
            this.textInputUtil.moveCursorToStart(Screen.hasShiftDown());
        }
        else
        {
            int i = this.textInputUtil.getSelectionStart();
            int j = this.getPage().getLineStartPos(i);
            this.textInputUtil.moveCursorTo(j, Screen.hasShiftDown());
        }
    }

    private void moveToLineEnd()
    {
        if (Screen.hasControlDown())
        {
            this.textInputUtil.moveCursorToEnd(Screen.hasShiftDown());
        }
        else
        {
            int i = this.textInputUtil.getSelectionStart();
            int j = getPage().getLineEndPos(i);
            this.textInputUtil.moveCursorTo(j, Screen.hasShiftDown());
        }
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
            SelectionManager.SelectionType selectionType = Screen.hasControlDown() ? SelectionManager.SelectionType.WORD : SelectionManager.SelectionType.CHARACTER;
            switch (keyCode)
            {
                case 257:
                case 335:
                    this.textInputUtil.insert("\n");
                    return true;
                case 259:
                    this.textInputUtil.delete(-1, selectionType);
                    return true;
                case 261:
                    this.textInputUtil.delete(1, selectionType);
                    return true;
                case 262:
                    this.textInputUtil.moveCursor(1, Screen.hasShiftDown(), selectionType);
                    return true;
                case 263:
                    this.textInputUtil.moveCursor(-1, Screen.hasShiftDown(), selectionType);
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
        else if (SharedConstants.isValidChar(codePoint))
        {
            this.textInputUtil.insert(Character.toString(codePoint));
            this.shouldRefresh();
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        Page page = this.getPage();

        for (Line line : page.lines)
        {
            this.font.draw(matrixStack, line.lineTextComponent, (float) line.x, (float) line.y, color);
        }

        this.renderSelection(page.selection);
        this.renderCursor(matrixStack, page.point, page.isInsert);
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {

    }

    private void renderSelection(Rect2i[] selection)
    {
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        RenderSystem.setShader(GameRenderer::getPositionProgram);
        RenderSystem.setShaderColor(0.0F, 0.0F, 1.0F, 1.0F);
        RenderSystem.enableColorLogicOp();
        RenderSystem.logicOp(GlStateManager.LogicOp.OR_REVERSE);
        bufferbuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);

        for (Rect2i rectangle2d : selection)
        {
            int i = rectangle2d.getX();
            int j = rectangle2d.getY();
            int k = i + rectangle2d.getWidth();
            int l = j + rectangle2d.getHeight();
            bufferbuilder.vertex(i, l, 0.0D).next();
            bufferbuilder.vertex(k, l, 0.0D).next();
            bufferbuilder.vertex(k, j, 0.0D).next();
            bufferbuilder.vertex(i, j, 0.0D).next();
        }

        tessellator.draw();
        RenderSystem.disableColorLogicOp();
    }

    private void renderCursor(MatrixStack matrixStack, Point point, boolean isInsert)
    {
        if (this.updateCount / 6 % 2 == 0 && height != 0)
        {
            point = this.getPointPosInScreen(point);
            if (!isInsert)
            {
                DrawableHelper.fill(matrixStack, point.x, point.y - 1, point.x + 1, point.y + 9, color);
            }
            else
            {
                this.font.draw(matrixStack, "_", (float) point.x, (float) point.y, color);
            }
        }

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        if (button == 0)
        {
            long i = Util.getMeasuringTimeMs();
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
                    this.textInputUtil.moveCursorTo(j, Screen.hasShiftDown());
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
            this.textInputUtil.moveCursorTo(i, true);
            this.shouldRefresh();
        }
        return true;
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder)
    {

    }

    private void setSelection(int cursorPos)
    {
        this.textInputUtil.setSelection(TextHandler.moveCursorByWords(page, -1, cursorPos, false), TextHandler.moveCursorByWords(page, 1, cursorPos, false));
    }

    private Point getPointPosInBox(Point pointIn)
    {
        return new Point(pointIn.x - this.getX(), pointIn.y - this.getY());
    }

    private Point getPointPosInScreen(Point pointIn)
    {
        return new Point(pointIn.x + this.getX(), pointIn.y + this.getY());
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

        public int getMousePointPosInText(TextRenderer font, Point point, int spacingPixel)
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
                return this.linesStartPos[linePos] + font.getTextHandler().getTrimmedLength(line.lineText, point.x, line.style);
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
        protected final Text lineTextComponent;
        protected final int x;
        protected final int y;

        public Line(Style style, String text, int x, int y)
        {
            this.style = style;
            this.lineText = text;
            this.x = x;
            this.y = y;
            this.lineTextComponent = (Text.literal(text)).setStyle(style);
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
