package com.zihan.appframe.biz.dbflow;

import com.orhanobut.logger.Logger;
import com.raizlabs.android.dbflow.annotation.Migration;
import com.raizlabs.android.dbflow.sql.SQLiteType;
import com.raizlabs.android.dbflow.sql.migration.AlterTableMigration;

/**
 * Created by jilibing on 2016/6/20/0020.
 */
@Migration(version = 4, database = AppDatabase.class)
public class UpdateCard extends AlterTableMigration<Card> {

    public UpdateCard(Class<Card> table) {
        super(table);
    }

    @Override
    public void onPreMigrate() {
        super.onPostMigrate();
        addColumn(SQLiteType.TEXT, "testCol");

        Logger.e("onPreMigrate");
    }


}
