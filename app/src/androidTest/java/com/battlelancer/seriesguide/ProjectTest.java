package com.battlelancer.seriesguide;

import static com.battlelancer.seriesguide.provider.SgRoomDatabase.MIGRATION_42_43;
import static com.battlelancer.seriesguide.provider.SgRoomDatabase.MIGRATION_43_44;
import static com.battlelancer.seriesguide.provider.SgRoomDatabase.MIGRATION_44_45;
import static com.battlelancer.seriesguide.provider.SgRoomDatabase.MIGRATION_45_46;
import static com.battlelancer.seriesguide.provider.SgRoomDatabase.MIGRATION_46_47;
import static com.google.common.truth.Truth.assertThat;

import android.database.sqlite.SQLiteDatabase;
import androidx.room.Room;
import androidx.room.testing.MigrationTestHelper;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import com.battlelancer.seriesguide.dataliberation.model.Show;
import com.battlelancer.seriesguide.model.SgEpisode;
import com.battlelancer.seriesguide.model.SgSeason;
import com.battlelancer.seriesguide.model.SgShow;
import com.battlelancer.seriesguide.provider.RoomDatabaseTestHelper;
import com.battlelancer.seriesguide.provider.SgRoomDatabase;
import com.battlelancer.seriesguide.provider.SqliteDatabaseTestHelper;
import com.battlelancer.seriesguide.provider.SqliteTestDbOpenHelper;
import com.battlelancer.seriesguide.ui.episodes.EpisodeFlags;
import com.battlelancer.seriesguide.ui.episodes.EpisodeTools;
import com.uwetrottmann.thetvdb.entities.Episode;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProjectTest {

    private static final String TEST_DB_NAME = "test-db";

    private static final Show SHOW = new Show();
    private static final SgSeason SEASON = new SgSeason();
    private static final Episode EPISODE = new Episode();

    static {
        SHOW.tvdb_id = 21;
        SHOW.title = "The No Answers Show";
        SHOW.runtime = 45;
        SHOW.poster = "example.jpg";

        SEASON.tvdbId = 21;
        SEASON.showTvdbId = "21";
        SEASON.number = 2;

        EPISODE.id = 21;
        EPISODE.episodeName = "Episode Title";
        EPISODE.airedEpisodeNumber = 1;
    }

    @Rule
    public MigrationTestHelper migrationTestHelper =
            new MigrationTestHelper(InstrumentationRegistry.getInstrumentation(),
                    SgRoomDatabase.class.getCanonicalName(),
                    new FrameworkSQLiteOpenHelperFactory());

    // Helper for creating SQLite database in version 42
    private SqliteTestDbOpenHelper sqliteTestDbHelper;

    @Test
    public void keepTrack() throws IOException {
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME,47);
        RoomDatabaseTestHelper.insertShow(SHOW, db,47);
        db.close();

        SgRoomDatabase database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                SgRoomDatabase.class, TEST_DB_NAME)
                .build();
        // close the database and release any stream resources when the test finishes
        migrationTestHelper.closeWhenFinished(database);

        SgShow dbShow = database.showHelper().getShow();
        assertThat(dbShow.tvdbId).isEqualTo(SHOW.tvdb_id);
        assertThat(dbShow.title).isEqualTo(SHOW.title);
        assertThat(dbShow.runtime).isEqualTo(String.valueOf(SHOW.runtime));
        assertThat(dbShow.poster).isEqualTo(SHOW.poster);
    }

    @Test
    public void keepTrackWatched() throws IOException {
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME,47);
        RoomDatabaseTestHelper.insertShow(SHOW, db,47);
        RoomDatabaseTestHelper.insertSeason(SEASON, db);
        RoomDatabaseTestHelper
                .insertEpisode(EPISODE, SHOW.tvdb_id, SEASON.tvdbId, SEASON.number, db);
        db.close();

        SgRoomDatabase database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                SgRoomDatabase.class, TEST_DB_NAME)
                .build();
        // close the database and release any stream resources when the test finishes
        migrationTestHelper.closeWhenFinished(database);

        SgShow dbShow = database.showHelper().getShow();
        assertThat(dbShow.tvdbId).isEqualTo(SHOW.tvdb_id);
        assertThat(dbShow.title).isEqualTo(SHOW.title);
        assertThat(dbShow.runtime).isEqualTo(String.valueOf(SHOW.runtime));
        assertThat(dbShow.poster).isEqualTo(SHOW.poster);

        SgSeason dbSeason = database.seasonHelper().getSeason();
        assertThat(dbSeason.tvdbId).isEqualTo(SEASON.tvdbId);
        assertThat(dbSeason.showTvdbId).isEqualTo(SEASON.showTvdbId);
        assertThat(dbSeason.number).isEqualTo(SEASON.number);

        SgEpisode dbEpisode = database.episodeHelper().getEpisode();
        assertThat(dbEpisode.tvdbId).isEqualTo(EPISODE.id);
        assertThat(dbEpisode.showTvdbId).isEqualTo(SHOW.tvdb_id);
        assertThat(dbEpisode.seasonTvdbId).isEqualTo(SEASON.tvdbId);
        assertThat(dbEpisode.title).isEqualTo(EPISODE.episodeName);
        assertThat(dbEpisode.number).isEqualTo(EPISODE.airedEpisodeNumber);
        assertThat(dbEpisode.season).isEqualTo(SEASON.number);
    }

    @Test
    public void ReviewShow() throws IOException {
        SgRoomDatabase database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                SgRoomDatabase.class, TEST_DB_NAME)
                .build();
        migrationTestHelper.closeWhenFinished(database);

        SgShow dbShow = database.showHelper().getShow();
//        dbShow.createReview("great movie",5);
//
//        dbShow = database.showHelper().getShow();
//        assertThat(dbShow.review.msg).isEqualTo("great movie");
//        assertThat(dbShow.review.star).isEqualTo(5);
    }

    @Test
    public void worksOffline() throws IOException {
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME,47);
        RoomDatabaseTestHelper.insertShow(SHOW, db,47);
        db.close();

        SgRoomDatabase database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                SgRoomDatabase.class, TEST_DB_NAME)
                .build();
        // close the database and release any stream resources when the test finishes
        migrationTestHelper.closeWhenFinished(database);

        SgShow dbShow = database.showHelper().getShow();
        assertThat(dbShow.tvdbId).isEqualTo(SHOW.tvdb_id);
        assertThat(dbShow.title).isEqualTo(SHOW.title);
        assertThat(dbShow.runtime).isEqualTo(String.valueOf(SHOW.runtime));
        assertThat(dbShow.poster).isEqualTo(SHOW.poster);
    }

    @Test
    public void loadTime() throws IOException {
        SupportSQLiteDatabase db = migrationTestHelper.createDatabase(TEST_DB_NAME,47);
        RoomDatabaseTestHelper.insertShow(SHOW, db,47);
        db.close();

        SgRoomDatabase database = Room.databaseBuilder(ApplicationProvider.getApplicationContext(),
                SgRoomDatabase.class, TEST_DB_NAME)
                .build();
        // close the database and release any stream resources when the test finishes
        migrationTestHelper.closeWhenFinished(database);

        long start = System.currentTimeMillis();
        SgShow dbShow = database.showHelper().getShow();
        long stop = System.currentTimeMillis();
        long time = stop - start;
        assertThat(dbShow.tvdbId).isEqualTo(SHOW.tvdb_id);
        assertThat(dbShow.title).isEqualTo(SHOW.title);
        assertThat(dbShow.runtime).isEqualTo(String.valueOf(SHOW.runtime));
        assertThat(dbShow.poster).isEqualTo(SHOW.poster);

        assertThat(time).isLessThan(7000);
    }


    @Test
    public void isCollected() {
        assertThat(EpisodeTools.isCollected(EpisodeFlags.WATCHED)).isTrue();
    }

    @Test
    public void isWatched() {
        assertThat(EpisodeTools.isWatched(EpisodeFlags.WATCHED)).isTrue();
        assertThat(EpisodeTools.isWatched(EpisodeFlags.SKIPPED)).isFalse();
        assertThat(EpisodeTools.isWatched(EpisodeFlags.UNWATCHED)).isFalse();
    }


}
