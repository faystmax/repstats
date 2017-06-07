package org.ams.gitapiwrapper;

import org.eclipse.egit.github.core.Issue;
import org.eclipse.egit.github.core.PullRequest;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.User;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.service.IssueService;
import org.eclipse.egit.github.core.service.PullRequestService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.service.UserService;

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
 *
 * Оболочка для org.eclipse.egit
 * используется для получения pull request`ов, issues и других данных
 */
public class GitApi {

    private static String username;            ///< логин github
    private static String password;            ///< пароль github

    private List<PullRequest> pullRequests;         ///< список pull request-ов
    private List<Issue> issues;                     ///< список issues-ов

    /**
     * Конструктор по умолчаниию
     */
    public GitApi() {
        username = "";
        password = "";
    }
    /**
     * Конструктор с параметрами
     *
     * @param username логин
     * @param password пароль
     */
    public GitApi(String username, String password) {
        GitApi.username = username;
        GitApi.password = password;
    }

    /**
     * Устанавливает логин и пароль
     *
     * @param username логин
     * @param password пароль
     */
    public static void setUsernameAndPasswoed(String username, String password) {
        GitApi.username = username;
        GitApi.password = password;
    }

    /**
     * Получаем логин
     *
     * @return логин
     */
    public static String getUsername() {
        return username;
    }

    /**
     * Получаем пароль
     *
     * @return пароль
     */
    public static String getPassword() {
        return password;
    }

    /**
     * Создаёт запрос на получение всех pull request`ов
     *
     * @param repositoryName имя репозитория
     * @param ownerName      имя владельца
     * @return List<PullRequest>
     * @throws Exception при ошибке ввода либо отсутствия подключения к интернету
     */
    public List<PullRequest> calcAllPullRequests(String repositoryName, String ownerName) throws Exception {
        if (this.pullRequests == null) {
            RepositoryService serviceRep = new RepositoryService();
            serviceRep.getClient().setCredentials(username, password);
            Repository repository = serviceRep.getRepository(ownerName, repositoryName);

            PullRequestService servicePull = new PullRequestService();
            servicePull.getClient().setCredentials(username, password);

            List<PullRequest> pullRequests = servicePull.getPullRequests(repository, IssueService.STATE_OPEN);
            pullRequests.addAll(servicePull.getPullRequests(repository, IssueService.STATE_CLOSED));

            //получанм полную информацию о pull request
            this.pullRequests = new ArrayList<>();
            for (PullRequest pullRequest : pullRequests) {
                this.pullRequests.add(servicePull.getPullRequest(repository, pullRequest.getNumber()));
            }


        }
        return this.pullRequests;
    }

    /**
     * Создаёт запрос на получение всех issues`ов
     *
     * @param repositoryName    имя репозитория
     * @param ownerName         имя владельца
     * @return List<Issue>
     * @throws Exception        при ошибке ввода либо отсутствия подключения к интернету
     */
    public List<Issue> calcAllIssues(String repositoryName, String ownerName) throws Exception {
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
                if (issue.getPullRequest().getUrl() == null) {  //значит это pull request
                    issuesWhithoutPull.add(issue);
                }
            }

            this.issues = issuesWhithoutPull;
        }
        return this.issues;
    }

    /**
     * Возвращает список pull request`ов полученных методом calcAllPullRequests
     *
     * @return List<PullRequest>
     */
    public List<PullRequest> getPullRequests() {
        return pullRequests;
    }

    /**
     * Возвращает список issues полученных методом calcAllIssues
     *
     * @return List<Issue>
     */
    public List<Issue> getIssues() {
        return issues;
    }

    /**
     * Считает количество слитых pull request`ов разработчиком
     *
     * @param repos     имя репозитория
     * @param owner     имя владельца
     * @param gitname   имя пользователя для поиска
     * @return int количество слитых pull request`ов разработчиком
     * @throws Exception при ошибке ввода либо отсутствия подключения к интернету
     */
    public int countUserMergePullRequests(String repos, String owner, String gitname) throws Exception {
        this.calcAllPullRequests(repos, owner);

        int mergeCount = 0;
        for (PullRequest pullRequest : pullRequests) {
            if (pullRequest.isMerged() == true && pullRequest.getMergedBy().getLogin().equals(gitname)) {
                mergeCount++;
            }
        }
        return mergeCount;
    }

    /**
     * Считает количество слитых pull request`ов всеми
     *
     * @param repos имя репозитория
     * @param owner имя владельца
     * @return int количество слитых pull request`ов разработчиком
     * @throws Exception при ошибке ввода либо отсутствия подключения к интернету
     */
    public int countUserMergePullRequests(String repos, String owner) throws Exception {
        this.calcAllPullRequests(repos, owner);

        int mergeCount = 0;
        for (PullRequest pullRequest : pullRequests) {
            if (pullRequest.isMerged() == true) {
                mergeCount++;
            }
        }
        return mergeCount;
    }


    /**
     * Считает количество pull-request, которые смерджили другие у данного разработчика
     *
     * @param repos     имя репозитория
     * @param owner     имя владельца
     * @param gitname   имя пользователя для поиска
     * @return int количество слитых pull request`ов разработчиком
     * @throws Exception при ошибке ввода либо отсутствия подключения к интернету
     */
    public int countMergedOtherPullRequests(String repos, String owner, String gitname) throws Exception {
        this.calcAllPullRequests(repos, owner);

        int mergedOtherPullRequests = 0;
        for (PullRequest pullRequest : pullRequests) {
            if (pullRequest.isMerged() == true && pullRequest.getUser().getLogin().equals(gitname)) {
                mergedOtherPullRequests++;
            }
        }
        return mergedOtherPullRequests;
    }

    /**
     * Количество pull-request, которые сделал разработчик, но другие их не смерджили
     *
     * @param repos     имя репозитория
     * @param owner     имя владельца
     * @param gitname   имя пользователя для поиска
     * @return int количество слитых pull request`ов разработчиком
     * @throws Exception при ошибке ввода либо отсутствия подключения к интернету
     */
    public int countNotMergedOtherPullRequests(String repos, String owner, String gitname) throws Exception {
        this.calcAllPullRequests(repos, owner);

        int notMergedOtherPullRequests = 0;
        for (PullRequest pullRequest : pullRequests) {
            if (pullRequest.isMerged() == false && pullRequest.getUser().getLogin().equals(gitname)) {
                notMergedOtherPullRequests++;
            }
        }
        return notMergedOtherPullRequests;
    }

    /**
     * Проверяет правильность ввода логина и пароля
     *
     * @param username имя пользователя
     * @param password пароль пользователя
     * @return boolean true данные верные, false иначе
     */
    public static boolean checkUsernameAndPass(String username, String password) {
        GitHubClient client = new GitHubClient();
        client.setCredentials(username, password);
        UserService uService = new UserService(client);
        try {
            User us = uService.getUser();
            String login = us.getLogin();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
