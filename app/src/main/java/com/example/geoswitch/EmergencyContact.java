package com.example.geoswitch;

import java.util.ArrayList;

public class EmergencyContact {

    String contactName;
    String phone;
    private static ArrayList<EmergencyContact> emergencyContacts = new ArrayList<>();

    public EmergencyContact(String contactName, String phone) {
        this.contactName = contactName;
        this.phone = phone;

        emergencyContacts.add(this);
    }

    public String getContactName() {
        return contactName;
    }

    public String getPhone() {
        return phone;
    }

    public static int getCount() {
        return emergencyContacts.size();
    }

    public static ArrayList<EmergencyContact> getEmergencyContacts() {
        return emergencyContacts;
    }

    public static ArrayList<String> getContactNumbers() {
        ArrayList<String> contactNumbers = new ArrayList<>();

        for (int i = 0; i < emergencyContacts.size(); i++) {
            contactNumbers.add(emergencyContacts.get(i).getPhone());
        }

        return contactNumbers;
    }

    public static void clearEmergencyList() {
        emergencyContacts.clear();
    }
}
