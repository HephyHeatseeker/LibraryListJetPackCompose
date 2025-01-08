package viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import repository.Repository
import room.BookEntity

class BookViewModel(val repository: Repository) : ViewModel() {

    fun addBook(book : BookEntity){
        viewModelScope.launch(){
            repository.addBookToRoom(book)
        }
    }


    val books = repository.getAllBooks()

    fun deleteBook(book : BookEntity){
        viewModelScope.launch(){
            repository.deleteFromRoom(book)
        }
    }

    fun updateBook(book : BookEntity){
        viewModelScope.launch(){
            repository.updateBook(book)
        }
    }


}