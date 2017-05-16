package org.ams.gitapiwrapper;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 14.05.2017
 * Time: 14:07
 */
public class GitApi {

    private String username = "";
    private String password = "";

    private List<PullRequest> pullRequests;
    private List<Issue> issues;

    public GitApi() {
    }

    public GitApi(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public List<PullRequest> calcAllPullRequests(String repositoryName, String ownerName) throws IOException {
        if (pullRequests == null) {
            RepositoryService serviceRep = new RepositoryService();
            serviceRep.getClient().setCredentials(username, password);
            Repository repository = serviceRep.getRepository(ownerName, repositoryName);

            PullRequestService servicePull = new PullRequestService();
            servicePull.getClient().setCredentials(username, password);

            this.pullRequests = servicePull.getPullRequests(repository, IssueService.STATE_OPEN);
            pullRequests.addAll(servicePull.getPullRequests(repository, IssueService.STATE_CLOSED));
        }
        return pullRequests;
    }

    public List<Issue> calcAllIssues(String repositoryName, String ownerName) throws IOException {
        if (issues == null) {
            RepositoryService serviceRep = new RepositoryService();
            serviceRep.getClient().setCredentials(username, password);
            Repository repository = serviceRep.getRepository(ownerName, repositoryName);

            IssueService issueService = new IssueService();
            issueService.getClient().setCredentials(username, password);

            PullRequestService servicePull = new PullRequestService();
            servicePull.getClient().setCredentials(username, password);

            Map<String, String> params = new HashMap<String, String>();
            params.put(IssueService.FILTER_STATE, IssueService.STATE_OPEN);
            List<Issue> issues = issueService.getIssues(repository, params);

            Map<String, String> paramsClosed = new HashMap<String, String>();
            paramsClosed.put(IssueService.FILTER_STATE, IssueService.STATE_CLOSED);
            issues.addAll(issueService.getIssues(repository, paramsClosed));

            List<Issue> issuesWhithoutPull = new ArrayList<>();
            for (Issue issue : issues) {
                if (issue.getPullRequest().getUrl() == null) {  //значит это pull rquest
                    issuesWhithoutPull.add(issue);
                }
            }

            this.issues = issuesWhithoutPull;
        }
        return this.issues;
    }

    public List<PullRequest> getPullRequests() {
        return pullRequests;
    }

    public List<Issue> getIssues() {
        return issues;
    }

}
