package com.weave.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.weave.app.ui.screens.gallery.GalleryScreen
import com.weave.app.ui.screens.materials.MaterialsScreen
import com.weave.app.ui.screens.workbench.WorkbenchScreen
import com.weave.app.ui.theme.WeaveTheme
import kotlinx.serialization.Serializable

/**
 * 导航目的地定义
 */
@Serializable
object WorkbenchDestination

@Serializable
object GalleryDestination

@Serializable
object MaterialsDestination

@Serializable
data class EditorDestination(val pageId: String)

/**
 * 织忆应用主入口
 */
@Composable
fun App() {
    WeaveTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val navController: NavHostController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = WorkbenchDestination
            ) {
                // 工作台首页
                composable<WorkbenchDestination> {
                    WorkbenchScreen(
                        onNavigateToGallery = {
                            navController.navigate(GalleryDestination)
                        },
                        onNavigateToMaterials = {
                            navController.navigate(MaterialsDestination)
                        },
                        onNavigateToEditor = {
                            // 导航到编辑器，传入当前页面ID
                            // navController.navigate(EditorDestination(pageId))
                        }
                    )
                }

                // 展览模式（书架）
                composable<GalleryDestination> {
                    GalleryScreen(
                        onJournalClick = { journalId ->
                            // 点击手账本后返回工作台
                            navController.popBackStack()
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

                // 素材库
                composable<MaterialsDestination> {
                    MaterialsScreen(
                        onMaterialSelect = { material ->
                            // 选择素材后返回工作台
                            navController.popBackStack()
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }

                // 页面编辑器
                composable<EditorDestination> { backStackEntry ->
                    val destination = backStackEntry.toRoute<EditorDestination>()
                    // PageEditorScreen(pageId = destination.pageId)
                }
            }
        }
    }
}
