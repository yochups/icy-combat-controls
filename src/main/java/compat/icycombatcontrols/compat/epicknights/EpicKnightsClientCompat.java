package compat.icycombatcontrols.compat.epicknights;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.datafixers.util.Pair;
import compat.icycombatcontrols.IcyCombatControls;
import icy.betterhorses.net.IHorseData;
import icy.betterhorses.net.client.render.BhHorseModel;
import icy.betterhorses.net.client.render.BhHorseRenderer;
import icy.betterhorses.net.client.render.BhMountedHorseVisibility;
import icy.betterhorses.net.entity.BhBreedHorse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.HorseArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mod.EventBusSubscriber(modid = IcyCombatControls.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class EpicKnightsClientCompat {
    private static final String EPIC_KNIGHTS = "magistuarmory";
    private static final ModelLayerLocation CAPARISON_LAYER = new ModelLayerLocation(
            new ResourceLocation(IcyCombatControls.MOD_ID, "epic_knights_caparison"), "main");

    private EpicKnightsClientCompat() {
    }

    @SubscribeEvent
    public static void registerLayer(EntityRenderersEvent.RegisterLayerDefinitions event) {
        if (ModList.get().isLoaded(EPIC_KNIGHTS)) {
            event.registerLayerDefinition(CAPARISON_LAYER, EpicKnightsClientCompat::caparisonGeometry);
        }
    }

    @SubscribeEvent
    public static void addLayers(EntityRenderersEvent.AddLayers event) {
        if (!ModList.get().isLoaded(EPIC_KNIGHTS)) {
            return;
        }

        for (EntityRenderer<?> renderer : Minecraft.getInstance().getEntityRenderDispatcher().renderers.values()) {
            if (renderer instanceof BhHorseRenderer<?> horseRenderer) {
                addLayer(horseRenderer, event);
            }
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void addLayer(BhHorseRenderer<?> renderer, EntityRenderersEvent.AddLayers event) {
        ((BhHorseRenderer) renderer).addLayer(new EpicKnightsCaparisonLayer(renderer, event));
    }

    private static LayerDefinition caparisonGeometry() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        PartDefinition body = root.addOrReplaceChild(
                "body",
                CubeListBuilder.create().texOffs(0, 0)
                        .addBox(-5.0F, -8.0F, -17.0F, 10.0F, 18.0F, 22.0F, new CubeDeformation(0.2F)),
                PartPose.offset(0.0F, 11.0F, 5.0F));
        body.addOrReplaceChild("tail", CubeListBuilder.create(), PartPose.ZERO);
        body.addOrReplaceChild("saddle", CubeListBuilder.create(), PartPose.ZERO);
        PartDefinition head = root.addOrReplaceChild("head_parts", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("left_saddle_mouth", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("right_saddle_mouth", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("left_saddle_line", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("right_saddle_line", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("head_saddle", CubeListBuilder.create(), PartPose.ZERO);
        head.addOrReplaceChild("mouth_saddle_wrap", CubeListBuilder.create(), PartPose.ZERO);
        for (String leg : List.of(
                "right_hind_leg", "left_hind_leg", "right_front_leg", "left_front_leg",
                "right_hind_baby_leg", "left_hind_baby_leg", "right_front_baby_leg", "left_front_baby_leg")) {
            root.addOrReplaceChild(leg, CubeListBuilder.create(), PartPose.ZERO);
        }
        return LayerDefinition.create(mesh, 64, 64);
    }

    private static final class EpicKnightsCaparisonLayer<T extends BhBreedHorse>
            extends RenderLayer<T, BhHorseModel<T>> {
        private static final ResourceLocation BASE_TEXTURE = new ResourceLocation(
                EPIC_KNIGHTS, "textures/entity/horse/armor/caparison.png");
        private static final String PATTERN_DIR = "textures/entity/horse/armor/caparison/";

        private final FittedCaparisonModel<T> model;

        private EpicKnightsCaparisonLayer(RenderLayerParent<T, BhHorseModel<T>> parent,
                                           EntityRenderersEvent.AddLayers event) {
            super(parent);
            model = new FittedCaparisonModel<>(event.getContext().bakeLayer(CAPARISON_LAYER));
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T entity,
                           float limbSwing, float limbSwingAmount, float partialTick,
                           float ageInTicks, float netHeadYaw, float headPitch) {
            ItemStack armor = IHorseData.of(entity).bh_getBarding();
            if (!(armor.getItem() instanceof HorseArmorItem)
                    || BlockItem.getBlockEntityData(armor) == null
                    || entity.isInvisible()) {
                return;
            }

            BhHorseModel<T> horseModel = getParentModel();
            ModelPart horseBody = horseModel.bh_getBody();
            model.fitTo(horseBody);
            List<Pair<Holder<BannerPattern>, DyeColor>> patterns = BannerBlockEntity.createPatterns(
                    ShieldItem.getColor(armor), BannerBlockEntity.getItemPatterns(armor));
            float opacity = BhMountedHorseVisibility.currentOpacity();
            if (opacity <= 0.01F) {
                return;
            }

            model.renderBody(poseStack, horseModel.root(), horseBody, buffer.getBuffer(opacity < 1.0F
                            ? RenderType.entityTranslucent(BASE_TEXTURE)
                            : RenderType.entityCutout(BASE_TEXTURE)),
                    packedLight, 1.0F, 1.0F, 1.0F, opacity);

            Set<Pair<Holder<BannerPattern>, DyeColor>> rendered = new HashSet<>();
            int count = 0;
            for (Pair<Holder<BannerPattern>, DyeColor> pattern : patterns) {
                if (count >= 17 || !rendered.add(pattern) || pattern.getFirst().unwrapKey().isEmpty()) {
                    continue;
                }
                ResourceLocation id = pattern.getFirst().unwrapKey().orElseThrow().location();
                ResourceLocation texture = new ResourceLocation(
                        EPIC_KNIGHTS, PATTERN_DIR + id.getPath() + ".png");
                if (Minecraft.getInstance().getResourceManager().getResource(texture).isEmpty()) {
                    continue;
                }
                float[] color = pattern.getSecond().getTextureDiffuseColors();
                VertexConsumer consumer = ItemRenderer.getArmorFoilBuffer(
                        buffer,
                        opacity < 1.0F ? RenderType.entityTranslucent(texture) : RenderType.entityNoOutline(texture),
                        false,
                        false);
                model.renderBody(poseStack, horseModel.root(), horseBody, consumer, packedLight,
                        color[0], color[1], color[2], opacity);
                count++;
            }
        }
    }

    private static final class FittedCaparisonModel<T extends AbstractHorse> extends HorseModel<T> {
        private final Map<ModelPart, CaparisonFit> fits = new IdentityHashMap<>();

        private FittedCaparisonModel(ModelPart root) {
            super(root);
        }

        private void fitTo(ModelPart source) {
            CaparisonFit fit = fits.computeIfAbsent(source, FittedCaparisonModel::measure);
            body.x = fit.offsetX();
            body.y = fit.offsetY();
            body.z = fit.offsetZ();
            body.xRot = 0.0F;
            body.yRot = 0.0F;
            body.zRot = 0.0F;
            body.xScale = fit.scaleX();
            body.yScale = fit.scaleY();
            body.zScale = fit.scaleZ();
        }

        private void renderBody(PoseStack poseStack, ModelPart horseRoot, ModelPart horseBody,
                                VertexConsumer consumer, int packedLight,
                                float red, float green, float blue, float alpha) {
            poseStack.pushPose();
            horseRoot.translateAndRotate(poseStack);
            horseBody.translateAndRotate(poseStack);
            body.render(poseStack, consumer, packedLight, OverlayTexture.NO_OVERLAY,
                    red, green, blue, alpha);
            poseStack.popPose();
        }

        private static CaparisonFit measure(ModelPart source) {
            float[] bounds = {
                    Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY,
                    Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY
            };
            source.visit(new PoseStack(), (pose, path, index, cube) -> {
                if (!path.isEmpty()) {
                    return;
                }
                bounds[0] = Math.min(bounds[0], cube.minX);
                bounds[1] = Math.min(bounds[1], cube.minY);
                bounds[2] = Math.min(bounds[2], cube.minZ);
                bounds[3] = Math.max(bounds[3], cube.maxX);
                bounds[4] = Math.max(bounds[4], cube.maxY);
                bounds[5] = Math.max(bounds[5], cube.maxZ);
            });
            if (!Float.isFinite(bounds[0])) {
                return CaparisonFit.around(-5.0F, -8.0F, -17.0F, 5.0F, 2.0F, 5.0F);
            }
            return CaparisonFit.around(
                    bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
        }
    }
}
