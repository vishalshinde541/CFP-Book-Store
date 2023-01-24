package com.example.bookstore.mode

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.view.isGone
import androidx.core.view.isVisible
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

    class BookViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val bookImage : ImageView = itemView.findViewById(R.id.iv_book)
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

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        Glide.with(context).load(booklist[position].imageUrl).into(holder.bookImage)
        holder.bookTitle.text = booklist[position].bookTitle
        holder.auther.text = booklist[position].author
        holder.price.text = booklist[position].price

        holder.addToCartBtn.setOnClickListener {
            holder.wishlistBtn.visibility = View.GONE
            holder.addToCartBtn.visibility = View.GONE
            holder.addedToCartBtn.visibility = View.VISIBLE
            // And NEED TO ADD THE THAT BOOK TO CART ALSO
        }

        holder.wishlistBtn.setOnClickListener {
            holder.wishlistBtn.setBackgroundResource(R.color.white)
        }
    }

    override fun getItemCount(): Int {
        return bookFilterList.size
    }


}