package com.rgosdeveloper.tasks.utils

object AppConstants {
    // Strings relacionadas ao Navigation Drawer
    const val OPEN_NAV = "Abrir navegação"
    const val CLOSE_NAV = "Fechar navegação"
    const val LOGOUT_MESSAGE = "Saindo do app"
    const val EMPTY_INPUT_MESSAGE = "Por favor, insira um texto"

    const val KEY_PUT_EXTRA_FRAGMENT = "filter"

    // Filtros de tarefas
    const val FILTER_TODAY = "today"
    const val FILTER_TOMORROW = "tomorrow"
    const val FILTER_WEEK = "week"
    const val FILTER_MONTH = "month"

    const val DAY_TO_ADD_TOMORROW = 1
    const val DAY_TO_ADD_WEEK = 7
    const val DAY_TO_ADD_MONTH = 30
}

object ApiConstants {
    const val BASE_URL = "http://192.168.0.18:3000"
    const val QUERY_DATE = "date"
    const val PATH_ID = "id"

    // Endpoints
    object Endpoints {
        const val SIGN_UP = "/signup"
        const val SIGN_IN = "/signin"
        const val TASKS = "/tasks"
        const val TASKS_PUT = "/tasks/{id}/toggle"
        const val TASKS_DELETE = "/tasks/{id}"
    }
}

object DataStoreConstants {
    const val NAME_DATA_STORE = "tasks_preferences"
    const val USER_NAME = "user_name"
    const val USER_EMAIL = "user_email"
    const val USER_TOKEN = "user_token"
}

object MainConstants{
    const val ERROR_USER_NULL = "Erro ao buscar informações do usuário!"
    const val SUCCESS_SIGN_OUT_USER = "Logout realizado com sucesso!"
    const val TITLE_SIGN_OUT = "Deslogar?"
    const val MESSAGE_SIGN_OUT = "Deseja realmente sair?"
    const val TXT_NEGATIVE_BUTTON = "Cancelar"
    const val TXT_POSITIVE_BUTTON = "Sim"
}