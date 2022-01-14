package cloud.lemonslice.silveroak.client;

import cloud.lemonslice.silveroak.common.CommonProxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class ClientProxy extends CommonProxy
{
    @Override
    public Level getClientWorld()
    {
        return Minecraft.getInstance().level;
    }

    @Override
    public Player getClientPlayer()
    {
        return Minecraft.getInstance().player;
    }
}
