package com.example.onscreendictionary.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.onscreendictionary.App
import com.example.onscreendictionary.di.base.BaseComponent
import kotlin.reflect.KClass

class ViewModelFactory(
    private val fragment: BaseFragment
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val component = getComponent()
        val providers = component.viewModelProviders()
        val provider = providers[modelClass] ?: throw IllegalArgumentException(
            "Cannot get provider for $modelClass for ${fragment.javaClass}"
        )
        val viewModel = provider.get() as T
        (viewModel as BaseViewModel).component = component
        viewModel._args = fragment._args
        return viewModel
    }

    private fun getComponent(): BaseComponent {
        return fragment.onCreateComponent(::findParentComponent)
            ?: findParentComponent(null) as BaseComponent
    }

    private fun findParentComponent(kClass: KClass<*>?): Any {
        var parent = fragment.parentFragment
        while (parent != null) {
            val component = (parent as? BaseFragment)?.viewModel?.component
            if (component != null && (kClass == null || component::class == kClass)) {
                return component
            }
            parent = parent.parentFragment
        }
        return (fragment.requireContext().applicationContext as App).appComponent
    }
}