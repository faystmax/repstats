package com.oneandone.sales.svnstats;

import com.oneandone.sales.svnstats.model.ChangedPath;
import com.oneandone.sales.svnstats.model.File;
import com.oneandone.sales.svnstats.model.NodeKind;
import com.oneandone.sales.svnstats.model.Revision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileStats {

    private Map<String, File> files = new HashMap<String, File>();
    private Map<String, List<File>> fileTypes = new HashMap<String, List<File>>();
    public static final Pattern LINES_ADDED_PATTERN = Pattern.compile("^[+] ", Pattern.MULTILINE);
    public static final Pattern LINES_DELETED_PATTERN = Pattern.compile("^[-] ", Pattern.MULTILINE);
    public static final Pattern CHANGED_VERSION_PATTERN = Pattern.compile(
            "<groupId>(.*?)</groupId>\\s*" +
                    "<artifactId>(.*?)</artifactId>\\s*" +
                    "^-\\s*<version>.*\\s*" +
                    "^[+]\\s*<version>",
            Pattern.MULTILINE);

    public FileStats(List<Revision> revisions) {
        for (Revision revision : revisions) {
            for (ChangedPath path : revision.changedPaths()) {
                if (path.kind() != NodeKind.FILE) {
                    continue;
                }

                File file;
                if (files.containsKey(path.path())) {
                    file = files.get(path.path());
                    file.update(path);
                } else {
                    file = new File(path);
                    files.put(path.path(), file);
                }

                parseDiff(file, path.diff());
            }
        }

        for (File file : files.values()) {
            if (fileTypes.containsKey(file.type())) {
                fileTypes.get(file.type()).add(file);
            } else {
                List<File> fileTypeFiles = new ArrayList<File>();
                fileTypeFiles.add(file);
                fileTypes.put(file.type(), fileTypeFiles);
            }
        }
    }

    private void parseDiff(File file, String diff) {
        if (diff == null || diff.isEmpty()) {
            return;
        }

        file.linesAdded(countLines(diff, LINES_ADDED_PATTERN));
        file.linesDeleted(countLines(diff, LINES_DELETED_PATTERN));
        Matcher versionMatcher = CHANGED_VERSION_PATTERN.matcher(diff);
        while (versionMatcher.find()) {
            file.updatedDependency(versionMatcher.group(1) + "." + versionMatcher.group(2));
        }
    }

    private int countLines(String diff, Pattern pattern) {
        int linesAdded = 0;
        Matcher linesAddedMatcher = pattern.matcher(diff);
        while (linesAddedMatcher.find()) {
            linesAdded += 1;
        }
        return linesAdded;
    }

    public Map<String, File> files() {
        return files;
    }

    public Map<String, List<File>> fileTypes() {
        return fileTypes;
    }

}
