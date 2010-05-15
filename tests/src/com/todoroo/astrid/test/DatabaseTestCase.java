package com.todoroo.astrid.test;

import com.todoroo.andlib.service.Autowired;
import com.todoroo.andlib.service.TestDependencyInjector;
import com.todoroo.andlib.test.TodorooTestCase;
import com.todoroo.astrid.dao.Database;
import com.todoroo.astrid.service.AstridDependencyInjector;
import com.todoroo.astrid.tagsold.TagsDatabase;

/**
 * Test case that automatically sets up and tears down a test database
 *
 * @author Tim Su <tim@todoroo.com>
 *
 */
public class DatabaseTestCase extends TodorooTestCase {

    private static final String SYNC_TEST = "synctest";
    private static final String ALERTS_TEST = "alertstest";
    private static final String TAG_TASK_TEST = "tagtasktest";
    private static final String TAGS_TEST = "tagstest";
    private static final String TASKS_TEST = "taskstest";

    @Autowired
	public Database database;

    static {
        AstridDependencyInjector.initialize();

        TestDependencyInjector injector = TestDependencyInjector.initialize("db");
        injector.addInjectable("tasksTable", TASKS_TEST);
        injector.addInjectable("tagsTable", TAGS_TEST);
        injector.addInjectable("tagTaskTable", TAG_TASK_TEST);
        injector.addInjectable("alertsTable", ALERTS_TEST);
        injector.addInjectable("syncTable", SYNC_TEST);
        injector.addInjectable("database", new TestDatabase());
    }

	@Override
	protected void setUp() throws Exception {
	    super.setUp();

		// create new test database
        database.clear();
		database.openForWriting();

		// and plugin databases too
		TagsDatabase tagsDatabase = new TagsDatabase();
		tagsDatabase.clear();

		// clear legacy databases
		getContext().deleteDatabase(TASKS_TEST);
		getContext().deleteDatabase(TAGS_TEST);
		getContext().deleteDatabase(TAG_TASK_TEST);
		getContext().deleteDatabase(ALERTS_TEST);
		getContext().deleteDatabase(SYNC_TEST);
	}

	@Override
	protected void tearDown() throws Exception {
		database.close();
	}

	public static class TestDatabase extends TagsDatabase {
	    private static final String NAME = "databasetest";

        @Override
	    protected String getName() {
	        return NAME;
	    }
	}
}