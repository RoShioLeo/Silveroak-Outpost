package cloud.lemonslice.silveroak.client.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class IconButton extends Button
{
    private boolean isPressed = false;

    public IconButton(int x, int y, int width, int height, Component title, OnPress pressedAction)
    {
        super(x, y, width, height, title, pressedAction);
    }

    public IconButton(int x, int y, int width, int height, Component title, OnPress pressedAction, OnTooltip onTooltip)
    {
        super(x, y, width, height, title, pressedAction, onTooltip);
    }

    @Override
    public void renderButton(PoseStack poseStack, int mouseX, int mouseY, float partialTicks)
    {
        if (this.m_198029_())
        {
            poseStack.pushPose();
            poseStack.translate(0, 0, 500);
            this.renderToolTip(poseStack, mouseX, mouseY);
            poseStack.popPose();
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
