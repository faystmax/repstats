package com.selesse.gitwrapper.myobjects;

import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

/**
 * \brief Класс предназначен для чтения репозитоия.
 * \author faystmax
 * \version 0.5
 * \date 18 февраля 2017 года
 * <p>
 * Не хранит в себе никакой информации.Представляет собой набор
 * статичных методов для взаимодействия с репозиторием.
 */
public class GitRepositoryReader {

    /**
     * Проверка казанного пути на git репозиторий.
     * Проверяет является ли указанный путь папкой и есть ли в нём
     * скрытка директория .git
     *
     * @param path полный путь до репозитория
     * @return истинна вверный путь,иначе ложь
     */
    public static boolean isValidGitRoot(String path) {
        File gitRoot = new File(path);
        if (!gitRoot.isDirectory()) {
            return false;
        }

        File gitDirectoryInRoot = new File(gitRoot, ".git");
        return gitDirectoryInRoot.isDirectory();
    }

    /**
     * Возвращает ссылку на репозиторий по указанному пути.
     * При этом проверяет указанный путь на наличие в ней репозитория git.
     *
     * @param directory директория
     * @return ссылку на репозиторий
     * @throws IOException ошибка при неправильном пути до репозитория
     * @see #isValidGitRoot(String)
     */
    public static GitRepository loadRepository(File directory) throws IOException {
        if (!isValidGitRoot(directory.getAbsolutePath())) {
            throw new IOException("Invalid Git root: " + directory.getAbsolutePath());
        }

        Repository repository = new FileRepositoryBuilder().findGitDir(directory).build();
        return new GitRepository(repository);
    }

    /**
     * Возвращает последний коммит в указанном репозитории по указанной ветке.
     *
     * @param gitRepository ссылка на репозиторий
     * @param branch        имя ветки
     * @return последний коммит
     * @throws IOException ошибка при неправильном пути до репозитория git
     */
    public static Commit loadLastCommit(GitRepository gitRepository, Branch branch) throws IOException {
        Repository repository = gitRepository.getRepository();

        ObjectId lastCommitId = repository.resolve(branch.getName());
        RevWalk revWalk = new RevWalk(repository);
        Commit commit = Commits.fromRevCommit(repository, revWalk.parseCommit(lastCommitId), null);
        revWalk.release();

        return commit;
    }
}
