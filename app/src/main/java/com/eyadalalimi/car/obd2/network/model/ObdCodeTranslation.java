package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;

public class ObdCodeTranslation {

    @SerializedName("id")
    private int id;

    @SerializedName("obd_code_id")
    private int obdCodeId;

    @SerializedName("language_code")
    private String languageCode;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    @SerializedName("symptoms")
    private String symptoms;

    @SerializedName("causes")
    private String causes;

    @SerializedName("solutions")
    private String solutions;

    @SerializedName("severity")
    private String severity;

    @SerializedName("diagnosis")
    private String diagnosis;

    @SerializedName("category")
    private String category;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("embedding")
    private Object embedding;

    public ObdCodeTranslation() { }

    public int getId() {
        return id;
    }

    public int getObdCodeId() {
        return obdCodeId;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public String getCauses() {
        return causes;
    }

    public String getSolutions() {
        return solutions;
    }

    public String getSeverity() {
        return severity;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getCategory() {
        return category;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Object getEmbedding() {
        return embedding;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setObdCodeId(int obdCodeId) {
        this.obdCodeId = obdCodeId;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public void setCauses(String causes) {
        this.causes = causes;
    }

    public void setSolutions(String solutions) {
        this.solutions = solutions;
    }

    public void setSeverity(String severity) {
        this.severity = severity;
    }

    public void setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setEmbedding(Object embedding) {
        this.embedding = embedding;
    }
}
