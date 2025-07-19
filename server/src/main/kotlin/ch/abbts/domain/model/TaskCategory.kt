package ch.abbts.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TaskCategory {
    @SerialName("SHOPPING") SHOPPING,
    @SerialName("TRANSPORT") TRANSPORT,
    @SerialName("CLEANING") CLEANING,
    @SerialName("PETCARE") PETCARE,
    @SerialName("GARDENING") GARDENING,
    @SerialName("TUTORING") TUTORING,
    @SerialName("TECHHELP") TECHHELP,
    @SerialName("CHILDCARE") CHILDCARE,
    @SerialName("LANGUAGETANDEM") LANGUAGETANDEM,
    @SerialName("HOMEWORK") HOMEWORK,
    @SerialName("REPAIRS") REPAIRS,
    @SerialName("OTHERS") OTHERS,
}
