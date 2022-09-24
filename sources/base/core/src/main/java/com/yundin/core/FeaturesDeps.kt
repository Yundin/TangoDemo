package com.yundin.core

import com.yundin.core.repository.ProductsRepository

interface ProductListDependencies {
    val productsRepository: ProductsRepository
}