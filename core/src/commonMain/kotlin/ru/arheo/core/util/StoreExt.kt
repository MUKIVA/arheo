package ru.arheo.core.util

import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.mvikotlin.core.store.Store

class StoreHolder<T : Store<*, *, *>>(
    val store: T,
) : InstanceKeeper.Instance {
    override fun onDestroy() {
        store.dispose()
    }
}

@Suppress("UNCHECKED_CAST")
fun <T : Store<*, *, *>> InstanceKeeper.getStore(
    key: Any,
    factory: () -> T,
): T {
    val existing = get(key) as? StoreHolder<T>
    if (existing != null) return existing.store
    val store = factory()
    put(key, StoreHolder(store))
    return store
}
