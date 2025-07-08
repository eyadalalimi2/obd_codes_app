package com.proapp.obdcodes.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.proapp.obdcodes.network.model.ObdCode;

@Entity(tableName = "obd_codes")
public class ObdCodeEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String code;
    public String title;
    public String description;
    public String symptoms;
    public String causes;
    public String solutions;
    public String severity;
    public String diagnosis;
    public String image;
    public boolean isSaved;
    public String updatedAt;

    /** ✅ تحويل من ObdCode (API) إلى كيان Room */
    public static ObdCodeEntity fromObdCode(ObdCode code) {
        ObdCodeEntity entity = new ObdCodeEntity();
        entity.code = code.getCode();
        entity.title = code.getTitle();
        entity.description = code.getDescription();
        entity.symptoms = code.getSymptoms();
        entity.causes = code.getCauses();
        entity.solutions = code.getSolutions();
        entity.severity = code.getSeverity();
        entity.diagnosis = code.getDiagnosis();
        entity.image = code.getImage();
        entity.updatedAt = code.getUpdatedAt();
        entity.isSaved = false;
        return entity;
    }

    /** ✅ تحويل من كيان Room إلى كائن ObdCode لاستخدامه في الواجهة */
    public ObdCode toObdCode() {
        ObdCode code = new ObdCode();
        code.setCode(this.code);
        code.setTitle(this.title);
        code.setDescription(this.description);
        code.setSymptoms(this.symptoms);
        code.setCauses(this.causes);
        code.setSolutions(this.solutions);
        code.setSeverity(this.severity);
        code.setDiagnosis(this.diagnosis);
        code.setImage(this.image);
        code.setUpdatedAt(this.updatedAt);
        return code;
    }

}
