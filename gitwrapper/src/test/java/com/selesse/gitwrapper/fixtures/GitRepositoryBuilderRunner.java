package com.selesse.gitwrapper.fixtures;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

/**
 * \brief Вспомогательный класс для тестирования репозитория.
 * \version 0.5
 * \date 19 февраля 2017 года
 * <p>
 * Предоставляет методы по работы с git репозиторием
 */
public class GitRepositoryBuilderRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitRepositoryBuilderRunner.class); ///< ссылка на логгер

    private File temporaryDirectory;    ///< временная директория

    /**
     * Создаём временную директорию
     */
    GitRepositoryBuilderRunner() {
        temporaryDirectory = Files.createTempDir();
    }

    /**
     * Запускает команду в консоли
     *
     * @param command
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public GitRepositoryBuilderRunner runCommand(String command) throws IOException, InterruptedException {
        executeCommand(command);
        return this;
    }

    /**
     * Запускает список команд в консоли
     *
     * @param commandList
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    public GitRepositoryBuilderRunner runCommand(String... commandList) throws IOException, InterruptedException {
        executeCommandList(Lists.newArrayList(commandList));
        return this;
    }

    /**
     * Создаёт файл с указанным название и содержимым
     *
     * @param file
     * @param contents
     * @return
     * @throws FileNotFoundException
     */
    public GitRepositoryBuilderRunner createFile(String file, String contents) throws FileNotFoundException {
        addFile(file, contents);
        return this;
    }

    /**
     * @return GitRepositoryBuilder с нужной директорией
     */
    public GitRepositoryBuilder build() {
        return new GitRepositoryBuilder(temporaryDirectory);
    }

    /**
     * Запускает команды разделённые пробелами
     *
     * @param command
     * @throws IOException
     * @throws InterruptedException
     */
    private void executeCommand(String command) throws IOException, InterruptedException {
        List<String> commandSplitBySpace = Splitter.on(" ").splitToList(command);

        executeCommandList(commandSplitBySpace);
    }

    /**
     * Запуск списка команд в отдельном потоке
     *
     * @param commandList
     * @throws IOException
     * @throws InterruptedException
     */
    private void executeCommandList(List<String> commandList) throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(commandList);
        processBuilder.directory(temporaryDirectory);
        Process process = processBuilder.start();
        process.waitFor();

        InputStream inputStream = process.getInputStream();

        LOGGER.info("Printing output of '{}' to stdout", commandList);
        ByteStreams.copy(inputStream, System.out);
    }

    /**
     * Добавляем файл в директорию
     *
     * @param file
     * @param contents
     * @throws FileNotFoundException
     */
    private void addFile(String file, String contents) throws FileNotFoundException {
        LOGGER.info("Creating {}", file);

        File desiredFile = new File(temporaryDirectory, file);
        File desiredFileParent = desiredFile.getParentFile();

        if (!desiredFileParent.exists()) {
            boolean madeDirectories = desiredFileParent.mkdirs();
            if (!madeDirectories) {
                LOGGER.error("Error creating needed directories: {}", desiredFileParent.getAbsolutePath());
            }
        }

        PrintWriter printWriter = new PrintWriter(desiredFile);
        printWriter.println(contents);
        printWriter.flush();
        printWriter.close();
    }

}