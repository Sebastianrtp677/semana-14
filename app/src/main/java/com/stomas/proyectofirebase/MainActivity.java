package com.stomas.proyectofirebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    // Declaro variables
    private EditText txtCodigo, txtNombre, txtDueno, txtDireccion;
    private ListView lista;
    private Spinner spMascota;

    // Variable de la conexión a Firestore
    private FirebaseFirestore db;

    // Datos del spinner de tipos de mascotas
    String[] TiposMascotas = {"Perro", "Gato", "Pájaro"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializo Firestore
        db = FirebaseFirestore.getInstance();

        // Uno las variables con los Id del XML
        txtCodigo = findViewById(R.id.txtCodigo);
        txtNombre = findViewById(R.id.txtNombre);
        txtDueno = findViewById(R.id.txtDueno);
        txtDireccion = findViewById(R.id.txtDireccion);
        spMascota = findViewById(R.id.spMascota);
        lista = findViewById(R.id.lista);

        // Poblar Spinner de los tipos de mascotas
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, TiposMascotas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spMascota.setAdapter(adapter);

        // Llamamos al método que cargará la lista
        CargarListaFirestore();
    }

    // Método Enviar Datos (onClick del botón "Enviar datos")
    public void enviarDatosFirestore(View view) {
        // Obtenemos los campos ingresados en el formulario
        String codigo = txtCodigo.getText().toString().trim();
        String nombre = txtNombre.getText().toString();
        String dueno = txtDueno.getText().toString();
        String direccion = txtDireccion.getText().toString();
        String tipoMascota = spMascota.getSelectedItem().toString();

        // El código se usa como id del documento: no puede ir vacío
        if (codigo.isEmpty()) {
            Toast.makeText(this, "Ingrese un código", Toast.LENGTH_SHORT).show();
            return;
        }

        // Creamos un mapa con los datos a enviar
        Map<String, Object> mascota = new HashMap<>();
        mascota.put("codigo", codigo);
        mascota.put("nombre", nombre);
        mascota.put("dueño", dueno);
        mascota.put("direccion", direccion);
        mascota.put("tipoMascota", tipoMascota);

        // Enviamos los datos a Firestore
        db.collection("mascotas")
                .document(codigo)
                .set(mascota)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(MainActivity.this,
                            "Datos enviados a Firestore correctamente", Toast.LENGTH_SHORT).show();
                    CargarListaFirestore();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MainActivity.this,
                            "Error al enviar datos a Firestore: " + e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                });
    }

    // onClick del botón "Cargar datos"
    public void cargarDatos(View view) {
        CargarListaFirestore();
    }

    // Método Cargar Lista
    public void CargarListaFirestore() {
        // Hacemos una consulta a la colección llamada mascotas
        db.collection("mascotas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            // Lista para almacenar las cadenas de información de mascotas
                            List<String> listaMascotas = new ArrayList<>();

                            // Recorre todos los datos obtenidos ordenándolos en la lista
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String linea = "||" + document.getString("codigo") + "||" +
                                        document.getString("nombre") + "||" +
                                        document.getString("dueño") + "||" +
                                        document.getString("direccion");
                                listaMascotas.add(linea);
                            }
                            // Establece el adaptador en el ListView
                            ArrayAdapter<String> adaptador = new ArrayAdapter<>(MainActivity.this,
                                    android.R.layout.simple_list_item_1, listaMascotas);
                            lista.setAdapter(adaptador);
                        } else {
                            // Se imprimirá en consola si hay errores al traer los datos
                            Log.e("TAG", "Error al obtener datos de Firestore", task.getException());
                        }
                    }
                });
    }
}
