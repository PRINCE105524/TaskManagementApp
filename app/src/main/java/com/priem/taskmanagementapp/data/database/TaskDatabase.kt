package com.priem.taskmanagementapp.data.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.priem.taskmanagementapp.data.dao.TaskDao
import com.priem.taskmanagementapp.data.entity.Label
import com.priem.taskmanagementapp.data.entity.Task
import com.priem.taskmanagementapp.data.entity.TaskLabelCrossRef

@Database(
    entities = [
        Task::class,
        Label::class,
        TaskLabelCrossRef::class
    ],
    version = 1
)
abstract class TaskDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var INSTANCE: TaskDatabase? = null

        fun getDatabase(context: Context): TaskDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TaskDatabase::class.java,
                    "task_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
