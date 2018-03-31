package br.com.appteatro.appteatro.domain.service;

import android.net.Uri;

import com.google.android.gms.common.api.Response;
import com.google.firebase.auth.FirebaseUser;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import br.com.appteatro.appteatro.domain.model.Usuario;
import br.com.appteatro.appteatro.utils.HttpHelper;

/**
 * Created by Windows 10 on 18/03/2018.
 */

public class UsuarioService {

    private static final String URL = "https://teatro-api.herokuapp.com/usuarios";


    public static void salvarUsuario(FirebaseUser user) {
        HttpHelper http = new HttpHelper();
        try {
            String json = http.doPost(URL, formatDataAsJson(user), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static Map<String,String>  formatDataAsJson(FirebaseUser user){
        Map<String,String> params = new HashMap<String, String>();
        params.put("nome", user.getDisplayName());
        params.put("email",user.getEmail());
        params.put("uid", user.getUid());
        params.put("token", user.getProviderId());
        params.put("foto", (user.getPhotoUrl() != null ? user.getPhotoUrl().toString() : ""));

        return params;
    }

}
