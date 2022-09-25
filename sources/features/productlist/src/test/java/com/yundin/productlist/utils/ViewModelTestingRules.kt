package com.yundin.productlist.utils

import org.junit.rules.RuleChain

fun viewModelTestingRules(): RuleChain =
    RuleChain
        .outerRule(MainCoroutineDispatcherRule())