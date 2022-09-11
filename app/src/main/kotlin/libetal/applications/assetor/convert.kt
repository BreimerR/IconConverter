package libetal.applications.assetor


import br.com.devsrsouza.svg2compose.Size
import br.com.devsrsouza.svg2compose.Svg2Compose
import br.com.devsrsouza.svg2compose.VectorType
import java.io.File
import kotlin.io.path.createTempDirectory
import libetal.kotlin.debug.*

fun convert(
    type: VectorType,
    iconContentString: String,
    iconName: String,
    receiverName: String,
    packageName: String,
    size: Size? = null,
    onError: (Throwable) -> Unit
): String {

    val iconPath = createTempDirectory(prefix = "ic_converter").toFile()

    val destinationDir = File(iconPath, "generated")
    val fromDestination = File(iconPath, "icon").apply { mkdirs() }

    val iconFile = File(fromDestination, iconName.trim().split(" ", "-").joinToString("").snakeCase + ".svg")

    return try {
        iconFile.outputStream().use { stream ->
            stream.writer().use { writer ->
                writer.write(iconContentString)
            }
        }
        iconFile.convert(type, destinationDir, receiverName, packageName, size)
    } catch (e: Exception) {
        onError(e)
        error("Converter", "Failed to convert icon", e)
        ""
    }

}

fun File.convert(
    type: VectorType,
    destinationDir: File,
    receiverName: String,
    packageName: String,
    size: Size? = null
): String {

    if (!canRead()) throw RuntimeException("Access denied to $path")

    if (inputStream().readAllBytes().isEmpty()) throw RuntimeException("File is empty $path")

    val iconMember = Svg2Compose.parse(path, destinationDir, type, packageName, receiverName, size = size)

    val ktFileName = iconMember.simpleName

    var results = ""

    val iconKtFile = File(destinationDir, "${packageName.replace(".", "/")}/$ktFileName.kt")

    iconKtFile.inputStream().use { stream ->
        stream.readAllBytes()?.decodeToString()?.let { ktClassFileContent ->
            results = ktClassFileContent
        }
    }

    return results

}

val String.snakeCase: String
    get() {
        var result = ""

        forEachIndexed { i, char ->
            val lowerCaseChar = char.lowercase()

            result += if (char.isUpperCase()) {
                if (i == 0) {
                    lowerCaseChar
                } else "_$lowerCaseChar"
            } else lowerCaseChar
        }

        return result
    }