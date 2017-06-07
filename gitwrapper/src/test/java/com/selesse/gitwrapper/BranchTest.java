package com.selesse.gitwrapper;

import com.selesse.gitwrapper.fixtures.GitRepositoryBuilder;
import com.selesse.gitwrapper.fixtures.SimpleGitFixture;
import com.selesse.gitwrapper.myobjects.Branch;
import com.selesse.gitwrapper.myobjects.Commit;
import com.selesse.gitwrapper.myobjects.GitRepository;
import com.selesse.gitwrapper.myobjects.GitRepositoryReader;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * \brief Тесты на Branch.
 * \version 0.5
 * \date 19 февраля 2017 года
 * <p>
 * Модульные тесты на Branch и связанные с ним классы.
 */
public class BranchTest {
    /**
     * Проверка GetCommits на существующем репозитории
     *
     * @throws Exception
     */
    @Test
    public void testGetCommits() throws Exception {
        Branch branch = SimpleGitFixture.getBranch();
        List<Commit> commits = branch.getCommits();
        assertThat(commits).hasSize(4);
    }

    /**
     * Проверка GetCommits на временном репозитории
     *
     * @throws Exception
     */
    @Test
    public void testGetCommits_dynamic() throws Exception {
        GitRepositoryBuilder repositoryBuilder = GitRepositoryBuilder.create().
                runCommand("git init").
                createFile("README.md", "This is a README").
                runCommand("git add README.md").
                runCommand("git commit -m Commit").
                createFile("new_file/first", "This is a new first file").
                runCommand("git add new_file/first").
                runCommand("git commit -m Commit2").
                createFile("second_file", "This is second file").
                runCommand("git add second_file").
                runCommand("git commit -m Commit3").
                build();

        File directory = repositoryBuilder.getDirectory();
        GitRepository repository = GitRepositoryReader.loadRepository(directory);
        Branch branch = repository.getBranch("master");
        List<Commit> commits = branch.getCommits();

        repositoryBuilder.cleanUp();

        assertThat(commits).hasSize(3);
    }

    /**
     * Проверка GetCommits на другой ветке
     *
     * @throws Exception
     */
    @Test
    public void testGetCommits_onOtherBranch() throws Exception {
        GitRepositoryBuilder repositoryBuilder = GitRepositoryBuilder.create().
                runCommand("git init").
                createFile("README.md", "This is a README").
                runCommand("git add README.md").
                runCommand("git", "commit", "-m", "Initial Commit").
                runCommand("git checkout -b newBranch").
                createFile("new_file/first", "This is a new first file").
                runCommand("git add new_file/first").
                runCommand("git commit -m Commit2").
                createFile("second_file", "This is second file").
                runCommand("git add second_file").
                runCommand("git commit -m Commit3").
                build();

        File directory = repositoryBuilder.getDirectory();
        GitRepository repository = GitRepositoryReader.loadRepository(directory);

        Branch masterBranch = repository.getBranch("master");
        List<Commit> masterCommits = masterBranch.getCommits();

        Branch newBranch = repository.getBranch("newBranch");
        List<Commit> newBranchCommits = newBranch.getCommits();

        repositoryBuilder.cleanUp();

        assertThat(masterCommits).hasSize(1);
        assertThat(newBranchCommits).hasSize(3);
    }
}
