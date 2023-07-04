package com.example.palabras;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.palabras.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private int posDel = -1;
    ArrayList<Palabras> palabra;
    RecyclerView rv;
    EditText etWord;
    Button bAdd, bDel,bEdit;
    AdaptadorPalabra ap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        rv= binding.rv;
        etWord = binding.et1;
        bAdd = binding.btnAdd;
        bDel = binding.btnDel;
        bEdit= binding.btnEdit;

        palabra = new ArrayList<Palabras>();
        for(int i=0;i < 20;i++){
            palabra.add(new Palabras("Palabra " + (i+1)));
        }
        LinearLayoutManager l = new LinearLayoutManager(this);
        rv.setLayoutManager(l);
        ap= new AdaptadorPalabra();
        rv.setAdapter(ap);

        DividerItemDecoration did = new DividerItemDecoration(this,l.getOrientation());
        rv.addItemDecoration(did);



        bDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar(v);
                posDel=-1;
            }
        });

        bAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agregar(v);
            }
        });

        bEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar(v);
            }
        });
    }

    public void eliminar(View v){
        if(posDel != -1 && posDel< palabra.size()){
            Toast.makeText(this,"[** DELETED ** ] " + palabra.get(posDel).getWord() + " Eliminado",Toast.LENGTH_SHORT).show();
            palabra.remove(posDel);
            etWord.setText("");
            ap.notifyDataSetChanged();

        }else{
            Toast.makeText(this,"[** WARNING ** ] ¡Ups! No seleccionaste lo que deseas Eliminar!",Toast.LENGTH_SHORT).show();

        }
    }
    public void editar (View v){
        String oracion = etWord.getText().toString().trim();
        etWord.setError(null);
        if (!oracion.isEmpty()){
           if (posDel>=0 && posDel < palabra.size()){
               palabra.get(posDel).setWord(oracion);
               ap.notifyDataSetChanged();
               rv.scrollToPosition(posDel);
               Toast.makeText(this,"[** EDIT ** ] Palabra editada",Toast.LENGTH_SHORT).show();
               posDel=-1;
               etWord.setText("");
           }

        }else{
            if (posDel != -1) {
                Toast.makeText(this, "[** EMPTY ** ] Debes ingresar un texto para editar!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "[** INVALID ** ] Debes Seleccionar una palabra para editar!", Toast.LENGTH_SHORT).show();

            }
            etWord.setError("No encontre texto!");
        }
    }


    public void agregar(View v){
        posDel=-1; //Aprovechamos de limpiar si quedo alguna seleccion anterior
        String oracion = etWord.getText().toString().trim();
        etWord.setError(null);
        if (!oracion.isEmpty()){
            Palabras p1 = new Palabras(oracion);
            palabra.add(p1);
            etWord.setText("");
            ap.notifyDataSetChanged();
            rv.scrollToPosition(palabra.size()-1);
            Toast.makeText(this,"[** ADD ** ] Palabra nueva Agregada",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"[** EMPTY ** ] Debes ingresar un texto para agregar!",Toast.LENGTH_SHORT).show();
            etWord.setError("No encontre texto!");
        }
    }
    public void mostrar (int pos){
         etWord.setText(palabra.get(pos).getWord());
        posDel=pos;
        Toast.makeText(this,"[ ** SELECTED "+ posDel +" ** ] " + palabra.get(pos).getWord(),Toast.LENGTH_SHORT).show();
    }


    private class AdaptadorPalabra extends RecyclerView.Adapter<AdaptadorPalabra.AdaptadorPalabraHolder>{

        @NonNull
        @Override
        public AdaptadorPalabra.AdaptadorPalabraHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorPalabraHolder(getLayoutInflater().inflate(R.layout.item_palabra,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorPalabra.AdaptadorPalabraHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return palabra.size();
        }

        class AdaptadorPalabraHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView tvWord;

            public AdaptadorPalabraHolder(@NonNull View itemView){
                super (itemView);
                tvWord = itemView.findViewById(R.id.tvPalabra);
                itemView.setOnClickListener(this);
            }

            public void imprimir (int position){
                tvWord.setText(palabra.get(position).getWord());
            }

            public void onClick(View v){
                mostrar(getLayoutPosition());
            }
        }
    }
}
