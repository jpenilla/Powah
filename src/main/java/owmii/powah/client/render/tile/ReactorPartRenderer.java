package owmii.powah.client.render.tile;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import owmii.lib.client.model.CubeModel;
import owmii.lib.client.renderer.tile.AbstractTileRenderer;
import owmii.powah.Powah;
import owmii.powah.block.reactor.ReactorPartTile;

public class ReactorPartRenderer extends AbstractTileRenderer<ReactorPartTile> {
    public static final CubeModel CUBE_MODEL = new CubeModel(16, RenderType::entitySolid);

    protected ReactorPartRenderer(BlockEntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(ReactorPartTile te, float pt, PoseStack matrix, MultiBufferSource rtb, Minecraft mc, ClientLevel world, LocalPlayer player, int light, int ov) {
        if (te.isBuilt()) return;
        matrix.pushPose();
        matrix.translate(0.5, 0.5, 0.5);
        matrix.scale(1.0f, -1.0f, -1.0f);
        VertexConsumer buffer = rtb.getBuffer(CUBE_MODEL.renderType(new ResourceLocation(Powah.MOD_ID, "textures/model/tile/reactor_block_" + te.getVariant().getName() + ".png")));
        CUBE_MODEL.renderToBuffer(matrix, buffer, light, ov, 1.0F, 1.0F, 1.0F, 1.0F);
        matrix.popPose();
    }
}
