package com.example.bookstore.mode

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class BookAdapter(private val context: Context, private val booklist: ArrayList<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    var bookFilterList = ArrayList<Book>()
    private var database: FirebaseFirestore
    private var firebaseAuth: FirebaseAuth

    init {
        bookFilterList = booklist
        database = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
    }

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val bookImage: ImageView = itemView.findViewById(R.id.iv_book)
        val bookTitle: TextView = itemView.findViewById(R.id.tv_booktitle)
        val auther: TextView = itemView.findViewById(R.id.tv_authername)
        val price: TextView = itemView.findViewById(R.id.tv_price)
        val addToCartBtn: Button = itemView.findViewById(R.id.addToCartBtn)
        val wishlistBtn: Button = itemView.findViewById(R.id.wishlistBtn)
        val addedToCartBtn: Button = itemView.findViewById(R.id.addedToCartBtn)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.book_item_view, parent, false)
        return BookViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        Glide.with(context).load(booklist[position].imageUrl).into(holder.bookImage)
        holder.bookTitle.text = booklist[position].bookTitle
        holder.auther.text = booklist[position].author
        holder.price.text = booklist[position].price

        holder.addToCartBtn.setOnClickListener {
            holder.wishlistBtn.visibility = View.GONE
            holder.addToCartBtn.visibility = View.GONE
            holder.addedToCartBtn.visibility = View.VISIBLE
            // And NEED TO SAVE THAT CHANGED VISIBILITY OF BUTTONS
            val cartBookId = database.collection("user").document(firebaseAuth.currentUser?.uid!!)
                .collection("cartItems").document().id

            val userCart = UserCart(
                booklist[position].bookId,
                booklist[position].imageUrl,
                booklist[position].bookTitle,
                booklist[position].author,
                booklist[position].price
            )

            val bookMap = hashMapOf(
                "bookId" to userCart.bookId,
                "imageUrl" to userCart.imageUrl,
                "bookTitle" to userCart.bookTitle,
                "author" to userCart.author,
                "price" to userCart.price,
                "cartId" to cartBookId
            )

            addToCart(cartBookId, bookMap)

        }

        holder.wishlistBtn.setOnClickListener {
            holder.wishlistBtn.setBackgroundColor(Color.YELLOW)
        }
    }

    private fun addToCart(cartBookId: String, bookMap: HashMap<String, String>) {

        val currentUserId = firebaseAuth.currentUser?.uid!!

        val docRef = database.collection("user").document(currentUserId)
        docRef.collection("cartItems").document(cartBookId).set(bookMap)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(context, "Book added to cart", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Failed to add to cart", Toast.LENGTH_SHORT).show()
                }
            }
    }

    override fun getItemCount(): Int {
        return bookFilterList.size
    }


}