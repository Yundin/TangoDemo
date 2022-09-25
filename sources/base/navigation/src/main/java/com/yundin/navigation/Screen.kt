package com.yundin.navigation

sealed class Screen(val route: String) {
    object ProductList : Screen("product_list")
}
