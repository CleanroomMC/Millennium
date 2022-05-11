package com.cleanroommc.millennium.client.model;

import com.cleanroommc.millennium.common.entity.EntityNewVillager;
import com.cleanroommc.millennium.common.village.VillagerProfession;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelVillagerModern extends ModelVillager
{
    public ModelRenderer villagerHeadwear;
    public ModelRenderer villagerHatRimHigh;
    public ModelRenderer villagerHatRimLow;

    public ModelVillagerModern(float headScale)
    {
        this(headScale, 0F, 64, 64); // Texture files are 64 by 64
    }

    public ModelVillagerModern(float scale, float headPivotOffset, int textureFileWidth, int textureFileHeight)
    {
        super(scale, headPivotOffset, textureFileWidth, textureFileHeight);

        float headscaleOffset = 0.5F; // How much the mantle layer gets "inflated"

        // Main headwear portion
        this.villagerHeadwear = new ModelRenderer(this).setTextureSize(textureFileWidth, textureFileHeight);
        this.villagerHeadwear.setTextureOffset(32, 0).addBox(-4.0F, -10.0F, -4.0F, 8, 10, 8, scale + headscaleOffset);
        this.villagerHeadwear.setRotationPoint(0.0F, 0.0F + headPivotOffset, 0.0F);

        // Higher Rim -- used for Farmer and Fisherman
        this.villagerHatRimHigh = new ModelRenderer(this).setTextureSize(textureFileWidth, textureFileHeight);
        int rimHighTextureOffsetX = 15;
        int rimHighTextureOffsetY = 48;
        this.villagerHatRimHigh.setTextureOffset(rimHighTextureOffsetX, rimHighTextureOffsetY);

        // My version, which implements a singly-textured plane:
        this.villagerHatRimHigh.cubeList.add(new ModelPlane(this.villagerHatRimHigh, rimHighTextureOffsetX, rimHighTextureOffsetY, -8F, -6F, -8F, 16, 0, 16, 0.0F));
        this.villagerHatRimHigh.setRotationPoint(0.0F, 0.0F + headPivotOffset, 0.0F);

        // Lower Rim -- used for Shepherd
        this.villagerHatRimLow = new ModelRenderer(this).setTextureSize(textureFileWidth, textureFileHeight);
        int rimLowTextureOffsetX = 32;
        int rimLowTextureOffsetY = 48;
        this.villagerHatRimLow.setTextureOffset(rimLowTextureOffsetX, rimLowTextureOffsetY);
        this.villagerHatRimLow.cubeList.add(new ModelPlane(this.villagerHatRimLow, rimLowTextureOffsetX, rimLowTextureOffsetY, -8F, -5F, -8F, 16, 0, 16, 0.0F));
        this.villagerHatRimLow.setRotationPoint(0.0F, 0.0F + headPivotOffset, 0.0F);

    }

    @Override
    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);

        this.villagerHeadwear.rotateAngleY = this.villagerHead.rotateAngleY;
        this.villagerHeadwear.rotateAngleX = this.villagerHead.rotateAngleX;

        this.villagerHatRimHigh.rotateAngleY = this.villagerHead.rotateAngleY;
        this.villagerHatRimHigh.rotateAngleX = this.villagerHead.rotateAngleX;

        this.villagerHatRimLow.rotateAngleY = this.villagerHead.rotateAngleY;
        this.villagerHatRimLow.rotateAngleX = this.villagerHead.rotateAngleX;
    }

    @Override
    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.render(entity, f, f1, f2, f3, f4, f5);

        VillagerProfession prof = ((EntityNewVillager)entity).getProfession();

        // You reach this point if the villager needs its head examined lol gottem

        if (((EntityNewVillager) entity).isChild())
        {
            //Re-upscale baby head lmao
            GL11.glPushMatrix();
            GL11.glScalef(1.5F, 1.5F, 1.5F);
            this.villagerHead.render(f5);
            this.villagerHeadwear.render(f5);
            this.villagerHatRimHigh.render(f5);
            this.villagerHatRimLow.render(f5);
            GL11.glPopMatrix();
        }
        else
        {
            this.villagerHeadwear.render(f5);
            this.villagerHatRimHigh.render(f5);
            this.villagerHatRimLow.render(f5);
        }
    }

}
