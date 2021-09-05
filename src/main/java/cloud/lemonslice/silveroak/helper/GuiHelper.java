package cloud.lemonslice.silveroak.helper;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.client.widget.IconButton;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.TransformationMatrix;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.client.gui.GuiUtils;

import java.text.DecimalFormat;
import java.util.List;

public final class GuiHelper
{
    @Deprecated
    public static void drawLayer(MatrixStack matrixStack, int x, int y, ResourceLocation texture, TexturePos pos)
    {
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        GuiUtils.drawTexturedModalRect(matrixStack, x, y, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), 0);
    }

    public static void drawLayer(MatrixStack matrixStack, int x, int y, TexturePos pos)
    {
        GuiUtils.drawTexturedModalRect(matrixStack, x, y, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), 0);
    }

    public static void drawLayerBySize(MatrixStack matrixStack, int x, int y, TexturePos pos, int textureWidth, int textureHeight)
    {
        AbstractGui.blit(matrixStack, x, y, pos.getWidth(), pos.getHeight(), pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), textureWidth, textureHeight);
    }

    public static void drawLayerBySize(MatrixStack matrixStack, int x, int y, TexturePos pos)
    {
        drawLayerBySize(matrixStack, x, y, pos, pos.getWidth(), pos.getHeight());
    }

    public static void renderIconButton(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY, ResourceLocation texture, IconButton button, TexturePos normalPos, TexturePos hoveredPos, TexturePos pressedPos)
    {
        button.render(matrixStack, mouseX, mouseY, partialTicks);

        RenderSystem.enableAlphaTest();
        Minecraft.getInstance().getTextureManager().bindTexture(texture);
        if (button.isPressed())
        {
            GuiHelper.drawLayer(matrixStack, button.x, button.y, pressedPos);
            RenderSystem.disableAlphaTest();
            return;
        }
        else if (button.isHovered())
        {
            GuiHelper.drawLayer(matrixStack, button.x, button.y, hoveredPos);
            RenderSystem.disableAlphaTest();
            return;
        }

        GuiHelper.drawLayer(matrixStack, button.x, button.y, normalPos);
        RenderSystem.disableAlphaTest();
    }

    public static void renderButton(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY, ResourceLocation texture, Button button, TexturePos normalPos, TexturePos hoveredPos)
    {
        button.render(matrixStack, mouseX, mouseY, partialTicks);

        RenderSystem.enableAlphaTest();
        RenderSystem.enableBlend();
        Minecraft.getInstance().getTextureManager().bindTexture(texture);

        if (button.isHovered())
        {
            GuiHelper.drawLayer(matrixStack, button.x, button.y, hoveredPos);
        }
        else
        {
            GuiHelper.drawLayer(matrixStack, button.x, button.y, normalPos);
        }

        RenderSystem.disableAlphaTest();
        RenderSystem.disableBlend();
    }

    public static void drawTank(Screen gui, TexturePos pos, FluidStack fluid, int fluidHeight)
    {
        int width = pos.getWidth();
        if (fluid.isEmpty())
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
                    drawSmallFluidSprite(pos.getX() + j * 16, pos.getY() + pos.getHeight() - (count + 1) * 16, 0, 0, 0, 16, 16, sprite);
                    fluidH -= 16;
                    count++;
                }
                drawSmallFluidSprite(pos.getX() + j * 16, pos.getY() + pos.getHeight() - count * 16 - fluidH, 0, 0, 16 - fluidH, 16, fluidH, sprite);
            }
            int fluidWidth = width % 16;
            int j = width / 16;
            if (fluidWidth != 0)
            {
                int count = 0;
                int fluidH = fluidHeight;
                while (fluidH > 16)
                {
                    drawSmallFluidSprite(pos.getX() + j * 16, pos.getY() + pos.getHeight() - (count + 1) * 16, 0, 0, 0, fluidWidth, 16, sprite);
                    fluidH -= 16;
                    count++;
                }
                drawSmallFluidSprite(pos.getX() + j * 16, pos.getY() + pos.getHeight() - count * 16 - fluidH, 0, 0, 16 - fluidH, fluidWidth, fluidH, sprite);
            }
        }
    }

    public static void drawTransparentStringDefault(FontRenderer font, String text, float x, float y, int color, boolean shadow)
    {
        drawSpecialString(font, text, x, y, color, shadow, true, 0, 15728880);
    }

    public static void drawSpecialString(FontRenderer font, String text, float x, float y, int color, boolean shadow, boolean transparent, int colorBackground, int packedLight)
    {
        IRenderTypeBuffer.Impl iRenderTypeBuffer = IRenderTypeBuffer.getImpl(Tessellator.getInstance().getBuffer());
        font.renderString(text, x, y, color, shadow, TransformationMatrix.identity().getMatrix(), iRenderTypeBuffer, transparent, colorBackground, packedLight);
        iRenderTypeBuffer.finish();
    }

    public static void drawTooltip(Screen gui, MatrixStack matrix, int mouseX, int mouseY, int x, int y, int weight, int height, List<ITextComponent> list)
    {
        if (x <= mouseX && mouseX <= x + weight && y <= mouseY && mouseY <= y + height)
        {
            gui.func_243308_b(matrix, list, mouseX, mouseY);
        }
    }

    public static void drawFluidTooltip(Screen gui, MatrixStack matrix, int mouseX, int mouseY, int x, int y, int width, int height, ITextComponent name, int amount)
    {
        if (amount != 0)
        {
            List<ITextComponent> list = Lists.newArrayList(name);
            DecimalFormat df = new DecimalFormat("#,###");
            list.add(new StringTextComponent(df.format(amount) + " mB").mergeStyle(TextFormatting.GRAY));
            drawTooltip(gui, matrix, mouseX, mouseY, x, y, width, height, list);
        }
    }

    public static void drawSmallFluidSprite(int x0, int y0, int z, int u0, int v0, int width, int height, TextureAtlasSprite sprite)
    {
        float minU = sprite.getMinU();
        float maxU = sprite.getMaxU();
        float minV = sprite.getMinV();
        float maxV = sprite.getMaxV();

        innerBlit(x0, x0 + width, y0, y0 + height, z, getCorrespondingUV(minU, maxU, u0), getCorrespondingUV(minU, maxU, width), getCorrespondingUV(minV, maxV, v0), getCorrespondingUV(minV, maxV, height));
    }

    private static float getCorrespondingUV(float min, float max, int uv)
    {
        return min + (max - min) * uv / 16;
    }

    static void innerBlit(int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1)
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
