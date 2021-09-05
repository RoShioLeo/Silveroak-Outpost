package cloud.lemonslice.silveroak.common.block;

import net.minecraft.block.Block;

public class NormalBlock extends Block
{
    public NormalBlock(Properties properties)
    {
        super(properties);
    }

    @Deprecated
    // Please use DeferredRegister rather than RegistryModule
    public NormalBlock(String name, Properties properties)
    {
        super(properties);
        this.setRegistryName(name);
    }
}
