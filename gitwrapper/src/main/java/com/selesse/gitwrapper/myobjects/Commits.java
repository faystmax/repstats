package com.selesse.gitwrapper.myobjects;

import com.google.common.collect.Lists;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

/**
 * \brief Класс для построения коммита из RevCommit.
 * \author faystmax
 * \version 0.5
 * \date 18 февраля 2017 года
 * <p>
 * Представляет собой LOGGER для записи прочитанный файлов и одного статического метода
 * для построения Commit из внешнего класса RevCommit (org.eclipse.jgit.revwalk.RevCommit)
 */
class Commits {
    private static final Logger LOGGER = LoggerFactory.getLogger(Commits.class); ///< ссылка на логер

    /**
     * Возваращает коммит из (com.selesse.gitwrapper.myobjects) собранный из revCommit.
     * Является по сути конвертером преобразуя коммит из внешнего пакета в коммит из myobjects.
     *
     * @param repository ссылка на репозиторий
     * @param revCommit  ссылка на коммит (из внешнего пакета)
     * @return ссылку на коммит
     * @throws IOException ошибка при чтении репозитория
     */
    public static Commit fromRevCommit(Repository repository, RevCommit revCommit) throws IOException {
        List<GitFile> gitFileList = Lists.newArrayList();

        TreeWalk treeWalk = new TreeWalk(repository);
        treeWalk.addTree(revCommit.getTree());
        while (treeWalk.next()) {
            int objectIdIndex = 0;

            String pathString = treeWalk.getPathString();
            FileMode fileMode = treeWalk.getFileMode(objectIdIndex);

            LOGGER.debug("{} : {}", FileModes.asString(fileMode), pathString);

            if (treeWalk.isSubtree()) {
                treeWalk.enterSubtree();
            } else {
                ObjectId objectId = treeWalk.getObjectId(objectIdIndex);
                ObjectLoader objectLoader = repository.open(objectId);
                byte[] fileBytes = objectLoader.getBytes();

                GitFile gitFile = new GitFile(pathString, treeWalk.getFileMode(objectIdIndex), fileBytes);
                gitFileList.add(gitFile);
            }
        }

        String commitSHA = revCommit.toObjectId().getName();
        Author author = new Author(revCommit.getAuthorIdent());
        Author committer = new Author(revCommit.getCommitterIdent());

        Instant commitInstant = Instant.ofEpochSecond(revCommit.getCommitTime());
        ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(commitInstant, ZoneId.systemDefault());

        return new Commit(commitSHA, revCommit.getFullMessage(), zonedDateTime, author, committer, gitFileList);
    }
}
