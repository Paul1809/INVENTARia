
package com.invenai.model

data class Product(
    val id: Int? = null,
    val name: String,
    val description: String? = null,
    val price: Double = 0.0,
    val stock: Int = 0,
    val sku: String? = null
)
