package com.tantd.spyzie.data.db;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

@Entity
public class Note {

    @Id
    long id;

    String text;
    String comment;
    Date date;

    public Note(long id, String text, String comment, Date date) {
        this.id = id;
        this.text = text;
        this.comment = comment;
        this.date = date;
    }

    public Note(String text, String comment, Date date) {
        this.text = text;
        this.comment = comment;
        this.date = date;
    }

    public Note() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getComment() {
        return this.comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
