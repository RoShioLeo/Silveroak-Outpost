package cloud.lemonslice.silveroak.network;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

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

    public TextBoxEditMessage(PacketBuffer buf)
    {
        this.item = buf.readItemStack();
        this.held = buf.readInt();
    }

    @Override
    public void toBytes(PacketBuffer packetBuffer)
    {
        packetBuffer.writeItemStack(item);
        packetBuffer.writeInt(held);
    }

    @Override
    public void process(Supplier<NetworkEvent.Context> supplier)
    {
        NetworkEvent.Context ctx = supplier.get();
        ServerPlayerEntity player = ctx.getSender();
        if (player != null)
        {
            ctx.enqueueWork(() ->
            {
                if (item.hasTag())
                {
                    if (PlayerInventory.isHotbar(held) || held == 40)
                    {
                        ItemStack card = player.inventory.getStackInSlot(held);
                        card.setTag(item.getTag());
                    }
                }
            });
        }
    }
}
