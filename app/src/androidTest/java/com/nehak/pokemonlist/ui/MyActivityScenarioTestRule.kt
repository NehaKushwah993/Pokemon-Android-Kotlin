package com.nehak.pokemonlist.ui

import android.app.Activity
import android.content.Intent
import androidx.test.core.app.ActivityScenario
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

/**
 * Created by Neha Kushwah on 10/9/21.
 * To perform manual launch of Activity
 */
class MyActivityScenarioTestRule<T : Activity> : TestRule {

    var scenario: ActivityScenario<T>? = null

    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                try {
                    base.evaluate()
                } finally {
                    scenario?.close()
                }
            }
        }
    }

    fun launchActivity(intent: Intent) {
        scenario = ActivityScenario.launch(intent)
    }
}