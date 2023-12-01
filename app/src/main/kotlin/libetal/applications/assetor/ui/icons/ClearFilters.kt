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

val Assetor.ClearFilters: ImageVector
    get() {
        if (_clearFilters != null) {
            return _clearFilters!!
        }
        _clearFilters = Builder(
            name = "ClearFilters", defaultWidth = 12.0.dp, defaultHeight =
            8.0.dp, viewportWidth = 12.0f, viewportHeight = 8.0f
        ).apply {
            path(
                fill = SolidColor(Color(0xFFffffff)), stroke = null, strokeLineWidth = 0.0f,
                strokeLineCap = Butt, strokeLineJoin = Miter, strokeLineMiter = 4.0f,
                pathFillType = NonZero
            ) {
                moveTo(1.3333f, 4.6667f)
                horizontalLineTo(10.6667f)
                verticalLineTo(3.3333f)
                horizontalLineTo(1.3333f)
                verticalLineTo(4.6667f)
                close()
                moveTo(0.0f, 7.3333f)
                horizontalLineTo(9.3333f)
                verticalLineTo(6.0f)
                horizontalLineTo(0.0f)
                verticalLineTo(7.3333f)
                close()
                moveTo(2.6667f, 0.6667f)
                verticalLineTo(2.0f)
                horizontalLineTo(12.0f)
                verticalLineTo(0.6667f)
                horizontalLineTo(2.6667f)
                close()
            }
        }
            .build()
        return _clearFilters!!
    }

private var _clearFilters: ImageVector? = null
