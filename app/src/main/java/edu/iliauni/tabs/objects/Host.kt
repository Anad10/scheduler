package edu.iliauni.tabs.objects

import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import java.sql.Date
import java.sql.Time

open class Host: RealmObject{
    var id: Long = 0
    var firstName: String = ""
    var lastName: String = ""
}