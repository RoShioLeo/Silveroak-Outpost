package cloud.lemonslice.silveroak.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.Function;

import static cloud.lemonslice.silveroak.SilveroakOutpost.MODID;

public final class SimpleNetworkHandler
{
    public static final String NETWORK_VERSION = "1.0";
    public static final SimpleChannel CHANNEL = NetworkRegistry.ChannelBuilder.named(new ResourceLocation(MODID, "main")).networkProtocolVersion(() -> NETWORK_VERSION).serverAcceptedVersions(NETWORK_VERSION::equals).clientAcceptedVersions(NETWORK_VERSION::equals).simpleChannel();

    public static void init()
    {
        int id = 0;
        registerMessage(id++, TextBoxEditMessage.class, TextBoxEditMessage::new, NetworkDirection.PLAY_TO_SERVER);
    }

    private static <T extends INormalMessage> void registerMessage(int index, Class<T> messageType, Function<FriendlyByteBuf, T> decoder)
    {
        CHANNEL.registerMessage(index, messageType, INormalMessage::toBytes, decoder, (message, context) ->
        {
            message.process(context);
            context.get().setPacketHandled(true);
        });
    }

    private static <T extends INormalMessage> void registerMessage(int index, Class<T> messageType, Function<FriendlyByteBuf, T> decoder, NetworkDirection direction)
    {
        CHANNEL.registerMessage(index, messageType, INormalMessage::toBytes, decoder, (message, context) ->
        {
            message.process(context);
            context.get().setPacketHandled(true);
        }, Optional.of(direction));
    }
}
