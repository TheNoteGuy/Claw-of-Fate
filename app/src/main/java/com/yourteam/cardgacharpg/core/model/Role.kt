package com.yourteam.cardgacharpg.core.model

// Tank, Krieger, Ranger, Magier, Support
// Zielwahl-Regel im Kampf (Person 3): Tank/Krieger -> vorne; Ranger -> hinten zuerst;
// Magier -> Einzelziel; Support -> niedrigste HP im eigenen Team
enum class Role {
    TANK,
    KRIEGER,
    RANGER,
    MAGIER,
    SUPPORT
}