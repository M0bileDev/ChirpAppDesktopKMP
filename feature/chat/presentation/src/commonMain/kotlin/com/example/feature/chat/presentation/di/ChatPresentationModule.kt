package com.example.feature.chat.presentation.di

import com.example.feature.chat.presentation.chat_detail.ChatDetailViewModel
import com.example.feature.chat.presentation.chat_list.ChatListViewModel
import com.example.feature.chat.presentation.chat_list_detail.ChatListDetailViewModel
import com.example.feature.chat.presentation.create_chat.CreateChatViewModel
import com.example.feature.chat.presentation.manage_chat.ManageChatViewModel
import com.example.feature.chat.presentation.profile.ProfileViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val chatPresentationModule = module {
    viewModelOf(::ChatListViewModel)
    viewModelOf(::ChatListDetailViewModel)
    viewModelOf(::CreateChatViewModel)
    viewModelOf(::ChatDetailViewModel)
    viewModelOf(::ManageChatViewModel)
    viewModelOf(::ProfileViewModel)
}