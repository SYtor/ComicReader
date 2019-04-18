package ua.syt0r.comicreader.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds
import com.google.android.material.navigation.NavigationView
import ua.syt0r.comicreader.ComicApplication
import ua.syt0r.comicreader.R
import ua.syt0r.comicreader.database.entity.DbFile
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var file: DbFile

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as ComicApplication).component.inject(this)

        Toast.makeText(this, "Main Test ${file.path}", Toast.LENGTH_LONG).show()

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        val drawerLayout = findViewById<DrawerLayout>(R.id.drawer_layout)

        val appBarConfiguration = AppBarConfiguration(navigationView.menu, drawerLayout)

        setSupportActionBar(toolbar)
        toolbar.setupWithNavController(navController, appBarConfiguration)
        navigationView.setupWithNavController(navController)
        setupActionBarWithNavController(navController, appBarConfiguration)

        MobileAds.initialize(this, getString(R.string.ad_mob_app_id))
        findViewById<AdView>(R.id.ad_view).loadAd(AdRequest.Builder().build())

    }
}
