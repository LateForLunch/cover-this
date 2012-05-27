/*
 * PrintUnitTools.java
 * Created on May 27, 2012, 3:04:50 PM
 */
package com.googlecode.coverthis.model;

/**
 *
 * @author Jochem Van denbussche <jvandenbussche@gmail.com>
 */
public class PrintUnitTools {

    public static final double UNITS_IN_INCH = 72.0;
    public static final double MILLIMETERS_IN_INCH = 25.4;

    public static double inchesToUnits(double inches) {
        return inches * UNITS_IN_INCH;
    }

    public static double unitsToInches(int units) {
        return units / UNITS_IN_INCH;
    }

    public static double millimetersToUnits(double millimeters) {
        return inchesToUnits(millimeters / MILLIMETERS_IN_INCH);
    }

    public static double unitsToMillimeters(int units) {
        return MILLIMETERS_IN_INCH * unitsToInches(units);
    }
}
