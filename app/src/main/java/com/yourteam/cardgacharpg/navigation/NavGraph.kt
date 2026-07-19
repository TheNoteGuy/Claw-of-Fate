package com.yourteam.cardgacharpg.navigation

// Wires all feature screens together — shared, coordinate before editing
// Owner: Person 5 (Robin) — zentraler Navigations-Hub
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.yourteam.cardgacharpg.core.model.BattleLog
import com.yourteam.cardgacharpg.core.model.BattleSide
import com.yourteam.cardgacharpg.feature.arena.ui.ArenaScreen
import com.yourteam.cardgacharpg.feature.arena.ui.HomeScreen
import com.yourteam.cardgacharpg.feature.battle.ui.BattleResultScreen
import com.yourteam.cardgacharpg.feature.battle.ui.BattleScreen
import com.yourteam.cardgacharpg.feature.battle.ui.FormationEditorScreen
import com.yourteam.cardgacharpg.feature.campaign.ui.CampaignBattleScreen
import com.yourteam.cardgacharpg.feature.campaign.ui.CampaignMapScreen
import com.yourteam.cardgacharpg.feature.campaign.ui.PveBattleResultScreen
import com.yourteam.cardgacharpg.feature.collection.ui.CardDetailScreen
import com.yourteam.cardgacharpg.feature.collection.ui.CollectionScreen
import com.yourteam.cardgacharpg.feature.gacha.ui.GachaScreen

// Alle Routen
object Routes {
    const val HOME = "home"
    const val COLLECTION = "collection"
    const val CARD_DETAIL_ARG = "cardId"
    const val CARD_DETAIL = "card_detail/{$CARD_DETAIL_ARG}"
    fun cardDetail(cardId: Int) = "card_detail/$cardId"
    const val GACHA = "gacha"
    const val FORMATION = "formation"
    const val BATTLE = "battle" // NEU (Person 3): Testkampf aus dem Formations-Editor
    const val CAMPAIGN = "campaign"
    // NEU: echter Kampagnen-Kampf (Person 3+4). levelId als Nav-Argument.
    const val CAMPAIGN_BATTLE = "campaign_battle/{levelId}"
    fun campaignBattle(levelId: Int) = "campaign_battle/$levelId"
    // ⚠ ERWEITERT: Route enthaelt jetzt zusaetzlich die Belohnungen (gold/gems/potions),
    // damit der Result-Screen sie anzeigen kann, ohne den State erneut laden zu muessen.
    const val BATTLE_RESULT = "battle_result/{levelId}/{isVictory}/{stars}/{gold}/{gems}/{potions}"
    fun battleResult(levelId: Int, isVictory: Boolean, stars: Int, gold: Int, gems: Int, potions: Int) =
        "battle_result/$levelId/$isVictory/$stars/$gold/$gems/$potions"
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


        composable(Routes.COLLECTION) {
            CollectionScreen(
                onCardClick = { card -> navController.navigate(Routes.cardDetail(card.id)) },
                onOpenGacha = { navController.navigate(Routes.GACHA) }
            )
        }

        composable(
            route = Routes.CARD_DETAIL,
            arguments = listOf(navArgument(Routes.CARD_DETAIL_ARG) { type = NavType.IntType })
        ) {
            CardDetailScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.GACHA) {
            GachaScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.FORMATION) {
            FormationEditorScreen(
                onStartTestBattle = { navController.navigate(Routes.BATTLE) }
            )
        }

        // NEU (Person 3): Testkampf-Flow. Verwaltet intern, ob gerade der Kampf-Log oder
        // das Ergebnis angezeigt wird — vermeidet, den kompletten BattleLog über Nav-Argumente
        // serialisieren zu müssen (analog zu GachaScreen, das lastPull auch lokal hält).
        composable(Routes.BATTLE) {
            var result by remember { mutableStateOf<Pair<Boolean, BattleLog>?>(null) }
            val current = result

            if (current == null) {
                BattleScreen(
                    onBack = { navController.popBackStack() },
                    onFinished = { isVictory, log -> result = isVictory to log }
                )
            } else {
                BattleResultScreen(
                    isVictory = current.second.winner == BattleSide.PLAYER,
                    playerSurvivors = current.second.playerSurvivors,
                    playerTotalUnits = current.second.playerTotalUnits,
                    onContinue = { navController.popBackStack(Routes.FORMATION, inclusive = false) }
                )
            }
        }

        composable(Routes.CAMPAIGN) {
            CampaignMapScreen(
                onBack = { navController.popBackStack() },
                onStartBattle = { levelId ->
                    navController.navigate(Routes.campaignBattle(levelId))
                }
            )
        }

        // NEU: Kampagnen-Kampf — simuliert den Kampf gegen die Level-Gegner,
        // schreibt Sterne/Freischaltung/Belohnungen (CompleteCampaignLevelUseCase)
        // und leitet danach zum Ergebnis-Screen weiter.
        composable(
            route = Routes.CAMPAIGN_BATTLE,
            arguments = listOf(navArgument("levelId") { type = NavType.IntType })
        ) {
            CampaignBattleScreen(
                onBack = { navController.popBackStack() },
                onFinished = { outcome ->
                    navController.navigate(
                        Routes.battleResult(
                            levelId = outcome.levelId,
                            isVictory = outcome.isVictory,
                            stars = outcome.starsEarned,
                            gold = outcome.rewards.gold,
                            gems = outcome.rewards.gems,
                            potions = outcome.rewards.xpPotions
                        )
                    ) {
                        // Kampf-Screen vom Backstack entfernen: Zurueck vom Ergebnis
                        // fuehrt direkt zur Kampagnen-Karte, nicht erneut in den Kampf.
                        popUpTo(Routes.CAMPAIGN) { inclusive = false }
                    }
                }
            )
        }

        composable(
            route = Routes.BATTLE_RESULT,
            arguments = listOf(
                navArgument("levelId") { type = NavType.IntType },
                navArgument("isVictory") { type = NavType.BoolType },
                navArgument("stars") { type = NavType.IntType },
                navArgument("gold") { type = NavType.IntType },
                navArgument("gems") { type = NavType.IntType },
                navArgument("potions") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val args = backStackEntry.arguments
            val levelId = args?.getInt("levelId") ?: 0
            val isVictory = args?.getBoolean("isVictory") ?: false
            val stars = args?.getInt("stars") ?: 0

            PveBattleResultScreen(
                isVictory = isVictory,
                starsEarned = stars,
                levelId = levelId,
                goldReward = args?.getInt("gold") ?: 0,
                gemReward = args?.getInt("gems") ?: 0,
                xpPotionReward = args?.getInt("potions") ?: 0,
                onContinue = {
                    navController.popBackStack(Routes.CAMPAIGN, inclusive = false)
                }
            )
        }

        composable(Routes.ARENA) {
            ArenaScreen(onBack = { navController.popBackStack() })
        }
    }
}
