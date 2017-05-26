package org.ams.repstats.fortableview;

import com.selesse.gitwrapper.myobjects.Commit;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.Collection;

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

    private SimpleIntegerProperty id_developer_project;
    private SimpleStringProperty role_in_project;

    private SimpleStringProperty gitname;
    private SimpleStringProperty gitemail;

    //extended
    private SimpleIntegerProperty commitCount;
    private SimpleIntegerProperty linesAdded;
    private SimpleIntegerProperty linesRemoved;
    private SimpleIntegerProperty netContribution;
    private SimpleDoubleProperty rating = new SimpleDoubleProperty();

    private SimpleStringProperty FIO;

    //доп. поля
    private ArrayList<ProjectTable> projectTables = new ArrayList<ProjectTable>();
    private Collection<Commit> commits = new ArrayList<Commit>();

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
                          int id_role, int age, String role_name, int id_developer_project, String role_in_project) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.middlename = new SimpleStringProperty(middleName);
        this.id_role = new SimpleIntegerProperty(id_role);
        this.age = new SimpleIntegerProperty(age);
        this.role_name = new SimpleStringProperty(role_name);

        this.id_developer_project = new SimpleIntegerProperty(id_developer_project);
        this.role_in_project = new SimpleStringProperty(role_in_project);
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

    public DeveloperTable(int id, String name, String surname, String middleName,
                          String gitname, String gitemail,
                          int commitCount, int linesAdded, int linesRemoved, int netContribution) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.middlename = new SimpleStringProperty(middleName);

        this.gitname = new SimpleStringProperty(gitname);
        this.gitemail = new SimpleStringProperty(gitemail);

        this.commitCount = new SimpleIntegerProperty(commitCount);
        this.linesAdded = new SimpleIntegerProperty(linesAdded);
        this.linesRemoved = new SimpleIntegerProperty(linesRemoved);
        this.netContribution = new SimpleIntegerProperty(netContribution);

        this.FIO = new SimpleStringProperty(surname + " " + name + " " + middleName);
    }

    public DeveloperTable(int id, String name, String surname, String middleName,
                          String gitname, String gitemail,
                          int commitCount, int linesAdded, int linesRemoved, int netContribution, double rating) {
        this.id = new SimpleIntegerProperty(id);
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.middlename = new SimpleStringProperty(middleName);

        this.gitname = new SimpleStringProperty(gitname);
        this.gitemail = new SimpleStringProperty(gitemail);

        this.commitCount = new SimpleIntegerProperty(commitCount);
        this.linesAdded = new SimpleIntegerProperty(linesAdded);
        this.linesRemoved = new SimpleIntegerProperty(linesRemoved);
        this.netContribution = new SimpleIntegerProperty(netContribution);

        this.FIO = new SimpleStringProperty(surname + " " + name + " " + middleName);
        this.rating = new SimpleDoubleProperty(rating);
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

    public String getRole_in_project() {
        return role_in_project.get();
    }

    public SimpleStringProperty role_in_projectProperty() {
        return role_in_project;
    }

    public void setRole_in_project(String role_in_project) {
        this.role_in_project.set(role_in_project);
    }

    public int getId_developer_project() {
        return id_developer_project.get();
    }

    public SimpleIntegerProperty id_developer_projectProperty() {
        return id_developer_project;
    }

    public void setId_developer_project(int id_developer_project) {
        this.id_developer_project.set(id_developer_project);
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

    public String getFIO() {
        return FIO.get();
    }

    public SimpleStringProperty FIOProperty() {
        return FIO;
    }

    public void setFIO(String FIO) {
        this.FIO.set(FIO);
    }

    public void addProjectTable(ProjectTable projectTable) {
        projectTables.add(projectTable);
    }

    public ArrayList<ProjectTable> getProjectTables() {
        return projectTables;
    }

    public void setProjectTables(ArrayList<ProjectTable> projectTables) {
        this.projectTables = projectTables;
    }

    public void addCommitCount(int commitCount) {
        this.commitCount.set(this.commitCount.get() + commitCount);
    }

    public void addLinesAdd(int linesAdd) {
        this.linesAdded.set(this.linesAdded.get() + linesAdd);
    }

    public void addLinesDelete(int linesDelete) {
        this.linesRemoved.set(this.linesRemoved.get() + linesDelete);
    }

    public void addNetContributiont(int netContribution) {
        this.netContribution.set(this.netContribution.get() + netContribution);
    }

    public Collection<Commit> getCommits() {
        return commits;
    }

    public void setCommits(Collection<Commit> commits) {
        this.commits = commits;
    }

    public void addCommits(Collection<Commit> commits) {
        this.commits.addAll(commits);
    }

    public double getRating() {
        return rating.get();
    }

    public SimpleDoubleProperty ratingProperty() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating.set(rating);
    }
}
