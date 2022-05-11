package com.cleanroommc.millennium.client.render.entity;

import com.cleanroommc.millennium.Millennium;
import com.cleanroommc.millennium.client.model.ModelVillagerModern;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Adapted from Village Names by AstroTibs
 */

@SideOnly(Side.CLIENT)
public class RenderVillagerModern extends RenderLiving<EntityVillager> {

    // Constructor
    public RenderVillagerModern(RenderManager renderManagerIn) {
        //this.mainModel = new ModelVillagerModern(0.0F);
        //this.villagerModel = (ModelVillagerModern) this.mainModel;
        //this.setRenderPassModel(new ModelVillagerModern(0.1F));
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

    // Biome-based types
    private static final ResourceLocation villagerTypeDesert   = new ResourceLocation(MIDLC, VAD + "type/desert.png");
    private static final ResourceLocation villagerTypeJungle   = new ResourceLocation(MIDLC, VAD + "type/jungle.png");
    private static final ResourceLocation villagerTypePlains   = new ResourceLocation(MIDLC, VAD + "type/plains.png");
    private static final ResourceLocation villagerTypeSavanna  = new ResourceLocation(MIDLC, VAD + "type/savanna.png");
    private static final ResourceLocation villagerTypeSnow     = new ResourceLocation(MIDLC, VAD + "type/snow.png");
    private static final ResourceLocation villagerTypeSwamp    = new ResourceLocation(MIDLC, VAD + "type/swamp.png");
    private static final ResourceLocation villagerTypeTaiga    = new ResourceLocation(MIDLC, VAD + "type/taiga.png");

    // Profession-based layer
    private static final ResourceLocation villagerProfessionArmorer = new ResourceLocation(MIDLC, VAD + "profession/armorer.png");
    private static final ResourceLocation villagerProfessionButcher = new ResourceLocation(MIDLC, VAD + "profession/butcher.png");
    private static final ResourceLocation villagerProfessionCartographer = new ResourceLocation(MIDLC, VAD + "profession/cartographer.png");
    private static final ResourceLocation villagerProfessionCleric = new ResourceLocation(MIDLC, VAD + "profession/cleric.png");
    private static final ResourceLocation villagerProfessionFarmer = new ResourceLocation(MIDLC, VAD + "profession/farmer.png");
    private static final ResourceLocation villagerProfessionFisherman = new ResourceLocation(MIDLC, VAD + "profession/fisherman.png");
    private static final ResourceLocation villagerProfessionFletcher = new ResourceLocation(MIDLC, VAD + "profession/fletcher.png");
    private static final ResourceLocation villagerProfessionLeatherworker = new ResourceLocation(MIDLC, VAD + "profession/leatherworker.png");
    private static final ResourceLocation villagerProfessionLibrarian = new ResourceLocation(MIDLC, VAD + "profession/librarian.png");
    private static final ResourceLocation villagerProfessionMason = new ResourceLocation(MIDLC, VAD + "profession/mason.png");
    private static final ResourceLocation villagerProfessionNitwit = new ResourceLocation(MIDLC, VAD + "profession/nitwit.png");
    private static final ResourceLocation villagerProfessionShepherd = new ResourceLocation(MIDLC, VAD + "profession/shepherd.png");
    private static final ResourceLocation villagerProfessionToolsmith = new ResourceLocation(MIDLC, VAD + "profession/toolsmith.png");
    private static final ResourceLocation villagerProfessionWeaponsmith = new ResourceLocation(MIDLC, VAD + "profession/weaponsmith.png");

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
    protected ResourceLocation getEntityTexture(EntityVillager villager)
    {
        int indexofmodprof = GeneralConfig.professionID_a.indexOf(villager.getProfessionForge().getRegistryName().toString());

        if (
                GeneralConfig.modernVillagerSkins
                        && villager.getProfession() >= 0
                        && (villager.getProfession() <= 5 || (indexofmodprof > -1 && !((String) GeneralConfig.careerAsset_a.get(indexofmodprof)).equals("") ) )
        )
        {
            return villagerBaseSkin;
        }
        else
        {
            // Condition for modern villager skins OFF
            switch (villager.getProfession())
            {
                case 0:
                    return defaultOldFarmer;
                case 1:
                    return defaultOldLibrarian;
                case 2:
                    return defaultOldPriest;
                case 3:
                    return defaultOldSmith;
                case 4:
                    return defaultOldButcher;
                default:
                    return villager.getProfessionForge().getSkin();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public class LayerVillagerBiomeType implements LayerRenderer<EntityVillager>
    {
        private final RenderVillagerModern villagerLayerRenderer;
        private final ModelVillagerModern villagerLayerModel = new ModelVillagerModern(0.1F);

        public LayerVillagerBiomeType(RenderVillagerModern villagerRenderIn)
        {
            this.villagerLayerRenderer = villagerRenderIn;
        }

        public void doRenderLayer(EntityVillager villager, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
        {
            if (
                    villager.getProfession() >= 0
                            // v3.2: Is vanilla OR is a modular type
                            && (villager.getProfession() <= 5 || GeneralConfig.professionID_a.indexOf(villager.getProfessionForge().getRegistryName().toString())!=-1)
                            && !villager.isInvisible())
            {
                // Biome type skins
                if (GeneralConfig.modernVillagerSkins)
                {/**/
                    switch (villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null).getBiomeType())
                    {
                        case 11:
                            this.villagerLayerRenderer.bindTexture(villagerTypeSnow); break;
                        case 9:
                            this.villagerLayerRenderer.bindTexture(villagerTypeSavanna); break;
                        case 8:
                            this.villagerLayerRenderer.bindTexture(villagerTypeDesert); break;
                        case 3:
                            this.villagerLayerRenderer.bindTexture(villagerTypeForest); break;
                        case 7:
                            this.villagerLayerRenderer.bindTexture(villagerTypeTaiga); break;
                        case 6:
                            this.villagerLayerRenderer.bindTexture(villagerTypeSwamp); break;
                        case 5:
                            this.villagerLayerRenderer.bindTexture(villagerTypeJungle); break;
                        default:
                        case 0:
                            this.villagerLayerRenderer.bindTexture(villagerTypePlains); break;
                    }/**/

                }
                this.villagerLayerModel.setModelAttributes(this.villagerLayerRenderer.getMainModel());
                this.villagerLayerModel.render(villager, p_177141_2_, p_177141_3_, p_177141_5_, p_177141_6_, p_177141_7_, scale);
            }

        }

        @Override
        public boolean shouldCombineTextures() {
            return true;
        }
    }


    @SideOnly(Side.CLIENT)
    public class LayerVillagerProfession implements LayerRenderer<EntityVillager>
    {
        private final RenderVillagerModern villagerLayerRenderer;
        private final ModelVillagerModern villagerLayerModel = new ModelVillagerModern(0.2F);

        public LayerVillagerProfession(RenderVillagerModern villagerRenderIn)
        {
            this.villagerLayerRenderer = villagerRenderIn;
        }

        public void doRenderLayer(EntityVillager villager, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
        {
            // Changed in v3.2 to allow for modded skins
            if (villager.getProfession() >= 0 && !villager.isInvisible() && !villager.isChild())
            {
                String villagerForgeProfessionRegistryName = villager.getProfessionForge().getRegistryName().toString();
                if (villager.getProfession() <= 5 && villagerForgeProfessionRegistryName.length()>=10 && villagerForgeProfessionRegistryName.substring(0, 10).equals("minecraft:"))
                {
                    // Profession skins
                    if (GeneralConfig.modernVillagerSkins)
                    {

                        /**/
                        int career = villager.getCapability(ModularSkinProvider.MODULAR_SKIN, null).getCareer();

                        switch (villager.getProfession())
                        {
                            case 0: // Farmer type
                                switch (career)
                                {
                                    default:
                                    case 1:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionFarmer); break;
                                    case 2:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionFisherman); break;
                                    case 3:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionShepherd); break;
                                    case 4:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionFletcher); break;
                                }
                                break;
                            case 1: // Librarian type
                                switch (career)
                                {
                                    default:
                                    case 1:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionLibrarian); break;
                                    case 2:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionCartographer); break;
                                }
                                break;
                            case 2: // Priest type
                                switch (career)
                                {
                                    default:
                                    case 1:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionCleric); break;
                                }
                                break;
                            case 3: // Smith type
                                switch (career)
                                {
                                    case 1:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionArmorer); break;
                                    case 2:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionWeaponsmith); break;
                                    default:
                                    case 3:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionToolsmith); break;
                                    case 4:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionMason); break;
                                }
                                break;
                            case 4: // Butcher type
                                switch (career)
                                {
                                    default:
                                    case 1:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionButcher); break;
                                    case 2:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionLeatherworker); break;
                                }
                                break;
                            case 5: // Nitwit
                                switch (career)
                                {
                                    default:
                                    case 1:
                                        this.villagerLayerRenderer.bindTexture(villagerProfessionNitwit); break;
                                }
                                break;
                            default: // No profession skin
                        }
                        /**/
                    }
                }
                else
                {
                    // Mod profession skins
                    int indexofmodprof = GeneralConfig.professionID_a.indexOf(villager.getProfessionForge().getRegistryName().toString());
                    if (
                            indexofmodprof > -1 // Has a skin asset mapping
                                    && !((String) GeneralConfig.careerAsset_a.get(indexofmodprof)).equals("") // That mapping isn't blank
                    )
                    {
                        final String profRootName = (String) (GeneralConfig.careerAsset_a.get(indexofmodprof));
                        final ResourceLocation modCareerSkin = new ResourceLocation(MIDLC, VAD + "profession/"+profRootName+".png");
                        this.villagerLayerRenderer.bindTexture(modCareerSkin);
                    }
					/*
					else
					{
						// If all else fails, bind the nitwit.
						this.villagerLayerRenderer.bindTexture(villagerProfessionNitwit);
					}
					*/
                }

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
    public class LayerVillagerProfessionLevel implements LayerRenderer<EntityVillager>
    {
        private final RenderVillagerModern villagerLayerRenderer;
        private final ModelVillagerModern villagerLayerModel = new ModelVillagerModern(0.3F);

        public LayerVillagerProfessionLevel(RenderVillagerModern villagerRenderIn)
        {
            this.villagerLayerRenderer = villagerRenderIn;
        }

        public void doRenderLayer(EntityVillager villager, float p_177141_2_, float p_177141_3_, float partialTicks, float p_177141_5_, float p_177141_6_, float p_177141_7_, float scale)
        {
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
    protected void preRenderCallback(EntityVillager entitylivingbaseIn, float partialTickTime)
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
