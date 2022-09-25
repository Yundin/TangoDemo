package com.yundin.productlist.utils

import com.yundin.core.model.Product
import java.math.BigDecimal

fun createProduct(
    name: String = "",
    imageUrl: String = "",
    manufacturerName: String = "",
    price: BigDecimal = BigDecimal.ZERO
): Product {
    return Product(
        name = name,
        imageUrl = imageUrl,
        manufacturerName = manufacturerName,
        price = price
    )
}
