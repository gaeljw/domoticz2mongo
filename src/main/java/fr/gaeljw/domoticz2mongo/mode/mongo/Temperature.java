package fr.gaeljw.domoticz2mongo.mode.mongo;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;

@Document
public class Temperature {

    @Id
    private ObjectId id;
    @Field("idDevice")
    private String idDevice;
    @Field("nameDevice")
    private String nameDevice;
    @Field("dateTime")
    private Date dateTime; // Mongo Driver does not handle Java 8 Time well for now
    @Field("temperature")
    private double temperature;

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getIdDevice() {
        return idDevice;
    }

    public void setIdDevice(String idDevice) {
        this.idDevice = idDevice;
    }

    public String getNameDevice() {
        return nameDevice;
    }

    public void setNameDevice(String nameDevice) {
        this.nameDevice = nameDevice;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String toMongoJson(boolean showId) {
        // TODO use Jackson ?
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        if (showId) {
            builder.append("id:" + id);
            builder.append(", ");
        }
        builder.append("idDevice:\"" + idDevice + "\"");
        builder.append(", ");
        builder.append("nameDevice:\"" + nameDevice + "\"");
        builder.append(", ");
        builder.append("dateTime:ISODate(\"" + dateFormat.format(dateTime) + "\")");
        builder.append(", ");
        builder.append("temperature:" + temperature);
        builder.append("}");
        return builder.toString();
    }
}
