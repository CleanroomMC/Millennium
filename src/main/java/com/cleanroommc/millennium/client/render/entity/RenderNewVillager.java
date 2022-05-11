package com.cleanroommc.millennium.client.render.entity;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.client.model.ModelVillagerModern;
import com.cleanroommc.millennium.common.entity.EntityNewVillager;
import com.cleanroommc.millennium.common.village.VillagerProfession;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Adapted from Village Names by AstroTibs
 */

@SideOnly(Side.CLIENT)
public class RenderNewVillager extends RenderLiving<EntityNewVillager> {

    // Constructor
    public RenderNewVillager(RenderManager renderManagerIn) {
        super(renderManagerIn, new ModelVillagerModern(0.0F), 0.5F);
        this.addLayer(new LayerVillagerBiomeType(this));
        this.addLayer(new LayerVillagerProfession(this));
        this.addLayer(new LayerVillagerProfessionLevel(this));
    }

    // ------------------------------ //
    // --- Skin resource elements --- //
    // ------------------------------ //

    // Added in v3.2
    static final String VAD = "textures/entity/villager/"; // Villager address, because it's used so damn much
    static final String MIDLC = Millennium.MODID; // Same with Mod ID

    // Base skin texture
    private static final ResourceLocation villagerBaseSkin     = new ResourceLocation(MIDLC, VAD + "villager.png");


    private static final ResourceLocation defaultOldNitwit = new ResourceLocation(VAD + "villager.png");
    private static final ResourceLocation defaultOldFarmer = new ResourceLocation(VAD + "farmer.png");
    private static final ResourceLocation defaultOldLibrarian = new ResourceLocation(VAD + "librarian.png");
    private static final ResourceLocation defaultOldPriest = new ResourceLocation(VAD + "priest.png");
    private static final ResourceLocation defaultOldSmith = new ResourceLocation(VAD + "smith.png");
    private static final ResourceLocation defaultOldButcher = new ResourceLocation(VAD + "butcher.png");

    // Profession level purses
    private static final ResourceLocation villagerProfessionLevelStone = new ResourceLocation(MIDLC, VAD + "profession_level/stone.png");
    private static final ResourceLocation villagerProfessionLevelIron = new ResourceLocation(MIDLC, VAD + "profession_level/iron.png");
    private static final ResourceLocation villagerProfessionLevelGold = new ResourceLocation(MIDLC, VAD + "profession_level/gold.png");
    private static final ResourceLocation villagerProfessionLevelEmerald = new ResourceLocation(MIDLC, VAD + "profession_level/emerald.png");
    private static final ResourceLocation villagerProfessionLevelDiamond = new ResourceLocation(MIDLC, VAD + "profession_level/diamond.png");

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityNewVillager villager)
    {
        return villagerBaseSkin;
    }

    @SideOnly(Side.CLIENT)
    public class LayerVillagerBiomeType implements LayerRenderer<EntityNewVillager>
    {
        private final RenderNewVillager villagerLayerRenderer;
        private final ModelVillagerModern villagerLayerModel = new ModelVillagerModern(0.1F);

        public LayerVillagerBiomeType(RenderNewVillager villagerRenderIn)
        {
            this.villagerLayerRenderer = villagerRenderIn;
        }

        public void doRenderLayer(EntityNewVillager villager, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
        {
            ResourceLocation villagerType = villager.getType().getRegistryName();
            this.villagerLayerRenderer.bindTexture(new ResourceLocation(villagerType.getNamespace(), VAD + "type/" + villagerType.getPath() + ".png"));
            this.villagerLayerModel.setModelAttributes(this.villagerLayerRenderer.getMainModel());
            this.villagerLayerModel.render(villager, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
        }

        @Override
        public boolean shouldCombineTextures() {
            return true;
        }
    }


    @SideOnly(Side.CLIENT)
    public class LayerVillagerProfession implements LayerRenderer<EntityNewVillager>
    {
        private final RenderNewVillager villagerLayerRenderer;
        private final ModelVillagerModern villagerLayerModel = new ModelVillagerModern(0.2F);

        public LayerVillagerProfession(RenderNewVillager villagerRenderIn)
        {
            this.villagerLayerRenderer = villagerRenderIn;
        }

        public void doRenderLayer(EntityNewVillager villager, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
        {
            VillagerProfession profession = villager.getProfession();
            if(profession != VillagerProfession.NONE && !villager.isChild()) {
                this.villagerLayerRenderer.bindTexture(new ResourceLocation(profession.getRegistryName().getNamespace(), VAD + "profession/" + profession.getRegistryName().getPath() + ".png"));
                this.villagerLayerRenderer.getMainModel().render(villager, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
                this.villagerLayerModel.render(villager, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
            }
        }

        @Override
        public boolean shouldCombineTextures() {
            return true;
        }
    }


    @SideOnly(Side.CLIENT)
    public class LayerVillagerProfessionLevel implements LayerRenderer<EntityNewVillager>
    {
        private final RenderNewVillager villagerLayerRenderer;
        private final ModelVillagerModern villagerLayerModel = new ModelVillagerModern(0.3F);

        public LayerVillagerProfessionLevel(RenderNewVillager villagerRenderIn)
        {
            this.villagerLayerRenderer = villagerRenderIn;
        }

        public void doRenderLayer(EntityNewVillager villager, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
        {
            /*
            if (
                    villager.getProfession() >= 0
                            // v3.2: Is vanilla OR is a modular type
                            && (villager.getProfession() <= 5 || GeneralConfig.professionID_a.indexOf(villager.getProfessionForge().getRegistryName().toString())!=-1)
                            && !villager.isInvisible()
            )
            {
                // Profession levels
                if (GeneralConfig.modernVillagerSkins)
                {
                    final int profLevel = (villager.isChild() | villager.getProfession()==5) ? 0 : villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null).getProfessionLevel();
                    if (profLevel >= 5) {this.villagerLayerRenderer.bindTexture(villagerProfessionLevelDiamond);}
                    switch (profLevel)
                    {
                        case 1: this.villagerLayerRenderer.bindTexture(villagerProfessionLevelStone); break;
                        case 2: this.villagerLayerRenderer.bindTexture(villagerProfessionLevelIron); break;
                        case 3: this.villagerLayerRenderer.bindTexture(villagerProfessionLevelGold); break;
                        case 4: this.villagerLayerRenderer.bindTexture(villagerProfessionLevelEmerald); break;
                    }
                }
                this.villagerLayerRenderer.getMainModel().render(villager, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
                this.villagerLayerModel.render(villager, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
            }
            */
            return;
        }

        @Override
        public boolean shouldCombineTextures() {
            return true;
        }
    }

    // v3.1.1 - Added in to allow villagers to render as babies and not man-babies
    // summon Villager ~ ~ ~ {Age:-24000}
    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
    @Override
    protected void preRenderCallback(EntityNewVillager entitylivingbaseIn, float partialTickTime)
    {
        float f = 0.9375F;

        if (entitylivingbaseIn.getGrowingAge() < 0)
        {
            f = (float)((double)f * 0.5D);
            this.shadowSize = 0.25F;
        }
        else
        {
            this.shadowSize = 0.5F;
        }

        GlStateManager.scale(f, f, f);
    }
}
