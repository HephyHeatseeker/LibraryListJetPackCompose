package repository

import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import room.BookEntity
import room.BooksDB

class Repository( val booksDB: BooksDB) {

    suspend fun addBookToRoom(bookEntity: BookEntity){
        booksDB.bookDAO().addBook(bookEntity)
    }


    fun getAllBooks() = booksDB.bookDAO().getAllBooks()



    suspend fun deleteFromRoom(bookEntity: BookEntity){
        booksDB.bookDAO().deleteBook(bookEntity)
    }

    suspend fun updateBook(bookEntity: BookEntity){
        booksDB.bookDAO().updateBook(bookEntity)
    }

}