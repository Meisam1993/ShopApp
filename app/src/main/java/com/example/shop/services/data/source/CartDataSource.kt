package com.example.shop.services.data.source

import android.os.Message
import com.example.shop.services.data.dataclasses.cart.AddToCartResponse
import com.example.shop.services.data.dataclasses.cart.CartItemCount
import com.example.shop.services.data.dataclasses.cart.CartListResponse
import io.reactivex.rxjava3.core.Single

interface CartDataSource {

    fun addToCart(productId: Int): Single<AddToCartResponse>

    fun getCartList(): Single<CartListResponse>

    fun removeCartItem(cartItemId: Int): Single<Message>

    fun changeCartItemCount(cartItemId: Int, count: Int): Single<AddToCartResponse>

    fun getCartItemsCount(): Single<CartItemCount>
}