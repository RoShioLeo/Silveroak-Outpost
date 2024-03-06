package cloud.lemonslice.silveroak.client.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IconButton extends Button
{
    private boolean isPressed = false;
    protected final OnTooltip onTooltip;

    public IconButton(int x, int y, int width, int height, Component title, OnPress pressedAction)
    {
        super(x, y, width, height, title, pressedAction, DEFAULT_NARRATION);
        this.onTooltip = null;
    }

    public IconButton(int x, int y, int width, int height, Component title, OnPress pressedAction, OnTooltip onTooltip)
    {
        super(x, y, width, height, title, pressedAction, DEFAULT_NARRATION);
        this.onTooltip = onTooltip;
    }

    @Override
    protected void renderWidget(GuiGraphics gui, int mouseX, int mouseY, float partialTicks)
    {
        if (this.isHoveredOrFocused())
        {
            this.renderToolTip(gui, mouseX, mouseY);
        }
    }

    public void renderToolTip(GuiGraphics gui, int pMouseX, int pMouseY)
    {
        if (this.onTooltip != null)
        {
            this.onTooltip.onTooltip(this, gui, pMouseX, pMouseY);
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

    public interface OnTooltip
    {
        void onTooltip(Button button, GuiGraphics gui, int mouseX, int mouseY);
    }
}
