package che.codes.goodweather

import android.animation.LayoutTransition
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import che.codes.goodweather.core.ui.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        setupNavigation()

        collapseToolbar()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

    private fun setupNavigation() {
        val navController = findNavController(R.id.nav_host_fragment)

        appBarConfiguration = AppBarConfiguration(setOf(R.id.fragment_location_list), drawer_layout)

        setupActionBarWithNavController(navController, appBarConfiguration)

        nav_view.setupWithNavController(navController)
    }

    override fun expandToolbar() {
        toolbar.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        toolbar.layoutParams.let {
            it.height = resources.getDimension(R.dimen.action_bar_expanded_height).toInt()

            toolbar.layoutParams = it
        }
    }

    override fun collapseToolbar() {
        toolbar.layoutParams.let {
            it.height = resources.getDimension(R.dimen.action_bar_default_height).toInt()

            toolbar.layoutParams = it
        }
    }
}
