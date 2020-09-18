package com.example.consigliaviaggi19.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.consigliaviaggi19.Models.Strutture;
import com.example.consigliaviaggi19.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class AdapterStrutture extends FirestoreRecyclerAdapter<Strutture, AdapterStrutture.NoteHolder>{

    private OnItemClickListner listner;

    public AdapterStrutture(@NonNull FirestoreRecyclerOptions<Strutture> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteHolder noteHolder, int i, @NonNull Strutture strutture) {
        noteHolder.textViewName.setText(strutture.getNome());
        Picasso.get().load(strutture.getImmagine()).into(noteHolder.imageV);
        String valutazione = Double.toString(strutture.getValutazione());
        String valutazione_troncata = valutazione.substring(0,3);
        noteHolder.textValutazione.setText(valutazione_troncata);
    }





    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.riga_lista_strutture,
                parent, false);
        return new NoteHolder(v);
    }

    class NoteHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        ImageView imageV;
        TextView textValutazione;


        public NoteHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewNomeStruttura);
            imageV=itemView.findViewById(R.id.imageViewStruttura);
            textValutazione=itemView.findViewById(R.id.textViewValutazione);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(position!=RecyclerView.NO_POSITION && listner!=null){
                        listner.onItemClick(getSnapshots().getSnapshot(position),position);
                    }
                }
            });
        }
    }
    public interface OnItemClickListner{
        void onItemClick(DocumentSnapshot docSnapshot,int position);
    }

    public void setOnItemClickListner(OnItemClickListner listner){

        this.listner = listner;
    }

}
