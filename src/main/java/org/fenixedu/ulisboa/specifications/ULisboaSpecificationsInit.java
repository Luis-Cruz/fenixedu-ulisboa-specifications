/**
 * This file was created by Quorum Born IT <http://www.qub-it.com/> and its 
 * copyright terms are bind to the legal agreement regulating the FenixEdu@ULisboa 
 * software development project between Quorum Born IT and Serviços Partilhados da
 * Universidade de Lisboa:
 *  - Copyright © 2015 Quorum Born IT (until any Go-Live phase)
 *  - Copyright © 2015 Universidade de Lisboa (after any Go-Live phase)
 *
 * Contributors: joao.roxo@qub-it.com
 *
 * 
 * This file is part of FenixEdu fenixedu-ulisboa-specifications.
 *
 * FenixEdu fenixedu-ulisboa-specifications is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * FenixEdu fenixedu-ulisboa-specifications is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with FenixEdu fenixedu-ulisboa-specifications.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fenixedu.ulisboa.specifications;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;

import org.fenixedu.ulisboa.specifications.domain.ULisboaSpecificationsRoot;

import pt.ist.fenixframework.Atomic;

@WebServlet(value = "/ULisboaSpecificationsInit", name = "ULisboaSpecificationsInit", loadOnStartup = 8)
public class ULisboaSpecificationsInit extends HttpServlet {
    @Atomic(mode = Atomic.TxMode.READ, flattenNested = false)
    @Override
    public void init() throws ServletException {
        super.init();
        ULisboaSpecificationsRoot.init();
    }
}
