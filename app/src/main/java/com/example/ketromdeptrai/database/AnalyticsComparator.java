package com.example.ketromdeptrai.database;

/**
 * Created by ketromdeptrai on 4/30/2017.
 */


import java.util.Comparator;


/**
 * A collection of {@link Comparator}s for {@link Analytics} objects.
 *
 * @author ISchwarz
 */
public final class AnalyticsComparator {

    private AnalyticsComparator() {
        //no instance
    }

    public static Comparator<Analytics> getAnalyticsIDComparator() {
        return new AnalyticsIDComparator();
    }

    public static Comparator<Analytics> getAnalyticsStudentNameComparator() {
        return new AnalyticsStudentNameComparator();
    }

    public static Comparator<Analytics> getAnalyticsStudentIDComparator() {
        return new AnalyticsStudentIDComparator();
    }

    public static Comparator<Analytics> getAnalyticsCol4Comparator() {
        return new AnalyticsCol4Comparator();
    }


    private static class AnalyticsIDComparator implements Comparator<Analytics> {

        @Override
        public int compare(final Analytics analytics1, final Analytics analytics2) {
            return analytics1.getId() - analytics2.getId();
        }
    }

    private static class AnalyticsStudentNameComparator implements Comparator<Analytics> {

        @Override
        public int compare(final Analytics analytics1, final Analytics analytics2) {
            return analytics1.getStudentName().compareTo(analytics2.getStudentName()) ;
        }
    }

    private static class AnalyticsStudentIDComparator implements Comparator<Analytics> {

        @Override
        public int compare(final Analytics analytics1, final Analytics analytics2) {
            return analytics1.getStudentID().compareTo(analytics2.getStudentID());
        }
    }

    private static class AnalyticsCol4Comparator implements Comparator<Analytics> {

        @Override
        public int compare(final Analytics analytics1, final Analytics analytics2) {
            return analytics1.getCol4().compareTo(analytics2.getCol4());
        }
    }

}