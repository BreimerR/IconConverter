package libetal.applications.assetor.models

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import kotlinx.coroutines.Job
import libetal.kotlin.compose.narrator.lifecycle.ViewModel
import libetal.kotlin.io.File
import libetal.kotlin.laziest
import libetal.kotlin.log.info

class FolderViewModel<T : Any>(
    private val folder: File,
    private val foldersViewModels: SnapshotStateList<FolderViewModel<T>>,
    val level: Long = 0,
    private val claim: (File) -> T?
) : ViewModel() {

    val path
        get() = folder.path

    val name
        get() = folder.name

    val onClick = {
        var backStackCount = foldersViewModels.size
        foldersViewModels.add(this)

        while (--backStackCount >= level) {
            foldersViewModels.get(backStackCount).pause(1000)
            foldersViewModels.removeAt(backStackCount)
        }

    }

    constructor(
        path: String,
        foldersViewModels: SnapshotStateList<FolderViewModel<T>>,
        level: Long = 0,
        claim: (File) -> T?
    ) : this(
        File(path).also {
            if (!it.isDirectory) throw RuntimeException("Folder is invalid")
        },
        foldersViewModels,
        level,
        claim
    )

    val subFolders by laziest {
        mutableStateListOf<FolderViewModel<T>>().apply {
            ioLaunch {
                folder.listFiles()?.forEach { nullableFile ->
                    nullableFile?.let { nonNullFile ->
                        if (nonNullFile.isDirectory) launch {
                            add(FolderViewModel(nonNullFile, foldersViewModels, level + 1, claim))
                        }
                    }
                }
            }
        }
    }

    val items: State<SnapshotStateList<T>> by laziest {
        derivedStateOf {
            mutableStateListOf<T>().apply {
                ioLaunch {
                    folder.listFiles()?.forEach { nullableFile ->
                        nullableFile?.let { nonNullFile ->
                            claim(nonNullFile)?.let { claimed ->
                                launch {
                                    add(claimed)
                                }
                            }
                        }
                    }

                    subFolders.forEach { subFolder ->
                        subFolder.items.value.forEach { item ->
                            add(item)
                        }
                    }
                }
            }
        }
    }

    fun getAll(snapshotStateList: SnapshotStateList<T>): Job = ioLaunch {
        folder.listFiles()?.forEach { it ->
            if (it.isFile) claim(it)?.let { claimed ->
                launch {
                    snapshotStateList.add(claimed)
                }
            }
        }

        for (folder in subFolders) {
            ioLaunch {
                folder.getAll(snapshotStateList)
            }
        }
    }


    companion object {
        val TAG = "FolderViewModel"
    }
}
