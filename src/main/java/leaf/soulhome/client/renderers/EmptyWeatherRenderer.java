/*
 * File created ~ 27 - 1 - 2022 ~Leaf
 */

package leaf.soulhome.client.renderers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.IWeatherRenderHandler;

@OnlyIn(Dist.CLIENT)
public class EmptyWeatherRenderer implements IWeatherRenderHandler
{
    @Override
    public void render(int ticks, float partialTicks, ClientLevel world, Minecraft mc, LightTexture lightmapIn, double xIn, double yIn, double zIn)
    {
        //no weather in the soul at this stage.
        //todo random soul based events that cause weather? could be interesting.
    }
}
