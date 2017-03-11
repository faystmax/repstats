package org.ams.repstats.fortableview;

import javafx.beans.property.SimpleStringProperty;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 06.01.2017
 * Time: 12:20
 */
public class TableFiles {
    SimpleStringProperty path;
    SimpleStringProperty isBinary;
    SimpleStringProperty numberOfLines;


    public TableFiles(String path, String isBinary, String numberOfLines) {
        this.path = new SimpleStringProperty(path);
        this.isBinary = new SimpleStringProperty(isBinary);
        this.numberOfLines = new SimpleStringProperty(numberOfLines);
    }

    public String getPath() {
        return path.get();
    }

    public SimpleStringProperty pathProperty() {
        return path;
    }

    public void setPath(String path) {
        this.path.set(path);
    }

    public String getIsBinary() {
        return isBinary.get();
    }

    public SimpleStringProperty isBinaryProperty() {
        return isBinary;
    }

    public void setIsBinary(String isBinary) {
        this.isBinary.set(isBinary);
    }

    public String getNumberOfLines() {
        return numberOfLines.get();
    }

    public SimpleStringProperty numberOfLinesProperty() {
        return numberOfLines;
    }

    public void setNumberOfLines(String numberOfLines) {
        this.numberOfLines.set(numberOfLines);
    }

}
