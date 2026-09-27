package com.siren.homeproxy

/**
 * ┌─────────────────────────────────────────────────────────────┐
 * │  SIREN HOME PROXY — TARGET CONFIGURATION                   │
 * │                                                             │
 * │  Edit the two values below to point SIREN at any app.       │
 * │  Every component in the project reads from this file.       │
 * └─────────────────────────────────────────────────────────────┘
 */
object SirenConfig {

    /**
     * The package name of the app that SIREN keeps alive.
     * Example: "com.uncode.app"
     */
    const val TARGET_PACKAGE = "com.uncode.app"

    /**
     * The fully qualified Activity class to launch when the target is dead.
     * Example: "com.uncode.app.MainActivity"
     */
    const val TARGET_ACTIVITY = "com.uncode.app.MainActivity"
}
