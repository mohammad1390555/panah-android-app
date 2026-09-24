package com.example

import android.app.Application
import com.example.data.database.AppDatabase
import com.example.data.repository.ApiKeyRepository
import com.example.data.repository.ChatRepository
import com.example.data.repository.WorkspaceRepository
import com.example.domain.usecase.AiCodingChatUseCase
import com.example.domain.usecase.ManageApiKeysUseCase
import com.example.domain.usecase.WorkspaceFileUseCase
import com.example.service.AiService
import com.example.service.FileManagerService

class VibeForgeApp : Application() {

    lateinit val database: AppDatabase
    lateinit val apiKeyRepository: ApiKeyRepository
    lateinit val chatRepository: ChatRepository
    lateinit val workspaceRepository: WorkspaceRepository
    lateinit val fileManagerService: FileManagerService
    lateinit val aiService: AiService
    
    lateinit val manageApiKeysUseCase: ManageApiKeysUseCase
    lateinit val workspaceFileUseCase: WorkspaceFileUseCase
    lateinit val aiCodingChatUseCase: AiCodingChatUseCase

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Database & Repositories
        database = AppDatabase.getDatabase(this)
        apiKeyRepository = ApiKeyRepository(database.apiKeyDao())
        chatRepository = ChatRepository(database.chatSessionDao(), database.chatMessageDao())
        workspaceRepository = WorkspaceRepository(database.pinnedFileDao(), database.snapshotDao())
        
        // Initialize Services
        fileManagerService = FileManagerService(this)
        aiService = AiService()
        
        // Initialize Use Cases
        manageApiKeysUseCase = ManageApiKeysUseCase(apiKeyRepository)
        workspaceFileUseCase = WorkspaceFileUseCase(fileManagerService, workspaceRepository)
        aiCodingChatUseCase = AiCodingChatUseCase(
            aiService = aiService,
            apiKeyRepository = apiKeyRepository,
            chatRepository = chatRepository,
            workspaceUseCase = workspaceFileUseCase,
            fileManagerService = fileManagerService
        )
    }
}
