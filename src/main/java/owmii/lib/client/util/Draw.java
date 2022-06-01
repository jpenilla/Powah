package owmii.lib.client.util;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraftforge.client.gui.GuiUtils;
import owmii.lib.logistics.energy.Energy;

// TODO PORT fix everything that's commented
public class Draw {
    public static void gaugeV(TextureAtlasSprite sprite, int x, int y, int w, int h, int cap, int cur) {
        /*
        if (cap > 0 && cur > 0) {
            int i = (int) (((float) cur / cap) * h);
            final int j = i / 16;
            final int k = i - j * 16;
            for (int l = 0; l <= j; l++) {
                int height = l == j ? k : 16;
                int yy = (y - (l + 1) * 16) + h;
                if (height > 0) {
                    int m = 16 - height;
                    int n = 16 - w;
                    float uMin = sprite.getU0();
                    float uMax = sprite.getU1();
                    float vMin = sprite.getV0();
                    float vMax = sprite.getV1();
                    uMax = uMax - n / 16.0F * (uMax - uMin);
                    vMin = vMin - m / 16.0F * (vMin - vMax);
                    Tesselator tessellator = Tesselator.getInstance();
                    BufferBuilder buffer = tessellator.getBuilder();
                    buffer.begin(7, DefaultVertexFormat.POSITION_TEX);
                    buffer.vertex(x, yy + 16, 0).uv(uMin, vMax).endVertex();
                    buffer.vertex(x + w, yy + 16, 0).uv(uMax, vMax).endVertex();
                    buffer.vertex(x + w, yy + m, 0).uv(uMax, vMin).endVertex();
                    buffer.vertex(x, yy + m, 0).uv(uMin, vMin).endVertex();
                    tessellator.end();
                }
            }
        }
        */
    }

    public static void gaugeV(int x, int y, int w, int h, int uvX, int uvY, double cap, double cur) {
        if (cap > 0 && cur > 0) {
            int i = (int) (((float) cur / cap) * h);
            //GuiUtils.drawTexturedModalRect(x, y + h - i, uvX, uvY + h - i, w, i, 0);
        }
    }

    public static void gaugeV(int x, int y, int w, int h, int uvX, int uvY, Energy energy) {
        gaugeV(x, y, w, h, uvX, uvY, energy.getCapacity(), energy.getStored());
    }

    public static void gaugeV(int x, int y, int w, int h, int uvX, int uvY, long cap, long cur) {
        if (cap > 0 && cur > 0) {
            int i = (int) (((float) cur / cap) * h);
            //GuiUtils.drawTexturedModalRect(x, y + h - i, uvX, uvY + h - i, w, i, 0);
        }
    }

    public static void gaugeH(int x, int y, int w, int h, int uvX, int uvY, Energy energy) {
        gaugeH(x, y, w, h, uvX, uvY, energy.getCapacity(), energy.getStored());
    }

    public static void gaugeH(int x, int y, int w, int h, int uvX, int uvY, long cap, long cur) {
        if (cap > 0 && cur > 0) {
            int i = (int) (((float) cur / cap) * w);
            //GuiUtils.drawTexturedModalRect(x, y, uvX, uvY, i, h, 0);
        }
    }
}