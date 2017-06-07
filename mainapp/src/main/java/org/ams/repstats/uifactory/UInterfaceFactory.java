package org.ams.repstats.uifactory;

import org.ams.repstats.uinterface.GitUInterface;
import org.ams.repstats.uinterface.SvnUInterface;
import org.ams.repstats.uinterface.UInterface;

/**
 * Created with IntelliJ IDEA
 * User: Maxim Amosov <faystmax@gmail.com>
 * Date: 10.01.2017
 * Time: 11:14
 */
public class UInterfaceFactory {

    /**
     * Выдаёт фасад git или svn
     *
     * @param type
     * @return
     */
    public UInterface create(TypeUInterface type) {
        if (type == TypeUInterface.git) {
            return new GitUInterface();
        } else if (type == TypeUInterface.svn) {
            return new SvnUInterface();
        } else
            return null;
    }
}
