package com.oneandone.sales.svnstats.connectors.svnkit;

import com.oneandone.sales.svnstats.connectors.Repository;
import com.oneandone.sales.svnstats.model.ChangeType;
import com.oneandone.sales.svnstats.model.ChangedPath;
import com.oneandone.sales.svnstats.model.NodeKind;
import com.oneandone.sales.svnstats.model.Revision;
import org.tmatesoft.svn.core.*;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.internal.wc.DefaultSVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class SvnRepository implements Repository {

    private org.tmatesoft.svn.core.io.SVNRepository repository;
    private SVNClientManager manager;

    public SvnRepository(String path) {
        this(path, null, null);
    }

    public SvnRepository(String path, String username, String password) {
        setupLibrary();

        DefaultSVNOptions options = SVNWCUtil.createDefaultOptions(true);
        options.setAuthStorageEnabled(false);

        if (username != null && password != null) {
            ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(username, password);
            manager = SVNClientManager.newInstance(options, authManager);
        } else {
            manager = SVNClientManager.newInstance(options);
        }

        try {
            repository = manager.createRepository(SVNURL.parseURIEncoded(path), true);
        } catch (SVNException e) {
            throw new RuntimeException("error while connecting to svn repository '" + path + "': " + e.getMessage(), e);
        }
    }

    private void setupLibrary() {
        DAVRepositoryFactory.setup();
        SVNRepositoryFactoryImpl.setup();
        FSRepositoryFactory.setup();
    }

    @Override
    public long getDatedRevision(Date date) {
        try {
            return repository.getDatedRevision(date);
        } catch (SVNException e) {
            throw new RuntimeException("error while fetching dated revision: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Revision> fetchRevisions(long start, long end) {
        List<Revision> revisions = new ArrayList<Revision>();
        try {
            Collection<SVNLogEntry> logEntries = (Collection<SVNLogEntry>) repository.log(new String[]{""}, null, start, end, true, true);
            for (SVNLogEntry logEntry : logEntries) {
                revisions.add(parseRevision(logEntry));
            }
        } catch (SVNException e) {
            throw new RuntimeException("error while fetching the repository revisions [" + start + " > " + end + "]: " + e.getMessage(), e);
        }
        return revisions;
    }

    private Revision parseRevision(SVNLogEntry entry) {
        Revision revision = new Revision(entry.getRevision());
        revision.author(entry.getAuthor());
        revision.date(entry.getDate());
        revision.message(entry.getMessage());

        for (Object entryPath : entry.getChangedPaths().values()) {
            ChangedPath changedPath = parseChangedPath((SVNLogEntryPath) entryPath);
            if (changedPath.type() != ChangeType.ADDED) {
                changedPath.diff(diff(changedPath.path(), revision.id() - 1, revision.id()));
            }
            revision.addChangedPath(changedPath);
        }

        return revision;
    }

    private ChangedPath parseChangedPath(SVNLogEntryPath path) {
        ChangedPath changedPath = new ChangedPath(path.getPath());

        if (path.getKind().compareTo(SVNNodeKind.UNKNOWN) == 0) {
            if (path.getPath().contains(".")) {
                changedPath.kind(NodeKind.FILE);
            } else {
                changedPath.kind(NodeKind.UNKNOWN);
            }
        } else if (path.getKind().compareTo(SVNNodeKind.NONE) == 0) {
            changedPath.kind(NodeKind.NONE);
        } else if (path.getKind().compareTo(SVNNodeKind.FILE) == 0) {
            changedPath.kind(NodeKind.FILE);
        } else if (path.getKind().compareTo(SVNNodeKind.DIR) == 0) {
            changedPath.kind(NodeKind.DIR);
        }

        switch (path.getType()) {
            case 'A':
                changedPath.type(ChangeType.ADDED);
                break;
            case 'M':
                changedPath.type(ChangeType.MODIFIED);
                break;
            case 'D':
                changedPath.type(ChangeType.DELETED);
                break;
            default:
                changedPath.type(null);
        }

        return changedPath;
    }

    @Override
    public String diff(String path, long oldRevision, long newRevision) {
        try {
            SVNURL base = repository.getRepositoryRoot(false);
            SVNURL newFile = base.appendPath(path, false);
            SVNURL oldFile = newFile;
            ByteArrayOutputStream diffBytes = new ByteArrayOutputStream();
            manager.getDiffClient().doDiff(oldFile, SVNRevision.create(oldRevision),
                    newFile, SVNRevision.create(newRevision),
                    SVNDepth.INFINITY, false, diffBytes);
            return diffBytes.toString();
        } catch (SVNException e) {
            System.out.println("error while creating diff for '" + path + "': " + e.getMessage());
            return "";
        }
    }

}
