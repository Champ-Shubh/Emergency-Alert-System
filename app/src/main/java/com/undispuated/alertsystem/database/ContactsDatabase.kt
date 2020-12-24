package com.undispuated.alertsystem.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ContactsEntity::class], version = 1)
abstract class ContactsDatabase: RoomDatabase() {

    abstract fun contactsDao(): ContactsDao
}