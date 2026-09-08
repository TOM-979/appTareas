package com.senati.apptarea;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "appTareas.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_TASKS = "tareas";
    public static final String COL_ID = "id";
    public static final String COL_TITULO = "titulo";
    public static final String COL_DESCRIPCION = "descripcion";
    public static final String COL_ESTADO = "estado";
    public static final String COL_FECHA_VENCIMIENTO = "fecha_vencimiento";
    public static final String COL_FECHA_CREACION = "fecha_creacion";
    public static final String COL_USUARIO_ASIGNADO = "usuario_asignado";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_TASKS + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_TITULO + " TEXT NOT NULL, " +
                COL_DESCRIPCION + " TEXT, " +
                COL_ESTADO + " TEXT NOT NULL DEFAULT 'pendiente', " +
                COL_FECHA_VENCIMIENTO + " TEXT, " +
                COL_FECHA_CREACION + " TEXT NOT NULL, " +
                COL_USUARIO_ASIGNADO + " TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
        onCreate(db);
    }

    public long insertTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        long id = db.insert(TABLE_TASKS, null, buildValues(task));
        db.close();
        return id;
    }

    public void deleteTask(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_TASKS, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void updateEstado(long id, String nuevoEstado) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_ESTADO, nuevoEstado);
        db.update(TABLE_TASKS, values, COL_ID + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null,
                COL_FECHA_CREACION + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
                task.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO)));
                task.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPCION)));
                task.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(COL_ESTADO)));
                task.setFechaVencimiento(cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA_VENCIMIENTO)));
                task.setFechaCreacion(cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA_CREACION)));
                task.setUsuarioAsignado(cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_ASIGNADO)));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }


    /** Actualiza TODOS los campos de una tarea existente (editar). */
    public int updateTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        int rows = db.update(TABLE_TASKS, buildValues(task), COL_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
        db.close();
        return rows;
    }

    /** Devuelve tareas filtradas por estado (null = todas). */
    public List<Task> getTasksByEstado(String estado) {
        List<Task> tasks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor;
        if (estado == null || estado.equals("Todas")) {
            cursor = db.query(TABLE_TASKS, null, null, null, null, null, COL_FECHA_CREACION + " DESC");
        } else {
            cursor = db.query(TABLE_TASKS, null, COL_ESTADO + " = ?",
                    new String[]{estado}, null, null, COL_FECHA_CREACION + " DESC");
        }

        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(cursor.getLong(cursor.getColumnIndexOrThrow(COL_ID)));
                task.setTitulo(cursor.getString(cursor.getColumnIndexOrThrow(COL_TITULO)));
                task.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow(COL_DESCRIPCION)));
                task.setEstado(cursor.getString(cursor.getColumnIndexOrThrow(COL_ESTADO)));
                task.setFechaVencimiento(cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA_VENCIMIENTO)));
                task.setFechaCreacion(cursor.getString(cursor.getColumnIndexOrThrow(COL_FECHA_CREACION)));
                task.setUsuarioAsignado(cursor.getString(cursor.getColumnIndexOrThrow(COL_USUARIO_ASIGNADO)));
                tasks.add(task);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return tasks;
    }

    private ContentValues buildValues(Task task) {
        ContentValues values = new ContentValues();
        values.put(COL_TITULO, task.getTitulo());
        values.put(COL_DESCRIPCION, task.getDescripcion());
        values.put(COL_ESTADO, task.getEstado());
        values.put(COL_FECHA_VENCIMIENTO, task.getFechaVencimiento());
        values.put(COL_FECHA_CREACION, task.getFechaCreacion());
        values.put(COL_USUARIO_ASIGNADO, task.getUsuarioAsignado());
        return values;
    }
}
