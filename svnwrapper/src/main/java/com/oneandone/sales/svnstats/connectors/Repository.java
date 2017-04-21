package com.oneandone.sales.svnstats.connectors;

import com.oneandone.sales.svnstats.model.Revision;

import java.util.Date;
import java.util.List;

public interface Repository {

    List<Revision> fetchRevisions(long start, long end);

    long getDatedRevision(Date date);

    String diff(String path, long oldRevision, long newRevision);

}
