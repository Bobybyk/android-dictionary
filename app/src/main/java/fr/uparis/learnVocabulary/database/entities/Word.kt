package fr.uparis.learnVocabulary.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey


@Entity(
    primaryKeys = ["word", "sourceLanguage"],
    foreignKeys = [
        ForeignKey(
            entity = Language::class,
            parentColumns = arrayOf("lang"),
            childColumns = arrayOf("sourceLanguage"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Language::class,
            parentColumns = arrayOf("lang"),
            childColumns = arrayOf("destinationLanguage"),
            onDelete = ForeignKey.CASCADE
        )]
)
data class Word(
    var word : String,
    var sourceLanguage: String,
    var destinationLanguage: String
    )