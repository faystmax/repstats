package com.selesse.gitwrapper.fixtures;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * \brief Вспомогательный класс для тестирования репозитория.
 * \version 0.5
 * \date 19 февраля 2017 года
 * <p>
 * Предоставляет методы по созданию и удалению директория репозитория
 */
public class GitRepositoryBuilder {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitRepositoryBuilder.class);   ///< ссылка на логгер

    private File directory;     ///< будущая директория репозитория

    /**
     * @param directory будущая директория репозитория
     */
    GitRepositoryBuilder(File directory) {
        this.directory = directory;
    }

    /**
     * @return GitRepositoryBuilderRunner
     */
    public static GitRepositoryBuilderRunner create() {
        return new GitRepositoryBuilderRunner();
    }

    /**
     * @return будущая директория репозитория
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * Удаляет директорию репозитория
     *
     * @throws IOException
     */
    public void cleanUp() throws IOException {
        if (directory.isDirectory()) {
            FileUtils.deleteDirectory(directory);
        }
    }
}
