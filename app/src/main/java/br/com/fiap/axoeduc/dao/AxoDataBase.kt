package br.com.fiap.axoeduc.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.com.fiap.axoeduc.model.Cofrinho

@Database(entities = [Cofrinho::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cofrinhoDao(): CofrinhoDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cofrinho_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}