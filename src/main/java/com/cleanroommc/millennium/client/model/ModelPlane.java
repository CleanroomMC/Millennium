package com.cleanroommc.millennium.client.model;

import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.PositionTextureVertex;
import net.minecraft.client.model.TexturedQuad;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Just a copied-over and modified version of ModelBox that renders only the top face.
 * @author AstroTibs
 */

//Added in v3.1
public class ModelPlane extends ModelBox
{
    /** The (x,y,z) vertex positions and (u,v) texture coordinates for each of the 8 points on a cube */
    private PositionTextureVertex[] vertexPositions;
    /** An array of 6 TexturedQuads, one for each face of a cube */
    private TexturedQuad[] quadList;
    /** X vertex coordinate of lower box corner */
    public final float posX1;
    /** Y vertex coordinate of lower box corner */
    public final float posY1;
    /** Z vertex coordinate of lower box corner */
    public final float posZ1;
    /** X vertex coordinate of upper box corner */
    public final float posX2;
    /** Y vertex coordinate of upper box corner */
    public final float posY2;
    /** Z vertex coordinate of upper box corner */
    public final float posZ2;
    public String boxName;
    //private static final String __OBFID = "CL_00000872";

    public ModelPlane(ModelRenderer modelRenderer, int textureOffsetX, int textureOffsetY,
                      float originX, float originY, float originZ,
                      int width, int height, int depth, float scaleFactor)
    {
        super(modelRenderer, textureOffsetX, textureOffsetY, originX, originY, originZ, width, height, depth, scaleFactor);

        this.posX1 = originX;
        this.posY1 = originY;
        this.posZ1 = originZ;
        this.posX2 = originX + (float)width;
        this.posY2 = originY + (float)height;
        this.posZ2 = originZ + (float)depth;
        this.vertexPositions = new PositionTextureVertex[8];
        this.quadList = new TexturedQuad[1];//new TexturedQuad[6];
        float posX2scaled = originX + (float)width;
        float posY2scaled = originY + (float)height;
        float posZ2scaled = originZ + (float)depth;
        originX -= scaleFactor;
        originY -= scaleFactor;
        originZ -= scaleFactor;
        posX2scaled += scaleFactor;
        posY2scaled += scaleFactor;
        posZ2scaled += scaleFactor;

        // Swaps originX and posX2scaled if mirror is on
        if (modelRenderer.mirror)
        {
            float f7 = posX2scaled;
            posX2scaled = originX;
            originX = f7;
        }

        PositionTextureVertex posvertex_X1_Y1_Z1 = new PositionTextureVertex(originX, originY, originZ, 0.0F, 0.0F);
        PositionTextureVertex posvertex_X2_Y1_Z1 = new PositionTextureVertex(posX2scaled, originY, originZ, 0.0F, 8.0F);
        PositionTextureVertex posvertex_X2_Y2_Z1 = new PositionTextureVertex(posX2scaled, posY2scaled, originZ, 8.0F, 8.0F);
        PositionTextureVertex posvertex_X1_Y2_Z1 = new PositionTextureVertex(originX, posY2scaled, originZ, 8.0F, 0.0F);
        PositionTextureVertex posvertex_X1_Y1_Z2 = new PositionTextureVertex(originX, originY, posZ2scaled, 0.0F, 0.0F);
        PositionTextureVertex posvertex_X2_Y1_Z2 = new PositionTextureVertex(posX2scaled, originY, posZ2scaled, 0.0F, 8.0F);
        PositionTextureVertex posvertex_X2_Y2_Z2 = new PositionTextureVertex(posX2scaled, posY2scaled, posZ2scaled, 8.0F, 8.0F);
        PositionTextureVertex posvertex_X1_Y2_Z2 = new PositionTextureVertex(originX, posY2scaled, posZ2scaled, 8.0F, 0.0F);
        this.vertexPositions[0] = posvertex_X1_Y1_Z1;
        this.vertexPositions[1] = posvertex_X2_Y1_Z1;
        this.vertexPositions[2] = posvertex_X2_Y2_Z1;
        this.vertexPositions[3] = posvertex_X1_Y2_Z1;
        this.vertexPositions[4] = posvertex_X1_Y1_Z2;
        this.vertexPositions[5] = posvertex_X2_Y1_Z2;
        this.vertexPositions[6] = posvertex_X2_Y2_Z2;
        this.vertexPositions[7] = posvertex_X1_Y2_Z2;

      /*
      this.quadList[0] = new TexturedQuad(
      		new PositionTextureVertex[] {posvertex_X2_Y1_Z2, posvertex_X2_Y1_Z1, posvertex_X2_Y2_Z1, posvertex_X2_Y2_Z2},
      		textureOffsetX + depth + width, textureOffsetY + depth, textureOffsetX + depth + width + depth, textureOffsetY + depth + height, modelRenderer.textureWidth, modelRenderer.textureHeight);
      this.quadList[1] = new TexturedQuad(
      		new PositionTextureVertex[] {posvertex_X1_Y1_Z1, posvertex_X1_Y1_Z2, posvertex_X1_Y2_Z2, posvertex_X1_Y2_Z1},
      		textureOffsetX, textureOffsetY + depth, textureOffsetX + depth, textureOffsetY + depth + height, modelRenderer.textureWidth, modelRenderer.textureHeight);
      */
        // Top face
        this.quadList[0] = new TexturedQuad(
                new PositionTextureVertex[] {posvertex_X2_Y1_Z2, posvertex_X1_Y1_Z2, posvertex_X1_Y1_Z1, posvertex_X2_Y1_Z1},
                textureOffsetX + depth,
                textureOffsetY,
                textureOffsetX + depth + width,
                textureOffsetY + depth,
                modelRenderer.textureWidth, modelRenderer.textureHeight);
      /*
      // Bottom face
      this.quadList[0] = new TexturedQuad(
      		new PositionTextureVertex[] {posvertex_X2_Y2_Z1, posvertex_X1_Y2_Z1, posvertex_X1_Y2_Z2, posvertex_X2_Y2_Z2},
      		textureOffsetX + depth,// + width,
      		textureOffsetY + depth,
      		textureOffsetX + depth + width,// + width,
      		textureOffsetY,
      		modelRenderer.textureWidth, modelRenderer.textureHeight);

      this.quadList[4] = new TexturedQuad(
      		new PositionTextureVertex[] {posvertex_X2_Y1_Z1, posvertex_X1_Y1_Z1, posvertex_X1_Y2_Z1, posvertex_X2_Y2_Z1},
      		textureOffsetX + depth, textureOffsetY + depth, textureOffsetX + depth + width, textureOffsetY + depth + height, modelRenderer.textureWidth, modelRenderer.textureHeight);
      this.quadList[5] = new TexturedQuad(
      		new PositionTextureVertex[] {posvertex_X1_Y1_Z2, posvertex_X2_Y1_Z2, posvertex_X2_Y2_Z2, posvertex_X1_Y2_Z2},
      		textureOffsetX + depth + width + depth, textureOffsetY + depth, textureOffsetX + depth + width + depth + width, textureOffsetY + depth + height, modelRenderer.textureWidth, modelRenderer.textureHeight);
      */
        // Flip all faces
        if (modelRenderer.mirror)
        {
            for (int j1 = 0; j1 < this.quadList.length; ++j1)
            {
                this.quadList[j1].flipFace();
            }
        }
    }

    /**
     * Draw the six sided box defined by this ModelBox
     */
    @SideOnly(Side.CLIENT)
    public void render(BufferBuilder renderer, float scale)
    {
        for (TexturedQuad texturedquad : this.quadList)
        {
            texturedquad.draw(renderer, scale);
        }
    }

    public ModelPlane setBoxName(String boxName)
    {
        this.boxName = boxName;
        return this;
    }
}
