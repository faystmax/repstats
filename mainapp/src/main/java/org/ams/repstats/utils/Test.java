package org.ams.repstats.utils;

import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;

import java.util.List;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 13.05.2017
 * Time: 22:45
 */
public class Test {
    public static void main(final String[] args) throws Exception {
      /*  //Basic authentication
        GitHubClient client = new GitHubClient();
        client.setCredentials("faystmax", "");*/

        RepositoryService service1 = new RepositoryService();
        // service1.getClient().setCredentials("faystmax", "");

        Repository repository = service1.getRepository("Robpol86", "terminaltables");

        PullRequestService service = new PullRequestService();
        // service.getClient().setCredentials("faystmax", ");
        List<PullRequest> pullRequests = service.getPullRequests(repository, IssueService.STATE_CLOSED);

        pullRequests.addAll(service.getPullRequests(repository, IssueService.STATE_OPEN));
        for (Repository repo : service1.getRepositories("faystmax"))
            System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());

    }
}

