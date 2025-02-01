package com.rgosdeveloper.tasks.utils

object AppConstants {
    // Strings relacionadas ao Navigation Drawer
    const val OPEN_NAV = "Abrir navegação"
    const val CLOSE_NAV = "Fechar navegação"
    const val LOGOUT_MESSAGE = "Saindo do app"
    const val EMPTY_INPUT_MESSAGE = "Por favor, insira um texto"

    // Filtros de tarefas
    const val FILTER_TODAY = "today"
    const val FILTER_TOMORROW = "tomorrow"
    const val FILTER_WEEK = "week"
    const val FILTER_MONTH = "month"
}

object ApiConstants {
    const val BASE_URL = "http://192.168.0.18:3000/"

    // Endpoints
    object Endpoints {
        const val SIGN_UP = "signup"
        const val SIGN_IN = "signin"
    }
}