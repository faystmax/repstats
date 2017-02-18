package com.selesse.gitwrapper.analyzer;

import com.selesse.gitwrapper.myobjects.*;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * \brief Анализатор ветки репозитория git.
 * \author faystmax
 * \version 0.5
 * \date 18 февраля 2017 года
 *
 * Является по сути оболочкой для BranchDetails, хранит в себе минимум информации о репозитории
 * и предназначен для создания конкретного BranchDetails.
 */
public class BranchAnalyzer {

    private static final Logger LOGGER = LoggerFactory.getLogger(BranchAnalyzer.class); ///< ссылка на логер

    private final File gitRoot;         ///< ссылка корень директории git
    private final String branchName;    ///< имя ветки

    /**
     * Инициализирует BranchAnalyzer.
     * Не проверяет наличие указанной ветки в репозитории.
     *
     * @param gitRoot    ссылке на корень директории git
     * @param branchName имя ветки
     */
    public BranchAnalyzer(File gitRoot, String branchName) {
        this.gitRoot = gitRoot;
        this.branchName = branchName;
    }

    /**
     * Возвращает ссылку на корень директории git
     *
     * @return ссылку на корень директории git
     */
    public File getGitRoot() {
        return gitRoot;
    }

    /**
     * Возвращает BranchDetails основываясь на gitRoot и branchName в теле класса.
     * BranchDetails представляет собой подробное описание всей ветки в выбранном репозитории.
     *
     * @return BranchDetails - подробное описание ветки.
     * @throws IOException     ошибка при чтениии репозитория.
     * @throws GitAPIException ошибка при взаимодествии с git репозиторием через api.
     */
    public BranchDetails getBranchDetails() throws IOException, GitAPIException {
        LOGGER.info("Looking for a Git repository in {}", gitRoot.getAbsolutePath());
        GitRepository repository = GitRepositoryReader.loadRepository(gitRoot);

        Branch branch = repository.getBranch(branchName);
        List<Commit> commits = branch.getCommits();
        LOGGER.info("Found {} commits on {}", commits.size(), branch.getName());

        Commit commit = GitRepositoryReader.loadLastCommit(repository, branch);
        List<GitFile> filesChanged = commit.getFilesChanged();

        return new BranchDetails(repository, branch, commits, filesChanged);
    }

}
