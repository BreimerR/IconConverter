package libetal.applications.assetor.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Density
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.io.File
import libetal.kotlin.io.exists
import libetal.kotlin.laziest
import libetal.kotlin.log.info
import libetal.libraries.compose.painter.loadSvgPainter

class IconsViewModel : ViewModel() {

    val pathState by laziest {
        mutableStateOf(System.getProperty("user.home")!!)
    }

    val extensions = mutableListOf("svg")

    val folders by laziest {
        mutableStateListOf<String>()
    }

    val painters by laziest {
        mutableStateListOf<Painter>()
    }

    val maxThreads = 4
    var threads = 0

    var path
        get() = pathState.value
        set(value) {
            pathState.value = value
        }

    private var searchCoroutine = mutableListOf<Job>()

    /**TODO:
     * When you click explore
     * browse for icons in the current path as root
     * On click of a folder explore its child folders and set it
     * as current active folder
     * on click of a child folder set that as current active folder and it's child folders should
     * appear on the right of the screen.
     *
     * On click on an old state we need to clear all states in front of the current folder and show just the child
     * folders of the current selected folder.
     * */

    fun explore(foldersViewModelBackStack: SnapshotStateList <FolderViewModel<IconViewModel>>) {
        clearSearch()
        TAG info "Searching"

        val folderViewModel = FolderViewModel(path, foldersViewModelBackStack) {
            when (it.extension) {
                "svg" -> IconViewModel(it)
                else -> null
            }
        }
        foldersViewModelBackStack.add(folderViewModel)
    }


    fun explore(path: String) = ioLaunch {
        explorePath(path) {
            ioLaunch(
                when (it.extension) {
                    "svg" -> suspend {
                        TAG info "Adding a ${it.path}"

                        val painter = loadSvgPainter(it, Density(24f))

                        launch {
                            TAG info "Adding a painter"
                            painters += painter
                        }
                        Unit
                    }

                    "png" -> suspend {

                    }

                    else -> throw RuntimeException("This can never happend")
                }
            )

        }
    }

    fun explorePath(path: String, onFile: suspend (File) -> Unit): Job? {
        val file = File(path)
        if (!file.exists) return null

        TAG info "FileName = ${file.name}"
        return if (file.isFile && file.extension in extensions) {
            ioLaunch {
                onFile(file)
            }
            null
        } else {
            ioLaunch {
                while (threads >= maxThreads) delay(1000)
                threads += 1
                file.listFiles()?.forEach { child ->
                    folders.add(child.path)
                }
            }
        }
    }


    private fun clearSearch() {
        searchCoroutine.forEach {
            it.cancel(CancellationException("New Search initiated"))
        }

        searchCoroutine.clear()
    }


    companion object {
        const val TAG = "IconsViewModel"
    }

}
