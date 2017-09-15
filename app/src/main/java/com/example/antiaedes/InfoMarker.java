package com.example.antiaedes;

import com.example.antiaedes.entities.Denuncia;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.io.Serializable;
import java.util.ArrayList;

public class InfoMarker implements Serializable {

    private int id = 0;
    private static int idAll = 0;
    private int number_denunciations;
    private int number_denunciations_resolved;
    private int suspicion_den = 0;
    private int suspicion_res = 0;
    private int focus_den = 0;
    private int focus_res = 0;
    private ArrayList<Denuncia> denunciations;
    private ArrayList<Denuncia> denunciationsR;

    public InfoMarker(ArrayList<Denuncia> denunciationsResolved) { //Internet economy
        this.idAll ++;
        id = idAll;
        this.denunciations = new ArrayList<>();
        this.denunciationsR = new ArrayList<>();
        if(denunciationsResolved!=null)
            this.denunciationsR.addAll(denunciationsResolved);

    }

    public int getNumber_denunciations() {
        return number_denunciations;
    }

    public int getNumber_denunciations_resolved() {
        if (denunciationsR != null && denunciations != null) {
            for (Denuncia denunciation : denunciations) {
                boolean encontrou = false;
                for (Denuncia den : denunciationsR) {
                    if (denunciation.getId() == den.getId())
                        encontrou = true;
                }
                if(!encontrou){
                    number_denunciations_resolved++;
                }
            }
        }
        return number_denunciations_resolved;
    }

    public ArrayList<Denuncia> getDenunciations() {
        return denunciations;
    }

    public void addDenunciation(Denuncia denunciation) {
        if (denunciation != null) {
            denunciations.add(denunciation);
            //teste(denunciation);
            number_denunciations++;
            teste(denunciation);
        }
    }

    public void teste(Denuncia denuncia) {
        boolean boole = true;
        if (!this.denunciationsR.isEmpty()) {
            for (Denuncia d : this.denunciationsR) {
                boole = true;
                if (d.getId() == denuncia.getId()) {
                    if (d.getTipo() == 0) {
                        suspicion_res++;
                        boole = false;
                        break;
                    } else {
                        focus_res++;
                        boole = false;
                        break;
                    }
                }
            }
        }
        if (boole) {
            if (denuncia.getTipo() == 0)
                suspicion_den++;
            else
                focus_den++;
        }
    }

    public boolean containsDenunciation(Denuncia denunciation) {
        for (Denuncia d : denunciations) {
            if (d.getId() == denunciation.getId())
                return true;
        }
        return false;
    }

    public LatLng getLatLng() {
        return new LatLng(Double.parseDouble(denunciations.get(0).getLatitude()), Double.parseDouble(denunciations.get(0).getLongitude()));
    }

    public int getId() {
        return this.id;
    }

    public int getSuspicion_den() {
        return suspicion_den;
    }

    public int getSuspicion_res() {
        return suspicion_res;
    }

    public int getFocus_den() {
        return focus_den;
    }

    public int getFocus_res() {
        return focus_res;
    }
}
