package fr.uparis.learnVocabulary.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(
    primaryKeys = ["word", "sourceLanguage"],
    foreignKeys = [ForeignKey(
        entity = Language::class,
        parentColumns = arrayOf("lang"),
        childColumns = arrayOf("word"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Word(
    var word : String,
    var sourceLanguage: Language,
    var destinationLanguage: Language
    )