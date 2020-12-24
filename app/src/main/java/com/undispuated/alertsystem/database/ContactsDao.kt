package com.undispuated.alertsystem.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ContactsDao {

    @Insert
    fun insertContact(contactsEntity: ContactsEntity)

    @Delete
    fun deleteContact(contactsEntity: ContactsEntity)

    @Query(value = "SELECT * FROM contacts WHERE id = :contactId")
    fun getContactById(contactId: String): ContactsEntity

    @Query(value = "SELECT * FROM contacts")
    fun getAllContacts(): List<ContactsEntity>
}