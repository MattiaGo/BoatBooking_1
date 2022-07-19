package com.example.boatbooking_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.boatbooking_1.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //view binding
    private  lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.ic_home -> replaceFragment(Home())
                R.id.ic_like -> replaceFragment(Preferiti())
                R.id.ic_search -> replaceFragment(Cerca())
                R.id.ic_message -> replaceFragment(Messaggi())
                R.id.ic_user -> replaceFragment(Account())
                else ->{}
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment){
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frameLayout, fragment)
            commit()
        }
    }
}