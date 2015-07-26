package kpc.client.render.entity;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

public final class RenderEntityRobit
extends Render {
    private final ResourceLocation texture = new ResourceLocation("kpc", "textures/entity/robit.png");

    @Override
    public void doRender(Entity entity, double x, double y, double z, float yaw,  float dt){
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.texture;
    }
}