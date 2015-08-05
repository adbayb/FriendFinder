package dk.aau.mppss.friendfinder.model.maps;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by adibayoub on 04/08/2015.
 */
public class POIMarkerModel extends MarkerModel {
    //TODO ajouter une variable indiquant si le marqueur appartient à un ami ou non afin d'éviter l'ouverture de l'éditeur POI!
    private String description;
    //Variable that specifies if a poi belongs to current user or its friends:
    private boolean belongsToUser;

    public POIMarkerModel(String label, String description, double latitude, double longitude, Marker marker, boolean belongsToUser) {
        super(label, latitude, longitude, marker);
        this.description = description;
        this.belongsToUser = belongsToUser;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isBelongsToUser() {
        return belongsToUser;
    }

    public void setBelongsToUser(boolean belongsToUser) {
        this.belongsToUser = belongsToUser;
    }

    @Override
    public String toString() {
        return super.toString() +
                "POIMarkerModel{" +
                "description='" + description + '\'' +
                ", belongsToUser=" + belongsToUser +
                '}';
    }
}
