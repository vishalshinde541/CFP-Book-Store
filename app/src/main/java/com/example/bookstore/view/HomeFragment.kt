package com.example.bookstore.view

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentHomeBinding
import com.example.bookstore.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class HomeFragment : Fragment() {

    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        (activity as MainActivity).supportActionBar?.setTitle(R.string.home_title)

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()



        return view
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {
            R.id.opt_cart -> {

                val fragment = UserCartFragment()
                val transaction = fragmentManager?.beginTransaction()
                transaction?.replace(R.id.fragmentsContainer,fragment)?.commit()
                transaction?.addToBackStack(null)
                true
            }
            R.id.opt_search -> {
                Toast.makeText(requireContext(), "Clicked on search", Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.homepage_opt_menu, menu)

        val item = menu.findItem(R.id.opt_search)
        val searchView = item?.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {

                return false
            }

        })

        return super.onCreateOptionsMenu(menu, inflater)
    }


}