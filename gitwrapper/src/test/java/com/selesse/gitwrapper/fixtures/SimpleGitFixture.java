package com.selesse.gitwrapper.fixtures;

import com.google.common.io.Resources;
import com.selesse.gitwrapper.myobjects.Branch;
import com.selesse.gitwrapper.myobjects.GitRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;

/**
 * \brief Вспомогательный класс для тестирования репозитори.
 * \version 0.5
 * \date 19 февраля 2017 года
 * <p>
 * Предоставляет методы по получению репозитория и ветки для тестирования
 */
public class SimpleGitFixture {
    /**
     * Путь до папки в с репозиторием
     */
    private static final String simpleGitPath = Resources.getResource("simple-git/dot-git").getPath();

    /**
     * @return репозиторий из simpleGitPath
     * @throws IOException
     */
    public static GitRepository getRepository() throws IOException {
        Repository repository = new FileRepositoryBuilder().setGitDir(new File(simpleGitPath))
                .readEnvironment()
                .findGitDir()
                .build();

        return new GitRepository(repository);
    }

    /**
     * @return ветку master из репозитория simpleGitPath
     * @throws IOException
     */
    public static Branch getBranch() throws IOException {
        return getRepository().getBranch("master");
    }
}
