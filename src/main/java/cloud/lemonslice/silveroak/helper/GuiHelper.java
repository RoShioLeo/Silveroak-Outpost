package cloud.lemonslice.silveroak.helper;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.client.widget.IconButton;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Transformation;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;

import java.text.DecimalFormat;
import java.util.List;

import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;

public final class GuiHelper
{
    public static void drawLayer(GuiGraphics guiGraphics, int x, int y, ResourceLocation resourceLocation, TexturePos pos)
    {
        guiGraphics.blit(resourceLocation, x, y, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight());
    }

    public static void drawLayerBySize(GuiGraphics guiGraphics, int x, int y, ResourceLocation resourceLocation, TexturePos pos, int textureWidth, int textureHeight)
    {
        guiGraphics.blit(resourceLocation, x, y, pos.getWidth(), pos.getHeight(), pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), textureWidth, textureHeight);
    }

    public static void drawLayerBySize(GuiGraphics guiGraphics, int x, int y, ResourceLocation resourceLocation, TexturePos pos)
    {
        drawLayerBySize(guiGraphics, x, y, resourceLocation, pos, pos.getWidth(), pos.getHeight());
    }

    public static void renderIconButton(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY, int z, ResourceLocation texture, IconButton button, TexturePos normalPos, TexturePos hoveredPos, TexturePos pressedPos)
    {
        RenderSystem.enableBlend();
        if (button.isPressed())
        {
            GuiHelper.drawLayer(guiGraphics, button.getX(), button.getY(), texture, pressedPos);
            RenderSystem.disableBlend();
            return;
        }
        else if (button.isHoveredOrFocused())
        {
            GuiHelper.drawLayer(guiGraphics, button.getX(), button.getY(), texture, hoveredPos);
            RenderSystem.disableBlend();
            return;
        }

        GuiHelper.drawLayer(guiGraphics, button.getX(), button.getY(), texture, normalPos);
        RenderSystem.disableBlend();

        button.render(guiGraphics, mouseX, mouseY, partialTicks);
    }

    public static void renderButton(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY, ResourceLocation texture, Button button, TexturePos normalPos, TexturePos hoveredPos)
    {
        RenderSystem.enableBlend();

        if (button.isHoveredOrFocused())
        {
            GuiHelper.drawLayer(guiGraphics, button.getX(), button.getY(), texture, hoveredPos);
        }
        else
        {
            GuiHelper.drawLayer(guiGraphics, button.getX(), button.getY(), texture, normalPos);
        }
        RenderSystem.disableBlend();

        button.render(guiGraphics, mouseX, mouseY, partialTicks);
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
            TextureAtlasSprite sprite = gui.getMinecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture());
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            int color = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor();
            RenderSystem.setShaderColor(ColorHelper.getRedF(color), ColorHelper.getGreenF(color), ColorHelper.getBlueF(color), ColorHelper.getAlphaF(color));
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

    public static void drawTransparentStringDefault(Font font, String text, float x, float y, int color, boolean shadow)
    {
        drawSpecialString(font, text, x, y, color, shadow, true, 0, 15728880);
    }

    public static void drawSpecialString(Font font, String text, float x, float y, int color, boolean shadow, boolean transparent, int colorBackground, int packedLight)
    {
        MultiBufferSource.BufferSource iRenderTypeBuffer = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        font.drawInBatch(text, x, y, color, shadow, Transformation.identity().getMatrix(), iRenderTypeBuffer, transparent ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, colorBackground, packedLight);
        iRenderTypeBuffer.endBatch();
    }

    public static void drawTooltip(Screen gui, GuiGraphics guiGraphics, int mouseX, int mouseY, float deltaTick, int x, int y, int weight, int height, List<FormattedCharSequence> list)
    {
        if (x <= mouseX && mouseX <= x + weight && y <= mouseY && mouseY <= y + height)
        {
            gui.setTooltipForNextRenderPass(list);
            gui.renderWithTooltip(guiGraphics, mouseX, mouseY, deltaTick);
        }
    }

    public static void drawFluidTooltip(Screen gui, GuiGraphics guiGraphics, int mouseX, int mouseY, float deltaTick, int x, int y, int width, int height, Component name, int amount)
    {
        if (amount != 0)
        {
            List<FormattedCharSequence> list = Lists.newArrayList(name.getVisualOrderText());
            DecimalFormat df = new DecimalFormat("#,###");
            list.add(Component.literal(df.format(amount) + " mB").withStyle(ChatFormatting.GRAY).getVisualOrderText());
            drawTooltip(gui, guiGraphics, mouseX, mouseY, deltaTick, x, y, width, height, list);
        }
    }

    public static void drawSmallFluidSprite(int x0, int y0, int z, int u0, int v0, int width, int height, TextureAtlasSprite sprite)
    {
        float minU = sprite.getU0();
        float maxU = sprite.getU1();
        float minV = sprite.getV0();
        float maxV = sprite.getV1();

        innerBlit(x0, x0 + width, y0, y0 + height, z, getCorrespondingUV(minU, maxU, u0), getCorrespondingUV(minU, maxU, width), getCorrespondingUV(minV, maxV, v0), getCorrespondingUV(minV, maxV, height));
    }

    private static float getCorrespondingUV(float min, float max, int uv)
    {
        return min + (max - min) * uv / 16;
    }

    static void innerBlit(int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1)
    {
        RenderSystem.enableBlend();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        buffer.begin(QUADS, DefaultVertexFormat.POSITION_TEX);
        buffer.vertex(x0, y1, z).uv(u0, v1).endVertex();
        buffer.vertex(x1, y1, z).uv(u1, v1).endVertex();
        buffer.vertex(x1, y0, z).uv(u1, v0).endVertex();
        buffer.vertex(x0, y0, z).uv(u0, v0).endVertex();
        tesselator.end();

        RenderSystem.disableBlend();
    }
}
