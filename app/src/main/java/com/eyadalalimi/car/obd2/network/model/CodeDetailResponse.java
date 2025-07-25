package com.eyadalalimi.car.obd2.network.model;


import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CodeDetailResponse {

    @SerializedName("code")
    private String code;

    @SerializedName("status_text")
    private String statusText;

    @SerializedName("detailed_description")
    private String detailedDescription;

    @SerializedName("possible_symptoms")
    private List<String> possibleSymptoms;

    @SerializedName("possible_causes")
    private List<String> possibleCauses;

    @SerializedName("driving_implications")
    private String drivingImplications;

    @SerializedName("severity_level")
    private String severityLevel;

    @SerializedName("average_repair_cost")
    private String averageRepairCost;

    @SerializedName("top_troubleshooting_fixes")
    private List<String> topFixes;

    @SerializedName("affected_components")
    private List<String> affectedComponents;

    @SerializedName("risks_of_ignoring")
    private List<String> risksIgnoring;

    @SerializedName("faq_from_forums")
    private List<String> faqFromForums;

    @SerializedName("diagnostic_cautions")
    private List<String> diagnosticCautions;

    // — getters only —
    public String getCode() { return code; }
    public String getStatusText() { return statusText; }
    public String getDetailedDescription() { return detailedDescription; }
    public List<String> getPossibleSymptoms() { return possibleSymptoms; }
    public List<String> getPossibleCauses() { return possibleCauses; }
    public String getDrivingImplications() { return drivingImplications; }
    public String getSeverityLevel() { return severityLevel; }
    public String getAverageRepairCost() { return averageRepairCost; }
    public List<String> getTopFixes() { return topFixes; }
    public List<String> getAffectedComponents() { return affectedComponents; }
    public List<String> getRisksIgnoring() { return risksIgnoring; }
    public List<String> getFaqFromForums() { return faqFromForums; }
    public List<String> getDiagnosticCautions() { return diagnosticCautions; }
}
