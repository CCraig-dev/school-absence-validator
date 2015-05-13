package group5.caniskipclass.persistence;

import android.provider.BaseColumns;

/**
 * Created by jcdesimp on 3/1/15.
 */
public final class CanISkipClassContract {
    public CanISkipClassContract() {}

    public static abstract class CourseEntry implements BaseColumns {
        public static final String TABLE_NAME = "course";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_MIN_GRADE = "min_grade";
        public static final String COLUMN_NAME_NUM_ALLOWED_ABSENCE = "num_allowed_absence";
        public static final String COLUMN_NAME_LOSS_FOR_SKIP = "loss_for_skip";
        public static final String COLUMN_NAME_NUM_SKIPS = "num_skips";
        public static final String COLUMN_NAME_PROFESSOR_NAME = "professor_name";
    }

    public static abstract class AssignmentEntry implements BaseColumns {
        public static final String TABLE_NAME = "assignment";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_GRADE = "grade";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_CATEGORY_ID = "category_id";

    }

    public static abstract class CategoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "category";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_WEIGHT = "weight";
        public static final String COLUMN_NAME_COURSE_ID = "course_id";
    }







}
