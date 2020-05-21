package com.knj.jjaljub.di

import com.knj.jjaljub.model.JjalDatabase
import com.knj.jjaljub.viewmodel.JjalCreateViewModel
import com.knj.jjaljub.viewmodel.JjalJubViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module

val viewModelModule = module {
    factory { JjalJubViewModel(get()) }
}

val jjalCreateViewModelModule = module {
    factory { JjalCreateViewModel(get()) }
}

val roomModule = module {
    single { JjalDatabase.getInstance(androidApplication()) }
    single(createOnStart = false) { get<JjalDatabase>().jjalDao() }
}