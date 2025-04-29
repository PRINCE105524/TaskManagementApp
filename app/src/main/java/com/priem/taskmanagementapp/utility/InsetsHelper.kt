package com.priem.taskmanagementapp.utility

import android.view.View
import android.view.Window
import androidx.core.view.*

object InsetsHelper {

    /**
     * Call this in your Activity's onCreate after setContentView()
     *
     * @param window The window of the current activity.
     * @param rootView The root view where padding should be adjusted.
     * @param useEdgeToEdge true = full screen layout (transparent status/navigation bar), false = normal layout
     */
    fun setupWindowInsets(window: Window, rootView: View, useEdgeToEdge: Boolean) {
        WindowCompat.setDecorFitsSystemWindows(window, !useEdgeToEdge)

        if (useEdgeToEdge) {
            ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
                val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

                // You can fine-tune if you want nav bar at bottom only etc.
                view.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
                )
                insets
            }
        } else {
            // Clear manual paddings if you previously set them
            ViewCompat.setOnApplyWindowInsetsListener(rootView, null)
        }
    }
}
