package com.example.bookstore.mode

data class UserCart(
    var bookId: String = "",
    var imageUrl: String = "",
    var bookTitle: String = "",
    var author: String = "",
    var price: String = "",
    var cartId: String = ""
)
