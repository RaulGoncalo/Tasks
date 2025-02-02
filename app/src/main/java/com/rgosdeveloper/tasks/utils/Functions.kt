package com.rgosdeveloper.tasks.utils

import android.os.Build
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

fun formatDate(dateString: String): String {
    return try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val formatterOutput = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            // Verifica se a string contém "T", indicando que tem horário
            val localDate = if (dateString.contains("T")) {
                val formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                LocalDateTime.parse(dateString, formatterInput).toLocalDate()
            } else {
                val formatterInput = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                LocalDate.parse(dateString, formatterInput)
            }

            localDate.format(formatterOutput)
        } else {
            val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

            val inputFormat = if (dateString.contains("T")) {
                SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            } else {
                SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            }

            val date = inputFormat.parse(dateString)
            outputFormat.format(date ?: dateString) // Evita erro se a data for nula
        }
    } catch (e: Exception) {
        e.printStackTrace()
        dateString // Retorna a string original em caso de erro
    }
}

fun addDaysToDate(date: String, daysToAdd: Int): String {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val localDate = LocalDate.parse(date, formatter)
        val newDate = localDate.plusDays(daysToAdd.toLong())
        newDate.format(formatter)
    } else {
        // Para versões mais antigas
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val calendar = Calendar.getInstance()
            calendar.time = inputFormat.parse(date)
            calendar.add(Calendar.DAY_OF_MONTH, daysToAdd)

            val outputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            outputFormat.format(calendar.time)
        } catch (e: ParseException) {
            e.printStackTrace()
            date // Retorna a string original se houver erro
        }
    }
}