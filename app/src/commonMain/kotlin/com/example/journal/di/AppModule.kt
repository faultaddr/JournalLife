package com.example.journal.di

import com.example.journal.repository.InMemoryJournalRepository
import com.example.journal.repository.JournalRepository
import com.example.journal.viewmodel.JournalViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single<JournalRepository> { InMemoryJournalRepository() }
    viewModel<JournalViewModel> { JournalViewModel(get()) }
}