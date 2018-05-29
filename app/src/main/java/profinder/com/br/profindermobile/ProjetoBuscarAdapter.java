package profinder.com.br.profindermobile;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class ProjetoBuscarAdapter extends RecyclerView.Adapter<ProjetoBuscarAdapter.MyViewHolder> {
    private List<Projeto> projetos;
    private MaterialDialog materialDialog;

    public ProjetoBuscarAdapter(List<Projeto> projetos) {
        this.projetos = projetos;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projetos_buscar_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Projeto projeto = projetos.get(position);
        holder.nomeProjeto.setText(projeto.getNome());
        holder.coordenadorProjeto.setText(projeto.getCoordenador());
        String conteudo = "Nome: "+projeto.getNome()+"\n"
                +"Coordenador(a): "+projeto.getCoordenador()+"\n"
                +"Área: "+projeto.getArea()+"\n"
                +"Descrição: "+projeto.getDescricao()+"\n"
                +"Quantidade de Vagas: "+projeto.getQntAlunos();
        holder.infoProjeto.setOnClickListener((view) -> {
            materialDialog = new MaterialDialog.Builder(view.getContext())
                    .title("Projeto")
                    .content(conteudo)
                    .positiveText("OK")
                    .onPositive((dialog, witch) -> {
                        dialog.dismiss();
                    })
                    .build();
            materialDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        return projetos.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeProjeto;
        public TextView coordenadorProjeto;
        public FancyButton infoProjeto;

        public MyViewHolder(View itemView) {
            super(itemView);
            nomeProjeto = itemView.findViewById(R.id.titulo_projeto_buscar);
            coordenadorProjeto = itemView.findViewById(R.id.nome_coordenador);
            infoProjeto = itemView.findViewById(R.id.info_projeto);

            IconicsDrawable info = new IconicsDrawable(itemView.getContext()).icon(GoogleMaterial.Icon.gmd_info)
                    .color(itemView.getResources().getColor(R.color.md_white_1000));
            Drawable infoIcon = new BitmapDrawable(itemView.getResources(), info.toBitmap());
            infoProjeto.setIconResource(infoIcon);
            infoProjeto.setFontIconSize(40);
            infoProjeto.setIconPadding(4,4,4,4);

        }
    }
}
