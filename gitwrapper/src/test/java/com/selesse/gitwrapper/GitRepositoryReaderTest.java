package com.selesse.gitwrapper;

import com.google.common.base.Splitter;
import com.google.common.io.Files;
import com.selesse.gitwrapper.fixtures.GitRepositoryBuilder;
import com.selesse.gitwrapper.fixtures.SimpleGitFixture;
import com.selesse.gitwrapper.myobjects.*;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.FileMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * \brief Тесты на GitRepositoryReader.
 * \version 0.5
 * \date 19 февраля 2017 года
 * <p>
 * Модульные тесты на GitRepositoryReader и связанные с ним классы.
 */
public class GitRepositoryReaderTest {

    private File temporaryDirectory;            ///< временная директория - репозиторий
    private File gitRoot;                       ///< ссылка на .git в репозитории
    private File fileInGitRoot;                 ///< файл в репозитории

    /**
     * Предварительная настройка
     * Создаём временную директорию в ней папку .git и 2 файла
     *
     * @throws IOException
     */
    @Before
    public void setup() throws IOException {
        temporaryDirectory = Files.createTempDir();
        gitRoot = new File(temporaryDirectory, ".git");

        boolean gitRootNewFile = gitRoot.mkdir();
        assertThat(gitRootNewFile).isTrue();

        fileInGitRoot = new File(gitRoot, "INDEX");
        boolean fileInGitRootNewFile = fileInGitRoot.createNewFile();
        assertThat(fileInGitRootNewFile).isTrue();

        File anotherFileInGitRoot = new File(gitRoot, ".git");
        boolean anotherFileInGitRootNewFile = anotherFileInGitRoot.createNewFile();
        assertThat(anotherFileInGitRootNewFile).isTrue();
    }

    /**
     * После отработки всех тестов удаляем временную папку
     *
     * @throws IOException
     */
    @After
    public void tearDown() throws IOException {
        FileUtils.deleteDirectory(temporaryDirectory);
    }

    /**
     * Проверка метода IsValidGitRoot
     *
     * @throws IOException
     */
    @Test
    public void testIsValidGitRoot() throws IOException {
        assertThat(GitRepositoryReader.isValidGitRoot("")).isFalse();
        assertThat(GitRepositoryReader.isValidGitRoot(temporaryDirectory.getAbsolutePath())).isTrue();
        assertThat(GitRepositoryReader.isValidGitRoot(fileInGitRoot.getAbsolutePath())).isFalse();
        assertThat(GitRepositoryReader.isValidGitRoot(gitRoot.getAbsolutePath())).isFalse();
    }

    /**
     * Проверка метода LoadRepositoryLastCommit
     * Вместе с проверкой методов класса Commit
     *
     * @throws IOException
     * @throws GitAPIException
     */
    @Test
    public void testLoadRepositoryLastCommit() throws IOException, GitAPIException {
        GitRepository repository = SimpleGitFixture.getRepository();
        Branch branch = SimpleGitFixture.getBranch();

        Commit lastCommit = GitRepositoryReader.loadLastCommit(repository, branch);
        List<GitFile> gitFiles = lastCommit.getFilesChanged();
        assertThat(gitFiles).hasSize(4);
        assertThat(lastCommit.getAuthor()).isNotNull();
        assertThat(lastCommit.getCommitter()).isEqualTo(lastCommit.getAuthor());
        assertThat(lastCommit.getCommitMessage()).isEqualTo("chmod +755 hello.o\n");
        ZonedDateTime commitDateTime = lastCommit.getCommitDateTime();
        ZonedDateTime commitDateTimeUTC = ZonedDateTime.ofInstant(commitDateTime.toInstant(), ZoneId.of("Z"));
        assertThat(commitDateTimeUTC).isEqualTo(ZonedDateTime.of(2014, 9, 28, 19, 26, 19, 0, ZoneOffset.UTC));

        verifyTextFile(gitFiles.get(0), "README.md", "README\n");
        verifyTextFile(gitFiles.get(1), "some-file-renamed", "");
        verifyTextFile(gitFiles.get(2), "some-other-file", "this is some file\n" + "lol\n");
        verifyExecutableBinaryFile(gitFiles.get(3), "some/binary/file/hello.o");
    }

    /**
     * Проверка метода loadRepository
     *
     * @throws IOException
     * @throws InterruptedException
     */
    @Test
    public void testLoadRepository() throws IOException, InterruptedException {
        GitRepositoryBuilder repositoryBuilder = GitRepositoryBuilder.create().runCommand("git init").build();
        GitRepository repository = GitRepositoryReader.loadRepository(repositoryBuilder.getDirectory());

        assertThat(repository).isNotNull();
    }

    /**
     * Проверка метода LoadRepository на Exception
     * Подсовываем ему не корректную  директорию
     *
     * @throws IOException
     */
    @Test
    public void testLoadRepository_throwsException() throws IOException {
        boolean exceptionWasThrown = false;

        try {
            GitRepositoryReader.loadRepository(gitRoot);
        } catch (IOException e) {
            assertThat(e).hasMessage("Invalid Git root: " + gitRoot.getAbsolutePath());
            exceptionWasThrown = true;
        }

        assertThat(exceptionWasThrown).isTrue();
    }

    /**
     * Проверка на ExecutableBinaryFile
     *
     * @param gitFile файл
     * @param path    полный поть до него
     */
    private void verifyExecutableBinaryFile(GitFile gitFile, String path) {
        assertThat(gitFile.getPath()).isEqualTo(path);
        assertThat(gitFile.getFileMode()).isEqualTo(FileMode.EXECUTABLE_FILE);
        assertThat(gitFile.isBinary()).isTrue();
    }

    /**
     * Проверка на TextFile
     *
     * @param gitFile  файл
     * @param path     полный путь до него
     * @param contents содержание
     */
    private void verifyTextFile(GitFile gitFile, String path, String contents) {
        assertThat(gitFile.getPath()).isEqualTo(path);
        assertThat(gitFile.getFileMode()).isEqualTo(FileMode.REGULAR_FILE);
        assertThat(gitFile.isBinary()).isFalse();

        List<String> contentList = Splitter.on("\n").splitToList(contents);

        assertThat(gitFile.getNumberOfLines()).isEqualTo(contentList.size());
    }
}