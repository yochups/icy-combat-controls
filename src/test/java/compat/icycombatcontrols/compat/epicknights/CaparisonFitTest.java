package compat.icycombatcontrols.compat.epicknights;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CaparisonFitTest {
    @Test
    void preservesEpicKnightsVanillaHorsePlacement() {
        CaparisonFit fit = CaparisonFit.around(-5.0F, -8.0F, -17.0F, 5.0F, 2.0F, 5.0F);
        assertEquals(1.0F, fit.scaleX());
        assertEquals(1.0F, fit.scaleY());
        assertEquals(1.0F, fit.scaleZ());
        assertEquals(0.0F, fit.offsetX());
        assertEquals(0.0F, fit.offsetY());
        assertEquals(0.0F, fit.offsetZ());
    }

    @Test
    void fitsAroundBetterHorsesMediumBody() {
        CaparisonFit fit = CaparisonFit.around(-5.0F, -5.0F, -11.0F, 5.0F, 5.0F, 11.0F);
        assertEquals(1.0F, fit.scaleX());
        assertEquals(1.0F, fit.scaleY());
        assertEquals(1.0F, fit.scaleZ());
        assertEquals(3.0F, fit.offsetY());
        assertEquals(6.0F, fit.offsetZ());
    }
}
