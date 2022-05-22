package com.yundin.core.model

import java.math.BigDecimal

data class Product(
    val name: String,
    val imageUrl: String,
    val manufacturerName: String,
    val price: BigDecimal
)
