package com.oneandone.sales.svnstats.output;

import com.oneandone.sales.svnstats.FileStats;
import com.oneandone.sales.svnstats.model.File;

import java.util.*;

public class ConsoleOutput implements Output {

    public String print(FileStats fileStats, int numberOfFiles) {
        StringBuilder result = new StringBuilder();
        result.append("Changed Files: ").append(fileStats.files().size()).append("\n");
        result.append("\n");

        for (String type : fileStats.fileTypes().keySet()) {
            result.append(type).append("\n");
            List<File> fileTypeFiles = fileStats.fileTypes().get(type);
            printCounts(result, fileTypeFiles);
            printFiles(result, fileTypeFiles, numberOfFiles);

        }

        System.out.println(result.toString());
        return result.toString();
    }

    private void printCounts(StringBuilder result, List<File> fileTypeFiles) {
        int totalChanges = 0;
        int added = 0;
        int modified = 0;
        int deleted = 0;

        for (File file : fileTypeFiles) {
            totalChanges += file.timesChanged();
            added += file.timesAdded();
            modified += file.timesModified();
            deleted += file.timesDeleted();
        }

        result.append("   Affected Files: ").append(fileTypeFiles.size()).append("\n");
        result.append("   Changes in Total: ").append(totalChanges).append("\n");
        result.append("   Added: ").append(added).append("\n");
        result.append("   Modified: ").append(modified).append("\n");
        result.append("   Deleted: ").append(deleted).append("\n");
        result.append("\n");
    }

    private void printFiles(StringBuilder result, List<File> fileTypeFiles, int numberOfFiles) {
        if (numberOfFiles > 0) {
            result.append("   Changed Files:\n");

            File[] fileArray = fileTypeFiles.toArray(new File[fileTypeFiles.size()]);
            Arrays.sort(fileArray, new Comparator<File>() {
                public int compare(File o1, File o2) {
                    return o2.timesChanged() - o1.timesChanged();
                }
            });

            int fileCount = 0;
            for (File file : fileArray) {
                if (fileCount >= numberOfFiles) {
                    break;
                }
                fileCount += 1;
                result.append("      ")
                        .append(file.linesAdded())
                        .append(" (A) - ")
                        .append(file.linesDeleted())
                        .append(" (D) - ")
                        .append(file.timesChanged())
                        .append(" (C) - ")
                        .append(file.path())
                        .append("\n");

                if (file.dependenciesUpdated().size() > 0) {
                    result.append("        Updated Dependencies:\n");
                    Map<String, Integer> dependenciesCount = new HashMap<String, Integer>();
                    for (String dependency : file.dependenciesUpdated()) {
                        if (dependenciesCount.containsKey(dependency)) {
                            dependenciesCount.put(dependency, dependenciesCount.get(dependency) + 1);
                        } else {
                            dependenciesCount.put(dependency, Integer.valueOf(1));
                        }
                    }
                    for (Map.Entry<String, Integer> dependencyCount : dependenciesCount.entrySet()) {
                        result.append("          ")
                                .append(dependencyCount.getValue())
                                .append(" - ")
                                .append(dependencyCount.getKey())
                                .append("\n");
                    }
                }
            }
            result.append("\n");
        }
    }
}
