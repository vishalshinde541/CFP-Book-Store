package com.example.bookstore.mode

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookstore.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class CartItemAdapter(private val context: Context, private val cartBookList : ArrayList<UserCart>)
    : RecyclerView.Adapter<CartItemAdapter.CartItemViewHolder>() {

    var bookFilterList = ArrayList<UserCart>()
    private var database: FirebaseFirestore
    private var firebaseAuth: FirebaseAuth

    init {
        bookFilterList = cartBookList
        database = FirebaseFirestore.getInstance()
        firebaseAuth = FirebaseAuth.getInstance()
    }


    class CartItemViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val bookImage : ImageView = itemView.findViewById(R.id.iv_book_CV)
        val bookTitle : TextView = itemView.findViewById(R.id.tv_booktitle_CV)
        val auther: TextView = itemView.findViewById(R.id.tv_authername_CV)
        val price: TextView = itemView.findViewById(R.id.tv_price_CV)
        val removeFromCartBtn: Button = itemView.findViewById(R.id.removeBook_CV)
        val placeOrderBtn: Button = itemView.findViewById(R.id.placeOrder_CV)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartItemViewHolder {
       val itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_list_item_view, parent, false)
        return CartItemAdapter.CartItemViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CartItemViewHolder, position: Int) {
        Glide.with(context).load(cartBookList[position].imageUrl).into(holder.bookImage)
        holder.bookTitle.text = cartBookList[position].bookTitle
        holder.auther.text = cartBookList[position].author
        holder.price.text = cartBookList[position].price
    }

    override fun getItemCount(): Int {
        return bookFilterList.size
    }
}