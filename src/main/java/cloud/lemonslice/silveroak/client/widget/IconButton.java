package cloud.lemonslice.silveroak.client.widget;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class IconButton extends ButtonWidget
{
    private boolean isPressed = false;

    public IconButton(int x, int y, int width, int height, Text message, PressAction pressAction)
    {
        super(x, y, width, height, message, pressAction, DEFAULT_NARRATION_SUPPLIER);
    }

    public IconButton(int x, int y, int width, int height, Text message, PressAction pressAction, Tooltip tooltip)
    {
        super(x, y, width, height, message, pressAction, DEFAULT_NARRATION_SUPPLIER);
        this.setTooltip(tooltip);
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
