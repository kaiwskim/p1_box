package com.example.gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val viewPager = findViewById<ViewPager>(R.id.viewPager)
        if(viewPager != null) {
            val adapter = ViewPageAdapter(supportFragmentManager)
            viewPager.adapter = adapter
            }
        }
    class ViewPageAdapter internal constructor(fm: FragmentManager) : FragmentPagerAdapter(fm){
        private val COUNT = 3

        override fun getCount(): Int {
            return COUNT
        }

        override fun getItem(position: Int): Fragment {
            var fragment = when(position) {
                0 -> FirstFragment().newInstance()
                1 -> SecondFragment().newInstance()
                else -> ThirdFragment().newInstance()
            }
            return fragment
        }

        override fun getPageTitle(position: Int): CharSequence? {
            val title = when(position){
                0 -> "contact"
                1 -> "album"
                else -> "morse"
            }
            return title
        }
    }
}
