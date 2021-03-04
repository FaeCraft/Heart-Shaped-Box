package io.github.faecraft.heartshapedbox.client

import com.mojang.blaze3d.systems.RenderSystem
import io.github.faecraft.heartshapedbox.main.HSBMain
import net.minecraft.client.MinecraftClient
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.util.math.Vector3f
import net.minecraft.entity.LivingEntity
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText
import net.minecraft.util.Identifier

public class HealthDisplayScreen(public val allowHealing: Boolean) : Screen(LiteralText("Health")) {
    public val viewTitle: TranslatableText = TranslatableText("screen.${HSBMain.MOD_ID}.health_screen.title_view")
    public val healTitle: TranslatableText = TranslatableText("screen.${HSBMain.MOD_ID}.health_screen.title_heal")

    override fun isPauseScreen(): Boolean = false

    override fun render(matrices: MatrixStack?, mouseX: Int, mouseY: Int, delta: Float) {
        renderBackground(matrices)
        drawBackground(matrices)
        super.render(matrices, mouseX, mouseY, delta)
    }

    private fun drawBackground(matrices: MatrixStack?) {
        RenderSystem.color4f(1.0f, 1.0f, 1.0f, 1.0f)
        client!!.textureManager.bindTexture(BACKGROUND_TEXTURE)

        val x = (width - TEXTURE_WIDTH) / 2
        val y = (height - TEXTURE_HEIGHT) / 2

        drawTexture(matrices, x, y, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT)
        drawPlayer(x, y, client!!.player!!)

        val text = if (allowHealing) healTitle else viewTitle
        textRenderer.draw(
            matrices,
            text,
            x + PLAYER_X_OFFSET - textRenderer.getWidth(text) / 2,
            y + TITLE_VERTICAL_OFFSET,
            TEXT_COLOR
        )
    }

    private fun drawPlayer(x: Int, y: Int, entity: LivingEntity) {
        // Taken directly from InventoryScreen with small tweaks

        RenderSystem.pushMatrix()
        RenderSystem.translatef(x + PLAYER_X_OFFSET, y + PLAYER_Y_OFFSET, UNKNOWN_TRANSFORM_A)
        RenderSystem.scalef(1.0f, 1.0f, -1.0f)
        val matrixStack = MatrixStack()
        matrixStack.translate(0.0, 0.0, UNKNOWN_TRANSFORM_B)
        matrixStack.scale(PLAYER_SCALE, PLAYER_SCALE, PLAYER_SCALE)
        val quaternion = Vector3f.POSITIVE_Z.getDegreesQuaternion(0f)
        val quaternion2 = Vector3f.POSITIVE_X.getDegreesQuaternion(FLIP)
        quaternion.hamiltonProduct(quaternion2)
        matrixStack.multiply(quaternion)
        val h = entity.bodyYaw
        val i = entity.yaw
        val j = entity.pitch
        val k = entity.prevHeadYaw
        val l = entity.headYaw
        entity.bodyYaw = FLIP * (FORTY_DEGREES / 2)
        entity.yaw = FLIP * FORTY_DEGREES
        entity.pitch = 0f
        entity.headYaw = entity.yaw
        entity.prevHeadYaw = entity.yaw
        val entityRenderDispatcher = MinecraftClient.getInstance().entityRenderDispatcher
        quaternion2.conjugate()
        entityRenderDispatcher.rotation = quaternion2
        entityRenderDispatcher.setRenderShadows(false)
        val immediate = MinecraftClient.getInstance().bufferBuilders.entityVertexConsumers
        RenderSystem.runAsFancy {
            entityRenderDispatcher.render(
                entity,
                0.0,
                0.0,
                0.0,
                0.0f,
                1.0f,
                matrixStack,
                immediate,
                RENDER_LIGHTING
            )
        }
        immediate.draw()
        entityRenderDispatcher.setRenderShadows(true)
        entity.bodyYaw = h
        entity.yaw = i
        entity.pitch = j
        entity.prevHeadYaw = k
        entity.headYaw = l
        RenderSystem.popMatrix()
    }

    public companion object {
        public val BACKGROUND_TEXTURE: Identifier = Identifier(HSBMain.MOD_ID, "textures/gui/health_screen.png")

        public const val TEXTURE_WIDTH: Int = 256
        public const val TEXTURE_HEIGHT: Int = 166

        public const val PLAYER_X_OFFSET: Float = 127f
        public const val PLAYER_Y_OFFSET: Float = 111f

        public const val PLAYER_SCALE: Float = 30f

        public const val RENDER_LIGHTING: Int = 15_728_880

        public const val FLIP: Float = 180f
        public const val FORTY_DEGREES: Float = 40f

        public const val UNKNOWN_TRANSFORM_A: Float = 1050f
        public const val UNKNOWN_TRANSFORM_B: Double = 1000.0

        public const val TEXT_COLOR: Int = 4_210_752
        public const val TITLE_VERTICAL_OFFSET: Float = 7.0f
    }
}
