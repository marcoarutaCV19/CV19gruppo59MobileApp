package com.example.consigliaviaggi19.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consigliaviaggi19.Models.Recensioni;
import com.example.consigliaviaggi19.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class AdapterRecensioni extends FirestoreRecyclerAdapter<Recensioni, AdapterRecensioni.NoteHolder>{

    private OnItemClickListner listener;
    private FirebaseFirestore database = FirebaseFirestore.getInstance();

    public AdapterRecensioni(@NonNull FirestoreRecyclerOptions<Recensioni> options) { super(options); }

    @Override
    protected void onBindViewHolder(@NonNull final NoteHolder noteHolder, int i, @NonNull Recensioni recensioni) {

        database.collection("Strutture").document(recensioni.getStruttura()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if(document.exists()){
                        Picasso.get().load(document.getString("immagine")).into(noteHolder.imageV);
                        noteHolder.textViewName.setText(document.getString("nome"));
                    }
                }
            }
        });

        noteHolder.textValutazione.setText(String.valueOf(recensioni.getVoto()));
        noteHolder.testo_Recensione.setText(recensioni.getTesto());
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.riga_lista_recensioni,
                parent, false);
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageV;
        TextView textValutazione;
        TextView testo_Recensione;


        public NoteHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewStruttura);
            imageV=itemView.findViewById(R.id.imageViewStruttura);
            textValutazione=itemView.findViewById(R.id.textViewValutazione);
            testo_Recensione = itemView.findViewById(R.id.textViewRecensione);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listener!=null)
                        listener.onItemClick(getSnapshots().getSnapshot(position),position);
                }
            });
        }
    }

    public interface OnItemClickListner{
        void onItemClick(DocumentSnapshot docSnapshot,int position);
    }



}


