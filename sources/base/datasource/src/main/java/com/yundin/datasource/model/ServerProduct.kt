package com.yundin.datasource.model

import com.google.gson.annotations.SerializedName
import com.yundin.core.model.Product

data class ServerProduct(
    @SerializedName("id")
    val id: String,
    @SerializedName("product_name")
    val productName: String,
    @SerializedName("gross_price")
    val grossPrice: String,
    @SerializedName("images")
    val images: List<ServerImage>,
    @SerializedName("manufacturer")
    val manufacturer: ServerManufacturer
) {
    fun toDomain(): Product = Product(
        name = productName,
        imageUrl = images.first().imageUrl,
        manufacturerName = manufacturer.name,
        price = grossPrice.toBigDecimal()
    )
}

data class ServerImage(
    @SerializedName("image")
    val imageUrl: String,
    @SerializedName("order")
    val order: Int
)

data class ServerManufacturer(
    @SerializedName("slug")
    val slug: String,
    @SerializedName("name")
    val name: String
)