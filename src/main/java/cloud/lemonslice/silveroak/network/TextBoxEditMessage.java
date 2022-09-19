package cloud.lemonslice.silveroak.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class TextBoxEditMessage implements INormalMessage
{
    private ItemStack item;
    private int held;

    public TextBoxEditMessage(ItemStack item, int held)
    {
        this.item = item;
        this.held = held;
    }

    public TextBoxEditMessage(FriendlyByteBuf buf)
    {
        this.item = buf.readItem();
        this.held = buf.readInt();
    }

    @Override
    public void toBytes(FriendlyByteBuf packetBuffer)
    {
        packetBuffer.writeItem(item);
        packetBuffer.writeInt(held);
    }

    @Override
    public void process(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context ctx = supplier.get();
        ServerPlayer player = ctx.getSender();
        if (player != null)
        {
            ctx.enqueueWork(() ->
            {
                if (item.hasTag())
                {
                    if (Inventory.isHotbarSlot(held) || held == 40)
                    {
                        ItemStack card = player.getInventory().getItem(held);
                        card.setTag(item.getTag());
                    }
                }
            });
        }
    }
}
