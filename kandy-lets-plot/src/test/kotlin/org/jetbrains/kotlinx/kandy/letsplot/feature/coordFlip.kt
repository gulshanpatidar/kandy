package org.jetbrains.kotlinx.kandy.letsplot.feature

import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.ir.Layer
import org.jetbrains.kotlinx.kandy.ir.aes.Aes
import org.jetbrains.kotlinx.kandy.ir.bindings.Mapping
import org.jetbrains.kotlinx.kandy.ir.bindings.Setting
import org.jetbrains.kotlinx.kandy.ir.geom.Geom
import kotlin.test.Test
import kotlin.test.assertEquals
import io.mockk.mockk
import io.mockk.every
import org.jetbrains.kotlinx.kandy.dsl.internal.BindingCollector
import org.jetbrains.kotlinx.kandy.dsl.internal.LayerContextInterface

class CoordFlipTest {
    @Test
    fun `test coord flip feature`() {
        val geom = mockk<Geom>()
        val mockLayer = Layer(
            datasetIndex = 0, geom = geom,
            mappings = mapOf(Aes("a") to mockk<Mapping>()),
            settings = mapOf(Aes("a") to mockk<Setting>()),
            features = emptyMap(), freeScales = emptyMap(), inheritsBindings = true
        )
        val layerContext = mockk<LayerContextInterface> {
            every { requiredAes } returns emptySet()
            every { bindingCollector } returns BindingCollector()
            every { toLayer(true) } returns mockLayer
        }
        val plot = plot {
            addLayer(layerContext)
            coordFlip()
        }
        assertEquals(CoordFlip, plot.features[CoordFlip.FEATURE_NAME])
    }
}