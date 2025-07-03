package ch.abbts.application.dto

interface DTO <T> {

    fun toModel():T

    companion object{}
}