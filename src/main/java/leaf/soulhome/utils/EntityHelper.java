/*
 * File created ~ 25 - 4 - 2021 ~ Leaf
 */

package leaf.soulhome.utils;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.function.Predicate;

public class EntityHelper
{
    public static List<LivingEntity> getLivingEntitiesInRange(LivingEntity selfEntity, int range, boolean includeSelf)
    {
        AABB areaOfEffect = new AABB(selfEntity.blockPosition());
        areaOfEffect = areaOfEffect.inflate(range, range, range);

        List<LivingEntity> entitiesFound = selfEntity.level().getEntitiesOfClass(LivingEntity.class, areaOfEffect);

        if (!includeSelf)
        {
            entitiesFound.remove(selfEntity);
        }

        return entitiesFound;
    }

    // canChangeDimensions now requires source/dest levels as parameters in 1.21.1,
    // so we omit that check here and let TeleportHelper handle it.
    private static final Predicate<Entity> ALLOWED_TO_TELEPORT =
            EntitySelector.NO_SPECTATORS
                    .and(EntitySelector.LIVING_ENTITY_STILL_ALIVE)
                    .and((entity) -> !(entity instanceof Enemy));

    public static List<Entity> getEntitiesInRange(Entity entity, double range, boolean includeSelf)
    {
        AABB areaOfEffect = new AABB(entity.blockPosition());
        areaOfEffect = areaOfEffect.inflate(range, range, range);

        List<Entity> entitiesFound = entity.level().getEntitiesOfClass(Entity.class, areaOfEffect);

        for (Entity ent : entitiesFound)
        {
            final boolean removeSelf = ent == entity && !includeSelf;
            if (removeSelf || !ALLOWED_TO_TELEPORT.test(ent))
            {
                entitiesFound.remove(ent);
                break;
            }
        }

        return entitiesFound;
    }


}
