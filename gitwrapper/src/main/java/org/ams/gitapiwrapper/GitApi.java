package org.ams.gitapiwrapper;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 14.05.2017
 * Time: 14:07
 */
public class GitApi {

    private String username;
    private String password;

    public GitApi() {
    }

    public GitApi(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public List<PullRequest> getAllPullRequests(String repositoryName, String ownerName) throws IOException {
        RepositoryService serviceRep = new RepositoryService();
        Repository repository = serviceRep.getRepository(ownerName, repositoryName);

        PullRequestService servicePull = new PullRequestService();

        List<PullRequest> pullRequests = servicePull.getPullRequests(repository, null);
        return pullRequests;
    }

    public List<Issue> getAllIssues(String repositoryName, String ownerName) throws IOException {
        RepositoryService serviceRep = new RepositoryService();
        Repository repository = serviceRep.getRepository(ownerName, repositoryName);

        IssueService issueService = new IssueService();

        List<Issue> issues = issueService.getIssues(repository, null);
        return issues;
    }
}
