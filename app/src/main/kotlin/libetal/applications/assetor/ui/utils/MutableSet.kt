package libetal.applications.assetor.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList

class MutableSet<T>(private val items: SnapshotStateList<T> = mutableStateListOf()) :
    MutableList<T> by items {

    override fun add(element: T): Boolean {
        return if (element in this) false
        else items.add(element)
    }

    override fun add(index: Int, element: T) {
        if (element !in this) items.add(index, element)
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        val itemsFiltered = elements.filter { it !in this }

        return items.addAll(index, itemsFiltered)
    }

    override fun addAll(elements: Collection<T>): Boolean {
        val itemsFiltered = elements.filter { it !in this }

        return items.addAll(itemsFiltered)
    }

}


fun <T> mutableStateSetOf(vararg items: T) = MutableSet(mutableStateListOf(*items))

@Composable
fun <T> rememberMutableStateSetOf(vararg items: T) = remember { mutableStateSetOf(*items) }
