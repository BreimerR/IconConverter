package libetal.applications.assetor.models

import libetal.kotlin.laziest

interface Visitable<T> {

    val visitors: MutableList<Visitor>

    abstract fun visit()

}
