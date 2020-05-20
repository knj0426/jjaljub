package com.knj.jjaljub.di

import androidx.room.Room
import com.knj.jjaljub.model.JjalDao
import com.knj.jjaljub.model.JjalDatabase
import com.knj.jjaljub.viewmodel.JjalCreateViewModel
import com.knj.jjaljub.viewmodel.JjalJubViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module

val viewModelModule = module {
    factory { JjalJubViewModel(get()) }
}

val jjalCreateViewModelModule = module {
    factory { JjalCreateViewModel(get())}
}

val roomModule = module {
    single { JjalDatabase.getInstance(androidApplication())}
    single(createOnStart = false) { get<JjalDatabase>().jjalDao()}
}