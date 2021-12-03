package cloud.lemonslice.silveroak.helper;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.client.widget.IconButton;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Transformation;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.client.gui.GuiUtils;
import net.minecraftforge.fluids.FluidStack;

import java.text.DecimalFormat;
import java.util.List;

import static com.mojang.blaze3d.vertex.VertexFormat.Mode.QUADS;

public final class GuiHelper
{
    public static void drawLayer(PoseStack poseStack, int x, int y, TexturePos pos, int z)
    {
        GuiUtils.drawTexturedModalRect(poseStack, x, y, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), z);
    }

    public static void drawLayer(PoseStack poseStack, int x, int y, TexturePos pos)
    {
        GuiUtils.drawTexturedModalRect(poseStack, x, y, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), 0);
    }

    public static void drawLayer(Screen gui, PoseStack poseStack, int x, int y, TexturePos pos)
    {
        gui.blit(poseStack, x, y, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight());
    }

    public static void drawLayerBySize(PoseStack poseStack, int x, int y, TexturePos pos, int textureWidth, int textureHeight)
    {
        GuiComponent.blit(poseStack, x, y, pos.getWidth(), pos.getHeight(), pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), textureWidth, textureHeight);
    }

    public static void drawLayerBySize(PoseStack poseStack, int x, int y, TexturePos pos)
    {
        drawLayerBySize(poseStack, x, y, pos, pos.getWidth(), pos.getHeight());
    }

    public static void renderIconButton(PoseStack poseStack, float partialTicks, int mouseX, int mouseY, int z, ResourceLocation texture, IconButton button, TexturePos normalPos, TexturePos hoveredPos, TexturePos pressedPos)
    {
        button.render(poseStack, mouseX, mouseY, partialTicks);

        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);
        if (button.isPressed())
        {
            GuiHelper.drawLayer(poseStack, button.x, button.y, pressedPos, z);
            RenderSystem.disableBlend();
            return;
        }
        else if (button.m_198029_())
        {
            GuiHelper.drawLayer(poseStack, button.x, button.y, hoveredPos, z);
            RenderSystem.disableBlend();
            return;
        }

        GuiHelper.drawLayer(poseStack, button.x, button.y, normalPos, z);
        RenderSystem.disableBlend();
    }

    public static void renderButton(PoseStack poseStack, float partialTicks, int mouseX, int mouseY, int z, ResourceLocation texture, Button button, TexturePos normalPos, TexturePos hoveredPos)
    {
        button.render(poseStack, mouseX, mouseY, partialTicks);

        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, texture);

        if (button.m_198029_())
        {
            GuiHelper.drawLayer(poseStack, button.x, button.y, hoveredPos, z);
        }
        else
        {
            GuiHelper.drawLayer(poseStack, button.x, button.y, normalPos, z);
        }

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
            TextureAtlasSprite sprite = gui.getMinecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluid.getFluid().getAttributes().getStillTexture());
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
            int color = fluid.getFluid().getAttributes().getColor(fluid);
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
        font.drawInBatch(text, x, y, color, shadow, Transformation.identity().getMatrix(), iRenderTypeBuffer, transparent, colorBackground, packedLight);
        iRenderTypeBuffer.endBatch();
    }

    public static void drawTooltip(Screen gui, PoseStack matrix, int mouseX, int mouseY, int x, int y, int weight, int height, List<Component> list)
    {
        if (x <= mouseX && mouseX <= x + weight && y <= mouseY && mouseY <= y + height)
        {
            gui.renderComponentTooltip(matrix, list, mouseX, mouseY);
        }
    }

    public static void drawFluidTooltip(Screen gui, PoseStack matrix, int mouseX, int mouseY, int x, int y, int width, int height, Component name, int amount)
    {
        if (amount != 0)
        {
            List<Component> list = Lists.newArrayList(name);
            DecimalFormat df = new DecimalFormat("#,###");
            list.add(new TextComponent(df.format(amount) + " mB").withStyle(ChatFormatting.GRAY));
            drawTooltip(gui, matrix, mouseX, mouseY, x, y, width, height, list);
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
