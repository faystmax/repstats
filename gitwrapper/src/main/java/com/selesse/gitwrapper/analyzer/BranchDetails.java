package com.selesse.gitwrapper.analyzer;

import com.google.common.collect.*;
import com.selesse.gitwrapper.myobjects.*;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ListBranchCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.*;

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
     * Бежит по коммитам и инициализирует списки authorToCommitMap, authorToCommitDiffMap
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

    /**
     * Возвращает Репозиторий Git
     *
     * @return
     */
    public GitRepository getRepository() {
        return repository;
    }

    /**
     * Возвращает текущую ветку
     *
     * @return текущую ветку
     */
    public ArrayList<Branch> getListCurBranches() {

        ArrayList<Branch> curBranches = new ArrayList<Branch>();
        Repository repository = this.getRepository().getRepository();
        Git git = new Git(repository);

        LOGGER.info("Listing local branches:");
        try {
            List<Ref> call = null;
            call = git.branchList().call();

            for (Ref ref : call) {
                curBranches.add(new Branch(repository, ref.getName(), ref.getObjectId().getName()));
                LOGGER.info("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return curBranches;
    }

    /**
     * Возвращает все слитые ветки
     *
     * @return все слитые ветки
     */
    public ArrayList<Branch> getListMergedBranches() {

        ArrayList<Branch> mergedBranches = new ArrayList<Branch>();
        Repository repository = this.getRepository().getRepository();
        Git git = new Git(repository);

        try {
            List<Ref> call = null;
            call = git.branchList().call();

            LOGGER.info("Now including remote branches:");
            call = git.branchList().setListMode(ListBranchCommand.ListMode.ALL).call();
            for (Ref ref : call) {
                mergedBranches.add(new Branch(repository, ref.getName(), ref.getObjectId().getName()));
                LOGGER.info("Branch: " + ref + " " + ref.getName() + " " + ref.getObjectId().getName());
            }
        } catch (GitAPIException e) {
            e.printStackTrace();
        }
        return mergedBranches;
    }

    /**
     * Возвращает кол-во коммитов по месяцам
     *
     * @return кол-во коммитов по месяцам
     */
    public HashMap<Author, ArrayList<Integer>> getCommitsByMonths(ArrayList<Author> allAvtors) {
        HashMap<Author, ArrayList<Integer>> authorCommitMap = new HashMap<Author, ArrayList<Integer>>();

        try {
            for (Author author : allAvtors) {
                ArrayList<Integer> commitsByMonths = new ArrayList<Integer>();
                int daysInMonth = LocalDate.now().lengthOfMonth();
                for (int i = 0; i < 12; i++) {
                    commitsByMonths.add(0);
                }
                // Сохранили автора с его коммитами
                authorCommitMap.put(author, commitsByMonths);
            }

            ArrayList<Commit> revCommitList = null;
            revCommitList = (ArrayList) branch.getCommits();

            // Сортируем
            for (Commit aRevCommitList : revCommitList) {
                if (aRevCommitList.getCommitDateTime().getYear() != LocalDate.now().getYear()) {
                    continue;
                }
                int month = aRevCommitList.getCommitDateTime().getMonthValue();
                month--;

                ArrayList<Integer> commitsByMonths = authorCommitMap.get(aRevCommitList.getAuthor());
                if (commitsByMonths != null) {
                    commitsByMonths.set(month, commitsByMonths.get(month) + 1);
                }
            }
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
        return authorCommitMap;
    }

    /**
     * Возвращает кол-во коммитов за месяц
     *
     * @return кол-во коммитов за месяц
     */
    public HashMap<Author, ArrayList<Integer>> getCommitsByDaysInCurMonth(ArrayList<Author> allAvtors) {
        HashMap<Author, ArrayList<Integer>> authorCommitMap = new HashMap<Author, ArrayList<Integer>>();

        try {
            for (Author author : allAvtors) {
                ArrayList<Integer> commitsDaysInCurMonth = new ArrayList<Integer>();
                int daysInMonth = LocalDate.now().lengthOfMonth();
                for (int i = 0; i < daysInMonth; i++) {
                    commitsDaysInCurMonth.add(0);
                }
                // Сохранили автора с его коммитами
                authorCommitMap.put(author, commitsDaysInCurMonth);
            }

            int curNumberOfMonth = LocalDate.now().getMonthValue();

            ArrayList<Commit> revCommitList = null;
            revCommitList = (ArrayList) branch.getCommits();

            // Сортируем
            for (Commit aRevCommitList : revCommitList) {
                if (aRevCommitList.getCommitDateTime().getMonthValue() != curNumberOfMonth) {
                    continue;
                }
                int day = aRevCommitList.getCommitDateTime().getDayOfMonth();
                day--;

                ArrayList<Integer> commitsByDaysInCurMonth = authorCommitMap.get(aRevCommitList.getAuthor());
                if (commitsByDaysInCurMonth != null) {
                    commitsByDaysInCurMonth.set(day, commitsByDaysInCurMonth.get(day) + 1);
                }
            }
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
        return authorCommitMap;
    }

    public HashMap<Author, ArrayList<Integer>> getCommitsByWeek(ArrayList<Author> allAvtors) {
        HashMap<Author, ArrayList<Integer>> authorCommitMap = new HashMap<Author, ArrayList<Integer>>();

        try {
            for (Author author : allAvtors) {
                ArrayList<Integer> commitsByWeek = new ArrayList<Integer>();
                for (int i = 0; i < 7; i++) {
                    commitsByWeek.add(0);
                }

                // Сохранили автора с его коммитами
                authorCommitMap.put(author, commitsByWeek);
            }

            ArrayList<Commit> revCommitList = null;
            revCommitList = (ArrayList) branch.getCommits();

            // Сортируем
            for (Commit aRevCommitList : revCommitList) {

                WeekFields weekFields = WeekFields.of(Locale.getDefault());
                int commitWeekNumber = aRevCommitList.getCommitDateTime().get(weekFields.weekOfWeekBasedYear());
                int curWeekNumber = LocalDate.now().get(weekFields.weekOfWeekBasedYear());
                if (commitWeekNumber != curWeekNumber || aRevCommitList.getCommitDateTime().getYear() != LocalDate.now().getYear()) {
                    continue;
                }
                int dayOfWeek = aRevCommitList.getCommitDateTime().getDayOfWeek().getValue();
                dayOfWeek--;
                ArrayList<Integer> commitsByWeek = authorCommitMap.get(aRevCommitList.getAuthor());
                if (commitsByWeek != null) {
                    commitsByWeek.set(dayOfWeek, commitsByWeek.get(dayOfWeek) + 1);
                }
            }
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
        return authorCommitMap;
    }

    public HashMap<Author, HashMap<LocalDate, Integer>> getCommitsByCustomDate(ArrayList<Author> allAvtors) {
        HashMap<Author, HashMap<LocalDate, Integer>> authorCommitMap = new HashMap<Author, HashMap<LocalDate, Integer>>();

        try {
            for (Author author : allAvtors) {
                HashMap<LocalDate, Integer> commitsByDate = new HashMap<LocalDate, Integer>();

                // Сохранили автора с его коммитами
                authorCommitMap.put(author, commitsByDate);
            }

            ArrayList<Commit> revCommitList = null;
            revCommitList = (ArrayList) branch.getCommits();

            // Сортируем
            for (Commit aRevCommitList : revCommitList) {

                LocalDate commitDate = aRevCommitList.getCommitDateTime().toLocalDate();

                HashMap<LocalDate, Integer> commitsByDate = authorCommitMap.get(aRevCommitList.getAuthor());
                if (commitsByDate != null) {
                    if (commitsByDate.containsKey(commitDate)) {
                        commitsByDate.put(commitDate, commitsByDate.get(commitDate) + 1);
                    } else {
                        commitsByDate.put(commitDate, 1);
                    }
                }
            }
        } catch (GitAPIException | IOException e) {
            e.printStackTrace();
        }
        return authorCommitMap;
    }
}
