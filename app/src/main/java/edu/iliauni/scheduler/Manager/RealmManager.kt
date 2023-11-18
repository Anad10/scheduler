package edu.iliauni.scheduler.Manager

import edu.iliauni.scheduler.objects.*
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object RealmManager {
    val config = RealmConfiguration.Builder(schema = setOf(Attendee::class, Event::class, Host::class, UserDetail::class, Connection::class))
        .schemaVersion(10)
        .build()

    val realm: Realm by lazy {
        Realm.open(config)
    }
}