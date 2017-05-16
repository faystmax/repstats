package org.ams.repstats.utils;

import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;

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

        Repository TicTacToe = service1.getRepository("faystmax", "Tic-tac-toe");

        PullRequestService service = new PullRequestService();
        // service.getClient().setCredentials("faystmax", ");
        service.getPullRequests(TicTacToe, null);
        for (Repository repo : service1.getRepositories("faystmax"))
            System.out.println(repo.getName() + " Watchers: " + repo.getWatchers());

    }
}

