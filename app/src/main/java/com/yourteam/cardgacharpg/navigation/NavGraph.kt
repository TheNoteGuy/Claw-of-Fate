package com.yourteam.cardgacharpg.navigation

// Wires all feature screens together — shared, coordinate before editing
// Owner: Person 5 (Robin) — zentraler Navigations-Hub
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yourteam.cardgacharpg.feature.arena.ui.ArenaScreen
import com.yourteam.cardgacharpg.feature.arena.ui.HomeScreen
import com.yourteam.cardgacharpg.feature.battle.ui.FormationEditorScreen
import com.yourteam.cardgacharpg.feature.campaign.ui.CampaignMapScreen
import com.yourteam.cardgacharpg.feature.collection.ui.CardDetailScreen
import com.yourteam.cardgacharpg.feature.collection.ui.CollectionScreen
import com.yourteam.cardgacharpg.feature.gacha.ui.GachaScreen

// Alle Routen
object Routes {
    const val HOME = "home"
    const val COLLECTION = "collection"
    const val CARD_DETAIL = "card_detail"
    const val GACHA = "gacha"
    const val FORMATION = "formation"
    const val CAMPAIGN = "campaign"
    const val ARENA = "arena"

}

// Home ist der Einstiegspunkt/Hub: von dort erreicht man alle Hauptbereiche.
// Zurück-Navigation läuft automatisch über den Compose-NavHost-Backstack (Hardware-/Gesten-Back).
@Composable
fun NavGraph(navController: NavHostController = rememberNavController()) {
    NavHost(navController = navController, startDestination = Routes.HOME) {

        composable(Routes.HOME) {
            HomeScreen(
                onOpenCollection = { navController.navigate(Routes.COLLECTION) },
                onOpenGacha = { navController.navigate(Routes.GACHA) },
                onOpenFormation = { navController.navigate(Routes.FORMATION) },
                onOpenCampaign = { navController.navigate(Routes.CAMPAIGN) },
                onOpenArena = { navController.navigate(Routes.ARENA) }
            )
        }

        // TODO (Person 1): cardId-Argument statt "_", sobald CardDetailScreen es annimmt
        composable(Routes.COLLECTION) {
            CollectionScreen(
                onCardClick = { _ -> navController.navigate(Routes.CARD_DETAIL) },
                onOpenGacha = { navController.navigate(Routes.GACHA) }
            )
        }

        composable(Routes.CARD_DETAIL) {
            CardDetailScreen()
        }

        composable(Routes.GACHA) {
            GachaScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.FORMATION) {
            FormationEditorScreen()
        }

        composable(Routes.CAMPAIGN) {
            CampaignMapScreen()
        }

        composable(Routes.ARENA) {
            ArenaScreen(onBack = { navController.popBackStack() })
        }
    }
}