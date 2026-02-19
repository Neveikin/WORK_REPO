package org.example;

import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

public class Kabinet {

    private String id;
    private int places;
    private int compsPlaces;

    public Kabinet() {
        this.id = "0-0";
        this.places = 0;
        this.compsPlaces = 0;
    }

    public Kabinet(String id, int places, int compsPlaces) {
        this.id = id;
        this.places = places;
        this.compsPlaces = compsPlaces;
    }

    public static LinkedList<Kabinet> getCabinetsFromJson(String JSON) {
        LinkedList<Kabinet> newCabs = new LinkedList<>();

        if (JSON == null || JSON.trim().isEmpty()) {
            return newCabs;
        }

        String jsonArray = JSON.trim();
        if (jsonArray.startsWith("[") && jsonArray.endsWith("]")) {
            jsonArray = jsonArray.substring(1, jsonArray.length() - 1);
        }

        String parts[] = jsonArray.split("},");

        for (String part : parts) {
            String cabJson = part.trim();
            if (!cabJson.startsWith("{")) cabJson = "{" + cabJson;
            if (!cabJson.endsWith("}")) cabJson = cabJson + "}";

            Kabinet newCab = getFromJSON(cabJson);
            if (newCab != null) {
                newCabs.add(newCab);
            }
        }

        if (!newCabs.isEmpty()) {
            return newCabs;
        } else return null;
    }

    public static Kabinet getFromJSON(String cabJson) {
        Kabinet newCabinet = new Kabinet();

        int result = 0;

        String w = cabJson.trim();
        StringTokenizer attrs = new StringTokenizer(w.substring(1, w.length() - 1), ",");

        while (attrs.hasMoreTokens()) {
            StringTokenizer attr = new StringTokenizer(attrs.nextToken(), ":");
            if (attr.countTokens() == 2) {
                String key = attr.nextToken().trim();
                String value = attr.nextToken().trim();

                switch (key) {
                    case "\"id\"":
                        if (value.length() > 2) {
                            newCabinet.id = value.substring(1, value.length() - 1);
                            result |= 4;
                        }
                        break;

                    case "\"places\"":
                        if (value.length() > 0) {
                            try {
                                newCabinet.places = Integer.parseInt(value);
                                result |= 1;
                            } catch (NumberFormatException nf) {
                            }
                        }
                        break;

                    case "\"compPlaces\"":
                        if (value.length() > 0) {
                            try {
                                newCabinet.compsPlaces = Integer.parseInt(value);
                                result |= 2;
                            } catch (NumberFormatException nf) {
                            }
                        }
                }
            }
        }

        if (result != 7) {
            return null;
        }
        return newCabinet;
    }

    public static void returnCabinets(List<Kabinet> cabinets) {
        if (cabinets == null) {
            System.out.println("Пустой JSON");
            return;
        }
        for (Kabinet cab : cabinets) {
            System.out.println("\nКабинет: " + cab.getId());
            System.out.println("кол-во мест: " + cab.getPlaces() + "/" + cab.getCompsPlaces());
        }
    }

    public String toJSON() {
        return "{\"id\":\"" + this.id + "\",\"places\":" + this.places + ",\"compPlaces\":" + this.compsPlaces + "}";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPlaces() {
        return places;
    }

    public void setPlaces(int places) {
        this.places = places;
    }

    public int getCompsPlaces() {
        return compsPlaces;
    }

    public void setCompsPlaces(int compsPlaces) {
        this.compsPlaces = compsPlaces;
    }
}

