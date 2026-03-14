package br.com.fiap.axoeduc.dao

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import br.com.fiap.axoeduc.model.Cofrinho
import br.com.fiap.axoeduc.model.CredencialEmail
import br.com.fiap.axoeduc.model.CredencialGoogle
import br.com.fiap.axoeduc.model.Usuario

@Database(
    entities = [Usuario::class, Cofrinho::class, CredencialEmail::class, CredencialGoogle::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = AppDatabase.Migration2To3::class)
    ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDAO
    abstract fun cofrinhoDao(): CofrinhoDAO
    abstract fun credencialEmailDao(): CredencialEmailDAO
    abstract fun credencialGoogleDao(): CredencialGoogleDAO

    @DeleteColumn(tableName = "tb_usuario", columnName = "senha")
    class Migration2To3 : AutoMigrationSpec

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "cofrinho_db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}