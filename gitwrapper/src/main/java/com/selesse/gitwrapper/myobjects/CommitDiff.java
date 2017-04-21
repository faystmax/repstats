package com.selesse.gitwrapper.myobjects;

import com.google.common.base.Splitter;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * \brief Cущность изменённого файла при коммите.
 * \author faystmax
 * \version 0.5
 * \date 18 февраля 2017 года
 * <p>
 * Представляет собой сущность изменённого файла при коммите.
 * Хранит информацию о том что сделали с файлом при коммитею.
 */
public class CommitDiff {
    private static final Logger LOGGER = LoggerFactory.getLogger(CommitDiff.class);     ///< Ссылка на логгер

    private Repository repository;              ///< Ссылка на репозиторий
    private String oldPath;                     ///< Ссылка на старый путь файла
    private String newPath;                     ///< Ссылка на новый путь файла
    private int linesAdded;                     ///< Строк добавлено
    private int linesRemoved;                   ///< Строк удалено
    private DiffEntry.ChangeType changeType;    ///< Тип изминения

    /**
     * Инициализируетт CommitDiff из DiffEntry(org.eclipse.jgit.diff.DiffEntry)
     *
     * @param repository ссылка на репозиторий
     * @param diffEntry  сущность изминённого файла
     * @throws IOException ошибка при чтении репозитория
     * @see #calculateLines(DiffEntry)
     */
    public CommitDiff(Repository repository, DiffEntry diffEntry) throws IOException {
        this.repository = repository;
        this.oldPath = diffEntry.getOldPath();
        this.newPath = diffEntry.getNewPath();
        this.linesAdded = 0;
        this.linesRemoved = 0;
        this.changeType = diffEntry.getChangeType();

        calculateLines(diffEntry);
    }

    /**
     * Просчитывает количество добавленный и удалённых строк и пишет в логгер.
     * Изменяет значение переменных  linesAdded и linesRemoved.
     *
     * @param diffEntry сущность изминённого файла
     * @throws IOException ошибка при чтении репозитория
     */
    private void calculateLines(DiffEntry diffEntry) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        DiffFormatter diffFormatter = new DiffFormatter(out);
        diffFormatter.setRepository(repository);
        diffFormatter.setContext(0);

        LOGGER.info("Diff stats for {} -> {}", diffEntry.getOldPath(), diffEntry.getNewPath());
        diffFormatter.format(diffEntry);

        String diffText = out.toString("UTF-8");
        List<String> changes = Splitter.onPattern("\r?\n").splitToList(diffText);

        int diffStartPosition = 0;
        for (String change : changes) {
            if (change.startsWith("@@")) {
                break;
            }
            diffStartPosition++;
        }

        for (String change : changes.subList(diffStartPosition, changes.size())) {
            if (change.startsWith("+")) {
                linesAdded++;
            } else if (change.startsWith("-")) {
                linesRemoved++;
            }
        }

        LOGGER.info("{} -> {}: {} additions, {} removals", oldPath, newPath, linesAdded, linesRemoved);

        out.reset();
        diffFormatter.release();
    }

    /**
     * Возвращает старый путь до файла.
     *
     * @return OldPath
     */
    public String getOldPath() {
        return oldPath;
    }

    /**
     * Возвращает новый путь до файла.
     *
     * @return - новый путь до файла
     */
    public String getNewPath() {
        return newPath;
    }

    /**
     * Возвращает кол-во добавленных строк
     *
     * @return кол-во добавленных строк
     */
    public int getLinesAdded() {
        return linesAdded;
    }

    /**
     * Возвращает кол-во добавленных строк
     *
     * @return кол-во добавленных строк
     */
    public int getLinesRemoved() {
        return linesRemoved;
    }

    /**
     * Возвращает тип изминения файла
     *
     * @return тип изминения файла
     */
    public DiffEntry.ChangeType getChangeType() {
        return changeType;
    }
}
