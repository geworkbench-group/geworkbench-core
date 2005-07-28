package org.systemsbiology.chem;

/*
 * Copyright (C) 2003 by Institute for Systems Biology,
 * Seattle, Washington, USA.  All rights reserved.
 * 
 * This source code is distributed under the GNU Lesser 
 * General Public License, the text of which is available at:
 *   http://www.gnu.org/copyleft/lesser.html
 */

import org.systemsbiology.util.IncludeHandler;
import org.systemsbiology.util.InvalidInputException;

import java.io.BufferedReader;
import java.io.IOException;

public interface IModelBuilder {
    public Model buildModel(BufferedReader pInputReader, IncludeHandler pIncludeHandler) throws InvalidInputException, IOException;

    public String getFileRegex();
}
