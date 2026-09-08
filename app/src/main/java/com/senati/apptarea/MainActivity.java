package com.senati.apptarea;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private ListView listViewTareas;
    private EditText etTitulo;
    private Spinner spinnerFiltro;
    private ArrayAdapter<Task> adapter;
    private List<Task> tareas;

    private final String[] OPCIONES_FILTRO = {
            "Todas", "pendiente", "en_progreso", "completada"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dbHelper = new DatabaseHelper(this);
        etTitulo = findViewById(R.id.etTitulo);
        listViewTareas = findViewById(R.id.listViewTareas);
        spinnerFiltro = findViewById(R.id.spinnerFiltro);
        Button btnAgregar = findViewById(R.id.btnAgregar);

        // Spinner de filtro
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, OPCIONES_FILTRO);
        spinnerFiltro.setAdapter(spinnerAdapter);

        spinnerFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                cargarTareas();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        cargarTareas();

        // Crear
        btnAgregar.setOnClickListener(v -> {
            String titulo = etTitulo.getText().toString().trim();
            if (titulo.isEmpty()) {
                Toast.makeText(this, "Escribe un título", Toast.LENGTH_SHORT).show();
                return;
            }
            Task nueva = new Task();
            nueva.setTitulo(titulo);
            nueva.setEstado(Task.ESTADO_PENDIENTE);
            nueva.setFechaCreacion(new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    .format(Calendar.getInstance().getTime()));
            dbHelper.insertTask(nueva);
            etTitulo.setText("");
            cargarTareas();
        });

        // Tap = cambiar estado
        listViewTareas.setOnItemClickListener((parent, view, position, id) -> {
            Task tarea = tareas.get(position);
            String nuevoEstado;
            if (Task.ESTADO_PENDIENTE.equals(tarea.getEstado())) {
                nuevoEstado = Task.ESTADO_EN_PROGRESO;
            } else if (Task.ESTADO_EN_PROGRESO.equals(tarea.getEstado())) {
                nuevoEstado = Task.ESTADO_COMPLETADA;
            } else {
                nuevoEstado = Task.ESTADO_PENDIENTE;
            }
            dbHelper.updateEstado(tarea.getId(), nuevoEstado);
            cargarTareas();
        });

        // Long-press = Editar / Eliminar
        listViewTareas.setOnItemLongClickListener((parent, view, position, id) -> {
            Task tarea = tareas.get(position);
            new AlertDialog.Builder(this)
                    .setTitle(tarea.getTitulo())
                    .setItems(new String[]{"Editar título", "Eliminar"}, (dialog, which) -> {
                        if (which == 0) {
                            mostrarDialogoEditar(tarea);
                        } else {
                            dbHelper.deleteTask(tarea.getId());
                            cargarTareas();
                        }
                    })
                    .show();
            return true;
        });
    }

    private void mostrarDialogoEditar(Task tarea) {
        EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(tarea.getTitulo());

        new AlertDialog.Builder(this)
                .setTitle("Editar título")
                .setView(input)
                .setPositiveButton("Guardar", (DialogInterface dialog, int which) -> {
                    String nuevoTitulo = input.getText().toString().trim();
                    if (!nuevoTitulo.isEmpty()) {
                        tarea.setTitulo(nuevoTitulo);
                        dbHelper.updateTask(tarea);
                        cargarTareas();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void cargarTareas() {
        String seleccion = OPCIONES_FILTRO[spinnerFiltro.getSelectedItemPosition()];
        if ("Todas".equals(seleccion)) {
            tareas = dbHelper.getAllTasks();
        } else {
            tareas = dbHelper.getTasksByEstado(seleccion);
        }
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tareas);
        listViewTareas.setAdapter(adapter);
    }
}