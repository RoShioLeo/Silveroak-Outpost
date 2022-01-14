package cloud.lemonslice.silveroak.common.item;

import cloud.lemonslice.silveroak.client.ClientEnvironmentDataHandler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ThermometerItem extends NormalItem
{
    public ThermometerItem()
    {
        super(new Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced)
    {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (pLevel != null && pLevel.isClientSide)
        {
            ClientEnvironmentDataHandler.addTemperatureInfo(pTooltipComponents);
        }
    }
}
