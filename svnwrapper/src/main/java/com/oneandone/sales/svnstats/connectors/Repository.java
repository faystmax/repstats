package com.oneandone.sales.svnstats.connectors;

import com.oneandone.sales.svnstats.model.Revision;

import java.util.Date;
import java.util.List;

public interface Repository {

    public List<Revision> fetchRevisions(long start, long end);

    public long getDatedRevision(Date date);

    public String diff(String path, long oldRevision, long newRevision);

}
