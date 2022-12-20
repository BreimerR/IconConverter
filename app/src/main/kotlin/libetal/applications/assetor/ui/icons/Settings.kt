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

val Assetor.Settings: ImageVector
    get() {
        if (_settings != null) {
            return _settings!!
        }
        _settings = Builder(
            name = "Settings", defaultWidth = 48.0.dp, defaultHeight = 48.0.dp,
            viewportWidth = 48.0f, viewportHeight = 48.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFF000000)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(38.86f, 25.95f)
                curveToRelative(0.08f, -0.64f, 0.14f, -1.29f, 0.14f, -1.95f)
                reflectiveCurveToRelative(-0.06f, -1.31f, -0.14f, -1.95f)
                lineToRelative(4.23f, -3.31f)
                curveToRelative(0.38f, -0.3f, 0.49f, -0.84f, 0.24f, -1.28f)
                lineToRelative(-4.0f, -6.93f)
                curveToRelative(-0.25f, -0.43f, -0.77f, -0.61f, -1.22f, -0.43f)
                lineToRelative(-4.98f, 2.01f)
                curveToRelative(-1.03f, -0.79f, -2.16f, -1.46f, -3.38f, -1.97f)
                lineTo(29.0f, 4.84f)
                curveToRelative(-0.09f, -0.47f, -0.5f, -0.84f, -1.0f, -0.84f)
                horizontalLineToRelative(-8.0f)
                curveToRelative(-0.5f, 0.0f, -0.91f, 0.37f, -0.99f, 0.84f)
                lineToRelative(-0.75f, 5.3f)
                curveToRelative(-1.22f, 0.51f, -2.35f, 1.17f, -3.38f, 1.97f)
                lineTo(9.9f, 10.1f)
                curveToRelative(-0.45f, -0.17f, -0.97f, 0.0f, -1.22f, 0.43f)
                lineToRelative(-4.0f, 6.93f)
                curveToRelative(-0.25f, 0.43f, -0.14f, 0.97f, 0.24f, 1.28f)
                lineToRelative(4.22f, 3.31f)
                curveTo(9.06f, 22.69f, 9.0f, 23.34f, 9.0f, 24.0f)
                reflectiveCurveToRelative(0.06f, 1.31f, 0.14f, 1.95f)
                lineToRelative(-4.22f, 3.31f)
                curveToRelative(-0.38f, 0.3f, -0.49f, 0.84f, -0.24f, 1.28f)
                lineToRelative(4.0f, 6.93f)
                curveToRelative(0.25f, 0.43f, 0.77f, 0.61f, 1.22f, 0.43f)
                lineToRelative(4.98f, -2.01f)
                curveToRelative(1.03f, 0.79f, 2.16f, 1.46f, 3.38f, 1.97f)
                lineToRelative(0.75f, 5.3f)
                curveToRelative(0.08f, 0.47f, 0.49f, 0.84f, 0.99f, 0.84f)
                horizontalLineToRelative(8.0f)
                curveToRelative(0.5f, 0.0f, 0.91f, -0.37f, 0.99f, -0.84f)
                lineToRelative(0.75f, -5.3f)
                curveToRelative(1.22f, -0.51f, 2.35f, -1.17f, 3.38f, -1.97f)
                lineToRelative(4.98f, 2.01f)
                curveToRelative(0.45f, 0.17f, 0.97f, 0.0f, 1.22f, -0.43f)
                lineToRelative(4.0f, -6.93f)
                curveToRelative(0.25f, -0.43f, 0.14f, -0.97f, -0.24f, -1.28f)
                lineToRelative(-4.22f, -3.31f)
                close()
                moveTo(24.0f, 31.0f)
                curveToRelative(-3.87f, 0.0f, -7.0f, -3.13f, -7.0f, -7.0f)
                reflectiveCurveToRelative(3.13f, -7.0f, 7.0f, -7.0f)
                reflectiveCurveToRelative(7.0f, 3.13f, 7.0f, 7.0f)
                reflectiveCurveToRelative(-3.13f, 7.0f, -7.0f, 7.0f)
                close()
            }
        }
            .build()
        return _settings!!
    }

private var _settings: ImageVector? = null
