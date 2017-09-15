package com.example.antiaedes;

import com.example.antiaedes.converter.Converter;
import com.example.antiaedes.entities.Endereco;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

public class PostOffices {

    private static final String URL = "https://apps.correios.com.br/SigepMasterJPA/AtendeClienteService/AtendeCliente?wsdl";
    private static final String NAMESPACE = "http://cliente.bean.master.sigep.bsb.correios.com.br/";
    private static final String FETCHADDRESS = "consultaCEP";

    public Endereco fetchAddress(String cep){
        Endereco endereco = null;
        SoapObject address = new SoapObject(NAMESPACE, FETCHADDRESS);
        address.addProperty("cep",cep);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(address);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            httpTransportSE.call("", envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            endereco = Converter.getAddress(response);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return endereco;
    }
}
