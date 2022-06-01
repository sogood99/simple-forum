package com.example.anyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.anyapp.databinding.ActivityHomeBinding
import com.example.anyapp.util.Constants.Companion.USER_TOKEN
import com.example.anyapp.util.FeedType
import com.google.android.material.bottomsheet.BottomSheetBehavior

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Create binding to activity_home
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // put in feed fragment
        val pagerAdapter = BottomNavPagerAdapter(this)
        binding.fragPager.adapter = pagerAdapter

        // put in new tweet fragment
        val newTweetFragment = NewTweetFragment.newInstance()
        val transaction = getSupportFragmentManager().beginTransaction()
        transaction.replace(R.id.newTweet, newTweetFragment)
        transaction.addToBackStack("NewTweet")
        transaction.commit()
        // its bottomsheet style
        BottomSheetBehavior.from(binding.newTweetBottomSheet).apply {
            peekHeight = 100
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        // For selecting the Menu Items
        binding.homeToolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.miSearch -> {
                    Log.v("Pity", "Clicked Search")
                    true
                }
                R.id.miLogout -> {
                    USER_TOKEN = null
                    // reset adapter
                    val adapter = binding.fragPager.adapter
                    binding.fragPager.adapter = null
                    binding.fragPager.adapter = adapter
                    true
                }
                else -> true
            }
        }

        // For selecting the Home
        binding.homeButton.setOnClickListener { button ->
            Log.v("Pity", "Clicked Home Button")
        }

        // fragPager stuff: page change
        binding.fragPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // set the homeButton text when change position
                if (position == 0) {
                    binding.homeButton.text = "Home"
                } else if (position == 1) {
                    binding.homeButton.text = "Profile"
                }
            }
        })

        // setup botNavBar when clicked
//        binding.botNavBar.setOnMenuItemClickListener {
//            when (it.itemId) {
//                R.id.navHome -> {
//                    binding.fragPager.currentItem = 0
//                    true
//                }
//                R.id.navProfile -> {
//                    binding.fragPager.currentItem = 1
//                    true
//                }
//                else -> {
//                    false
//                }
//            }
//        }
    }


    private inner class BottomNavPagerAdapter(
        fa: FragmentActivity,
    ) : FragmentStateAdapter(fa) {
        private val NUM_PAGES = 2

        override fun getItemCount(): Int {
            return NUM_PAGES
        }

        override fun createFragment(position: Int): Fragment {
            return if (position == 0) {
                FeedFragment.newInstance(FeedType.Popular)
            } else {
                ProfileFragment.newInstance(0)
            }
        }
    }
}