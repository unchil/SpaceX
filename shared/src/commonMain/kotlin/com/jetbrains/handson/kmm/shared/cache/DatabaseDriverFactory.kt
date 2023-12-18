package com.jetbrains.handson.kmm.shared.cache

import com.squareup.sqldelight.db.SqlDriver
import com.jetbrains.handson.kmm.shared.entity.Links
import com.jetbrains.handson.kmm.shared.entity.Patch
import com.jetbrains.handson.kmm.shared.entity.RocketLaunch

expect class DatabaseDriverFactory {
    fun createDriver(): SqlDriver
}
