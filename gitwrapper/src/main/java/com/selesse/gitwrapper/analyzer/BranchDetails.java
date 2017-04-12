package com.selesse.gitwrapper.analyzer;

import com.google.common.collect.*;
import com.selesse.gitwrapper.myobjects.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * \brief Содержит в себе всю основную информацию о ветке репозитория.
 * \author faystmax
 * \version 0.5
 * \date 18 февраля 2017 года
 * <p>
 * Содержит в себе всю информацию об авторах репозиториях их коммитах, файлах и т.п.
 */
public class BranchDetails {
    private static final Logger LOGGER = LoggerFactory.getLogger(BranchDetails.class);  ///< логгер

    private GitRepository repository;                               ///< Ссылка на репозиторий
    private final Branch branch;                                    ///< Ссылка на ветку
    private final List<Commit> commits;                             ///< Список коммитов
    private final List<GitFile> gitFileList;                        ///< Список файлов репозитория
    private long totalLinesAdded;                                   ///< Всего строк добавлено
    private long totalLinesRemoved;                                 ///< Всего строк удалено
    private Multimap<Author, Commit> authorToCommitMap;             ///< Список: Авторов - их коммитов
    private Multimap<Author, CommitDiff> authorToCommitDiffMap;     ///< Список: Автор - их изменённый файл

    /**
     * Инициализируем BranchDetails
     *
     * @param repository  ссылка на репозиторий
     * @param branch      ссылка на ветку
     * @param commits     список коммитов
     * @param gitFileList список файлов в репозитории
     */
    public BranchDetails(GitRepository repository, Branch branch, List<Commit> commits, List<GitFile> gitFileList) {
        this.repository = repository;
        this.branch = branch;
        this.commits = commits;
        this.gitFileList = gitFileList;

        Map<Author, Collection<Commit>> authorToCommitTreeMap = Maps.newTreeMap(getAuthorComparator());
        Map<Author, Collection<CommitDiff>> authorToCommitDiffTreeMap = Maps.newTreeMap(getAuthorComparator());

        this.authorToCommitMap = Multimaps.newListMultimap(authorToCommitTreeMap, Lists::newArrayList);
        this.authorToCommitDiffMap = Multimaps.newListMultimap(authorToCommitDiffTreeMap, Lists::newArrayList);

        computeMembers(commits);
    }

    /**
     * Необходим для инициализации BranchDetails.
     * Бежит по коммитов и инициализирует списки authorToCommitMap, authorToCommitDiffMap
     * и переменные totalLinesAdded и totalLinesRemoved.
     *
     * @param commits список коммитов
     */
    private void computeMembers(List<Commit> commits) {
        for (Commit commit : commits) {
            try {
                Author author = commit.getAuthor();
                authorToCommitMap.put(author, commit);

                List<CommitDiff> diffs = repository.getCommitDiffs(commit);
                for (CommitDiff diff : diffs) {
                    totalLinesAdded += diff.getLinesAdded();
                    totalLinesRemoved += diff.getLinesRemoved();

                    authorToCommitDiffMap.put(author, diff);
                }
            } catch (IOException e) {
                LOGGER.error("Error getting diffs for repository {} and commit {}", repository, commit);
            }
        }
    }

    /**
     * Возвращает список коммитов
     *
     * @return список коммитов
     */
    public List<Commit> getCommits() {
        return commits;
    }

    /**
     * Возвращает имя ветки
     *
     * @return имя ветки
     */
    public Branch getBranch() {
        return branch;
    }

    /**
     * Возвращает список файлов репозитория
     *
     * @return список файлов репозитория
     */
    public List<GitFile> getGitFileList() {
        return gitFileList;
    }

    /**
     * Возвращает всего кол-во строк в репозитории.
     * Бежит по файлам репозитория и суммирует кол-во строк.
     *
     * @return Всего кол-во строк
     */
    public long getTotalNumberOfLines() {
        long totalNumberOfLines = 0;

        for (GitFile gitFile : gitFileList) {
            if (!gitFile.isBinary()) {
                totalNumberOfLines += gitFile.getNumberOfLines();
            }
        }

        return totalNumberOfLines;
    }

    /**
     * Возвращает всего кол-во добавленных строк в репозиторий.
     *
     * @return кол-во добавленных строк в репозиторий
     */
    public long getTotalLinesAdded() {
        return totalLinesAdded;
    }

    /**
     * Возвращает всего кол-во удалённых строк в репозитории.
     *
     * @return всего кол-во удалённых строк в репозитории
     */
    public long getTotalLinesRemoved() {
        return totalLinesRemoved;
    }

    /**
     * Возвращает Список: Авторов - их коммитов
     *
     * @return Список: Авторов - их коммитов
     */
    public Multimap<Author, Commit> getAuthorToCommitMap() {
        return authorToCommitMap;
    }

    /**
     * Возвращает Список: Автор - их изменённый файл
     *
     * @return Список: Автор - их изменённый файл
     */
    public Multimap<Author, CommitDiff> getAuthorToCommitDiffsMap() {
        return authorToCommitDiffMap;
    }


    /**
     * Возвращает компаратор для сравнения авторов.
     * Необходим для сортировки #authorToCommitMap и authorToCommitDiffMap по авторам
     *
     * @return компоратор авторов
     */
    private Comparator<Author> getAuthorComparator() {
        return (o1, o2) -> ComparisonChain.start()
                .compare(o1.getName(), o2.getName())
                .compare(o1.getEmailAddress(), o2.getEmailAddress())
                .result();
    }

    /**
     * Закрываем репозиторий
     */
    public void closeRepository() {
        repository.close();
    }
}
