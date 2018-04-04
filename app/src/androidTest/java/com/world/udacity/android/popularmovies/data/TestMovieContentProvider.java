package com.world.udacity.android.popularmovies.data;

import android.content.ComponentName;
import android.content.Context;
import android.content.UriMatcher;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.fail;

@RunWith(AndroidJUnit4.class)
public class TestMovieContentProvider {

    private final Context mContext = InstrumentationRegistry.getTargetContext();

    @Before
    public void setUp() {
        /* Use TaskDbHelper to get access to a writable database */
        MovieDBHelper dbHelper = new MovieDBHelper(mContext);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        database.delete(MovieContract.MovieEntry.TABLE_NAME, null, null);
    }

    @Test
    public void testProviderRegistry() {

        /*
         * A ComponentName is an identifier for a specific application component, such as an
         * Activity, ContentProvider, BroadcastReceiver, or a Service.
         *
         * Two pieces of information are required to identify a component: the package (a String)
         * it exists in, and the class (a String) name inside of that package.
         *
         * We will use the ComponentName for our ContentProvider class to ask the system
         * information about the ContentProvider, specifically, the authority under which it is
         * registered.
         */
        String packageName = mContext.getPackageName();
        String movieProviderClassName = MovieContentProvider.class.getName();
        ComponentName componentName = new ComponentName(packageName, movieProviderClassName);

        try {

            /*
             * Get a reference to the package manager. The package manager allows us to access
             * information about packages installed on a particular device. In this case, we're
             * going to use it to get some information about our ContentProvider under test.
             */
            PackageManager pm = mContext.getPackageManager();

            /* The ProviderInfo will contain the authority, which is what we want to test */
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            String actualAuthority = providerInfo.authority;
            String expectedAuthority = packageName;

            /* Make sure that the registered authority matches the authority from the Contract */
            String incorrectAuthority =
                    "Error: TaskContentProvider registered with authority: " + actualAuthority +
                            " instead of expected authority: " + expectedAuthority;
            assertEquals(incorrectAuthority,
                    actualAuthority,
                    expectedAuthority);

        } catch (PackageManager.NameNotFoundException e) {
            String providerNotRegisteredAtAll =
                    "Error: TaskContentProvider not registered at " + mContext.getPackageName();
            /*
             * This exception is thrown if the ContentProvider hasn't been registered with the
             * manifest at all. If this is the case, you need to double check your
             * AndroidManifest file
             */
            fail(providerNotRegisteredAtAll);
        }
    }

    //================================================================================
    // Test UriMatcher
    //================================================================================


    private static final Uri TEST_MOVIES = MovieContract.MovieEntry.CONTENT_URI;
    // Content URI for a single task with id = 1
    private static final Uri TEST_MOVIE_WITH_ID = TEST_MOVIES.buildUpon().appendPath("1").build();


    /**
     * This function tests that the UriMatcher returns the correct integer value for
     * each of the Uri types that the ContentProvider can handle. Uncomment this when you are
     * ready to test your UriMatcher.
     */
    @Test
    public void testUriMatcher() {

        /* Create a URI matcher that the TaskContentProvider uses */
        UriMatcher testMatcher = MovieContentProvider.buildUriMatcher();

        /* Test that the code returned from our matcher matches the expected TASKS int */
        String moviesUriDoesNotMatch = "Error: The MOVIES URI was matched incorrectly.";
        int actualMoviesMatchCode = testMatcher.match(TEST_MOVIES);
        int expectedTasksMatchCode = MovieContentProvider.MOVIES;
        assertEquals(moviesUriDoesNotMatch,
                actualMoviesMatchCode,
                expectedTasksMatchCode);

        /* Test that the code returned from our matcher matches the expected TASK_WITH_ID */
        String movieWithIdDoesNotMatch =
                "Error: The MOVIE_WITH_ID URI was matched incorrectly.";
        int actualMovieWithIdCode = testMatcher.match(TEST_MOVIE_WITH_ID);
        int expectedTaskWithIdCode = MovieContentProvider.MOVIE_WITH_ID;
        assertEquals(movieWithIdDoesNotMatch,
                actualMovieWithIdCode,
                expectedTaskWithIdCode);
    }


}
