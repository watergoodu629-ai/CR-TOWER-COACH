package ai.ashna.crtowercoach

data class DetectedTroop(val name: String, val confidence: Float, val x: Float, val y: Float, val enemy: Boolean)

data class CoachAdvice(val title: String, val detail: String)

/** Replace this rule-based prototype with a bundled TFLite/ML Kit detector. */
class TroopDetector {
    fun detect(frame: Any): List<DetectedTroop> = emptyList()
}

class CoachingEngine {
    fun advise(troops: List<DetectedTroop>): CoachAdvice {
        val enemy = troops.filter { it.enemy }
        if (enemy.isEmpty()) return CoachAdvice("Watching", "No enemy troop confidently detected yet")
        val names = enemy.joinToString { it.name }
        return CoachAdvice("Defend now", "Detected $names. Place a counter in the opposite lane and save a spell.")
    }
}
