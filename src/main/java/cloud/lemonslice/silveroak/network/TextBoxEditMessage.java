package cloud.lemonslice.silveroak.network;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;


public class TextBoxEditMessage implements INormalMessage
{
    private ItemStack item;
    private int held;

    TextBoxEditMessage(ItemStack item, int held)
    {
        this.item = item;
        this.held = held;
    }

    @Override
    public PacketByteBuf toBytes()
    {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeItemStack(item);
        buf.writeInt(held);
        return buf;
    }

    public static Identifier getID()
    {
        return new Identifier(MODID + ":text_box_edit");
    }

    public static TextBoxEditMessage create(ItemStack item, int held)
    {
        return new TextBoxEditMessage(item, held);
    }

    public static void process(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender)
    {
        if (player != null)
        {
            server.executeSync(() ->
            {
                ItemStack item = buf.readItemStack();
                if (item.hasNbt())
                {
                    int held = buf.readInt();
                    if (PlayerInventory.isValidHotbarIndex(held) || held == 40)
                    {
                        ItemStack card = player.getInventory().getStack(held);
                        card.setNbt(item.getNbt());
                    }
                }
            });
        }
    }
}
