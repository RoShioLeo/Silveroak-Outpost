package cloud.lemonslice.silveroak.registry;

import cloud.lemonslice.silveroak.SilveroakOutpost;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class RegisterManager
{
    public static HashSet<String> MODID = new HashSet<>();
    public static HashMap<String, List<Item>> ITEMS = new HashMap<>();
    public static HashMap<String, List<Block>> BLOCKS = new HashMap<>();
    public static HashMap<String, List<TileEntityType<?>>> TILE_ENTITY_TYPES = new HashMap<>();
    public static HashMap<String, List<EntityType<?>>> ENTITY_TYPES = new HashMap<>();
    public static HashMap<String, List<Effect>> EFFECTS = new HashMap<>();
    public static HashMap<String, List<ContainerType<?>>> CONTAINER_TYPES = new HashMap<>();
    public static HashMap<String, List<Feature<?>>> FEATURES = new HashMap<>();
    public static HashMap<String, List<SoundEvent>> SOUNDS = new HashMap<>();

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event)
    {
        ITEMS.keySet().forEach(id ->
        {
            event.getRegistry().registerAll(ITEMS.get(id).toArray(new Item[0]));
            SilveroakOutpost.info("Successfully registered %d Item(s) for Mod %s.", ITEMS.size(), id);
        });
    }

    @SubscribeEvent
    public static void registerBlocks(RegistryEvent.Register<Block> event)
    {
        BLOCKS.keySet().forEach(id ->
        {
            event.getRegistry().registerAll(BLOCKS.get(id).toArray(new Block[0]));
            SilveroakOutpost.info("Successfully registered %d Block(s) for Mod %s.", BLOCKS.size(), id);
        });
    }

    @SubscribeEvent
    public static void registerTileEntityTypes(RegistryEvent.Register<TileEntityType<?>> event)
    {
        TILE_ENTITY_TYPES.keySet().forEach(id ->
        {
            event.getRegistry().registerAll(TILE_ENTITY_TYPES.get(id).toArray(new TileEntityType<?>[0]));
            SilveroakOutpost.info("Successfully registered %d TileEntity Type(s) for Mod %s.", TILE_ENTITY_TYPES.size(), id);
        });
    }

    @SubscribeEvent
    public static void registerEntityTypes(RegistryEvent.Register<EntityType<?>> event)
    {
        ENTITY_TYPES.keySet().forEach(id ->
        {
            event.getRegistry().registerAll(ENTITY_TYPES.get(id).toArray(new EntityType<?>[0]));
            SilveroakOutpost.info("Successfully registered %d Entity Type(s) for Mod %s.", ENTITY_TYPES.size(), id);
        });
    }

    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<Effect> event)
    {
        EFFECTS.keySet().forEach(id ->
        {
            event.getRegistry().registerAll(EFFECTS.get(id).toArray(new Effect[0]));
            SilveroakOutpost.info("Successfully registered %d Effect(s) for Mod %s.", EFFECTS.size(), id);
        });
    }

    @SubscribeEvent
    public static void registerContainerTypes(RegistryEvent.Register<ContainerType<?>> event)
    {
        CONTAINER_TYPES.keySet().forEach(id ->
        {
            event.getRegistry().registerAll(CONTAINER_TYPES.get(id).toArray(new ContainerType<?>[0]));
            SilveroakOutpost.info("Successfully registered %d Container Type(s) for Mod %s.", CONTAINER_TYPES.size(), id);
        });
    }

    @SubscribeEvent
    public static void registerFeatures(RegistryEvent.Register<Feature<?>> event)
    {
        FEATURES.keySet().forEach(id ->
        {
            event.getRegistry().registerAll(FEATURES.get(id).toArray(new Feature<?>[0]));
            SilveroakOutpost.info("Successfully registered %d Feature(s) for Mod %s.", FEATURES.size(), id);
        });
    }

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> event)
    {
        SOUNDS.keySet().forEach(id ->
        {
            event.getRegistry().registerAll(SOUNDS.get(id).toArray(new SoundEvent[0]));
            SilveroakOutpost.info("Successfully registered %d Sound(s) for Mod %s.", SOUNDS.size(), id);
        });
    }

    public static void clearAll()
    {
        ITEMS = null;
        BLOCKS = null;
        TILE_ENTITY_TYPES = null;
        ENTITY_TYPES = null;
        CONTAINER_TYPES = null;
        EFFECTS = null;
        FEATURES = null;
        SOUNDS = null;
    }
}
