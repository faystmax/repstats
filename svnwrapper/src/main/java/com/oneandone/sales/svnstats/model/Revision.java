package com.oneandone.sales.svnstats.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Revision {

    private long id;
    private String author;
    private Date date;
    private String message;
    private List<ChangedPath> changedPaths = new ArrayList<ChangedPath>();

    public Revision(long id) {
        this.id = id;
    }

    public long id() {
        return id;
    }

    public void author(String author) {
        this.author = author;
    }

    public String author() {
        return author;
    }

    public void date(Date date) {
        this.date = date;
    }

    public Date date() {
        return date;
    }

    public void message(String message) {
        this.message = message;
    }

    public String message() {
        return message;
    }

    public void addChangedPath(ChangedPath path) {
        changedPaths.add(path);
    }

    public List<ChangedPath> changedPaths() {
        return changedPaths;
    }
}
