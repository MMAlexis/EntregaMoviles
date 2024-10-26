package com.example.evaluaciont2;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.evaluaciont2.entity.Products;
import com.example.evaluaciont2.service.UserService;
import com.example.evaluaciont2.util.ConnectionRest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {


    Spinner spnProductos;
    ArrayAdapter<String> adaptadorProductos;
    ArrayList<String> listaProductos = new ArrayList<String>();

    Button btnFiltrar;
    TextView txtResultado;

    //Servicio de usuario de retrofit
    UserService userService;

    List<Products> lstSalida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        adaptadorProductos = new ArrayAdapter<String>(
                this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                listaProductos);
        spnProductos = findViewById(R.id.spnProductos);
        spnProductos.setAdapter(adaptadorProductos);

        btnFiltrar = findViewById(R.id.btnFiltrar);
        txtResultado = findViewById(R.id.txtResultado);
        userService = ConnectionRest.getConnecion().create(UserService.class);

        cargaProductos();

        btnFiltrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idProductos = (int)spnProductos.getSelectedItemId();
                Products objProducts = lstSalida.get(idProductos);
                String salida = "Producto: ";
                salida += "Titulo : " + objProducts.getTitle() +"\n";
                salida += "Precio : " + objProducts.getPrice() +"\n";
                salida += "Descripcion : " + objProducts.getDescription() +"\n";
                salida += "Categoria => Id  : " + objProducts.getCategory().getId() +"\n";
                salida += "Categoria => Nombre  : " + objProducts.getCategory().getName()+"\n";
                salida += "Categoria => Imagen : " + objProducts.getCategory().getTypeImg()+"\n";
                txtResultado.setText(salida);

            }
        });
    }

    void cargaProductos(){
    Call<List< Products>> call = userService.listaProducts();
    call.enqueue(new Callback<List<Products>>() {
                     @Override
                     public void onResponse(Call<List<Products>> call, Response<List<Products>> response) {
                         if (response.isSuccessful()){
                             lstSalida = response.body();
                             for (Products obj: lstSalida){
                                 listaProductos.add(obj.getId() + " - " + obj.getTitle());
                             }
                             adaptadorProductos.notifyDataSetChanged();
                         }
                     }

                     @Override
                     public void onFailure(Call<List<Products>> call, Throwable t) {

                     }
                 }
    );


    }
}