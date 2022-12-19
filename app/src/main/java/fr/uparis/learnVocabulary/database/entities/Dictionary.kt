package fr.uparis.learnVocabulary.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index

@Entity(
    indices = [Index("sourceLanguage"), Index("destinationLanguage")],
    primaryKeys = ["url", "sourceLanguage", "destinationLanguage"],
    foreignKeys = [
        ForeignKey(
            entity = Language::class,
            parentColumns = ["lang"],
            childColumns = ["sourceLanguage"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Language::class,
            parentColumns = ["lang"],
            childColumns = ["destinationLanguage"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Dictionary(
    val url : String,
    val sourceLanguage: String,
    val destinationLanguage: String
)