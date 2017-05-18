package org.ams.repstats.fortableview;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 05.01.2017
 * Time: 20:22
 */
public class AuthorTable {

    private SimpleStringProperty name;
    private SimpleStringProperty email;
    private SimpleIntegerProperty commitCount;
    private SimpleIntegerProperty linesAdded;
    private SimpleIntegerProperty linesRemoved;
    private SimpleIntegerProperty netContribution;

    private SimpleStringProperty FIO;


    public AuthorTable(String name, int commitCount, int linesAdded, int linesRemoved, int netContribution) {
        this.name = new SimpleStringProperty(name);
        this.commitCount = new SimpleIntegerProperty(commitCount);
        this.linesAdded = new SimpleIntegerProperty(linesAdded);
        this.linesRemoved = new SimpleIntegerProperty(linesRemoved);
        this.netContribution = new SimpleIntegerProperty(netContribution);
    }

    public AuthorTable(String name, int commitCount, int linesAdded, int linesRemoved, int netContribution, String email) {
        this.name = new SimpleStringProperty(name);
        this.commitCount = new SimpleIntegerProperty(commitCount);
        this.linesAdded = new SimpleIntegerProperty(linesAdded);
        this.linesRemoved = new SimpleIntegerProperty(linesRemoved);
        this.netContribution = new SimpleIntegerProperty(netContribution);
        this.email = new SimpleStringProperty(email);

    }

    public AuthorTable(String name, int commitCount, int linesAdded, int linesRemoved, int netContribution, String email, String FIO) {
        this.name = new SimpleStringProperty(name);
        this.commitCount = new SimpleIntegerProperty(commitCount);
        this.linesAdded = new SimpleIntegerProperty(linesAdded);
        this.linesRemoved = new SimpleIntegerProperty(linesRemoved);
        this.netContribution = new SimpleIntegerProperty(netContribution);
        this.email = new SimpleStringProperty(email);
        this.FIO = new SimpleStringProperty(FIO);
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public int getCommitCount() {
        return commitCount.get();
    }

    public SimpleIntegerProperty commitCountProperty() {
        return commitCount;
    }

    public void setCommitCount(int commitCount) {
        this.commitCount.set(commitCount);
    }

    public int getLinesAdded() {
        return linesAdded.get();
    }

    public SimpleIntegerProperty linesAddedProperty() {
        return linesAdded;
    }

    public void setLinesAdded(int linesAdded) {
        this.linesAdded.set(linesAdded);
    }

    public int getLinesRemoved() {
        return linesRemoved.get();
    }

    public SimpleIntegerProperty linesRemovedProperty() {
        return linesRemoved;
    }

    public void setLinesRemoved(int linesRemoved) {
        this.linesRemoved.set(linesRemoved);
    }

    public int getNetContribution() {
        return netContribution.get();
    }

    public SimpleIntegerProperty netContributionProperty() {
        return netContribution;
    }

    public void setNetContribution(int netContribution) {
        this.netContribution.set(netContribution);
    }

    public String getEmail() {
        return email.get();
    }

    public SimpleStringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getFIO() {
        return FIO.get();
    }

    public SimpleStringProperty FIOProperty() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO.set(FIO);
    }


}
