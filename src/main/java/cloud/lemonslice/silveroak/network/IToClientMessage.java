package cloud.lemonslice.silveroak.network;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;

public interface IToClientMessage extends INormalMessage
{
    void process(MinecraftClient client, ClientPlayNetworkHandler handler, PacketSender responseSender);
}
