package com.example.onscreendictionary.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.ViewBinding
import com.example.onscreendictionary.di.base.BaseComponent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.lang.reflect.Method
import kotlin.properties.PropertyDelegateProvider
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

abstract class BaseFragment : Fragment(),
    PropertyDelegateProvider<Any, ReadOnlyProperty<Any, Any?>>,
    ReadOnlyProperty<Any, Any?> {
    var _args: Args? = null
    open val args: Args? = null

    private var viewBindingKClass: KClass<*>? = null
    private var _viewBinding: ViewBinding? = null
    abstract val viewBinding: ViewBinding? // optional for custom no viewBinding fragments
    private var viewModelKClass: KClass<*>? = null
    private var _viewModel: BaseViewModel? = null
    abstract val viewModel: BaseViewModel? // optional for simple no viewModel fragments
    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            back()
        }
    }

    open fun back() {
        viewModel?.back()
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T> impl(): PropertyDelegateProvider<Any, ReadOnlyProperty<Any, T>> {
        return this as PropertyDelegateProvider<Any, ReadOnlyProperty<Any, T>>
    }

    @Suppress("UNCHECKED_CAST")
    protected fun <T: Args> args(value: T?): PropertyDelegateProvider<Any, ReadOnlyProperty<Any, T>> {
        this._args = value
        saveOrRestoreArgs()
        return this as PropertyDelegateProvider<Any, ReadOnlyProperty<Any, T>>
    }

    override fun provideDelegate(
        thisRef: Any,
        property: KProperty<*>
    ): ReadOnlyProperty<Any, Any?> {
        when (property.name) {
            "viewBinding" -> viewBindingKClass = property.returnType.classifier as KClass<*>
            "viewModel" -> viewModelKClass = property.returnType.classifier as KClass<*>
        }
        return this
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Any? {
        return when (property.name) {
            "viewBinding" -> _viewBinding
            "viewModel" -> _viewModel
            "args" -> _args
            else -> IllegalArgumentException("Property ${property.name} is not supported by ${this::class.simpleName}.impl delegate")
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        saveOrRestoreArgs()
        _viewModel = onProvideViewModel()
        viewModel?.attach()
    }

    private fun saveOrRestoreArgs() {
        val args = _args
        var arguments = arguments
        if (args != null) {
            if (arguments == null) {
                arguments = Bundle()
                this.arguments = arguments
            } else {
                if (arguments.containsKey("args")) {
                    return
                }
            }
            arguments.putParcelable("args", args)
        } else {
            if (arguments?.containsKey("args") == true) {
                _args = arguments.getParcelable("args")
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewBinding = onCreateViewBinding(inflater, container)
        if (viewBinding != null) {
            _viewBinding = viewBinding
            return viewBinding.root
        }
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            onBackPressedCallback
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _viewBinding = null
    }

    open fun onCreateViewBinding(inflater: LayoutInflater, container: ViewGroup?): ViewBinding? {
        val viewBindingKClass = viewBindingKClass ?: return null
        if (viewBindingKClass != ViewBinding::class) {
            var viewBindingFactory: Any? = viewBindingFactoryCache[viewBindingKClass]
            if (viewBindingFactory !== NO_VIEW_BINDING_FACTORY) {
                if (viewBindingFactory == null) {
                    viewBindingFactory = viewBindingKClass.java.getMethod(
                        "inflate",
                        LayoutInflater::class.java,
                        ViewGroup::class.java,
                        Boolean::class.java
                    )
                    viewBindingFactoryCache[viewBindingKClass] = viewBindingFactory
                }
                viewBindingFactory as Method
                return viewBindingFactory.invoke(null, inflater, container, false) as ViewBinding
            }
        }
        return null
    }

    open fun onProvideViewModel(): BaseViewModel? {
        val viewModelKClass = viewModelKClass ?: return null

        @Suppress("UNCHECKED_CAST")
        val clazz = (viewModelKClass as KClass<out BaseViewModel>).java
        val factory = ViewModelFactory(this)
        return ViewModelProvider(this, factory)[clazz]
    }

    // override for fragment-scoped component
    open fun onCreateComponent(parentComponentProvider: (KClass<*>) -> Any): BaseComponent? {
        return null
    }

    protected inline fun <reified T : Any> ((KClass<*>) -> Any).find(): T {
        return invoke(T::class) as T
    }

    inline fun <T> Flow<T>.collectOnUI(crossinline action: (T) -> Unit): Job {
        return viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main.immediate) {
            collect { data ->
                action(data)
            }
        }
    }

    companion object {
        private val viewBindingFactoryCache: MutableMap<KClass<*>, Any> = mutableMapOf()
        private val NO_VIEW_BINDING_FACTORY = Any()
    }
}