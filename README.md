# 🐾 Claw of Fate — Card Gacha RPG

Ein natives Android-Spiel (Kotlin + Jetpack Compose), das die typischen Bausteine eines
Gacha-Sammelkarten-RPGs vereint: Katzen-Helden ziehen, aufleveln, in Formationen aufstellen
und in deterministischen Auto-Battles gegen KI-Gegner (Kampagne & Arena) schicken.

Entstanden als Semesterprojekt "Mobile Technologien" — 5 Personen, 12 Wochen, MVVM,
Room-Persistenz, reines Kotlin für die Kampf-Engine.

> Abgabe: 20.07.2026 · Projektplan: `Projektplan_CardGachaRPG_1.pdf` · Game Design Doc: `Card_Gacha_RPG_GDD_v2_1.pdf`

---

## 🎮 Was macht man in diesem Spiel?

1. **Ziehen (Gacha):** Mit Gems im "Claw of Fate"-Banner Katzen-Helden ziehen (Single- oder
   10er-Pull), inkl. Pity-System, das ein Legendary garantiert.
2. **Sammeln & Leveln (Collection):** Gezogene Helden landen in der Collection, lassen sich
   filtern/sortieren und mit XP-Tränken aufleveln — Stats steigen nach einer festen Formel.
3. **Formation aufstellen (Formations-Editor):** Bis zu 6 Helden in einem 3×2-Grid (vordere/
   hintere Reihe) positionieren — das ist die Formation, mit der man in jeden Kampf zieht.
4. **Kämpfen:**
   - **Kampagne ("Der Dunkle Wald"):** 10 handgebaute PvE-Level inkl. Boss, Sterne-Bewertung,
     Freischaltung des nächsten Levels, Gold/Gems/XP-Tränke als Belohnung.
   - **Arena (PvP gegen KI):** Kampf gegen einen von 12 hardcodierten KI-Decks, Stats werden
     vorher auf eine feste Basislinie normiert ("Skill schlägt Grinding"), Trophäen & Ligen
     (Bronze/Silber/Gold) sowie ein wöchentlicher Belohnungs-Zyklus.
5. **Dashboard (Home):** Zentraler Hub mit Live-Werten (Gems/Gold), Formations-Vorschau,
   Kampagnen-Fortschritt und Trophäen-Stand — von hier aus erreicht man alle Bereiche.

Kämpfe sind **reine Simulationen**: Der `BattleSimulator` berechnet den kompletten Kampf in
einem Rutsch (deterministisch, kein Zufall während des Kampfes selbst) und liefert ein
`BattleLog`. Die UI spielt dieses Log danach nur noch Runde für Runde ab (mit 1×/2×-Speed und
Skip-Funktion) — Simulation ≠ Animation.

---

## 🧠 Die Kernsysteme im Detail

### Elemente & Rollen
- **4 Elemente:** Feuer, Natur, Wasser, Arkan — mit einem Schere-Stein-Papier-Zyklus
  (Feuer > Natur > Wasser > Feuer, je +25% Schaden) und einer Sonderregel Arkan vs. Arkan (+50%
  beidseitig).
- **5 Rollen:** Tank, Krieger, Ranger, Magier, Support — jede Rolle hat eine eigene
  Zielwahl-Logik im Kampf (Tank/Krieger greifen vorne an, Ranger zielt zuerst auf die hintere
  Reihe, Magier wählt fokussiert das schwächste Ziel, Support heilt den eigenen niedrigsten
  HP-Wert).
- **4 Seltenheiten:** Common → Rare → Epic → Legendary, jede mit eigenem Stat-Multiplikator
  und Maximallevel (20/30/40/50).

### Kampfformel (`DamageFormula`)
```
mitigation = DEF / (DEF + 100)
damage     = ATK * (1 - mitigation) * elementModifier * skillMultiplier
Minimum: 1 Schaden pro Angriff
```
Beispiel: 200 ATK gegen 100 DEF → 50% Mitigation → 100 Schaden.

### Kampfablauf (`BattleSimulator`)
1. **Initiative:** Alle Einheiten beider Seiten werden nach SPD absteigend sortiert (Gleichstand
   wird über die Karten-ID aufgelöst → garantiert deterministisch).
2. **Aktion:** Jede Einheit greift gemäß ihrer Rollen-Zielwahl an (oder heilt, bei Support).
3. **Fähigkeit:** Jede Aktion füllt einen Ladungsbalken; bei vollem Ladungsstand wird statt des
   Normalangriffs die aktive Fähigkeit mit 1,5×-Schadensmultiplikator ausgelöst.
4. **Rundendeckel:** Nach 30 Runden gewinnt die Seite mit dem höheren HP-Prozentanteil.

Das Ergebnis (`BattleLog`) enthält alle Runden/Aktionen sowie Formations-Snapshots, damit die
UI HP-Balken und den Kampfverlauf Schritt für Schritt nachspielen kann.

### Gacha & Pity (`GachaEngine`)
- Basis-Raten: Common 50% / Rare 30% / Epic 15% / Legendary 4%.
- **Soft-Pity** ab Pull 70: die Legendary-Chance steigt pro weiterem Pull um 5%.
- **Hard-Pity** bei Pull 90: garantiertes Legendary.
- Rein funktionale, deterministische Logik (gleicher Seed → gleiche Pulls) — dadurch mit einer
  10.000-Pull-Simulation testbar.
- Duplikate werden nicht als neue Karte, sondern als **Stack** (`count`-Feld) geführt.

### Leveling (`StatCalculator` / `LevelUpUseCase`)
```
currentStat = baseStat * rarity.statMultiplier * levelFactor(level)
levelFactor(level) = 1.0 + (level - 1) * 0.08      // +8% pro Level
```
Level-Ups kosten XP-Tränke (Kosten steigen linear pro Zielevel), die primär über
Kampagnen-Siege ins Inventar wandern.

### Stat-Normierung in der Arena (`StatNormalization`)
Damit die Arena nicht zum reinen Grind-Wettrennen wird, werden beide Seiten vor dem Kampf auf
eine feste Basislinie skaliert (700 HP / 100 ATK / 40 DEF / 50 SPD), multipliziert nur mit dem
Rarity-Faktor — das eigene Level fließt hier **nicht** ein.

---

## 🏗️ Architektur

- **Sprache/UI:** Kotlin, Jetpack Compose (Material 3), MVVM durchgängig (ViewModel hält
  State als `StateFlow`, Composables sind stateless).
- **Persistenz:** Room (SQLite) — je Feature eigene Entities/DAOs, zentral verdrahtet über
  Hilt (`DatabaseModule`, `RepositoryModule`).
- **Dependency Injection:** Dagger Hilt (`@HiltViewModel`, `@AndroidEntryPoint`).
- **Navigation:** Ein zentraler `NavGraph` (Compose Navigation) verbindet Home → Collection,
  Gacha, Formation, Kampagne, Arena.
- **Reine Kotlin-Module (kein Android-Import):** `BattleSimulator`, `DamageFormula`,
  `TurnOrder`, `GachaEngine`, `StatCalculator` — dadurch ohne Android-Emulator unit-testbar.

### Projektstruktur (Auszug)
```
core/
  model/        # Card, Element, Role, Rarity, BattleFormation, BattleLog, Skill …
  database/     # Room-Datenbank + TypeConverters
  di/           # Hilt-Module (Database, Repository)
  ui/theme/     # Farben, Typografie, Compose-Theme

feature/
  collection/   # Kartensammlung, Filter, Stat-Berechnung, Level-Up
  gacha/        # Pull-Engine, Pity-System, Held-Pool, Währung (Gems/Gold)
  battle/       # Formations-Editor, BattleSimulator, Kampf-Playback-UI
  campaign/     # PvE-Kampagne "Der Dunkle Wald" (10 Level + Boss)
  arena/        # PvP gegen KI, Trophäen/Liga-System, Dashboard/Home
```

### Team-Aufteilung (5 Personen, parallel arbeitend)
| Person | Bereich | Kernmodule |
|---|---|---|
| 1 (Leila) | Karten & Collection | `Card`, `StatCalculator`, `LevelUpUseCase`, `CardRepository` |
| 2 (Nico) | Gacha & Währung | `GachaEngine`, `HeroPool`, `CurrencyManager`, Pity-UI |
| 3 (Marc) | Kampf-Engine & Formation | `BattleSimulator`, `DamageFormula`, `TurnOrder`, Formations-Editor |
| 4 (Yassin) | PvE-Kampagne | `EnemyCatalog`, `EnemyFormationProvider`, `StarRatingUseCase`, Belohnungen |
| 5 (Robin) | Arena & Dashboard | `StatNormalization`, `TrophyManager`, `AiDeckPool`, `HomeScreen` |

Öffentliche Schnittstellen (z. B. `CardRepository`, `BattleSimulator.simulate()`,
`FormationDao`, `CurrencyDao`) sind als **Schnittstellenverträge** markiert und wurden ab
Woche 2 bewusst stabil gehalten, damit alle fünf Bereiche unabhängig voneinander entwickeln
konnten.

---

## ✅ Pflicht-Tests laut GDD

- Schadensformel-Unit-Test (Beispielrechnung: 200 ATK / 100 DEF → 100 Schaden)
- Pity-Kurven-Test mit 10.000 simulierten Pulls
- Determinismus-Test: gleiche Formation 1.000× simuliert → identisches Ergebnis
- Sterne-Vergabe-Logik (0 Verluste → 3 Sterne, 1–2 Verluste → 2 Sterne, sonst 1 Stern)
- Stat-Normierungs-Test (alle Seltenheiten korrekt auf Arena-Basislinie skaliert)
- Trophy-Delta- und Liga-Einstufungs-Tests
- UI-Tests: Gacha Happy Path, Formations-Editor (Tap-to-Place)

## ♿ Accessibility

- Element **und** Seltenheit sind zusätzlich zur Farbe über Symbol/Form erkennbar (z. B.
  Common = Kreis, Rare = Quadrat, Epic = Fünfeck, Legendary = Stern; Elemente über
  Unicode-Symbole ▲ ❋ ● ◆).
- Battle-Skip- und 2×-Speed-Button in der Kampf-Wiedergabe.

## 🚀 Setup

1. Projekt in Android Studio öffnen (Gradle synct automatisch).
2. Auf Emulator oder Gerät ausführen — `MainActivity` startet direkt den `NavGraph` mit dem
   `HomeScreen` als Einstiegspunkt.
3. Debug-Builds zeigen zusätzliche Debug-Werkzeuge auf dem Dashboard (Cooldown überspringen,
   Fake-Kampf für den Trophy-Test) — im Release-Build automatisch ausgeblendet
   (`BuildConfig.DEBUG`).

## 🔭 Mögliche Erweiterungen (Stretch-Features)

Laut Projektplan als optionale Erweiterungen nach dem MVP vorgesehen: Mythic-Seltenheit,
Kartenaufstieg via Scherben, ein täglicher Verlies-Modus, ein zweiter Kampagnenakt (Level
11–20), ein Battle-Pass-System sowie asynchrones Online-PvP (die deterministische
Kampf-Simulation ist dafür bereits vorbereitet).
