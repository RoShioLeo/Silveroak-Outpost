package cloud.lemonslice.silveroak.client.widget;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.Rectangle2d;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.text.CharacterManager;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.List;

public class ReadOnlyTextBox extends Widget
{
    private final Minecraft mc;
    private final FontRenderer font;

    private final int color;
    private final int spacing;

    private final EditableTextBox.Page currentPage;
    private String page = "";

    public ReadOnlyTextBox(ItemStack item, int x, int y, int boxWidth, int boxHeight, int spacingPixel, int color, ITextComponent title)
    {
        super(x, y, boxWidth, boxHeight, title);
        this.mc = Minecraft.getInstance();
        this.font = mc.fontRenderer;
        this.color = color;
        this.spacing = spacingPixel;

        CompoundNBT compoundnbt = item.getTag();
        if (compoundnbt != null)
        {
            INBT nbt = compoundnbt.get("Text");
            if (nbt != null)
            {
                this.page = nbt.copy().getString();
            }
        }

        this.currentPage = this.createPage();
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
            CharacterManager charactermanager = this.font.getCharacterManager();
            charactermanager.func_238353_a_(page, width, Style.EMPTY, true, (style, lineStartPos, lineEndPos) ->
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

            return new EditableTextBox.Page(page, new EditableTextBox.Point(0, 0), true, linesStartPos, lines.toArray(new EditableTextBox.Line[0]), new Rectangle2d[0]);
        }
    }

    private EditableTextBox.Point getPointPosInScreen(EditableTextBox.Point pointIn)
    {
        return new EditableTextBox.Point(pointIn.x + this.x, pointIn.y + this.y);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        for (EditableTextBox.Line line : currentPage.lines)
        {
            this.font.drawText(matrixStack, line.lineTextComponent, (float) line.x, (float) line.y, color);
        }
    }
}
