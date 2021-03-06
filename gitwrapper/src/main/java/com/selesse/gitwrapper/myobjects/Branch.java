package com.selesse.gitwrapper.myobjects;

import com.google.common.collect.Lists;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.filter.TreeFilter;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;

/**
 * \brief Ветка коммитов Git.
 * \author faystmax
 * \version 0.5
 * \date 18 февраля 2017 года
 * <p>
 * Хранит необходимую информацию о конкретной ветке репозитория.
 */
public class Branch {

    private Repository repository;          ///< Ссылка на репозиторий
    private String name;                    ///< Имя ветки
    private String id;                      ///< id ветки
    /**
     * Коммиты ветки с учётом заданного промежутка времени(если промежуток не задан revCommitList=revAllCommitList)
     */
    private List<Commit> revCommitList;
    private List<Commit> revAllCommitList;  ///< Все коммиты этой ветки

    /**
     * Инициализирует ветку по её имени и репозиторию.
     * Не проверяет наличие указанной ветки в выбранной репозитории.
     * Просто записывает имя ветки и ссылку на репозиторий.
     *
     * @param repository ссылка на репозиторий.
     * @param name       имя ветки.
     */
    Branch(Repository repository, String name) {
        this.repository = repository;
        this.name = name;
    }

    /**
     * Аналогично верхнему, но вместе с id
     *
     * @param repository ссылка на репозиторий.
     * @param name       имя ветки.
     * @param id         id ветки.
     */
    public Branch(Repository repository, String name, String id) {
        this.repository = repository;
        this.name = name;
        this.id = id;
    }

    /**
     * Извлекаем коммиты указанной ветки из репозитория.
     *
     * @return список коммитов, выбранной ветки в выбранном репозитории.
     * Вернёт пустой List, если ветки нет вы выбранном репозитории.
     * @throws GitAPIException ошибка при взаимодействии с Git Api.
     * @throws IOException     ошибка чтения репозитория.
     */
    public List<Commit> getCommits() throws GitAPIException, IOException {
        if (revCommitList == null) {
            revAllCommitList = Lists.newArrayList();
            revCommitList = revAllCommitList;

            Git git = new Git(repository);
            RevWalk walk = new RevWalk(repository);

            List<Ref> branches = git.branchList().call();

            for (Ref branch : branches) {
                String currentBranchName = branch.getName();
                String desiredBranch = Constants.R_HEADS + name;
                if (!desiredBranch.equals(currentBranchName)) {
                    continue;
                }

                Iterable<RevCommit> commits = git.log().all().call();

                for (RevCommit commit : commits) {
                    boolean foundInThisBranch = false;

                    RevCommit targetCommit = walk.parseCommit(repository.resolve(commit.getName()));
                    for (Map.Entry<String, Ref> stringRefEntry : repository.getAllRefs().entrySet()) {
                        if (stringRefEntry.getKey().startsWith(Constants.R_HEADS)) {
                            Ref ref = stringRefEntry.getValue();
                            if (walk.isMergedInto(targetCommit, walk.parseCommit(ref.getObjectId()))) {
                                String foundInBranch = ref.getName();
                                if (currentBranchName.equals(foundInBranch)) {
                                    foundInThisBranch = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (foundInThisBranch) {
                        List<DiffEntry> diffs = null;

                        if (commit.getParentCount() != 0) {
                            DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
                            df.setRepository(repository);
                            df.setDiffComparator(RawTextComparator.DEFAULT);
                            df.setDetectRenames(true);
                            RevCommit parent = walk.parseCommit(commit.getParent(0).getId());
                            diffs = df.scan(parent.getTree(), commit.getTree());
                        }

                        // bug with update funct.xml
                        try {
                            revAllCommitList.add(Commits.fromRevCommit(repository, commit, diffs));
                        } catch (Exception e) {
                            revAllCommitList.add(Commits.fromRevCommit(repository, commit.getParent(0), diffs));
                        }
                    }
                }
            }
        }
        return revCommitList;
    }

    /**
     * Аналогично верхней ветке, но с огранисением по дате
     * Извлекаем коммиты указанной ветки из репозитория.
     *
     * @return список коммитов, выбранной ветки в выбранном репозитории.
     * Вернёт пустой List, если ветки нет вы выбранном репозитории.
     * @throws GitAPIException ошибка при взаимодействии с Git Api.
     * @throws IOException     ошибка чтения репозитория.
     */
    public List<Commit> getCommits(LocalDate start, LocalDate end) throws GitAPIException, IOException {
        if (revCommitList == null) {
            revCommitList = Lists.newArrayList();

            Git git = new Git(repository);
            RevWalk walk = new RevWalk(repository);
            walk.setTreeFilter(TreeFilter.ANY_DIFF);

            List<Ref> branches = git.branchList().call();

            for (Ref branch : branches) {
                String currentBranchName = branch.getName();
                String desiredBranch = Constants.R_HEADS + name;
                if (!desiredBranch.equals(currentBranchName)) {
                    continue;
                }

                Iterable<RevCommit> commits = git.log().all().call();

                for (RevCommit commit : commits) {
                    boolean foundInThisBranch = false;

                    LocalDate commitDate = commit.getAuthorIdent().getWhen().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    if (commitDate.isBefore(start) || commitDate.isAfter(end)) {          // Пропускаем коммит
                        continue;
                    }

                    RevCommit targetCommit = walk.parseCommit(repository.resolve(commit.getName()));
                    for (Map.Entry<String, Ref> stringRefEntry : repository.getAllRefs().entrySet()) {
                        if (stringRefEntry.getKey().startsWith(Constants.R_HEADS)) {
                            Ref ref = stringRefEntry.getValue();
                            if (walk.isMergedInto(targetCommit, walk.parseCommit(ref.getObjectId()))) {
                                String foundInBranch = ref.getName();
                                if (currentBranchName.equals(foundInBranch)) {
                                    foundInThisBranch = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (foundInThisBranch) {

                        List<DiffEntry> diffs = null;

                        if (commit.getParentCount() != 0) {
                            DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
                            df.setRepository(repository);
                            df.setDiffComparator(RawTextComparator.DEFAULT);
                            df.setDetectRenames(true);
                            RevCommit parent = walk.parseCommit(commit.getParent(0).getId());
                            diffs = df.scan(parent.getTree(), commit.getTree());
                        }

                        // bug with update funct.xml
                        try {
                            revCommitList.add(Commits.fromRevCommit(repository, commit, diffs));
                        } catch (Exception e) {
                            revCommitList.add(Commits.fromRevCommit(repository, commit.getParent(0), diffs));
                        }
                    }
                }
            }
        }
        return revCommitList;
    }

    /**
     * Извлекаем все коммиты указанной ветки из репозитория.
     *
     * @return список коммитов, выбранной ветки в выбранном репозитории.
     * Вернёт пустой List, если ветки нет вы выбранном репозитории.
     * @throws GitAPIException ошибка при взаимодействии с Git Api.
     * @throws IOException     ошибка чтения репозитория.
     */
    public List<Commit> getAllCommits() throws GitAPIException, IOException {
        if (revAllCommitList == null) {
            revAllCommitList = Lists.newArrayList();

            Git git = new Git(repository);
            RevWalk walk = new RevWalk(repository);
            walk.setTreeFilter(TreeFilter.ANY_DIFF);

            List<Ref> branches = git.branchList().call();

            for (Ref branch : branches) {
                String currentBranchName = branch.getName();
                String desiredBranch = Constants.R_HEADS + name;
                if (!desiredBranch.equals(currentBranchName)) {
                    continue;
                }

                Iterable<RevCommit> commits = git.log().all().call();

                for (RevCommit commit : commits) {
                    boolean foundInThisBranch = false;

                    RevCommit targetCommit = walk.parseCommit(repository.resolve(commit.getName()));
                    for (Map.Entry<String, Ref> stringRefEntry : repository.getAllRefs().entrySet()) {
                        if (stringRefEntry.getKey().startsWith(Constants.R_HEADS)) {
                            Ref ref = stringRefEntry.getValue();
                            if (walk.isMergedInto(targetCommit, walk.parseCommit(ref.getObjectId()))) {
                                String foundInBranch = ref.getName();
                                if (currentBranchName.equals(foundInBranch)) {
                                    foundInThisBranch = true;
                                    break;
                                }
                            }
                        }
                    }

                    if (foundInThisBranch) {

                        List<DiffEntry> diffs = null;

                        if (commit.getParentCount() != 0) {
                            DiffFormatter df = new DiffFormatter(DisabledOutputStream.INSTANCE);
                            df.setRepository(repository);
                            df.setDiffComparator(RawTextComparator.DEFAULT);
                            df.setDetectRenames(true);
                            RevCommit parent = walk.parseCommit(commit.getParent(0).getId());
                            diffs = df.scan(parent.getTree(), commit.getTree());
                        }

                        // bug with update funct.xml
                        try {
                            revAllCommitList.add(Commits.fromRevCommit(repository, commit, diffs));
                        } catch (Exception e) {
                            revAllCommitList.add(Commits.fromRevCommit(repository, commit.getParent(0), diffs));
                        }
                    }
                }
            }
        }
        return revAllCommitList;
    }


    /**
     * Возвращает имя ветки.
     *
     * @return имя ветки.
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает id ветки.
     *
     * @return id ветки.
     */
    public String getId() {
        return id;
    }
}
