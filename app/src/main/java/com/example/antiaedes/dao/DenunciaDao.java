package com.example.antiaedes.dao;

import com.example.antiaedes.converter.Converter;
import com.example.antiaedes.entities.Denuncia;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

public class DenunciaDao{

    private static final String URL = "http://jbossews-desenvolvendo.rhcloud.com/WSAntiAedes/services/DenunciaDao?wsdl";
    private static final String NAMESPACE = "http://dao.antiaedes.example.com";
    private static final String REGISTER_DENUNCIATION = "registerDenunciation";
    private static final String GET_DENUNCIATION = "getDenunciation";
    private static final String UPDATE_DENUNCIATION = "updateDenunciation";
    private static final String GET_ALL_DENUNCIATIONS = "getAllDenunciations";
    private static final String GET_ALL_DENUNCIATIONS_ACTIVE = "getDenunciationsActives";
    private static final String GET_BY_ID = "buscarDenunciaPorId";

    public Denuncia getDenunciaById(int id){
        Denuncia dn;

        SoapObject denunciation = new SoapObject(NAMESPACE, GET_BY_ID);
        denunciation.addProperty("id", id);
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(denunciation);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call("urn:" + GET_BY_ID, envelope);

            if (envelope.getResponse() instanceof SoapObject) {
                SoapObject response = (SoapObject) envelope.getResponse();
                Denuncia d = Converter.getDenunciation(response);
                return d;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean registerDenunciation(Denuncia denuncia){
        SoapObject insert = new SoapObject(NAMESPACE, REGISTER_DENUNCIATION);
        SoapObject report = Converter.denunciationToSoap(NAMESPACE, denuncia);
        insert.addSoapObject(report);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(insert);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            httpTransportSE.call("uri:" + REGISTER_DENUNCIATION, envelope);
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

    public ArrayList<Denuncia> getAllDenunciationsActives(){
        ArrayList<Denuncia> denuncias = null;
        SoapObject denunciation = new SoapObject(NAMESPACE, GET_ALL_DENUNCIATIONS_ACTIVE);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(denunciation);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call("urn:" + GET_ALL_DENUNCIATIONS_ACTIVE, envelope);

            if(envelope.getResponse() instanceof Vector){
                Vector<SoapObject> vetorResposta = (Vector<SoapObject>) envelope.getResponse();
                denuncias = Converter.getDenunciations(vetorResposta);
            }else if (envelope.getResponse() instanceof SoapObject) {
                denuncias = new ArrayList<>();
                SoapObject response = (SoapObject) envelope.getResponse();
                Denuncia d = Converter.getDenunciation(response);
                denuncias.add(d);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return denuncias;
    }

    public ArrayList<Denuncia> getAllDenunciation(){
        ArrayList<Denuncia> denuncias = null;
        SoapObject denunciation = new SoapObject(NAMESPACE, GET_ALL_DENUNCIATIONS);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(denunciation);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call("urn:" + GET_ALL_DENUNCIATIONS, envelope);

            if(envelope.getResponse() instanceof Vector){
                Vector<SoapObject> vetorResposta = (Vector<SoapObject>) envelope.getResponse();
                denuncias = Converter.getDenunciations(vetorResposta);
            }else if (envelope.getResponse() instanceof SoapObject) {
                denuncias = new ArrayList<>();
                SoapObject response = (SoapObject) envelope.getResponse();
                Denuncia d = Converter.getDenunciation(response);
                denuncias.add(d);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return denuncias;
    }

    public Denuncia getDenunciation(Date date, String cep, int num_house){
        Denuncia denuncia = new Denuncia();
        SoapObject denunciation = new SoapObject(NAMESPACE, GET_DENUNCIATION);
        denunciation.addProperty("data", date);
        denunciation.addProperty("cep", cep);
        denunciation.addProperty("num_casa",num_house);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(denunciation);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {
            httpTransportSE.call("urn:" + GET_DENUNCIATION, envelope);

            SoapObject response = (SoapObject) envelope.getResponse();
            denuncia = Converter.getDenunciation(response);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        return denuncia;
    }

    public boolean updateDenunciation(Denuncia denuncia){
        SoapObject update = new SoapObject(NAMESPACE, UPDATE_DENUNCIATION);
        SoapObject report = Converter.denunciationToSoap(NAMESPACE, denuncia);
        update.addSoapObject(report);

        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.setOutputSoapObject(update);
        envelope.implicitTypes = true;

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            httpTransportSE.call("uri:" + UPDATE_DENUNCIATION, envelope);
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
}
