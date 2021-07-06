package com.luizbcorrea.todolist.model

class ToDoItem {
    /*
       * objectId manterá o ID gerado pelo Firebase no modelo. Isso será usado para
            identificar exclusivamente o nosso item quando o mostrarmos em uma lista
       * itemText conterá o texto do item ToDo
       * done irá conter um valor booleano, se o item por fazer listado está completo ou não
     */

    companion object Factory {
        fun create(): ToDoItem = ToDoItem()
    }

    var objectId: String? = null
    var title: String? = null
    var description: String? = null
    var hour: String? = null
    var date: String? = null
    var done: Boolean? = false
}

