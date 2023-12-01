package libetal.applications.assetor.models

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.io.File
import libetal.kotlin.laziest


class FolderViewModel(
    private val folder: File,
) : ViewModel(), Visitable<File> {

    override val visitors: MutableList<Visitor> by laziest {
        mutableListOf()
    }

    val path
        get() = folder.path

    val name
        get() = folder.name

    fun onClick() {

    }

    override fun visit() {

    }

    companion object {
        val TAG = "FolderViewModel"
    }

}
