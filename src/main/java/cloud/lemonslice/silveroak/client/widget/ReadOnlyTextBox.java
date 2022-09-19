package cloud.lemonslice.silveroak.client.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;

public class ReadOnlyTextBox extends AbstractWidget
{
    private final Minecraft mc;
    private final Font font;

    private final int color;
    private final int spacing;

    private final EditableTextBox.Page currentPage;
    private String page = "";

    public ReadOnlyTextBox(ItemStack item, int x, int y, int boxWidth, int boxHeight, int spacingPixel, int color, Component title)
    {
        super(x, y, boxWidth, boxHeight, title);
        this.mc = Minecraft.getInstance();
        this.font = mc.font;
        this.color = color;
        this.spacing = spacingPixel;

        CompoundTag compoundTag = item.getTag();
        if (compoundTag != null)
        {
            Tag nbt = compoundTag.get("Text");
            if (nbt != null)
            {
                this.page = nbt.copy().getAsString();
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
            StringSplitter stringSplitter = this.font.getSplitter();
            stringSplitter.splitLines(page, width, Style.EMPTY, true, (style, lineStartPos, lineEndPos) ->
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
        return new EditableTextBox.Point(pointIn.x + this.x, pointIn.y + this.y);
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        for (EditableTextBox.Line line : currentPage.lines)
        {
            this.font.draw(poseStack, line.lineTextComponent, (float) line.x, (float) line.y, color);
        }
    }

    @Override
    public void updateNarration(NarrationElementOutput pNarrationElementOutput)
    {

    }
}
