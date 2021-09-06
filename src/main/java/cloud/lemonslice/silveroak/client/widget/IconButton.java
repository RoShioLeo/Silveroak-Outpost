package cloud.lemonslice.silveroak.client.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IconButton extends Button
{
    private boolean isPressed = false;

    public IconButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction)
    {
        super(x, y, width, height, title, pressedAction);
    }

    public IconButton(int x, int y, int width, int height, ITextComponent title, IPressable pressedAction, ITooltip onTooltip)
    {
        super(x, y, width, height, title, pressedAction, onTooltip);
    }

    @Override
    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks)
    {
        if (this.isHovered())
        {
            this.renderToolTip(matrixStack, mouseX, mouseY);
        }
    }

    @Override
    public void onPress()
    {
        super.onPress();
        this.isPressed = true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY)
    {
        super.onRelease(mouseX, mouseY);
        this.isPressed = false;
    }

    public boolean isPressed()
    {
        return isPressed;
    }
}
