package org.ams.repstats.utils;

import com.selesse.gitwrapper.myobjects.Author;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import org.ams.repstats.fortableview.ProjectTable;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 13.05.2017
 * Time: 10:46
 */
public class LineChartCreator {

    private static NumberAxis yAxis;
    private static DateAxis dateAxis;
    private static CategoryAxis xAxis;

    private static LineChart<Date, Number> dateLineChart;
    private static LineChart<String, Number> numberLineChart;
    public static ObservableList<String> options =
            FXCollections.observableArrayList(
                    "За неделю",
                    "За месяц",
                    "За год",
                    "Свой вариант"
            );


    public static LineChart<Date, Number> createDateLineChart(HashMap<Author, HashMap<LocalDate, Integer>> commitsByCustomDate,
                                                              LocalDate start, LocalDate end) {
        yAxis = new NumberAxis();
        Date dateStart = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateEnd = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());
        dateAxis = new DateAxis(dateStart, dateEnd);

        dateLineChart = new LineChart<>(dateAxis, yAxis);

        dateLineChart.setTitle("Все коммиты");
        for (Author author : commitsByCustomDate.keySet()) {
            HashMap<LocalDate, Integer> commitsByCustom = commitsByCustomDate.get(author);
            XYChart.Series<Date, Number> authorSeries = new XYChart.Series<Date, Number>();

            for (LocalDate localdate : commitsByCustom.keySet()) {
                if (localdate.isAfter(start) && localdate.isBefore(end)) {
                    Date date = Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    authorSeries.getData().add(new XYChart.Data<>(date, commitsByCustom.get(localdate)));
                }
            }
            authorSeries.setName(author.getName());
            dateLineChart.getData().add(authorSeries);
        }

        return dateLineChart;
    }

    public static LineChart<String, Number> createNumberLineChart(String selected, HashMap<Author, ArrayList<Integer>> authorCommits) {
        yAxis = new NumberAxis();
        xAxis = new CategoryAxis();
        numberLineChart = new LineChart<>(xAxis, yAxis);
        numberLineChart.setTitle("Все коммиты");
        if (selected.equals(LineChartCreator.options.get(0))) {
            for (Author author : authorCommits.keySet()) {
                ArrayList<Integer> commitsByWeek = authorCommits.get(author);
                XYChart.Series authorSeries = new XYChart.Series();
                authorSeries.getData().add(new XYChart.Data("Пн", commitsByWeek.get(0)));
                authorSeries.getData().add(new XYChart.Data("Вт", commitsByWeek.get(1)));
                authorSeries.getData().add(new XYChart.Data("Ср", commitsByWeek.get(2)));
                authorSeries.getData().add(new XYChart.Data("Чт", commitsByWeek.get(3)));
                authorSeries.getData().add(new XYChart.Data("Пт", commitsByWeek.get(4)));
                authorSeries.getData().add(new XYChart.Data("Сб", commitsByWeek.get(5)));
                authorSeries.getData().add(new XYChart.Data("Вс", commitsByWeek.get(6)));

                authorSeries.setName(author.getName());
                numberLineChart.getData().add(authorSeries);
            }
        } else if (selected.equals(LineChartCreator.options.get(1))) {
            for (Author author : authorCommits.keySet()) {
                ArrayList<Integer> commitsByDaysInCurMonth = authorCommits.get(author);
                XYChart.Series authorSeries = new XYChart.Series();

                for (int i = 0; i < commitsByDaysInCurMonth.size(); i++) {
                    authorSeries.getData().add(new XYChart.Data(String.valueOf(i + 1), commitsByDaysInCurMonth.get(i)));
                }

                authorSeries.setName(author.getName());
                numberLineChart.getData().add(authorSeries);
            }
        } else if (selected.equals(LineChartCreator.options.get(2))) {
            for (Author author : authorCommits.keySet()) {
                ArrayList<Integer> commitsByMonth = authorCommits.get(author);
                XYChart.Series authorSeries = new XYChart.Series();

                authorSeries.getData().add(new XYChart.Data("Янв", commitsByMonth.get(0)));
                authorSeries.getData().add(new XYChart.Data("Фев", commitsByMonth.get(1)));
                authorSeries.getData().add(new XYChart.Data("Мар", commitsByMonth.get(2)));
                authorSeries.getData().add(new XYChart.Data("Апр", commitsByMonth.get(3)));
                authorSeries.getData().add(new XYChart.Data("Май", commitsByMonth.get(4)));
                authorSeries.getData().add(new XYChart.Data("Июнь", commitsByMonth.get(5)));
                authorSeries.getData().add(new XYChart.Data("Июль", commitsByMonth.get(6)));
                authorSeries.getData().add(new XYChart.Data("Авг", commitsByMonth.get(7)));
                authorSeries.getData().add(new XYChart.Data("Сен", commitsByMonth.get(8)));
                authorSeries.getData().add(new XYChart.Data("Окт", commitsByMonth.get(9)));
                authorSeries.getData().add(new XYChart.Data("Ноя", commitsByMonth.get(10)));
                authorSeries.getData().add(new XYChart.Data("Дек", commitsByMonth.get(11)));

                authorSeries.setName(author.getName());
                numberLineChart.getData().add(authorSeries);
            }
        }
        return numberLineChart;
    }


    public static LineChart<String, Number> createNumberLineChart(String selected, ObservableList items) {
        yAxis = new NumberAxis();
        xAxis = new CategoryAxis();
        numberLineChart = new LineChart<>(xAxis, yAxis);
        numberLineChart.setTitle("Все коммиты");
        if (selected.equals(LineChartCreator.options.get(0))) {

            for (Object project : items) {
                ProjectTable selectedProject = (ProjectTable) project;
                ArrayList<Integer> commitsByWeek = selectedProject.getCommitsByWeek();
                XYChart.Series authorSeries = new XYChart.Series();
                authorSeries.getData().add(new XYChart.Data("Пн", commitsByWeek.get(0)));
                authorSeries.getData().add(new XYChart.Data("Вт", commitsByWeek.get(1)));
                authorSeries.getData().add(new XYChart.Data("Ср", commitsByWeek.get(2)));
                authorSeries.getData().add(new XYChart.Data("Чт", commitsByWeek.get(3)));
                authorSeries.getData().add(new XYChart.Data("Пт", commitsByWeek.get(4)));
                authorSeries.getData().add(new XYChart.Data("Сб", commitsByWeek.get(5)));
                authorSeries.getData().add(new XYChart.Data("Вс", commitsByWeek.get(6)));

                authorSeries.setName(selectedProject.getName());
                numberLineChart.getData().add(authorSeries);
            }

        } else if (selected.equals(LineChartCreator.options.get(1))) {

            for (Object project : items) {
                ProjectTable selectedProject = (ProjectTable) project;
                ArrayList<Integer> commitsByDaysInCurMonth = selectedProject.getCommitsByDaysInCurMonth();
                XYChart.Series authorSeries = new XYChart.Series();

                for (int i = 0; i < commitsByDaysInCurMonth.size(); i++) {
                    authorSeries.getData().add(new XYChart.Data(String.valueOf(i + 1), commitsByDaysInCurMonth.get(i)));
                }

                authorSeries.setName(selectedProject.getName());
                numberLineChart.getData().add(authorSeries);
            }

        } else if (selected.equals(LineChartCreator.options.get(2))) {
            for (Object project : items) {
                ProjectTable selectedProject = (ProjectTable) project;
                ArrayList<Integer> commitsByMonth = selectedProject.getCommitsByMonths();
                XYChart.Series authorSeries = new XYChart.Series();

                authorSeries.getData().add(new XYChart.Data("Янв", commitsByMonth.get(0)));
                authorSeries.getData().add(new XYChart.Data("Фев", commitsByMonth.get(1)));
                authorSeries.getData().add(new XYChart.Data("Мар", commitsByMonth.get(2)));
                authorSeries.getData().add(new XYChart.Data("Апр", commitsByMonth.get(3)));
                authorSeries.getData().add(new XYChart.Data("Май", commitsByMonth.get(4)));
                authorSeries.getData().add(new XYChart.Data("Июнь", commitsByMonth.get(5)));
                authorSeries.getData().add(new XYChart.Data("Июль", commitsByMonth.get(6)));
                authorSeries.getData().add(new XYChart.Data("Авг", commitsByMonth.get(7)));
                authorSeries.getData().add(new XYChart.Data("Сен", commitsByMonth.get(8)));
                authorSeries.getData().add(new XYChart.Data("Окт", commitsByMonth.get(9)));
                authorSeries.getData().add(new XYChart.Data("Ноя", commitsByMonth.get(10)));
                authorSeries.getData().add(new XYChart.Data("Дек", commitsByMonth.get(11)));

                authorSeries.setName(selectedProject.getName());
                numberLineChart.getData().add(authorSeries);
            }
        }
        return numberLineChart;
    }

    public static LineChart<Date, Number> createDateLineChart(String selected, ObservableList items, LocalDate start, LocalDate end) {
        yAxis = new NumberAxis();
        Date dateStart = Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date dateEnd = Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant());
        dateAxis = new DateAxis(dateStart, dateEnd);

        dateLineChart = new LineChart<>(dateAxis, yAxis);
        dateLineChart.setTitle("Все коммиты");

        for (Object project : items) {
            ProjectTable selectedProject = (ProjectTable) project;
            HashMap<LocalDate, Integer> commitsByCustom = selectedProject.getCommitsByCustomDate();
            XYChart.Series<Date, Number> authorSeries = new XYChart.Series<Date, Number>();

            for (LocalDate localdate : commitsByCustom.keySet()) {
                if (localdate.isAfter(start) && localdate.isBefore(end)) {
                    Date date = Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant());
                    authorSeries.getData().add(new XYChart.Data<>(date, commitsByCustom.get(localdate)));
                }
            }

            authorSeries.setName(selectedProject.getName());
            dateLineChart.getData().add(authorSeries);
        }

        return dateLineChart;
    }
}
