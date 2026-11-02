package compat.icycombatcontrols.compat.epicknights;

public record CaparisonFit(float scaleX, float scaleY, float scaleZ,
                           float offsetX, float offsetY, float offsetZ) {
    private static final float MODEL_WIDTH = 10.0F;
    private static final float MODEL_TOP = -8.0F;
    private static final float MODEL_LENGTH = 22.0F;
    private static final float MODEL_CENTER_Z = -6.0F;

    public static CaparisonFit around(float minX, float minY, float minZ,
                                      float maxX, float maxY, float maxZ) {
        float scaleX = (maxX - minX) / MODEL_WIDTH;
        float scaleZ = (maxZ - minZ) / MODEL_LENGTH;
        float scaleY = (scaleX + scaleZ) * 0.5F;
        float centerX = (minX + maxX) * 0.5F;
        float centerZ = (minZ + maxZ) * 0.5F;
        return new CaparisonFit(
                scaleX,
                scaleY,
                scaleZ,
                centerX,
                minY - MODEL_TOP * scaleY,
                centerZ - MODEL_CENTER_Z * scaleZ);
    }
}
