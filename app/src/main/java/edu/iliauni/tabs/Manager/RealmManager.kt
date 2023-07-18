package edu.iliauni.tabs.Manager

import edu.iliauni.tabs.objects.Attendee
import edu.iliauni.tabs.objects.Event
import edu.iliauni.tabs.objects.Host
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

object RealmManager {
    val config = RealmConfiguration.Builder(schema = setOf(Attendee::class, Event::class, Host::class))
        .schemaVersion(2)
        .build()

    val realm: Realm by lazy {
        Realm.open(config)
    }
}