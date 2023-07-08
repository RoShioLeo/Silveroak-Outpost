package cloud.lemonslice.silveroak.client.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class IconButton extends ButtonWidget
{
    private boolean isPressed = false;
    protected final OnTooltip onTooltip;

    public IconButton(int x, int y, int width, int height, Text message, PressAction pressAction)
    {
        super(x, y, width, height, message, pressAction, DEFAULT_NARRATION_SUPPLIER);
        this.onTooltip = null;
    }

    public IconButton(int x, int y, int width, int height, Text message, PressAction pressAction, OnTooltip tooltip)
    {
        super(x, y, width, height, message, pressAction, DEFAULT_NARRATION_SUPPLIER);
        this.onTooltip = tooltip;
    }

    @Override
    public void renderButton(DrawContext drawContext, int mouseX, int mouseY, float delta)
    {
        if (this.isHovered())
        {
            this.renderToolTip(drawContext, mouseX, mouseY);
        }
    }

    public void renderToolTip(DrawContext drawContext, int pMouseX, int pMouseY)
    {
        if (this.onTooltip != null)
        {
            this.onTooltip.onTooltip(this, drawContext, pMouseX, pMouseY);
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
        void onTooltip(ButtonWidget button, DrawContext drawContext, int mouseX, int mouseY);
    }
}
