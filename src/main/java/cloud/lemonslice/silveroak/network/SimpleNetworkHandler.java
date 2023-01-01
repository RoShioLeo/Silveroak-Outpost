package cloud.lemonslice.silveroak.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

public final class SimpleNetworkHandler
{
    public static void init()
    {
        registerToServerMessage(TextBoxEditMessage.getID(), TextBoxEditMessage::process);
    }

    public static void registerToServerMessage(Identifier packetID, ServerPlayNetworking.PlayChannelHandler channelHandler)
    {
        ServerPlayNetworking.registerGlobalReceiver(packetID, channelHandler);
    }

    public static void registerToClientMessage(Identifier packetID, ClientPlayNetworking.PlayChannelHandler channelHandler)
    {
        ClientPlayNetworking.registerGlobalReceiver(packetID, channelHandler);
    }
}
