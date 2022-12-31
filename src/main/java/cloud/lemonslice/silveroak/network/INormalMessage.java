package cloud.lemonslice.silveroak.network;


import net.minecraft.network.PacketByteBuf;

public interface INormalMessage
{
    PacketByteBuf toBytes();
}
