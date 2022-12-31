package cloud.lemonslice.silveroak.common.item;

import cloud.lemonslice.silveroak.client.ClientEnvironmentDataHandler;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

public class ThermometerItem extends NormalItem
{
    public ThermometerItem()
    {
        super(new Identifier(MODID, "thermometer"), new FabricItemSettings().maxCount(1), ItemGroups.TOOLS);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context)
    {
        super.appendTooltip(stack, world, tooltip, context);
        if (world != null && world.isClient())
        {
            tooltip.add(ClientEnvironmentDataHandler.getTemperatureInfo());
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
    {
        ItemStack itemStack = user.getStackInHand(hand);
        if (world.isClient())
        {
            user.sendMessage(ClientEnvironmentDataHandler.getTemperatureInfo(), true);
        }
        return TypedActionResult.success(itemStack);
    }
}
