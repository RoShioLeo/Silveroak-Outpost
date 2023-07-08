package cloud.lemonslice.silveroak.helper;

import cloud.lemonslice.silveroak.client.texture.TexturePos;
import cloud.lemonslice.silveroak.client.widget.IconButton;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AffineTransformation;
import org.joml.Matrix4f;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;

import static net.minecraft.client.render.VertexFormat.DrawMode.QUADS;

public final class GuiHelper
{
    public static void drawTexturedModalRect(MatrixStack matrixStack, int x, int y, int u, int v, int width, int height, float zLevel)
    {
        final float uScale = 1f / 0x100;
        final float vScale = 1f / 0x100;

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder wr = tessellator.getBuffer();
        wr.begin(QUADS, VertexFormats.POSITION_TEXTURE);
        Matrix4f matrix = matrixStack.peek().getPositionMatrix();
        wr.vertex(matrix, x, y + height, zLevel).texture(u * uScale, ((v + height) * vScale)).next();
        wr.vertex(matrix, x + width, y + height, zLevel).texture((u + width) * uScale, ((v + height) * vScale)).next();
        wr.vertex(matrix, x + width, y, zLevel).texture((u + width) * uScale, (v * vScale)).next();
        wr.vertex(matrix, x, y, zLevel).texture(u * uScale, (v * vScale)).next();
        tessellator.draw();
    }

    public static void drawLayer(MatrixStack matrixStack, int x, int y, TexturePos pos, int z)
    {
        drawTexturedModalRect(matrixStack, x, y, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), z);
    }

    public static void drawLayer(MatrixStack matrixStack, int x, int y, TexturePos pos)
    {
        drawTexturedModalRect(matrixStack, x, y, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), 0);
    }

    public static void drawLayer(Screen gui, MatrixStack matrixStack, int x, int y, TexturePos pos)
    {
        gui.drawTexture(matrixStack, x, y, pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight());
    }

    public static void drawLayerBySize(MatrixStack matrixStack, int x, int y, TexturePos pos, int textureWidth, int textureHeight)
    {
        DrawableHelper.drawTexture(matrixStack, x, y, pos.getWidth(), pos.getHeight(), pos.getX(), pos.getY(), pos.getWidth(), pos.getHeight(), textureWidth, textureHeight);
    }

    public static void drawLayerBySize(MatrixStack matrixStack, int x, int y, TexturePos pos)
    {
        drawLayerBySize(matrixStack, x, y, pos, pos.getWidth(), pos.getHeight());
    }

    public static void renderIconButton(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY, int z, Identifier texture, IconButton button, TexturePos normalPos, TexturePos hoveredPos, TexturePos pressedPos)
    {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, texture);
        if (button.isPressed())
        {
            GuiHelper.drawLayer(matrixStack, button.getX(), button.getY(), pressedPos);
            RenderSystem.disableBlend();
            return;
        }
        else if (button.isHovered())
        {
            GuiHelper.drawLayer(matrixStack, button.getX(), button.getY(), hoveredPos);
            RenderSystem.disableBlend();
            return;
        }

        GuiHelper.drawLayer(matrixStack, button.getX(), button.getY(), normalPos);
        RenderSystem.disableBlend();

        button.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    public static void renderButton(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY, int z, Identifier texture, ButtonWidget button, TexturePos normalPos, TexturePos hoveredPos)
    {
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, texture);

        if (button.isHovered())
        {
            GuiHelper.drawLayer(matrixStack, button.getX(), button.getY(), hoveredPos);
        }
        else
        {
            GuiHelper.drawLayer(matrixStack, button.getX(), button.getY(), normalPos);
        }
        RenderSystem.disableBlend();

        button.render(matrixStack, mouseX, mouseY, partialTicks);
    }

//    public static void drawTank(Screen gui, TexturePos pos, FluidStack fluid, int fluidHeight)
//    {
//        int width = pos.getWidth();
//        if (fluid.isEmpty())
//        {
//            return;
//        }
//
//        if (fluidHeight != 0)
//        {
//            TextureAtlasSprite sprite = gui.getMinecraft().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(IClientFluidTypeExtensions.of(fluid.getFluid()).getStillTexture());
//            RenderSystem.setShader(GameRenderer::getPositionTexShader);
//            RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
//            int color = IClientFluidTypeExtensions.of(fluid.getFluid()).getTintColor();
//            RenderSystem.setShaderColor(ColorHelper.getRedF(color), ColorHelper.getGreenF(color), ColorHelper.getBlueF(color), ColorHelper.getAlphaF(color));
//            for (int j = 0; j < width / 16; j++)
//            {
//                int count = 0;
//                int fluidH = fluidHeight;
//                while (fluidH > 16)
//                {
//                    drawSmallFluidSprite(pos.getX() + j * 16, pos.getY() + pos.getHeight() - (count + 1) * 16, 0, 0, 0, 16, 16, sprite);
//                    fluidH -= 16;
//                    count++;
//                }
//                drawSmallFluidSprite(pos.getX() + j * 16, pos.getY() + pos.getHeight() - count * 16 - fluidH, 0, 0, 16 - fluidH, 16, fluidH, sprite);
//            }
//            int fluidWidth = width % 16;
//            int j = width / 16;
//            if (fluidWidth != 0)
//            {
//                int count = 0;
//                int fluidH = fluidHeight;
//                while (fluidH > 16)
//                {
//                    drawSmallFluidSprite(pos.getX() + j * 16, pos.getY() + pos.getHeight() - (count + 1) * 16, 0, 0, 0, fluidWidth, 16, sprite);
//                    fluidH -= 16;
//                    count++;
//                }
//                drawSmallFluidSprite(pos.getX() + j * 16, pos.getY() + pos.getHeight() - count * 16 - fluidH, 0, 0, 16 - fluidH, fluidWidth, fluidH, sprite);
//            }
//        }
//    }

    public static void drawTransparentStringDefault(TextRenderer font, String text, float x, float y, int color, boolean shadow)
    {
        drawSpecialString(font, text, x, y, color, shadow, true, 0, 15728880);
    }

    public static void drawSpecialString(TextRenderer font, String text, float x, float y, int color, boolean shadow, boolean transparent, int colorBackground, int packedLight)
    {
        VertexConsumerProvider.Immediate iRenderTypeBuffer = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
        font.draw(text, x, y, color, shadow, AffineTransformation.identity().getMatrix(), iRenderTypeBuffer, transparent ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.NORMAL, colorBackground, packedLight);
        iRenderTypeBuffer.draw();
    }

    public static void drawTooltip(Screen gui, MatrixStack matrix, int mouseX, int mouseY, int x, int y, int weight, int height, List<Text> list)
    {
        if (x <= mouseX && mouseX <= x + weight && y <= mouseY && mouseY <= y + height)
        {
            gui.renderTooltip(matrix, list, Optional.empty(), mouseX, mouseY);
        }
    }

    public static void drawFluidTooltip(Screen gui, MatrixStack matrix, int mouseX, int mouseY, int x, int y, int width, int height, Text name, int amount)
    {
        if (amount != 0)
        {
            List<Text> list = Lists.newArrayList(name);
            DecimalFormat df = new DecimalFormat("#,###");
            list.add(Text.literal(df.format(amount) + " mB").formatted(Formatting.GRAY));
            drawTooltip(gui, matrix, mouseX, mouseY, x, y, width, height, list);
        }
    }

//    public static void drawSmallFluidSprite(int x0, int y0, int z, int u0, int v0, int width, int height, SpriteContents sprite)
//    {
//        float minU = sprite.getU0();
//        float maxU = sprite.getU1();
//        float minV = sprite.getV0();
//        float maxV = sprite.getV1();
//
//        innerBlit(x0, x0 + width, y0, y0 + height, z, getCorrespondingUV(minU, maxU, u0), getCorrespondingUV(minU, maxU, width), getCorrespondingUV(minV, maxV, v0), getCorrespondingUV(minV, maxV, height));
//    }

    private static float getCorrespondingUV(float min, float max, int uv)
    {
        return min + (max - min) * uv / 16;
    }

    static void innerBlit(int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1)
    {
        RenderSystem.enableBlend();

        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder buffer = tessellator.getBuffer();
        buffer.begin(QUADS, VertexFormats.POSITION_TEXTURE);
        buffer.vertex(x0, y1, z).texture(u0, v1).next();
        buffer.vertex(x1, y1, z).texture(u1, v1).next();
        buffer.vertex(x1, y0, z).texture(u1, v0).next();
        buffer.vertex(x0, y0, z).texture(u0, v0).next();
        tessellator.draw();

        RenderSystem.disableBlend();
    }
}
