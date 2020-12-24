package com.undispuated.alertsystem.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class ContactsEntity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "is_male") val isMale: Boolean,
    @ColumnInfo(name = "orig_name") val origName: String,
    @ColumnInfo(name = "nickname") val nickname: String,
    @ColumnInfo(name = "phone_num") val phoneNum: Int
)