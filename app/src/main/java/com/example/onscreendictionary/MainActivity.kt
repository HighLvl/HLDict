package com.example.onscreendictionary

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams
import android.view.WindowInsets
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.updatePadding
import com.example.onscreendictionary.ui.main.MainArgs
import com.example.onscreendictionary.ui.main.MainFragment

class MainActivity : AppCompatActivity(), View.OnApplyWindowInsetsListener {
    private var isSystemUiVisible: Boolean = true
    fun setSystemUiVisible(isVisible: Boolean) {
        isSystemUiVisible = isVisible
        updateSystemUiVisible()
    }

    private var isSystemUiDark: Boolean = false
    fun setSystemUiDark(isDark: Boolean) {
        isSystemUiDark = isDark
        updateSystemUiVisible()
    }

    private var fragment: MainFragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (!isTaskRoot && callingActivity?.className == MainActivity::class.qualifiedName) {
            // flag singleTop from context menu is not working
            // because starts activity for result
            // this is workaround
            val intent = intent
            setResult(RESULT_OK)
            finish()
            startActivity(intent)
            return
        }

        val view = FrameLayout(this)
        view.id = R.id.fragmentContainer
        view.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        view.setOnApplyWindowInsetsListener(this)
        setContentView(view)
        if (savedInstanceState == null) {
            val processTextExtra = intent?.extras?.getString(Intent.EXTRA_PROCESS_TEXT)
            fragment = MainFragment(MainArgs(processTextExtra))
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, fragment!!)
                .commitNowAllowingStateLoss()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val processTextExtra = intent?.extras?.getString(Intent.EXTRA_PROCESS_TEXT) ?: return
        fragment?.search(processTextExtra)
    }

    override fun onResume() {
        super.onResume()
        updateSystemUiVisible()
    }

    @SuppressLint("InlinedApi")
    private fun updateSystemUiVisible() {
        var systemUiVisibility = if (isSystemUiVisible) {
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        } else {
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        systemUiVisibility = if (isSystemUiDark) {
            systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
        }
        window.decorView.systemUiVisibility = systemUiVisibility
    }

    override fun onApplyWindowInsets(view: View, insets: WindowInsets): WindowInsets {
        view.updatePadding(
            top = insets.systemWindowInsetTop,
            bottom = insets.systemWindowInsetBottom
        )
        return insets
    }
}