package com.nourfadaa.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nourfadaa.data.ChildRepository
import com.nourfadaa.domain.Section
import com.nourfadaa.ui.screens.ChildHomeScreen
import com.nourfadaa.ui.screens.ChildPickerScreen
import com.nourfadaa.ui.screens.SectionScreen

@Composable
fun NourNavHost(repo: ChildRepository) {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "picker") {

        composable("picker") {
            val vm: ChildPickerViewModel = viewModel(factory = VmFactory { ChildPickerViewModel(repo) })
            val children by vm.children.collectAsStateWithLifecycle()
            ChildPickerScreen(children = children, onPick = { nav.navigate("home/$it") })
        }

        composable(
            route = "home/{childId}",
            arguments = listOf(navArgument("childId") { type = NavType.LongType })
        ) { entry ->
            val childId = entry.arguments!!.getLong("childId")
            val vm: ChildHomeViewModel = viewModel(
                key = "home$childId",
                factory = VmFactory { ChildHomeViewModel(repo, childId) }
            )
            val child by vm.child.collectAsStateWithLifecycle()
            ChildHomeScreen(
                child = child,
                onSection = { nav.navigate("section/$childId/${it.id}") },
                onSwitchChild = { nav.popBackStack("picker", inclusive = false) }
            )
        }

        composable(
            route = "section/{childId}/{sectionId}",
            arguments = listOf(
                navArgument("childId") { type = NavType.LongType },
                navArgument("sectionId") { type = NavType.StringType }
            )
        ) { entry ->
            val childId = entry.arguments!!.getLong("childId")
            val sectionId = entry.arguments!!.getString("sectionId")!!
            val vm: SectionViewModel = viewModel(
                key = "section$childId$sectionId",
                factory = VmFactory { SectionViewModel(repo, childId, sectionId) }
            )
            val child by vm.child.collectAsStateWithLifecycle()
            val reward by vm.reward.collectAsStateWithLifecycle()
            SectionScreen(
                section = Section.fromId(sectionId),
                child = child,
                reward = reward,
                onCompleteDemo = vm::completeDemoLesson,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
