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

object DataStoreConstants {
    const val NAME_DATA_STORE = "tasks_preferences"
    const val USER_NAME = "user_name"
    const val USER_EMAIL = "user_email"
    const val USER_TOKEN = "user_token"
}

object MainConstants{
    const val ERROR_USER_NULL = "Erro ao buscar informações do usuário!"
    const val SUCCESS_SIGN_OUT_USER = "Logout realizado com sucesso!"

    const val KEY_PUT_EXTRA_FRAGMENT = "filter"

    const val TITLE_SIGN_OUT = "Deslogar?"
    const val MESSAGE_SIGN_OUT = "Deseja realmente sair?"
    const val TXT_NEGATIVE_BUTTON = "Cancelar"
    const val TXT_POSITIVE_BUTTON = "Sim"

}