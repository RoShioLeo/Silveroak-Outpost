package cloud.lemonslice.silveroak.helper;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.client.texture.TextureResource;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.text.DecimalFormat;
import java.util.List;

public final class GuiHelper
{
    public static void drawTank(Screen gui, TexturePos pos, FluidStack fluid, int fluidHeight)
    {
        int width = pos.getWidth();
        if (fluid == null)
        {
            return;
        }

        if (fluidHeight != 0)
        {
            TextureAtlasSprite sprite = gui.getMinecraft().getAtlasSpriteGetter(PlayerContainer.LOCATION_BLOCKS_TEXTURE).apply(fluid.getFluid().getAttributes().getStillTexture());
            gui.getMinecraft().getTextureManager().bindTexture(PlayerContainer.LOCATION_BLOCKS_TEXTURE);
            int color = fluid.getFluid().getAttributes().getColor(fluid);
            RenderSystem.color4f(ColorHelper.getRedF(color), ColorHelper.getGreenF(color), ColorHelper.getBlueF(color), ColorHelper.getAlphaF(color));
            for (int j = 0; j < width / 16; j++)
            {
                int count = 0;
                int fluidH = fluidHeight;
                while (fluidH > 16)
                {
                    drawFluid(pos.getX() + j * 16, pos.getY() + pos.getHeight() - (count + 1) * 16, 0, 16, 16, sprite);
                    fluidH -= 16;
                    count++;
                }
                drawFluid(pos.getX() + j * 16, pos.getY() + pos.getHeight() - count * 16 - fluidH, 0, 16, fluidH, sprite);
            }
            int fluidWidth = width % 16;
            int j = width / 16;
            if (fluidWidth != 0)
            {
                int count = 0;
                int fluidH = fluidHeight;
                while (fluidH > 16)
                {
                    drawFluid(pos.getX() + j * 16, pos.getY() + pos.getHeight() - (count + 1) * 16, 0, fluidWidth, 16, sprite);
                    fluidH -= 16;
                    count++;
                }
                drawFluid(pos.getX() + j * 16, pos.getY() + pos.getHeight() - count * 16 - fluidH, 0, fluidWidth, fluidH, sprite);
            }
        }
    }

    public static void drawLayer(Screen gui, int x, int y, TextureResource texture)
    {
        Minecraft.getInstance().getTextureManager().bindTexture(texture.getTexture());
        GuiUtils.drawTexturedModalRect(x, y, texture.getPos().getX(), texture.getPos().getY(), texture.getPos().getWidth(), texture.getPos().getHeight(), 0);
    }

    public static void drawTooltip(Screen gui, MatrixStack matrix, int mouseX, int mouseY, int x, int y, int weight, int height, List<ITextComponent> list)
    {
        if (x <= mouseX && mouseX <= x + weight && y <= mouseY && mouseY <= y + height)
        {
            // renderToolTip(matrix, list, mouseX, mouseY);
            gui.func_243308_b(matrix, list, mouseX, mouseY);
        }
    }

    public static void drawFluidTooltip(Screen gui, MatrixStack matrix, int mouseX, int mouseY, int x, int y, int width, int height, ITextComponent name, int amount)
    {
        if (amount != 0)
        {
            List<ITextComponent> list = Lists.newArrayList(name);
            DecimalFormat df = new DecimalFormat("#,###");
            list.add(new StringTextComponent(df.format(amount) + "mB").mergeStyle(TextFormatting.GRAY));
            drawTooltip(gui, matrix, mouseX, mouseY, x, y, width, height, list);
        }
    }

    public static void drawFluid(int x0, int y0, int z, int destWidth, int destHeight, TextureAtlasSprite sprite)
    {
        int height = ModelLoader.instance().sheetData.get(PlayerContainer.LOCATION_BLOCKS_TEXTURE).getSecond().height;
        int width = ModelLoader.instance().sheetData.get(PlayerContainer.LOCATION_BLOCKS_TEXTURE).getSecond().width;
        innerBlit(x0, x0 + destWidth, y0, y0 + destHeight, z, sprite.getMinU(), (sprite.getMaxU() * width - sprite.getWidth() + destWidth / 16.0F * sprite.getWidth()) / width, (sprite.getMinV() * height + sprite.getHeight() - destHeight / 16.0F * sprite.getHeight()) / height, sprite.getMaxV());
    }

    protected static void innerBlit(int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1)
    {
        BufferBuilder bufferbuilder = Tessellator.getInstance().getBuffer();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(x0, y1, z).tex(u0, v1).endVertex();
        bufferbuilder.pos(x1, y1, z).tex(u1, v1).endVertex();
        bufferbuilder.pos(x1, y0, z).tex(u1, v0).endVertex();
        bufferbuilder.pos(x0, y0, z).tex(u0, v0).endVertex();
        bufferbuilder.finishDrawing();
        RenderSystem.enableAlphaTest();
        WorldVertexBufferUploader.draw(bufferbuilder);
    }
}
