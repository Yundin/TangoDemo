package com.yundin.datasource.repository.utils

import com.yundin.datasource.model.ServerImage
import com.yundin.datasource.model.ServerManufacturer
import com.yundin.datasource.model.ServerProduct

fun createServerProduct(
    id: String = "",
    productName: String = "",
    grossPrice: String = "0",
    images: List<ServerImage> = listOf(createServerImage()),
    manufacturer: ServerManufacturer = createServerManufacturer()
): ServerProduct {
    return ServerProduct(
        id = id,
        productName = productName,
        grossPrice = grossPrice,
        images = images,
        manufacturer = manufacturer
    )
}

fun createServerManufacturer(
    slug: String = "",
    name: String = ""
): ServerManufacturer {
    return ServerManufacturer(
        slug = slug,
        name = name
    )
}

fun createServerImage(
    imageUrl: String = "",
    order: Int = 0
): ServerImage {
    return ServerImage(
        imageUrl = imageUrl,
        order = order
    )
}