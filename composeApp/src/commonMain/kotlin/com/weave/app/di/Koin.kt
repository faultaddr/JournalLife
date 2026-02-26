package com.weave.app.di

import com.weave.app.data.repository.JournalRepository
import com.weave.app.data.repository.MaterialRepository
import com.weave.app.data.repository.PageRepository
import com.weave.app.ui.screens.gallery.GalleryViewModel
import com.weave.app.ui.screens.materials.MaterialsViewModel
import com.weave.app.ui.screens.workbench.WorkbenchViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

/**
 * Koin 模块定义
 */
val appModule = module {
    // Repository (临时实现，后续需要添加实际实现)
    // single<JournalRepository> { JournalRepositoryImpl(get()) }
    // single<PageRepository> { PageRepositoryImpl(get()) }
    // single<MaterialRepository> { MaterialRepositoryImpl(get()) }

    // ViewModels
    viewModel { WorkbenchViewModel(get(), get()) }
    viewModel { GalleryViewModel(get()) }
    viewModel { MaterialsViewModel(get()) }
}

/**
 * 初始化 Koin
 */
fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(appModule)
    }
}
