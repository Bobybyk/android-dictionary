package fr.uparis.learnVocabulary.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(primaryKeys = ["word", "sourceLanguage"])
data class Word(
    var word : String,
    var sourceLanguage: Language,
    var destinationLanguage: Language
    )