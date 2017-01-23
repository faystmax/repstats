package org.ams.repstats.uifactory;

import org.ams.repstats.userinterface.GitUInterface;
import org.ams.repstats.userinterface.SvnUInterface;
import org.ams.repstats.userinterface.UInterface;

/**
 * Created with IntelliJ IDEA
 * User: Максим
 * Date: 10.01.2017
 * Time: 11:14
 */
public class UInterfaceFactory {

    public UInterface create(TypeUInterface type) {
        if (type == TypeUInterface.git) {
            return new GitUInterface();
        } else if (type == TypeUInterface.svn) {
            return new SvnUInterface();
        } else
            return null;

    }

}
