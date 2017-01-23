package org.ams.repstats.fortableview;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created with IntelliJ IDEA
 * User: Максим
 * Date: 05.01.2017
 * Time: 20:22
 */
public class TableAuthor {
    private SimpleStringProperty name;
    private SimpleIntegerProperty commitCount;
    private SimpleIntegerProperty linesAdded;
    private SimpleIntegerProperty linesRemoved;
    private SimpleIntegerProperty netContribution;

    public TableAuthor(String name, int commitCount, int linesAdded, int linesRemoved, int netContribution) {
        this.name = new SimpleStringProperty(name);
        this.commitCount = new SimpleIntegerProperty(commitCount);
        this.linesAdded = new SimpleIntegerProperty(linesAdded);
        this.linesRemoved = new SimpleIntegerProperty(linesRemoved);
        this.netContribution = new SimpleIntegerProperty(netContribution);
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
}
