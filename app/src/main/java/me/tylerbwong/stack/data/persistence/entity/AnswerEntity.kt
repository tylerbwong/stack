package me.tylerbwong.stack.data.persistence.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "answer", foreignKeys = [
    ForeignKey(entity = AnswerEntity::class, parentColumns = ["answerId"], childColumns = ["owner"], onDelete = ForeignKey.CASCADE),
    ForeignKey(entity = AnswerEntity::class, parentColumns = ["answerId"], childColumns = ["lastEditor"], onDelete = ForeignKey.CASCADE)])
data class AnswerEntity(
        val answerId: Int,
        val isAccepted: Boolean
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}
