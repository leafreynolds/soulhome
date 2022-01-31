/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.dimensions;

import leaf.soulhome.client.renderers.EmptyWeatherRenderer;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.world.phys.Vec3;


import net.minecraft.client.renderer.DimensionSpecialEffects.SkyType;

public class SoulDimensionRenderInfo extends DimensionSpecialEffects
{

    public SoulDimensionRenderInfo()
    {
        this(128,
                false,
                SkyType.NONE,
                false,
                true);
    }

    public SoulDimensionRenderInfo(float cloudLevel, boolean hasGround, SkyType skyType, boolean forceBrightLightmap, boolean constantAmbientLight)
    {
        super(cloudLevel, hasGround, skyType, forceBrightLightmap, constantAmbientLight);
        this.setWeatherRenderHandler(new EmptyWeatherRenderer());
    }

    @Override
    public Vec3 getBrightnessDependentFogColor(Vec3 p_230494_1_, float p_230494_2_)
    {
        //copied from overworld
        return p_230494_1_.multiply(p_230494_2_ * 0.94F + 0.06F, p_230494_2_ * 0.94F + 0.06F, p_230494_2_ * 0.91F + 0.09F);
    }

    @Override
    public boolean isFoggyAt(int p_230493_1_, int p_230493_2_)
    {
        return false;
    }
}
