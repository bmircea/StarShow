package com.example.starshow

import android.graphics.Bitmap
import android.location.Location
import android.media.Image
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "observation")
data class Observation(
    @PrimaryKey val ID: Int,
    val name: String,
    val obsText: String) {

}