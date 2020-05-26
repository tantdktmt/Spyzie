package com.tantd.spyzie.data.model;

import android.text.TextUtils;

import com.google.gson.annotations.Expose;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.objectbox.annotation.Convert;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.converter.PropertyConverter;

@Entity
public class Contact {

    @Id
    public long _id;
    @Expose
    public long id;
    @Expose
    public String name;
    @Expose
    @Convert(converter = ListConverter.class, dbType = String.class)
    public List<String> phone;

    public Contact() {
    }

    public Contact(long id, String name, List<String> phone) {
        this.id = id;
        this.name = name;
        this.phone = phone;
    }

    public static class ListConverter implements PropertyConverter<List<String>, String> {

        @Override
        public List<String> convertToEntityProperty(String databaseValue) {
            if (TextUtils.isEmpty(databaseValue)) {
                return Collections.EMPTY_LIST;
            }
            String[] temp = databaseValue.split(",");
            return Arrays.asList(temp);
        }

        @Override
        public String convertToDatabaseValue(List<String> entityProperty) {
            StringBuffer sb = new StringBuffer();
            for (String str :
                    entityProperty) {
                sb.append(str + ",");
            }
            sb.deleteCharAt(sb.length() - 1);
            return sb.toString();
        }
    }

    @Override
    public String toString() {
        return "Contact{" +
                "_id=" + _id +
                ", id=" + id +
                ", name='" + name + '\'' +
                ", phone=" + phone +
                '}';
    }
}
