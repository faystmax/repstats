package com.selesse.gitwrapper.myobjects;

import org.eclipse.jgit.lib.PersonIdent;

import java.util.Objects;

/**
 * \brief Автор Git.
 * \author faystmax
 * \version 0.5
 * \date 18 февраля 2017 года
 * <p>
 * Хранит необходимую информацию об авторе репозитория.
 */
public class Author {

    private String name;            ///< Имя автора
    private String emailAddress;    ///< Email автора

    /**
     * Создаёт автора по идентификатору.
     *
     * @param authorIndent из внешнего пакета.
     */
    public Author(PersonIdent authorIndent) {
        this.name = authorIndent.getName();
        this.emailAddress = authorIndent.getEmailAddress();
    }

    /**
     * Возвращает имя автора.
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Возвращает email автора.
     *
     * @return emailAddress
     */
    public String getEmailAddress() {
        return emailAddress;
    }

    /**
     * Переопределение метода сравнения авторов
     * Сравнение объектов происходит сначала по ссылке, затем по классу и содержимому.
     *
     * @param object ссылка на объект для сравнения
     * @return истина если равны.Ложь иначе.
     */
    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Author author = (Author) object;

        return Objects.equals(getName(), author.getName()) &&
                Objects.equals(getEmailAddress(), author.getEmailAddress());
    }

    /**
     * Возвращает хеш автора.
     * Использует стандартый метод object.hash().
     *
     * @return hash
     */
    @Override
    public int hashCode() {
        return Objects.hash(name, emailAddress);
    }

    /**
     * Возвращает строковое представление автора.
     * Использует методы getName() и GetEmailAddress() для возвращения результата.
     *
     * @return "name" + "<email>"
     */
    @Override
    public String toString() {
        return getName() + " <" + getEmailAddress() + ">";
    }
}
