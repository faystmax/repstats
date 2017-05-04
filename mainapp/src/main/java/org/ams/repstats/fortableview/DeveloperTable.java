package org.ams.repstats.fortableview;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 22.04.2017
 * Time: 16:36
 */
public class DeveloperTable {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private SimpleStringProperty middlename;
    private SimpleIntegerProperty id_team;
    private SimpleIntegerProperty age;
    private SimpleStringProperty phone;
    private SimpleIntegerProperty id_role;
    private SimpleStringProperty role_name;
    private SimpleStringProperty team_name;

    private SimpleStringProperty role_in_project;

    private SimpleStringProperty gitname;
    private SimpleStringProperty gitemail;

    public DeveloperTable(int id, String name, String surname, String middleName,
                          int id_role, int id_team, int age, String phone, String role_name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.middlename = new SimpleStringProperty(middleName);
        this.id_role = new SimpleIntegerProperty(id_role);
        this.id_team = new SimpleIntegerProperty(id_team);
        this.age = new SimpleIntegerProperty(age);
        this.phone = new SimpleStringProperty(phone);
        this.role_name = new SimpleStringProperty(role_name);
    }

    public DeveloperTable(int id, String name, String surname, String middleName,
                          int id_role, int id_team, int age, String phone, String role_name, String team_name) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.middlename = new SimpleStringProperty(middleName);
        this.id_role = new SimpleIntegerProperty(id_role);
        this.id_team = new SimpleIntegerProperty(id_team);
        this.age = new SimpleIntegerProperty(age);
        this.phone = new SimpleStringProperty(phone);
        this.role_name = new SimpleStringProperty(role_name);
        this.team_name = new SimpleStringProperty(team_name);
    }

    public DeveloperTable(int id, String name, String surname, String middleName,
                          int id_role, int id_team, int age, String phone, String role_name,
                          String gitname, String gitemail) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.middlename = new SimpleStringProperty(middleName);
        this.id_role = new SimpleIntegerProperty(id_role);
        this.id_team = new SimpleIntegerProperty(id_team);
        this.age = new SimpleIntegerProperty(age);
        this.phone = new SimpleStringProperty(phone);
        this.role_name = new SimpleStringProperty(role_name);

        this.gitname = new SimpleStringProperty(gitname);
        this.gitemail = new SimpleStringProperty(gitemail);
    }

    public DeveloperTable(int id, String name, String surname, String middleName,
                          int id_role, int id_team, int age, String phone, String role_name, String team_name,
                          String gitname, String gitemail) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.middlename = new SimpleStringProperty(middleName);
        this.id_role = new SimpleIntegerProperty(id_role);
        this.id_team = new SimpleIntegerProperty(id_team);
        this.age = new SimpleIntegerProperty(age);
        this.phone = new SimpleStringProperty(phone);
        this.role_name = new SimpleStringProperty(role_name);
        this.team_name = new SimpleStringProperty(team_name);

        this.gitname = new SimpleStringProperty(gitname);
        this.gitemail = new SimpleStringProperty(gitemail);
    }

    public int getId() {
        return id.get();
    }

    public SimpleIntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
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

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getMiddlename() {
        return middlename.get();
    }

    public SimpleStringProperty middlenameProperty() {
        return middlename;
    }

    public void setMiddlename(String middlename) {
        this.middlename.set(middlename);
    }

    public int getId_role() {
        return id_role.get();
    }

    public SimpleIntegerProperty id_roleProperty() {
        return id_role;
    }

    public void setId_role(int id_role) {
        this.id_role.set(id_role);
    }

    public int getId_team() {
        return id_team.get();
    }

    public SimpleIntegerProperty id_teamProperty() {
        return id_team;
    }

    public void setId_team(int id_team) {
        this.id_team.set(id_team);
    }

    public int getAge() {
        return age.get();
    }

    public SimpleIntegerProperty ageProperty() {
        return age;
    }

    public void setAge(int age) {
        this.age.set(age);
    }

    public String getPhone() {
        return phone.get();
    }

    public SimpleStringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getRole_name() {
        return role_name.get();
    }

    public SimpleStringProperty role_nameProperty() {
        return role_name;
    }

    public void setRole_name(String role_name) {
        this.role_name.set(role_name);
    }

    public String getTeam_name() {
        return team_name.get();
    }

    public SimpleStringProperty team_nameProperty() {
        return team_name;
    }

    public void setTeam_name(String team_name) {
        this.team_name.set(team_name);
    }

    public String getGitname() {
        return gitname.get();
    }

    public SimpleStringProperty gitnameProperty() {
        return gitname;
    }

    public void setGitname(String gitname) {
        this.gitname.set(gitname);
    }

    public String getGitemail() {
        return gitemail.get();
    }

    public SimpleStringProperty gitemailProperty() {
        return gitemail;
    }

    public void setGitemail(String gitemail) {
        this.gitemail.set(gitemail);
    }
}
