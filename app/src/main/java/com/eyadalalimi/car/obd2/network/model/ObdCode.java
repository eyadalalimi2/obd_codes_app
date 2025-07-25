package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;

public class ObdCode {
    @SerializedName("id")           private long id;
    @SerializedName("code")         private String code;
    @SerializedName("type")         private String type;
    @SerializedName("brand_id")     private Integer brandId;
    @SerializedName("title")        private String title;
    @SerializedName("description")  private String description;
    @SerializedName("symptoms")     private String symptoms;
    @SerializedName("causes")       private String causes;
    @SerializedName("solutions")    private String solutions;
    @SerializedName("severity")     private String severity;
    @SerializedName("diagnosis")    private String diagnosis;
    @SerializedName("image")        private String image;
    @SerializedName("updated_at")   private String updatedAt;

    // ——— Getters & Setters ———
    public long getId()                { return id; }
    public void setId(long id)         { this.id = id; }

    public String getCode()            { return code; }
    public void setCode(String code)   { this.code = code; }

    public String getType()            { return type; }
    public void setType(String type)   { this.type = type; }

    public Integer getBrandId()        { return brandId; }
    public void setBrandId(Integer brandId) { this.brandId = brandId; }

    public String getTitle()           { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription()                 { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getSymptoms()                   { return symptoms; }
    public void setSymptoms(String symptoms)       { this.symptoms = symptoms; }

    public String getCauses()                     { return causes; }
    public void setCauses(String causes)           { this.causes = causes; }

    public String getSolutions()                  { return solutions; }
    public void setSolutions(String solutions)     { this.solutions = solutions; }

    public String getSeverity()                   { return severity; }
    public void setSeverity(String severity)       { this.severity = severity; }

    public String getDiagnosis()                  { return diagnosis; }
    public void setDiagnosis(String diagnosis)     { this.diagnosis = diagnosis; }

    public String getImage()                      { return image; }
    public void setImage(String image)             { this.image = image; }

    public String getUpdatedAt()                  { return updatedAt; }
    public void setUpdatedAt(String updatedAt)     { this.updatedAt = updatedAt; }
}
