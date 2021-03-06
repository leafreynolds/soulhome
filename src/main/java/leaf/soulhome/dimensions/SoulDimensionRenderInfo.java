/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.dimensions;

import leaf.soulhome.client.renderers.EmptyWeatherRenderer;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraft.util.math.vector.Vector3d;


public class SoulDimensionRenderInfo extends DimensionRenderInfo
{

    public SoulDimensionRenderInfo()
    {
        this(128,
                false,
                FogType.NONE,
                false,
                true);
    }

    public SoulDimensionRenderInfo(float cloudLevel, boolean hasGround, FogType skyType, boolean forceBrightLightmap, boolean constantAmbientLight)
    {
        super(cloudLevel, hasGround, skyType, forceBrightLightmap, constantAmbientLight);
        this.setWeatherRenderHandler(new EmptyWeatherRenderer());
    }

    @Override
    public Vector3d getBrightnessDependentFogColor(Vector3d p_230494_1_, float p_230494_2_)
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
