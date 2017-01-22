package com.starwarspeople.data.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Divya on 1/22/2017.
 */

public class PeopleResponse {


    @SerializedName("count")
    @Expose
    private Integer count;
    @SerializedName("next")
    @Expose
    private String next;
    @SerializedName("previous")
    @Expose
    private String previous;
    @SerializedName("results")
    @Expose
    private List<Result> results = null;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }


    public class Result {

        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("height")
        @Expose
        private String height;
        @SerializedName("mass")
        @Expose
        private String mass;
        @SerializedName("hair_color")
        @Expose
        private String hairColor;
        @SerializedName("skin_color")
        @Expose
        private String skinColor;
        @SerializedName("eye_color")
        @Expose
        private String eyeColor;
        @SerializedName("birth_year")
        @Expose
        private String birthYear;
        @SerializedName("gender")
        @Expose
        private String gender;
        @SerializedName("homeworld")
        @Expose
        private String homeworld;
        @SerializedName("films")
        @Expose
        private List<String> films = null;
        @SerializedName("species")
        @Expose
        private List<String> species = null;
        @SerializedName("vehicles")
        @Expose
        private List<Object> vehicles = null;
        @SerializedName("starships")
        @Expose
        private List<String> starships = null;
        @SerializedName("created")
        @Expose
        private String created;
        @SerializedName("edited")
        @Expose
        private String edited;
        @SerializedName("url")
        @Expose
        private String url;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getHeight() {
            return height;
        }

        public void setHeight(String height) {
            this.height = height;
        }

        public String getMass() {
            return mass;
        }

        public void setMass(String mass) {
            this.mass = mass;
        }

        public String getHairColor() {
            return hairColor;
        }

        public void setHairColor(String hairColor) {
            this.hairColor = hairColor;
        }

        public String getSkinColor() {
            return skinColor;
        }

        public void setSkinColor(String skinColor) {
            this.skinColor = skinColor;
        }

        public String getEyeColor() {
            return eyeColor;
        }

        public void setEyeColor(String eyeColor) {
            this.eyeColor = eyeColor;
        }

        public String getBirthYear() {
            return birthYear;
        }

        public void setBirthYear(String birthYear) {
            this.birthYear = birthYear;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getHomeworld() {
            return homeworld;
        }

        public void setHomeworld(String homeworld) {
            this.homeworld = homeworld;
        }

        public List<String> getFilms() {
            return films;
        }

        public void setFilms(List<String> films) {
            this.films = films;
        }

        public List<String> getSpecies() {
            return species;
        }

        public void setSpecies(List<String> species) {
            this.species = species;
        }

        public List<Object> getVehicles() {
            return vehicles;
        }

        public void setVehicles(List<Object> vehicles) {
            this.vehicles = vehicles;
        }

        public List<String> getStarships() {
            return starships;
        }

        public void setStarships(List<String> starships) {
            this.starships = starships;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getEdited() {
            return edited;
        }

        public void setEdited(String edited) {
            this.edited = edited;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

    }
}
