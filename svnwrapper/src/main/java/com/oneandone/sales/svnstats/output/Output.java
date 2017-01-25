package com.oneandone.sales.svnstats.output;

import com.oneandone.sales.svnstats.FileStats;

public interface Output {

    public String print(FileStats fileStats, int numberOfFiles);

}
