package com.yundin.core

import com.yundin.core.repository.ProductsRepository
import com.yundin.core.utils.ResourceProvider

interface ProductListDependencies {
    val productsRepository: ProductsRepository
    val resourceProvider: ResourceProvider
}