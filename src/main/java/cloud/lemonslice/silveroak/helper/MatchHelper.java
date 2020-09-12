package cloud.lemonslice.silveroak.helper;

import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

public final class MatchHelper
{
    public static boolean itemContainsMatch(@Nonnull Item input, @Nonnull ResourceLocation tagId)
    {
        Tag<Item> tag = ItemTags.getCollection().get(tagId);
        if (tag != null)
        {
            return tag.contains(input);
        }
        return false;
    }
}
