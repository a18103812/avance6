package com.example.androidretrofitapi;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.androidretrofitapi.interfaces.ProductoApi;
import com.example.androidretrofitapi.models.Producto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText edtCodigo;
    TextView tvNombre,tvDescripcion,tvPrecio;
    ImageView imgProducto;
    Button btnBuscar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtCodigo=findViewById(R.id.edtCodigo);
        tvNombre=findViewById(R.id.tvNombre);
        tvDescripcion=findViewById(R.id.tvDescripcion);
        tvPrecio=findViewById(R.id.tvPrecio);
        imgProducto=findViewById(R.id.imgProducto);
        btnBuscar=findViewById(R.id.btnBuscar);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               find(edtCodigo.getText().toString());

               /*https://www.youtube.com/watch?v=kUzmksdC6ho  */
            }
        });
    }

    private void find( String codigo){
        Retrofit retrofit = new Retrofit.Builder().baseUrl("http://localhost:9993")
                .addConverterFactory(GsonConverterFactory.create()).build();

        ProductoApi productoApi=retrofit.create(ProductoApi.class);
        Call<Producto>call = productoApi.find(codigo);
        call.enqueue(new Callback<Producto>() {
            @Override
            public void onResponse(Call<Producto> call, Response<Producto> response) {
                try {
                       if (response.isSuccessful()){
                           Producto p = response.body();
                           String URL_IMG="http://localhost:9993/img/"+p.getPro_codigo()+".jpg";
                           tvNombre.setText(p.getPro_nombre());
                           tvDescripcion.setText(p.getPro_descripcion());
                           tvPrecio.setText(p.getPro_precio().toString());
                           Glide.with(getApplication()).load(URL_IMG).into(imgProducto);

                       }
                }catch (Exception ex){
                    Toast.makeText( MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Producto> call, Throwable t) {
                Toast.makeText( MainActivity.this, "error de coneion", Toast.LENGTH_SHORT).show();
            }

    });
    }
}