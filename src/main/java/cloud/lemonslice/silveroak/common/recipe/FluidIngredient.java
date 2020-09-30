package cloud.lemonslice.silveroak.common.recipe;

import com.google.common.collect.Lists;
import com.google.gson.*;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class FluidIngredient implements Predicate<FluidStack>
{
    private static final Set<FluidIngredient> INSTANCES = Collections.newSetFromMap(new WeakHashMap<>());

    public static void invalidateAll()
    {
        INSTANCES.stream().filter(Objects::nonNull).forEach(FluidIngredient::invalidate);
    }

    private static final Predicate<? super IFluidList> IS_EMPTY = (fluidList) -> !fluidList.getStacks().stream().allMatch(FluidStack::isEmpty);
    public static final FluidIngredient EMPTY = new FluidIngredient(Stream.empty());
    private final IFluidList[] acceptedFluids;
    private FluidStack[] matchingStacks;
    private IntList matchingStacksPacked;

    protected FluidIngredient(Stream<? extends IFluidList> fluidLists)
    {
        this.acceptedFluids = fluidLists.filter(IS_EMPTY).toArray(IFluidList[]::new);
        FluidIngredient.INSTANCES.add(this);
    }

    public FluidStack[] getMatchingStacks()
    {
        this.determineMatchingStacks();
        return this.matchingStacks;
    }

    private void determineMatchingStacks()
    {
        if (this.matchingStacks == null)
        {
            this.matchingStacks = Arrays.stream(this.acceptedFluids).flatMap((fluidList) -> fluidList.getStacks().stream()).distinct().toArray(FluidStack[]::new);
        }

    }

    public boolean test(@Nullable FluidStack fluidStackIn)
    {
        if (fluidStackIn == null)
        {
            return false;
        }
        else if (this.acceptedFluids.length == 0)
        {
            return fluidStackIn.isEmpty();
        }
        else
        {
            this.determineMatchingStacks();

            for (FluidStack fluidStack : this.matchingStacks)
            {
                if (fluidStack.getFluid() == fluidStackIn.getFluid() && fluidStackIn.getAmount() >= fluidStack.getAmount())
                {
                    return true;
                }
            }

            return false;
        }
    }

    public IntList getValidFluidStacksPacked()
    {
        if (this.matchingStacksPacked == null)
        {
            this.determineMatchingStacks();
            this.matchingStacksPacked = new IntArrayList(this.matchingStacks.length);

            for (FluidStack fluidStack : this.matchingStacks)
            {
                this.matchingStacksPacked.add(Registry.FLUID.getId(fluidStack.getFluid()));
            }

            this.matchingStacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
        }

        return this.matchingStacksPacked;
    }

    public final void write(PacketBuffer buffer)
    {
        this.determineMatchingStacks();
        buffer.writeVarInt(this.matchingStacks.length);

        for (FluidStack matchingStack : this.matchingStacks)
        {
            buffer.writeFluidStack(matchingStack);
        }

    }

    public JsonElement serialize()
    {
        if (this.acceptedFluids.length == 1)
        {
            return this.acceptedFluids[0].serialize();
        }
        else
        {
            JsonArray jsonarray = new JsonArray();

            for (IFluidList ingredient$ifluidlist : this.acceptedFluids)
            {
                jsonarray.add(ingredient$ifluidlist.serialize());
            }

            return jsonarray;
        }
    }

    public boolean hasNoMatchingFluids()
    {
        return this.acceptedFluids.length == 0 && (this.matchingStacks == null || this.matchingStacks.length == 0) && (this.matchingStacksPacked == null || this.matchingStacksPacked.isEmpty());
    }

    protected void invalidate()
    {
        this.matchingStacks = null;
        this.matchingStacksPacked = null;
    }

    public static FluidIngredient fromFluidListStream(Stream<? extends IFluidList> stream)
    {
        FluidIngredient ingredient = new FluidIngredient(stream);
        return ingredient.acceptedFluids.length == 0 ? EMPTY : ingredient;
    }

    public static FluidIngredient fromFluid(int amount, Fluid... fluids)
    {
        return fromFluidListStream(Arrays.stream(fluids).map((fluid) -> new SingleFluidList(new FluidStack(fluid, amount))));
    }

    public static FluidIngredient fromStacks(FluidStack... stacks)
    {
        return fromFluidListStream(Arrays.stream(stacks).map(SingleFluidList::new));
    }

    public static FluidIngredient fromTag(int amount, Tag<Fluid> tagIn)
    {
        return fromFluidListStream(Stream.of(new FluidIngredient.TagList(tagIn, amount)));
    }

    public static FluidIngredient read(PacketBuffer buffer)
    {
        int i = buffer.readVarInt();
        return fromFluidListStream(Stream.generate(() -> new SingleFluidList(buffer.readFluidStack())).limit(i));
    }

    public static FluidIngredient deserialize(@Nullable JsonElement json)
    {
        if (json != null && !json.isJsonNull())
        {
            if (json.isJsonObject())
            {
                return fromFluidListStream(Stream.of(deserializeFluidList(json.getAsJsonObject())));
            }
            else if (json.isJsonArray())
            {
                JsonArray jsonarray = json.getAsJsonArray();
                if (jsonarray.size() == 0)
                {
                    throw new JsonSyntaxException("Fluid array cannot be empty, at least one fluid must be defined");
                }
                else
                {
                    return fromFluidListStream(StreamSupport.stream(jsonarray.spliterator(), false).map(
                            (jsonElement) -> deserializeFluidList(JSONUtils.getJsonObject(jsonElement, "fluid"))));
                }
            }
            else
            {
                throw new JsonSyntaxException("Expected fluid to be object or array of objects");
            }
        }
        else
        {
            throw new JsonSyntaxException("Fluid cannot be null");
        }
    }

    public static IFluidList deserializeFluidList(JsonObject json)
    {
        if (json.has("fluid") && json.has("tag"))
        {
            throw new JsonParseException("An ingredient entry is either a tag or an fluid, not both");
        }
        else if (json.has("fluid"))
        {
            int amount = JSONUtils.getInt(json, "amount", 1000);
            ResourceLocation resourcelocation1 = new ResourceLocation(JSONUtils.getString(json, "fluid"));
            Fluid fluid = ForgeRegistries.FLUIDS.getValue(resourcelocation1);
            if (fluid == null)
            {
                throw new JsonSyntaxException("Unknown fluid '" + resourcelocation1 + "'");
            }
            return new SingleFluidList(new FluidStack(fluid, amount));
        }
        else if (json.has("tag"))
        {
            int amount = JSONUtils.getInt(json, "amount", 1000);
            ResourceLocation resourcelocation = new ResourceLocation(JSONUtils.getString(json, "tag"));
            Tag<Fluid> tag = FluidTags.getCollection().get(resourcelocation);
            if (tag == null)
            {
                throw new JsonSyntaxException("Unknown fluid tag '" + resourcelocation + "'");
            }
            else
            {
                return new FluidIngredient.TagList(tag, amount);
            }
        }
        else
        {
            throw new JsonParseException("An ingredient entry needs either a tag or an fluid");
        }
    }

    public static FluidIngredient merge(Collection<FluidIngredient> parts)
    {
        return fromFluidListStream(parts.stream().flatMap(i -> Arrays.stream(i.acceptedFluids)));
    }

    public interface IFluidList
    {
        Collection<FluidStack> getStacks();

        JsonObject serialize();
    }

    public static class SingleFluidList implements IFluidList
    {
        private final FluidStack stack;

        public SingleFluidList(FluidStack stackIn)
        {
            this.stack = stackIn;
        }

        public Collection<FluidStack> getStacks()
        {
            return Collections.singleton(this.stack);
        }

        public JsonObject serialize()
        {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("fluid", ForgeRegistries.FLUIDS.getKey(this.stack.getFluid()).toString());
            jsonobject.addProperty("amount", this.stack.getAmount());
            return jsonobject;
        }
    }

    public static class TagList implements IFluidList
    {
        private final Tag<Fluid> tag;
        private final int amount;

        public TagList(Tag<Fluid> tagIn, int amount)
        {
            this.tag = tagIn;
            this.amount = amount;
        }

        public Collection<FluidStack> getStacks()
        {
            List<FluidStack> list = Lists.newArrayList();

            for (Fluid fluid : this.tag.getAllElements())
            {
                list.add(new FluidStack(fluid, this.amount));
            }
            return list;
        }

        public JsonObject serialize()
        {
            JsonObject jsonobject = new JsonObject();
            jsonobject.addProperty("tag", this.tag.getId().toString());
            jsonobject.addProperty("amount", this.amount);
            return jsonobject;
        }
    }
}