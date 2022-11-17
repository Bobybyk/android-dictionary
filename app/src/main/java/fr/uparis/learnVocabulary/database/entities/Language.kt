package fr.uparis.learnVocabulary.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Language (
    @PrimaryKey var lang : String
    )