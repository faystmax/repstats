package org.ams.repstats.utils;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 26.05.2017
 * Time: 13:50
 */
public class DeveloperRating {
    //Коеффициеты
    private static double commitKoef = 0.3;
    private static double linesAddKoef = 0.7;
    private static double linesDelKoef = 0.5;
    private static double pullReqKoef = 0.5;
    private static double issuesKoef = 0.5;

    public static double calculateRating(int commitCount, int linesAdd, int linesDel, int pullReq, int issues) {
        return Utils.round(commitCount * commitKoef
                + linesAdd * linesAddKoef
                + linesDel * linesDelKoef
                + pullReq * pullReqKoef
                + issues * issuesKoef, 2);
    }

    public static void setKoef(double commitKoef, double linesAddKoef, double linesDelKoef, double pullReqKoef, double issuesKoef) {
        DeveloperRating.commitKoef = commitKoef;
        DeveloperRating.linesAddKoef = linesAddKoef;
        DeveloperRating.linesDelKoef = linesDelKoef;
        DeveloperRating.pullReqKoef = pullReqKoef;
        DeveloperRating.issuesKoef = issuesKoef;
    }

    public static double getCommitKoef() {
        return commitKoef;
    }

    public static void setCommitKoef(double commitKoef) {
        DeveloperRating.commitKoef = commitKoef;
    }

    public static double getLinesAddKoef() {
        return linesAddKoef;
    }

    public static void setLinesAddKoef(double linesAddKoef) {
        DeveloperRating.linesAddKoef = linesAddKoef;
    }

    public static double getLinesDelKoef() {
        return linesDelKoef;
    }

    public static void setLinesDelKoef(double linesDelKoef) {
        DeveloperRating.linesDelKoef = linesDelKoef;
    }

    public static double getPullReqKoef() {
        return pullReqKoef;
    }

    public static void setPullReqKoef(double pullReqKoef) {
        DeveloperRating.pullReqKoef = pullReqKoef;
    }

    public static double getIssuesKoef() {
        return issuesKoef;
    }

    public static void setIssuesKoef(double issuesKoef) {
        DeveloperRating.issuesKoef = issuesKoef;
    }
}
