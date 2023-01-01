package cloud.lemonslice.silveroak.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Identifier;

public final class SimpleNetworkHandler
{
    public static void init()
    {
        registerToServerMessage(TextBoxEditMessage.getID(), TextBoxEditMessage::fromBytes);
    }

    public static void registerToServerMessage(Identifier packetID, MessageHandler messageHandler)
    {
        ServerPlayNetworking.registerGlobalReceiver(packetID, (server, player, handler, buf, responseSender) ->
        {
            if (messageHandler.fromBuf(buf) instanceof IToServerMessage serverMessage)
            {
                serverMessage.process(server, player, handler, responseSender);
            }
        });
    }

    public static void registerToClientMessage(Identifier packetID, MessageHandler messageHandler)
    {
        ClientPlayNetworking.registerGlobalReceiver(packetID, (client, handler, buf, responseSender) ->
        {
            if (messageHandler.fromBuf(buf) instanceof IToClientMessage serverMessage)
            {
                serverMessage.process(client, handler, responseSender);
            }
        });
    }

    @FunctionalInterface
    public interface MessageHandler
    {
        INormalMessage fromBuf(PacketByteBuf buf);
    }
}
