package repository

import model.UserEntry

interface Repository<T> {
    fun create(vararg params: Any): T
    fun readAll(): List<T>
    fun update(id: Int, entry: T)
    fun delete(id: Int)
}
