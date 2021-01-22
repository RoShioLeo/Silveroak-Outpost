package cloud.lemonslice.silveroak.registry;

import cloud.lemonslice.silveroak.SilveroakOutpost;
import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.gen.feature.Feature;

import java.lang.reflect.Field;
import java.util.List;

public class RegistryModule
{
    public RegistryModule(String modid)
    {
        RegisterManager.MODID.add(modid);
        try
        {
            for (Field field : getClass().getFields())
            {
                Object o = field.get(null);
                if (o instanceof Item)
                {
                    List<Item> list = RegisterManager.ITEMS.getOrDefault(modid, Lists.newArrayList());
                    list.add((Item) o);
                    RegisterManager.ITEMS.put(modid, list);
                }
                else if (o instanceof Block)
                {
                    List<Block> list = RegisterManager.BLOCKS.getOrDefault(modid, Lists.newArrayList());
                    list.add((Block) o);
                    RegisterManager.BLOCKS.put(modid, list);
                }
                else if (o instanceof TileEntityType<?>)
                {
                    List<TileEntityType<?>> list = RegisterManager.TILE_ENTITY_TYPES.getOrDefault(modid, Lists.newArrayList());
                    list.add((TileEntityType<?>) o);
                    RegisterManager.TILE_ENTITY_TYPES.put(modid, list);
                }
                else if (o instanceof ContainerType<?>)
                {
                    List<ContainerType<?>> list = RegisterManager.CONTAINER_TYPES.getOrDefault(modid, Lists.newArrayList());
                    list.add((ContainerType<?>) o);
                    RegisterManager.CONTAINER_TYPES.put(modid, list);
                }
                else if (o instanceof EntityType<?>)
                {
                    List<EntityType<?>> list = RegisterManager.ENTITY_TYPES.getOrDefault(modid, Lists.newArrayList());
                    list.add((EntityType<?>) o);
                    RegisterManager.ENTITY_TYPES.put(modid, list);
                }
                else if (o instanceof Effect)
                {
                    List<Effect> list = RegisterManager.EFFECTS.getOrDefault(modid, Lists.newArrayList());
                    list.add((Effect) o);
                    RegisterManager.EFFECTS.put(modid, list);
                }
                else if (o instanceof Feature<?>)
                {
                    List<Feature<?>> list = RegisterManager.FEATURES.getOrDefault(modid, Lists.newArrayList());
                    list.add((Feature<?>) o);
                    RegisterManager.FEATURES.put(modid, list);
                }
                else if (o instanceof SoundEvent)
                {
                    List<SoundEvent> list = RegisterManager.SOUNDS.getOrDefault(modid, Lists.newArrayList());
                    list.add((SoundEvent) o);
                    RegisterManager.SOUNDS.put(modid, list);
                }
            }
        }
        catch (Exception ignored)
        {
            SilveroakOutpost.error("An error occurred while initializing registry in Mod %s.", modid);
        }
    }
}
