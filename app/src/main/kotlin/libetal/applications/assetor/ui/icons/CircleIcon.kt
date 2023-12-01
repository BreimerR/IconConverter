package libetal.applications.assetor.ui.icons

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import libetal.libraries.compose.ui.shape

@Composable
fun CircleIcon(
    painter: Painter,
    size: Dp = 20.dp,
    tint: Color = MaterialTheme.colors.primary,
    contentDescription: String = "",
    padding: Dp = 2.dp
) =
    Box(modifier = Modifier.size(size + (padding * 2)), contentAlignment = Alignment.Center) {
        Icon(painter, modifier = Modifier.size(size), contentDescription = contentDescription, tint = tint)
    }


@Composable
fun CircleIcon(
    imageVector: ImageVector,
    onClick: () -> Unit = {},
    size: Dp = 20.dp,
    tint: Color = MaterialTheme.colors.onPrimary,
    contentDescription: String = ""
) {

    Box(
        modifier = Modifier
            .size(size)
            .shape(RoundedCornerShape(50), MaterialTheme.colors.primary)
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(imageVector, modifier = Modifier.size(size * 0.6f), contentDescription = contentDescription, tint = tint)
    }

}
