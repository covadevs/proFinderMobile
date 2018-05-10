package profinder.com.br.profindermobile;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ProjetoAdapter extends RecyclerView.Adapter<ProjetoAdapter.MyViewHolder> {

    private List<Projeto> projetos;

    public ProjetoAdapter(List<Projeto> projetos) {
        this.projetos = projetos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projeto_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Projeto projeto = projetos.get(position);
        holder.titulo.setText(projeto.getNome());
    }

    @Override
    public int getItemCount() {
        return projetos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView titulo;

        public MyViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo_projeto);
        }
    }

}
