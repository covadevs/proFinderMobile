package profinder.com.br.profindermobile;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;

public class UsuarioAdapter extends RecyclerView.Adapter<UsuarioAdapter.MyViewHolder> {

    private List<Usuario> usuarios;
    private MaterialDialog materialDialog;

    public UsuarioAdapter(List<Usuario> usuarios) {
        this.usuarios = usuarios;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.usuario_list_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Usuario usuario = usuarios.get(position);
        holder.nomeUsuario.setText(usuario.getNome());

        holder.addAmigo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "Em desenvolvimeto: você ira poder adicionar "+usuario.getNome(), Toast.LENGTH_SHORT).show();
            }
        });

        holder.infoPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Usuario u = usuarios.get(position);
                String conteudo = "Nome: "+u.getNome()+"\n"
                        +"E-mail: "+u.getEmail()+"\n"
                        +"Matrícula: "+u.getMatricula()+"\n"
                        +"Tipo: "+u.getType();
                materialDialog = new MaterialDialog.Builder(view.getContext())
                        .title("Perfil")
                        .content(conteudo)
                        .positiveText("OK")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                materialDialog.dismiss();
                            }
                        })
                        .build();

                materialDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.usuarios.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nomeUsuario;
        public FancyButton addAmigo;
        public FancyButton imgUsuario;
        public FancyButton infoPerfil;
        public MyViewHolder(View itemView) {
            super(itemView);

            nomeUsuario = itemView.findViewById(R.id.nome_usuario);
            addAmigo = itemView.findViewById(R.id.adicionar_amigo);
            imgUsuario = itemView.findViewById(R.id.img_usuario);
            infoPerfil = itemView.findViewById(R.id.info_perfil);

            IconicsDrawable add = new IconicsDrawable(itemView.getContext()).icon(GoogleMaterial.Icon.gmd_add)
                    .color(itemView.getResources().getColor(R.color.md_white_1000));
            Drawable addIcon = new BitmapDrawable(itemView.getResources(), add.toBitmap());
            addAmigo.setIconResource(addIcon);
            addAmigo.setFontIconSize(40);
            addAmigo.setIconPadding(4,4,4,4);

            IconicsDrawable info = new IconicsDrawable(itemView.getContext()).icon(GoogleMaterial.Icon.gmd_info)
                    .color(itemView.getResources().getColor(R.color.md_white_1000));
            Drawable infoIcon = new BitmapDrawable(itemView.getResources(), info.toBitmap());
            infoPerfil.setIconResource(infoIcon);
            infoPerfil.setFontIconSize(40);
            infoPerfil.setIconPadding(4,4,4,4);
        }
    }
}
