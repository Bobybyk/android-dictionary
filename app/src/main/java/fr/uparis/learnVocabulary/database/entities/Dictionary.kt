package fr.uparis.learnVocabulary.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["sourceLanguage", "destinationLanguage", "url"],
    foreignKeys = [ForeignKey(
        entity = Language::class,
        parentColumns = arrayOf("lang"),
        childColumns = arrayOf("url"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Dictionary(
    var sourceLanguage: Language,
    var destinationLanguage: Language,
    var url: String
)