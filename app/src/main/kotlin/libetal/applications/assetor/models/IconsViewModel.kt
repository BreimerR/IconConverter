package libetal.applications.assetor.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Job
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.io.File
import libetal.kotlin.laziest
import libetal.kotlin.log.info

class IconsViewModel(initialPath: String = System.getProperty("user.home")!!) : ViewModel() {

    val file by lazy {
        File(path)
    }

    val pathState by laziest {
        mutableStateOf(initialPath)
    }

    val folderName by lazy {
        file.name
    }

    val folders by laziest {


        if (file.isDirectory)
            mutableStateListOf(
                File(path)
            )
        else mutableStateListOf()

    }

    val painters by laziest {
        mutableStateListOf<IconViewModel>()
    }

    var path
        get() = pathState.value
        set(value) {
            pathState.value = value
        }

    override fun onResume() {
        scan()
    }


    fun scan(): Job = launch {

        val currentFolders = buildList {
            folders.forEach { add(it) }
        }

        folders.clear()

        ioLaunch {
            for (folder in currentFolders) {
                (folder.listFiles() ?: emptyArray()).let { files ->
                    for (file in files) {
                        TAG info "Scanning: ${file.path}"
                        when (file?.isFile) {
                            true -> {
                                when (file.extension) {
                                    "svg" -> launch {
                                        painters.add(IconViewModel(file))
                                    }

                                }
                            }

                            else -> launch {
                                folders.add(
                                    file
                                )
                            }
                        }
                    }
                }

            }

            if (painters.isEmpty()) scan()
            else TAG info "Having ${painters.size} Icons"
        }

    }

    override fun onDestroy() {
        painters.clear()
        folders.clear()
    }

    companion object {
        const val TAG = "IconsViewModel"
    }

}
