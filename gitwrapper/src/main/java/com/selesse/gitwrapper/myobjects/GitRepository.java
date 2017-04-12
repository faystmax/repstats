package com.selesse.gitwrapper.myobjects;

import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.util.List;

/**
 * \brief Git репозиторий.
 * \author faystmax
 * \version 0.5
 * \date 18 февраля 2017 года
 * <p>
 * Хранит необходимую информацию о самом репозитории.
 */
public class GitRepository {

    private Repository repository;      ///< ссылка на репозиторий

    /**
     * Инициализируем git репозиторий.
     *
     * @param repository ссылка на репозиторий.Класс из внешнего пакета.
     */
    public GitRepository(Repository repository) {
        this.repository = repository;
    }

    /**
     * Возвращет ветку с указанный именем.
     * Просто создаёт ветку со ссылкой на данный репозиторий
     * и указанным именем ветки.
     *
     * @param name имя ветки
     * @return ветку репозитория с указанным именем
     */
    public Branch getBranch(String name) {
        return new Branch(repository, name);
    }

    /**
     * Возвращает ссылку на репозиторий.
     *
     * @return ссылку на репозиторий
     */
    public Repository getRepository() {
        return repository;
    }

    /**
     * Возвращает список изменённых файлов(CommitDiff) в коммите.
     *
     * @param commit коммит в котором вести поиск
     * @return список изминённых файлов
     * @throws IOException ошибка при чтении репозитория
     */
    public List<CommitDiff> getCommitDiffs(Commit commit) throws IOException {
        return CommitDiffs.getCommitDiffs(repository, commit);
    }

    /**
     * Закрываем репозиторий
     */
    public void close() {
        repository.close();
    }
}
