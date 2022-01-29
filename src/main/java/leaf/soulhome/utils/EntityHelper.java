/*
 * File created ~ 25 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class EntityHelper
{
    public static List<LivingEntity> getLivingEntitiesInRange(LivingEntity selfEntity, int range, boolean includeSelf)
    {
        AxisAlignedBB areaOfEffect = new AxisAlignedBB(selfEntity.blockPosition());
        areaOfEffect = areaOfEffect.expandTowards(range, range, range);

        List<LivingEntity> entitiesFound = selfEntity.level.getEntitiesOfClass(LivingEntity.class, areaOfEffect);

        if (!includeSelf)
        {
            //removes self entity if it exists in the list
            //otherwise list unchanged
            entitiesFound.remove(selfEntity);
        }

        return entitiesFound;
    }

    public static List<Entity> getEntitiesInRange(Entity entity, int range, boolean includeSelf)
    {
        AxisAlignedBB areaOfEffect = new AxisAlignedBB(entity.blockPosition());
        areaOfEffect = areaOfEffect.expandTowards(range, range, range);

        List<Entity> entitiesFound = entity.level.getEntitiesOfClass(Entity.class, areaOfEffect);

        if (!includeSelf)
        {
            entitiesFound.remove(entity);
        }

        return entitiesFound;
    }


}
