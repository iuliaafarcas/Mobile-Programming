package com.example.recommendation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.recommendation.adapter.ItemAdapter
import com.example.recommendation.data.Datasource
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadFragment(HomeFragment())
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottomNav)

        bottomNav.setOnItemReselectedListener setOnNavigationItemReselectedListener@{
            when (it.itemId) {
                R.id.homeIcon -> {
                    loadFragment(HomeFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.createIcon -> {
                    loadFragment(CreateFragment())
                    return@setOnNavigationItemReselectedListener
                }
                R.id.filterIcon -> {
                    loadFragment(FilterFragment())
                    return@setOnNavigationItemReselectedListener
                }
            }
        }

    }
    private fun loadFragment(fragment: Fragment){
        Log.d("debug", "loadFragment: " + fragment.toString())
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }
}
