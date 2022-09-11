package libetal.applications.assetor.ui.icons

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType.Companion.NonZero
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap.Companion.Butt
import androidx.compose.ui.graphics.StrokeJoin.Companion.Miter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.ImageVector.Builder
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

val Assetor.Resize24: ImageVector
    get() {
        if (resize24 == null) resize24 = Builder(
            name = "Resize-icon24", defaultWidth = 24.0.dp, defaultHeight =
            24.0.dp, viewportWidth = 24.0f, viewportHeight = 24.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(19.969334f, 0.3626667f)
                lineTo(4.076f, 0.3626667f)
                arcTo(3.7493334f, 3.7493334f, 0.0f, false, false, 0.316f, 4.12f)
                verticalLineToRelative(15.893333f)
                arcToRelative(3.7493334f, 3.7493334f, 0.0f, false, false, 3.76f, 3.76f)
                horizontalLineToRelative(15.893333f)
                arcToRelative(3.7493334f, 3.7493334f, 0.0f, false, false, 3.76f, -3.76f)
                lineTo(23.729336f, 4.166667f)
                curveTo(23.773335f, 2.0826669f, 22.053335f, 0.3626667f, 19.968f, 0.3626667f)
                close()
                moveTo(21.962667f, 20.060001f)
                curveToRelative(0.0f, 1.0866667f, -0.9066667f, 1.9933333f, -1.9933333f, 1.9933333f)
                lineTo(4.076f, 22.053335f)
                curveToRelative(
                    -1.0866667f,
                    0.0f, -1.9933333f, -0.9066667f, -1.9933333f, -1.9933333f
                )
                lineTo(2.0826669f, 4.166667f)
                curveToRelative(0.0f, -1.088f, 0.9066667f, -1.9933333f, 1.9933333f, -1.9933333f)
                horizontalLineToRelative(15.893333f)
                curveToRelative(1.0866667f, 0.0f, 1.9933333f, 0.9066667f, 1.9933333f, 1.9933333f)
                verticalLineToRelative(15.893333f)
                close()
            }
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(9.5546665f, 13.177334f)
                arcToRelative(0.87600005f, 0.87600005f, 0.0f, false, true, 1.268f, 0.0f)
                arcToRelative(0.87600005f, 0.87600005f, 0.0f, false, true, 0.0f, 1.268f)
                lineToRelative(-3.8933334f, 3.8933334f)
                lineTo(9.826667f, 18.338667f)
                curveToRelative(0.49733335f, 0.0f, 0.9066667f, 0.408f, 0.9066667f, 0.9066667f)
                curveToRelative(0.0f, 0.4986667f, -0.40933335f, 0.9066667f, -0.9066667f, 0.9066667f)
                lineTo(4.754667f, 20.152f)
                arcToRelative(0.908f, 0.908f, 0.0f, false, true, -0.9066667f, -0.9066667f)
                lineTo(3.848f, 14.173334f)
                curveToRelative(0.0f, -0.49733335f, 0.408f, -0.9066667f, 0.9066667f, -0.9066667f)
                curveToRelative(0.4986667f, 0.0f, 0.9066667f, 0.40933335f, 0.9066667f, 0.9066667f)
                verticalLineToRelative(2.8986669f)
                lineToRelative(3.8933334f, -3.8946667f)
                close()
                moveTo(19.336f, 3.7586668f)
                curveToRelative(0.49733335f, 0.0f, 0.9066667f, 0.408f, 0.9066667f, 0.9066667f)
                verticalLineToRelative(5.070667f)
                curveToRelative(
                    0.0f, 0.49733335f, -0.40933335f, 0.9066667f, -0.9066667f,
                    0.9066667f
                )
                arcToRelative(0.908f, 0.908f, 0.0f, false, true, -0.9066667f, -0.9066667f)
                lineTo(18.429333f, 6.837333f)
                lineToRelative(-3.8933334f, 3.8946667f)
                arcToRelative(0.8933334f, 0.8933334f, 0.0f, false, true, -0.6346667f, 0.272f)
                arcToRelative(0.8933334f, 0.8933334f, 0.0f, false, true, -0.6333333f, -0.272f)
                arcToRelative(0.87600005f, 0.87600005f, 0.0f, false, true, 0.0f, -1.2666667f)
                lineToRelative(3.8933334f, -3.896f)
                horizontalLineToRelative(-2.8973336f)
                arcToRelative(0.908f, 0.908f, 0.0f, false, true, -0.9066667f, -0.9053334f)
                curveToRelative(
                    0.0f, -0.49733335f, 0.40933335f, -0.9066667f,
                    0.9066667f, -0.9066667f
                )
                horizontalLineToRelative(5.072f)
                close()
            }
        }.build()

        return resize24!!
    }

private var resize24: ImageVector? = null
