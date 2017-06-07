package org.ams.repstats.view;

import org.ams.repstats.uinterface.UInterface;

import javax.swing.table.TableModel;
import java.util.Scanner;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 07.01.2017
 * Time: 12:58
 */
public class ConsoleViewInterface extends ViewInterfaceAbstract {

    private Scanner in = new Scanner(System.in);        ///< сканер для чтения с консоли

    public ConsoleViewInterface(UInterface uInterface) {
        super(uInterface);
    }


    /**
     * Выбор директории
     */
    @Override
    public void chooseProjectAction() {
        System.out.println("Введите путь к репозиторию:");
        String input = in.nextLine();
        if (!getuInterface().сhooseProjectDirectory(input)) {
            System.out.println("Ошибка! Выбрана невернаяя дирректория.");
            return;
        }
        if (!getuInterface().startProjectAnalyze()) {
            System.out.println("Ошибка анализа файлов проекта!");
            this.setStart(false);
        } else {
            this.setStart(true);
        }
    }

    /**
     * Выбод всех файлов репозитория
     */
    @Override
    public void showAllFiles() {
        if (checkStart()) {
            System.out.println("Путь                                                                   Бинарник   Кол-во строк ");
            TableModel allFiles = getuInterface().getAllFiles();
            for (int i = 0; i < allFiles.getRowCount(); i++) {
                System.out.printf("%-60s %15s  %15s  %n", allFiles.getValueAt(i, 0),
                        allFiles.getValueAt(i, 1),
                        allFiles.getValueAt(i, 2));
            }
            //задержка
            in.nextLine();
        }
    }

    /**
     * Выбод всех авторов репозитория
     */
    @Override
    public void showAvtors() {
        if (checkStart()) {
            System.out.println("Имя        Кол-во коммитов   Строк добавлено  Строк удалено    Чистый вклад ");
            TableModel authors = getuInterface().getAuthors();
            for (int i = 0; i < authors.getRowCount(); i++) {
                System.out.printf("%-15s %10d  %15d %15d %15d %n", authors.getValueAt(i, 0),
                        authors.getValueAt(i, 1),
                        authors.getValueAt(i, 2),
                        authors.getValueAt(i, 3),
                        authors.getValueAt(i, 4));

            }
            //задержка
            in.nextLine();
        }
    }

    /**
     * Выбод основной информации о репозитории
     */
    @Override
    public void showMainInf() {
        if (checkStart()) {
            System.out.print("Название репозитория:  ");
            System.out.printf("%15s%n", getuInterface().getRepName());
            System.out.print("Название ветки:        ");
            System.out.printf("%15s%n", getuInterface().getBranchName());
            System.out.print("Всего кол-во коммитов: ");
            System.out.printf("%15s%n", Integer.toString(getuInterface().getNumberOfCommits()));
            //задержка
            in.nextLine();
        }
    }

    /**
     * Проверка на начало анализа
     *
     * @return
     */
    public boolean checkStart() {
        if (this.isStart()) {
            return true;
        } else {
            System.out.println("Вы ещё не выбрали репозиторий");
            return false;
        }
    }

    /**
     * Начало анализа
     */
    @Override
    public void start() {
        boolean flag = true;
        while (flag) {
            writeMenu();
            System.out.println("Выберите пункт:");
            String input = in.nextLine();
            switch (input) {
                case "1":
                    chooseProjectAction();
                    break;
                case "2":
                    showMainInf();
                    break;
                case "3":
                    showAvtors();
                    break;
                case "4":
                    showAllFiles();
                    break;
                case "5":
                    flag = false;
                    System.out.println("Завершение работы");
                    break;
                default:
                    System.out.println("Не верный ввод!");
            }
        }
    }

    @Override
    public void closeRepository() {
        // TODO
    }

    /**
     * Вывод меню в консоль
     */
    public void writeMenu() {
        System.out.println("Меню:");
        System.out.println("1.Выбор репозитория.");
        System.out.println("2.Вывести основную информацию о репозитории.");
        System.out.println("3.Вывести пользователей репозитория.");
        System.out.println("4.Вывести все файлы репозитория.");
        System.out.println("5.Выход.");
    }
}
