package com.example.onscreendictionary


import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup.LayoutParams
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.onscreendictionary.ui.main.MainArgs
import com.example.onscreendictionary.ui.main.MainFragment

class MainActivity : AppCompatActivity() {
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
        view.layoutParams =
            LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
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
}