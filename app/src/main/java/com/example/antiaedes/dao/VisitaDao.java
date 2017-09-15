package com.example.antiaedes.dao;

import com.example.antiaedes.converter.Converter;
import com.example.antiaedes.entities.Denuncia;
import com.example.antiaedes.entities.Visita;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

public class VisitaDao {

    private static final String URL = "http://jbossews-desenvolvendo.rhcloud.com/WSAntiAedes/services/VisitaDao?wsdl";
    private static final String NAMESPACE = "http://dao.antiaedes.example.com";
    private static final String REGISTER_VISIT = "registerVisit";
    private static final String REGISTER_VISIT2 = "registerVisit2";
    private static final String GET_VISITS = "getVisits";

    public boolean registerVisit(Visita visita) {

        SoapObject register = new SoapObject(NAMESPACE, REGISTER_VISIT);
        SoapObject visit = Converter.visitToSoap(NAMESPACE, visita);
        register.addSoapObject(visit);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(register);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            httpTransportSE.call("uri:" + REGISTER_VISIT, envelope);
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

    public boolean registerVisit2(Visita visita, Denuncia denuncia) {

        SoapObject register = new SoapObject(NAMESPACE, REGISTER_VISIT2);
        SoapObject report = Converter.denunciationToSoap(NAMESPACE, denuncia);
        SoapObject visit = Converter.visitToSoap(NAMESPACE, visita);
        register.addSoapObject(report);
        register.addSoapObject(visit);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(register);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            httpTransportSE.call("uri:" + REGISTER_VISIT, envelope);
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

    public ArrayList<Visita> getVisitsBy(String cep, int num_house) {
        ArrayList<Visita> visits = null;
        SoapObject denunciation = new SoapObject(NAMESPACE, GET_VISITS);
        denunciation.addProperty("cep", cep);
        denunciation.addProperty("num_house", num_house);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(denunciation);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call("urn:" + GET_VISITS, envelope);

            if(envelope.getResponse() instanceof Vector) {
                Vector<SoapObject> response = (Vector<SoapObject>) envelope.getResponse();
                if (response != null)
                    visits = Converter.getVisits(response);
            }else if (envelope.getResponse() instanceof SoapObject) {
                SoapObject response = (SoapObject) envelope.getResponse();
                if (response != null)
                    visits = Converter.getVisits(response);
            } else return null;

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return visits;
    }
}
