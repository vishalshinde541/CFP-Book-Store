package com.example.bookstore.view

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.bookstore.R
import com.example.bookstore.databinding.FragmentHomeBinding
import com.example.bookstore.mode.Book
import com.example.bookstore.mode.BookAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var recyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var bookList: ArrayList<Book>
    private lateinit var tempArrayList: ArrayList<Book>
    lateinit var book: Array<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        (requireActivity() as AppCompatActivity).supportActionBar?.show()
        (activity as MainActivity).supportActionBar?.setTitle(R.string.home_title)
        (activity as MainActivity?)?.setDrawerUnlocked()

        firebaseAuth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        recyclerView = view.findViewById(R.id.recycler_home)
//        val gridlayout = GridLayoutManager(context, GridLayoutManager.VERTICAL)
//        recyclerView.layoutManager = gridlayout
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(2, GridLayoutManager.VERTICAL)
        recyclerView.layoutManager = staggeredGridLayoutManager

        bookList = arrayListOf<Book>()
        tempArrayList = arrayListOf<Book>()

        retrievBooksFromFirestoreAndStoreToBookList()

        return view
    }

    fun retrievBooksFromFirestoreAndStoreToBookList() {

        db.collection("books")
            .get().addOnCompleteListener {

                if (it.isSuccessful) {
                    for (document in it.result) {
                        val allBooks: Book = Book(
                            document["bookId"].toString(),
                            document["imageUrl"].toString(),
                            document["bookTitle"].toString(),
                            document["author"].toString(),
                            document["price"].toString()
                        )
                        bookList.add(allBooks)

                    }


                    tempArrayList.addAll(bookList)

                    recyclerView.adapter = BookAdapter(requireContext(), tempArrayList)
                }

            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), it.toString(), Toast.LENGTH_SHORT).show()
            }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item.itemId) {

            R.id.opt_cart -> {
                val currentUser = firebaseAuth.currentUser
                if (currentUser != null) {
                    val fragment = UserCartFragment()
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.fragmentsContainer, fragment)?.commit()
                    transaction?.addToBackStack(null)
                    true
                } else {

                    val fragmentManager = fragmentManager
                    val newFragment = UserNotLogedInDialogFragment()
                    newFragment.show(fragmentManager!!, "look")
                    true

                }

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

                tempArrayList.clear()
                val searchText = newText!!.lowercase(Locale.getDefault())
                if (searchText.isNotEmpty()) {

                    bookList.forEach {
                        if (it.bookTitle?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true ||
                            it.author?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true ||
                            it.price?.lowercase(Locale.getDefault())
                                ?.contains(searchText) == true
                        ) {

                            tempArrayList.add(it)
                        }
                    }
                    recyclerView.adapter!!.notifyDataSetChanged()
                } else {

                    tempArrayList.clear()
                    tempArrayList.addAll(bookList)
                    recyclerView.adapter!!.notifyDataSetChanged()
                }

                return false
            }

        })

        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        (activity as MainActivity?)?.setDrawerUnlocked()
        _binding = null
    }


}