package com.example.onscreendictionary.ui.main

import android.os.Bundle
import android.view.View
import android.view.WindowInsetsAnimation
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsAnimationCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import com.example.onscreendictionary.App
import com.example.onscreendictionary.R
import com.example.onscreendictionary.databinding.MainFragmentBinding
import com.example.onscreendictionary.di.app.AppComponent
import com.example.onscreendictionary.di.main.MainComponent
import com.example.onscreendictionary.di.main.MainModule
import com.example.onscreendictionary.ui.base.BaseFragment
import kotlin.reflect.KClass

class MainFragment(
    override val args: MainArgs? = null
) : BaseFragment() {
    override val viewBinding: MainFragmentBinding by impl()
    override val viewModel: MainViewModel by impl()
    private val navigator by lazy(LazyThreadSafetyMode.NONE) {
        MainNavigator(childFragmentManager, R.id.fragmentContainer)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        viewModel.onCreate(savedInstanceState?.getParcelable("MainInstanceState"))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putParcelable("MainInstanceState", viewModel.onSaveInstanceState())
        super.onSaveInstanceState(outState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) = with(viewBinding) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.coordinator = object : MainCoordinator {
            override fun back() {
                requireActivity().finish()
            }
        }

        bottomNavigationView.setOnItemSelectedListener { item ->
            val section = MainBottomSection.getByMenuItemId(item.itemId) ?: MainBottomSection.Search
            viewModel.onBottomSectionClick(section)
            true
        }

        viewModel.section.observe(viewLifecycleOwner) { (section, route) ->
            if (bottomNavigationView.selectedItemId != section.menuItemId) {
                bottomNavigationView.selectedItemId = section.menuItemId
            }
            bottomNavigationView.isVisible =
                !(route is MainRoute.Search || route is MainRoute.FavoriteSearch)

            navigator.setRoute(route)
        }

    }

    fun search(query: String) {
        viewModel.search(query)
    }

    override fun onCreateComponent(parentComponentProvider: (KClass<*>) -> Any): MainComponent {
        return parentComponentProvider.find<AppComponent>()
            .mainComponentFactory()
            .create(MainModule())
    }
}