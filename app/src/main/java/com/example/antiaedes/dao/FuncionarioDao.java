package com.example.antiaedes.dao;

import com.example.antiaedes.converter.Converter;
import com.example.antiaedes.entities.Funcionario;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class FuncionarioDao {

    private static final String URL = "http://192.168.0.4:8080/WSAntiAedes/services/FuncionarioDao?wsdl";
    private static final String NAMESPACE = "http://dao.antiaedes.example.com";
    private final String SEARCH_FUN = "login";

    public Funcionario searchFunctionary(String cpf, String password) {
        Funcionario funcionario = null;

        SoapObject user = new SoapObject(NAMESPACE,SEARCH_FUN);
        user.addProperty("email", cpf);
        user.addProperty("password", password);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(user);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call("urn:" + SEARCH_FUN, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            funcionario = Converter.getFunctionary(response);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return funcionario;
    }
}
