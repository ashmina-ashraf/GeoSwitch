package com.example.geoswitch.HelperClasses.HomeAdapter;

public class PlaceCategoryRecyclerHelperClass {

    int icon;
    String name;
    String placeType;

    public PlaceCategoryRecyclerHelperClass() {
    }

    public PlaceCategoryRecyclerHelperClass(int icon, String name, String placeType) {

        this.icon = icon;
        this.name = name;
        this.placeType = placeType;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }
}
