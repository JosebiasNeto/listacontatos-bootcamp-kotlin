package com.everis.listadecontatos.helpers

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.everis.listadecontatos.feature.listacontatos.model.ContatosVO

class HelperDB(
    context: Context?
) : SQLiteOpenHelper(context, NOME_BANCO, null, VERSAO_ATUAL) {

    companion object {
        private val NOME_BANCO = "contato.db"
        private val VERSAO_ATUAL = 1
    }

    val TABLE_NAME = "contato"
    val COLUMNS_ID = "id"
    val COLUMNS_NOME = "nome"
    val COLUMNS_TELEFONE = "telefone"
    val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
    val CREATE_TALBE = "CREATE TABLE $TABLE_NAME (" +
            "$COLUMNS_ID INTEGER NOT NULL," +
            "$COLUMNS_NOME TEXT NOT NULL," +
            "$COLUMNS_TELEFONE TEXT NOT NULL," +
            "" +
            "PRIMARY KEY ($COLUMNS_ID AUTOINCREMENT)" +
            ")"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TALBE)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        if(oldVersion != newVersion){
            db?.execSQL(DROP_TABLE)
            //update na tabela ou criar nova tabela
        }
        onCreate(db)

    }

    fun buscarContatos(busca: String) : List<ContatosVO> {
        val db = readableDatabase ?: return mutableListOf()
        val lista = mutableListOf<ContatosVO>()
        val sql = "SELECT * FROM $TABLE_NAME WHERE $COLUMNS_NOME LIKE '%?%'"
        var cursor = db.rawQuery(sql, arrayOf(busca))
        if(cursor == null){
            db.close()
            return mutableListOf()
        }
        while (cursor.moveToNext()){
            var contato = ContatosVO(
                cursor.getInt(cursor.getColumnIndex(COLUMNS_ID)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_NOME)),
                cursor.getString(cursor.getColumnIndex(COLUMNS_TELEFONE))
            )
            lista.add(contato)
        }
        db.close()
        return lista
    }

    fun salvarContato(contato: ContatosVO){
        val db = writableDatabase ?: return

//        val sql = "INSERT INTO $TABLE_NAME ($COLUMNS_NOME, $COLUMNS_TELEFONE) VALUES (? , ?)"
//        var array = arrayOf(contato.nome, contato.telefone)

        var content = ContentValues()
        content.put(COLUMNS_NOME, contato.nome)
        content.put(COLUMNS_TELEFONE, contato.telefone)
        db.insert(TABLE_NAME, null, content)

//        db.execSQL(sql, array)

        db.close()
    }
}