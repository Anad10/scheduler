package edu.iliauni.scheduler.Manager

import edu.iliauni.scheduler.objects.Attendee
import edu.iliauni.scheduler.objects.Event
import edu.iliauni.scheduler.objects.Host
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