# Card Gacha RPG (Claw of Fate) — Project Skeleton

Auto-generated project structure based on `Projektplan_CardGachaRPG_1.pdf`.
All `.kt` files are **empty stubs** (package declaration + bare class/interface/object/composable
shell + a `// TODO` or owner comment) — ready for each person to fill in their area.

## Structure

- `core/` — shared model, database aggregator, DI, Compose theme. Changes need team agreement (Week 1–2 lock-in per plan).
- `feature/collection/` — **Person 1 (Leila)** — Kartensystem & Collection
- `feature/gacha/` — **Person 2 (Nico)** — Gacha-System & Währungen
- `feature/battle/` — **Person 3 (Marc)** — Formation & Kampfsimulation (`engine/` = pure Kotlin, no Android imports!)
- `feature/campaign/` — **Person 4 (Yassin)** — PvE-Kampagne
- `feature/arena/` — **Person 5 (Robin)** — Arena (PvP) & Dashboard-Hub
- `navigation/` — shared NavGraph wiring all screens together

Files marked with `⚠ PUBLIC CONTRACT` in their header comment correspond to the interfaces in
Section 5 ("Schnittstellenverträge") of the project plan — these are the ones other people depend on,
so keep signatures stable once agreed.

## Note

This is a minimal Gradle scaffold (not a full Android Studio project export) — you'll likely want to
open it in Android Studio and let it regenerate the Gradle wrapper (`gradlew`) and any missing
resource folders (`res/`, `mipmap/` icons, `styles.xml`) on first sync.
