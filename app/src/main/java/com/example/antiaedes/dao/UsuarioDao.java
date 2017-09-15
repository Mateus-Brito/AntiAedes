package com.example.antiaedes.dao;

import com.example.antiaedes.converter.Converter;
import com.example.antiaedes.entities.Usuario;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class UsuarioDao {

    private static final String URL = "http://192.168.0.4:8080/WSAntiAedes/services/UsuarioDao?wsdl";
    private static final String NAMESPACE = "http://dao.antiaedes.example.com";
    private static final String REGISTER_USER = "registerUser";
    private static final String SEARCH_USER = "searchUser";
    private static final String LOGIN = "login";

    public boolean registerUser(Usuario usuario) {

        SoapObject insert = new SoapObject(NAMESPACE, REGISTER_USER);
        SoapObject user = Converter.userToSoap(NAMESPACE, usuario);
        insert.addSoapObject(user);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(insert);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            httpTransportSE.call("uri:" + REGISTER_USER, envelope);
            SoapPrimitive response = (SoapPrimitive) envelope.getResponse();

            return Boolean.parseBoolean(response.toString());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Usuario login(String email, String password) {
        Usuario usuario = null;

        SoapObject user = new SoapObject(NAMESPACE, LOGIN);
        user.addProperty("email", email);
        user.addProperty("password", password);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(user);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call("urn:" + LOGIN, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            usuario = Converter.getUser(response);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return usuario;
    }
}
