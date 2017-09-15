package com.example.antiaedes.converter;

import com.example.antiaedes.entities.Denuncia;
import com.example.antiaedes.entities.Endereco;
import com.example.antiaedes.entities.Funcionario;
import com.example.antiaedes.entities.Usuario;
import com.example.antiaedes.entities.Visita;

import org.ksoap2.serialization.SoapObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class Converter {

    public static Usuario getUser(SoapObject response) {
        if (response != null) {
            Usuario usuario = new Usuario();
            usuario.setId(Integer.parseInt(response.getPrimitivePropertyAsString("id")));
            usuario.setNome(response.getPrimitivePropertyAsString("nome"));
            usuario.setEmail(response.getPrimitivePropertyAsString("email"));
            usuario.setSaldo(Integer.parseInt(response.getPrimitivePropertyAsString("saldo")));
            usuario.setAtivo(Boolean.parseBoolean(response.getPrimitivePropertyAsString("ativo")));
            return usuario;
        } else return null;
    }

    public static SoapObject userToSoap(String NAMESPACE, Usuario usuario) {
        SoapObject user = new SoapObject(NAMESPACE, "usuario");
        user.addProperty("nome", usuario.getNome());
        user.addProperty("email", usuario.getEmail());
        user.addProperty("senha", usuario.getSenha());
        return user;
    }

    public static Funcionario getFunctionary(SoapObject response) {
        if (response != null) {
            Funcionario funcionario = new Funcionario();
            funcionario.setId(Integer.parseInt(response.getPrimitivePropertyAsString("id")));
            funcionario.setCpf(response.getPrimitivePropertyAsString("cpf"));
            funcionario.setNome(response.getPrimitivePropertyAsString("nome"));
            funcionario.setEmail(response.getPrimitivePropertyAsString("email"));
            funcionario.setAtivo(Boolean.parseBoolean(response.getPrimitivePropertyAsString("ativo")));
            return funcionario;
        } else return null;
    }

    public static SoapObject denunciationToSoap(String NAMESPACE, Denuncia denuncia) {
        SoapObject denun = new SoapObject(NAMESPACE, "denuncia");
        denun.addProperty("cep", denuncia.getCep());
        denun.addProperty("bairro", denuncia.getBairro());
        denun.addProperty("rua", denuncia.getRua());
        denun.addProperty("cidade", denuncia.getCidade());
        denun.addProperty("referencia", denuncia.getReferencia());
        denun.addProperty("descricao", denuncia.getDescricao());
        denun.addProperty("num_casa", denuncia.getNum_casa());
        denun.addProperty("latitude", denuncia.getLatitude());
        denun.addProperty("longitude", denuncia.getLongitude());
        denun.addProperty("data", denuncia.getData());
        denun.addProperty("tipo", denuncia.getTipo());
        denun.addProperty("imagem", denuncia.getImagem());
        denun.addProperty("codigo", denuncia.getCodigo());
        denun.addProperty("prioridade", denuncia.isPrioridade());
        denun.addProperty("id_user", denuncia.getId_user());
        denun.addProperty("id_fun", denuncia.getId_fun());
        return denun;
    }

    public static SoapObject visitToSoap(String NAMESPACE, Visita visita) {
        SoapObject visit = new SoapObject(NAMESPACE, "visita");
        visit.addProperty("id_fun", visita.getId_fun());
        visit.addProperty("id_den", visita.getId_den());
        visit.addProperty("data", visita.getData());
        visit.addProperty("situacao", visita.getSituation());
        visit.addProperty("observacao", visita.getObservacao());
        return visit;
    }

    public static Denuncia getDenunciation(SoapObject response) {
        if (response != null) {
            Denuncia denuncia = new Denuncia();
            denuncia.setId(Integer.parseInt(response.getPrimitivePropertyAsString("id")));
            denuncia.setCep(response.getPrimitivePropertyAsString("cep").toString());
            denuncia.setBairro(response.getPrimitivePropertyAsString("bairro").toString());
            denuncia.setRua(response.getPrimitivePropertyAsString("rua").toString());
            denuncia.setCidade(response.getPrimitivePropertyAsString("cidade").toString());
            denuncia.setReferencia(response.getPrimitivePropertyAsString("referencia").toString());
            denuncia.setDescricao(response.getPrimitivePropertyAsString("descricao").toString());
            denuncia.setNum_casa(Integer.parseInt(response.getPrimitivePropertyAsString("num_casa")));
            denuncia.setLatitude(response.getPrimitivePropertyAsString("latitude"));
            denuncia.setLongitude(response.getPrimitivePropertyAsString("longitude"));
            denuncia.setData(response.getPrimitivePropertyAsString("data"));
            denuncia.setTipo(Integer.parseInt(response.getPrimitivePropertyAsString("tipo")));
            denuncia.setImagem(response.getPrimitivePropertyAsString("imagem"));
            denuncia.setCodigo(Integer.parseInt(response.getPrimitivePropertyAsString("codigo")));
            denuncia.setPrioridade(Boolean.parseBoolean(response.getPrimitivePropertyAsString("prioridade")));
            denuncia.setId_fun(Integer.parseInt(response.getPrimitivePropertyAsString("id_fun")));
            denuncia.setId_user(Integer.parseInt(response.getPrimitivePropertyAsString("id_user")));
            return denuncia;
        } else return null;
    }

    public static Endereco getAddress(SoapObject response) {
        if (response != null) {
            Endereco endereco = new Endereco();
            endereco.setBairro(response.getPrimitivePropertyAsString("bairro"));
            endereco.setCep(response.getPrimitivePropertyAsString("cep"));
            endereco.setCidade(response.getPrimitivePropertyAsString("cidade"));
            endereco.setComplemento1(response.getPrimitivePropertyAsString("complemento"));
            endereco.setComplemento2(response.getPrimitivePropertyAsString("complemento2"));
            endereco.setEnd(response.getPrimitivePropertyAsString("end"));
            endereco.setUf(response.getPrimitivePropertyAsString("uf"));
            return endereco;
        } else return null;
    }

    public static ArrayList<Visita> getVisits(Vector<SoapObject> response) {
        ArrayList<Visita> visits = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (SoapObject object : response) {
            Visita visit = new Visita();
            visit.setId_fun(Integer.parseInt(object.getPrimitivePropertyAsString("id_fun")));
            visit.setId_den(Integer.parseInt(object.getPrimitivePropertyAsString("id_den")));
            visit.setSituation(Integer.parseInt(object.getPrimitivePropertyAsString("situacao")));
            visit.setData(object.getPrimitivePropertyAsString("data"));
            visits.add(visit);
        }
        return visits;
    }
    public static ArrayList<Visita> getVisits(SoapObject object) {
        ArrayList<Visita> visits = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Visita visit = new Visita();
        visit.setId_fun(Integer.parseInt(object.getPrimitivePropertyAsString("id_fun")));
        visit.setId_den(Integer.parseInt(object.getPrimitivePropertyAsString("id_den")));
        visit.setSituation(Integer.parseInt(object.getPrimitivePropertyAsString("situacao")));
        visit.setData(object.getPropertyAsString("date"));
        visits.add(visit);

        return visits;
    }
    public static ArrayList<Denuncia> getDenunciations(Vector<SoapObject> response) {
        ArrayList<Denuncia> denuncias = new ArrayList<>();
        for (SoapObject object : response) {
            Denuncia denuncia = new Denuncia();
            denuncia.setId(Integer.parseInt(object.getPrimitivePropertyAsString("id")));
            denuncia.setCep(object.getPrimitivePropertyAsString("cep").toString());
            denuncia.setBairro(object.getPrimitivePropertyAsString("bairro").toString());
            denuncia.setRua(object.getPrimitivePropertyAsString("rua").toString());
            denuncia.setCidade(object.getPrimitivePropertyAsString("cidade").toString());
            denuncia.setReferencia(object.getPrimitivePropertyAsString("referencia").toString());
            denuncia.setDescricao(object.getPrimitivePropertyAsString("descricao").toString());
            denuncia.setNum_casa(Integer.parseInt(object.getPrimitivePropertyAsString("num_casa")));
            denuncia.setLatitude(object.getPrimitivePropertyAsString("latitude"));
            denuncia.setLongitude(object.getPrimitivePropertyAsString("longitude"));
            denuncia.setData(object.getPrimitivePropertyAsString("data"));
            denuncia.setTipo(Integer.parseInt(object.getPrimitivePropertyAsString("tipo")));
            denuncia.setImagem(object.getPrimitivePropertyAsString("imagem"));
            denuncia.setCodigo(Integer.parseInt(object.getPrimitivePropertyAsString("codigo")));
            denuncia.setPrioridade(Boolean.parseBoolean(object.getPrimitivePropertyAsString("prioridade")));
            denuncia.setId_fun(Integer.parseInt(object.getPrimitivePropertyAsString("id_fun")));
            denuncia.setId_user(Integer.parseInt(object.getPrimitivePropertyAsString("id_user")));
            denuncias.add(denuncia);
        }
        return denuncias;
    }
}
