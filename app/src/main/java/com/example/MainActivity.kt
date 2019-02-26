package com.example

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.taskbar.*

class MainActivity : AppCompatActivity() {
  private var toolBarNavigationListenerIsRegistered = false
  private var searchView: SearchView? = null
  private var query: String? = null
  fun query() = query
  companion object {
    private const val QUERY = "QUERY"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    if (savedInstanceState != null) query = savedInstanceState.getString(QUERY)

    setContentView(R.layout.activity_main)
    setSupportActionBar(toolbar)
    getSupportActionBar()!!.setDisplayShowTitleEnabled(false)

    val fragment: Fragment
    var tmp: Fragment? = null
    if (savedInstanceState != null) {
      tmp = supportFragmentManager.findFragmentById(R.id.main_fragment_container)
      resolveUpButtonWithFragmentStack()
    }
    if (tmp==null) fragment = MainFragment()
    else fragment = tmp

    supportFragmentManager
        .beginTransaction()
        .replace(R.id.main_fragment_container, fragment)
        .commit()
  }

  override fun onNewIntent (intent: Intent) {
    if (Intent.ACTION_SEARCH == intent.action) {
      query = intent.getStringExtra(SearchManager.QUERY)
      if (query!=null) (supportFragmentManager.findFragmentById(R.id.main_fragment_container)
          as MainFragment).search(query!!)
    }
  }

  override fun onBackPressed() {
    val backStackCount = supportFragmentManager.backStackEntryCount
    if (backStackCount >= 1) {
      supportFragmentManager.popBackStack()
      // Change to hamburger icon if at bottom of stack
      if (backStackCount == 1) showUpButton(false)
    } else super.onBackPressed()
  }

  override fun onSaveInstanceState(outState: Bundle) {
    super.onSaveInstanceState(outState)
    if (searchView != null) {
      outState.putString(QUERY, searchView!!.query.toString())
    }
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.main_menu, menu)

    // Get the SearchView and set the searchable configuration
    val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
    searchView = menu.findItem(R.id.action_search).actionView as SearchView
    // Assumes current activity is the searchable activity
    searchView!!.setSearchableInfo(searchManager.getSearchableInfo(componentName))
    searchView!!.setIconifiedByDefault(false) // Do not iconify the widget; expand it by default
    searchView!!.post { searchView!!.setQuery(query, false) }

    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == android.R.id.home) onBackPressed()
    return super.onOptionsItemSelected(item)
  }

  private fun resolveUpButtonWithFragmentStack() {
    showUpButton(supportFragmentManager.backStackEntryCount > 0)
  }

  private fun showUpButton(show: Boolean) {
    // To keep states of ActionBar and ActionBarDrawerToggle synchronized,
    // when you enable on one, you disable on the other.
    // And as you may notice, the order for this operation is disable first, then enable
    if (show) {
      supportActionBar!!.setDisplayHomeAsUpEnabled(true)
      // when DrawerToggle is disabled i.e. setDrawerIndicatorEnabled(false), navigation icon
      // clicks are disabled i.e. the UP button will not work.
      // We need to add a listener, as in below, so DrawerToggle will forward
      // click events to this listener.
      if (!toolBarNavigationListenerIsRegistered) {
        toolBarNavigationListenerIsRegistered = true
      }
    } else {
      supportActionBar!!.setDisplayHomeAsUpEnabled(false)
      toolBarNavigationListenerIsRegistered = false
    }
  }

}
