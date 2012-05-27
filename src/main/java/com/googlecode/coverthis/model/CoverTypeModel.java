/*
 * CoverTypeModel.java
 * Created on May 27, 2012, 3:03:12 PM
 */
package com.googlecode.coverthis.model;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jochem Van denbussche <jvandenbussche@gmail.com>
 */
public class CoverTypeModel {

    private List<CoverType> coverTypes;

    public CoverTypeModel() {
        this.coverTypes = new ArrayList<CoverType>();
        coverTypes.add(CoverType.DVD);
        coverTypes.add(CoverType.DVD_SLIM);
        coverTypes.add(new DvdAsDvdSlim());
    }

    public String[] getCoverNames() {
        String[] names = new String[coverTypes.size()];
        for (int i = 0; i < coverTypes.size(); i++) {
            names[i] = coverTypes.get(i).getName();
        }
        return names;
    }

    public CoverType getCoverType(int index) {
        return coverTypes.get(index);
    }
}
