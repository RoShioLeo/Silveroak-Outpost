package cloud.lemonslice.silveroak.helper;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class MatchHelper
{
    public static boolean itemContainsMatch(@Nonnull Item input, @Nonnull ResourceLocation tagId)
    {
        ITag<Item> tag = ItemTags.getCollection().get(tagId);
        if (tag != null)
        {
            return tag.contains(input);
        }
        return false;
    }
}
