package cloud.lemonslice.silveroak.client.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextHandler;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Rect2i;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;

public class ReadOnlyTextBox extends ClickableWidget
{
    private final MinecraftClient mc;
    private final TextRenderer font;

    private final int color;
    private final int spacing;

    private final EditableTextBox.Page currentPage;
    private String page = "";

    public ReadOnlyTextBox(ItemStack item, int x, int y, int boxWidth, int boxHeight, int spacingPixel, int color, Text title)
    {
        super(x, y, boxWidth, boxHeight, title);
        this.mc = MinecraftClient.getInstance();
        this.font = mc.textRenderer;
        this.color = color;
        this.spacing = spacingPixel;

        NbtCompound compoundTag = item.getNbt();
        if (compoundTag != null)
        {
            NbtElement nbt = compoundTag.get("Text");
            if (nbt != null)
            {
                this.page = nbt.copy().asString();
            }
        }

        this.currentPage = this.createPage();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button)
    {
        return false;
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button)
    {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY)
    {
        return false;
    }

    private EditableTextBox.Page createPage()
    {
        if (page.isEmpty())
        {
            return EditableTextBox.Page.EMPTY;
        }
        else
        {
            IntList intlist = new IntArrayList();
            List<EditableTextBox.Line> lines = Lists.newArrayList();
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
                EditableTextBox.Point point = this.getPointPosInScreen(new EditableTextBox.Point(0, y));
                intlist.add(lineStartPos);
                lines.add(new EditableTextBox.Line(style, lineText, point.x, point.y));
            });
            int[] linesStartPos = intlist.toIntArray();

            return new EditableTextBox.Page(page, new EditableTextBox.Point(0, 0), true, linesStartPos, lines.toArray(new EditableTextBox.Line[0]), new Rect2i[0]);
        }
    }

    private EditableTextBox.Point getPointPosInScreen(EditableTextBox.Point pointIn)
    {
        return new EditableTextBox.Point(pointIn.x + this.getX(), pointIn.y + this.getY());
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        for (EditableTextBox.Line line : currentPage.lines)
        {
            this.font.draw(matrixStack, line.lineTextComponent, (float) line.x, (float) line.y, color);
        }
    }

    @Override
    public void renderButton(MatrixStack matrices, int mouseX, int mouseY, float delta) {

    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder)
    {

    }
}
