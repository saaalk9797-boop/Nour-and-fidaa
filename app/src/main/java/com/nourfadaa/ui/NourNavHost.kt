package com.nourfadaa.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nourfadaa.NourApp
import com.nourfadaa.data.ChildRepository
import com.nourfadaa.domain.Section
import com.nourfadaa.ui.screens.ChildHomeScreen
import com.nourfadaa.ui.screens.ChildPickerScreen
import com.nourfadaa.ui.screens.LessonListScreen
import com.nourfadaa.ui.screens.LessonScreen
import com.nourfadaa.ui.screens.SectionScreen

// الأقسام التي لها دروس حقيقية حاليًا. أضف هنا كل قسم جديد عند إضافة محتواه.
private val lessonSections = setOf(Section.ESSENTIALS, Section.PRAYER)

@Composable
fun NourNavHost(repo: ChildRepository) {
    val nav = rememberNavController()
    val content = (LocalContext.current.applicationContext as NourApp).content

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
                onSection = {
                    if (it in lessonSections) nav.navigate("lessons/$childId/${it.id}")
                    else nav.navigate("section/$childId/${it.id}")
                },
                onSwitchChild = { nav.popBackStack("picker", inclusive = false) }
            )
        }

        composable(
            route = "lessons/{childId}/{sectionId}",
            arguments = listOf(
                navArgument("childId") { type = NavType.LongType },
                navArgument("sectionId") { type = NavType.StringType }
            )
        ) { entry ->
            val childId = entry.arguments!!.getLong("childId")
            val sectionId = entry.arguments!!.getString("sectionId")!!
            val vm: LessonListViewModel = viewModel(
                key = "lessons$childId$sectionId",
                factory = VmFactory { LessonListViewModel(repo, content, childId, sectionId) }
            )
            val child by vm.child.collectAsStateWithLifecycle()
            val done by vm.done.collectAsStateWithLifecycle()
            val age = child?.ageYears ?: 0
            LessonListScreen(
                title = Section.fromId(sectionId).title,
                lessons = vm.lessons.filter { it.minAge <= age },
                completedIds = done,
                onOpen = { nav.navigate("lesson/$childId/$sectionId/$it") },
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            route = "lesson/{childId}/{sectionId}/{lessonId}",
            arguments = listOf(
                navArgument("childId") { type = NavType.LongType },
                navArgument("sectionId") { type = NavType.StringType },
                navArgument("lessonId") { type = NavType.StringType }
            )
        ) { entry ->
            val childId = entry.arguments!!.getLong("childId")
            val sectionId = entry.arguments!!.getString("sectionId")!!
            val lessonId = entry.arguments!!.getString("lessonId")!!
            val vm: LessonViewModel = viewModel(
                key = "lesson$childId$lessonId",
                factory = VmFactory { LessonViewModel(repo, content, childId, sectionId, lessonId) }
            )
            val child by vm.child.collectAsStateWithLifecycle()
            val reward by vm.reward.collectAsStateWithLifecycle()
            LessonScreen(
                lesson = vm.lesson,
                childName = child?.name ?: "",
                reward = reward,
                onCorrect = vm::complete,
                onBack = { nav.popBackStack() }
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
