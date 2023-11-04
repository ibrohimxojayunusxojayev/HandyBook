package com.example.dorixona.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.dorixona.R
import com.example.dorixona.ui.Saved
import com.example.dorixona.ui.Search
import com.example.dorixona.ui.Settings
import com.example.dorixona.databinding.FragmentMainBinding
import com.example.dorixona.model.User
import com.example.dorixona.util.ShPHelper
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import nl.joery.animatedbottombar.AnimatedBottomBar

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Main.newInstance] factory method to
 * create an instance of this fragment.
 */
class Main : Fragment(), NavigationView.OnNavigationItemSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var drawerLayout: DrawerLayout? = null
    private var bottomBar: AnimatedBottomBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater, container, false)
        var user = ShPHelper.getInstance(requireContext()).getUser()
        val bundle = Bundle()


        drawerLayout = binding.drawerLayout
        val toolbar = binding.toolbar
        val navigationView = binding.navView
        bottomBar = binding.bottomBar

        val header = binding.navView.getHeaderView(0)
        header.findViewById<TextView>(R.id.header_title).text = user.fullname
        header.findViewById<TextView>(R.id.sub_title).text = user.username

        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        navigationView.setNavigationItemSelectedListener(this)

        val toggle = ActionBarDrawerToggle(
            requireActivity(), binding.drawerLayout, toolbar, R.string.open_nav,
            R.string.close_nav
        )
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
//        if (savedInstanceState == null) {
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.container, Home()).commit();
//            navigationView.setCheckedItem(R.id.nav_home);
//        }
        loadFragment(Home())
        binding.avatar.setOnClickListener {
            findNavController().navigate(R.id.action_main_to_personalFragment, bundle)
        }
        bottomBar!!.setOnTabSelectListener(object : AnimatedBottomBar.OnTabSelectListener {
            override fun onTabSelected(
                lastIndex: Int,
                lastTab: AnimatedBottomBar.Tab?,
                newIndex: Int,
                newTab: AnimatedBottomBar.Tab
            ) {
                when (newIndex) {
                    0 -> {
                        loadFragment(Home())
                        toolbar.title = "Bosh Sahifa"
                        toolbar.setTitleTextAppearance(
                            requireContext(),
                            R.style.MontserratSemiBoldTextAppearance
                        )
                    }

                    1 -> {
                        loadFragment(Search())
                        toolbar.title = "Qidiruv"
                        toolbar.setTitleTextAppearance(
                            requireContext(),
                            R.style.MontserratSemiBoldTextAppearance
                        )
                    }

                    2 -> {
                        loadFragment(Feather())
                        toolbar.title = "Izohlar"
                        toolbar.setTitleTextAppearance(
                            requireContext(),
                            R.style.MontserratSemiBoldTextAppearance
                        )
                    }

                    3 -> {
                        loadFragment(Saved())
                        toolbar.title = "Saqlanganlar"
                        toolbar.setTitleTextAppearance(
                            requireContext(),
                            R.style.MontserratSemiBoldTextAppearance
                        )
                    }

                    4 -> {
                        loadFragment(Settings())
                        toolbar.title = "Sozlamalar"
                        toolbar.setTitleTextAppearance(
                            requireContext(),
                            R.style.MontserratSemiBoldTextAppearance
                        )
                    }
                }
            }

        })

        return binding.root
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                loadFragment(Home())
            }

            R.id.nav_settings -> {
                loadFragment(Settings())
            }

            R.id.nav_saved -> {
                loadFragment(Saved())
            }

            R.id.nav_feather -> {
                loadFragment(Feather())

            }

            R.id.nav_search -> {
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container, Search()).commit()

            }

            R.id.nav_logout -> {
                var user = User("",-1,"","")
                ShPHelper.getInstance(requireContext()).setUser(user)
                findNavController().navigate(R.id.action_main_to_splash)
            }
        }
        drawerLayout!!.closeDrawer(GravityCompat.START)
        return true
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.commit()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Main.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Main().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}