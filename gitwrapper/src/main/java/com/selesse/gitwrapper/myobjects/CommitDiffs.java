package com.selesse.gitwrapper.myobjects;

import com.google.common.collect.Lists;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.diff.RawTextComparator;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.EmptyTreeIterator;
import org.eclipse.jgit.util.io.DisabledOutputStream;

import java.io.IOException;
import java.util.List;

/**
 * \brief Класс необходимый для создания списка изменённых файлов в указанном коммите.
 * \author faystmax
 * \version 0.5
 * \date 18 февраля 2017 года
 * <p>
 * Представляет собой 2 статических метода, реализующих функцию создания списка CommitDiff.
 */
class CommitDiffs {

    /**
     * Возвращает список изменнённых файлов в указанном коммите.
     * По сути преобразует DiffEntry(org.eclipse.jgit.diff.DiffEntry)
     * в CommitDiff(com.selesse.gitwrapper.myobjects).
     *
     * @param repository ссылка на репозиторий
     * @param commit     коммит из которого брать изменённые файлы
     * @return список изменённых файлов
     * @throws IOException ошибка при чтении репозитория
     * @see #getDiffs(Repository, Commit)
     */
    static List<CommitDiff> getCommitDiffs(Repository repository, Commit commit) throws IOException {
        List<CommitDiff> commitDiffList = Lists.newArrayList();

        List<DiffEntry> diffEntries = getDiffs(repository, commit);
        for (DiffEntry diffEntry : diffEntries) {
            CommitDiff commitDiff = new CommitDiff(repository, diffEntry);
            commitDiffList.add(commitDiff);
        }

        return commitDiffList;
    }

    /**
     * Возвращает список DiffEntry.
     * DiffEntry - сущность изменённого файла при коммите.
     *
     * @param repository ссылка на репозиторий
     * @param commit     коммит из которого брать изменённые файлы
     * @return список изменённых файлов(DiffEntry) из внешнего пакета
     * @throws IOException ошибка при чтении репозитория
     */
    private static List<DiffEntry> getDiffs(Repository repository, Commit commit) throws IOException {
        List<DiffEntry> diffEntries = Lists.newArrayList();

        RevWalk revWalk = new RevWalk(repository);

        RevCommit parent = null;
        ObjectId id = ObjectId.fromString(commit.getSHA());
        RevCommit revCommit = revWalk.parseCommit(id);
        if (revCommit.getParentCount() > 0 && revCommit.getParent(0) != null) {
            parent = revWalk.parseCommit(revCommit.getParent(0).getId());
        }

        DiffFormatter diffFormatter = new DiffFormatter(DisabledOutputStream.INSTANCE);
        diffFormatter.setRepository(repository);
        diffFormatter.setDiffComparator(RawTextComparator.DEFAULT);
        diffFormatter.setDetectRenames(true);

        List<DiffEntry> diffs;
        if (parent == null) {
            EmptyTreeIterator emptyTreeIterator = new EmptyTreeIterator();
            CanonicalTreeParser canonicalTreeParser =
                    new CanonicalTreeParser(null, revWalk.getObjectReader(), revCommit.getTree());
            diffs = diffFormatter.scan(emptyTreeIterator, canonicalTreeParser);
        } else {
            diffs = diffFormatter.scan(parent.getTree(), revCommit.getTree());
        }

        for (DiffEntry diff : diffs) {
            diffEntries.add(diff);
        }

        revWalk.release();

        return diffEntries;
    }
}
