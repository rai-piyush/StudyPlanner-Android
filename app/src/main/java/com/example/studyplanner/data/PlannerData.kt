package com.example.studyplanner.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

data class Topic(
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var subject: String,
    var studyDate: String,
    var type: String,
    var allocatedMinutes: Int,
    var notes: String = ""
)

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val topicId: String,
    var topicName: String,
    var subject: String,
    var stage: String,
    var stageLabel: String,
    var dueDate: String,
    var priority: String,
    var notes: String = "",
    var allottedMinutes: Int,
    var completed: Boolean = false,
    var completedAt: String? = null,
    var timeSpent: Int = 0
)

data class TimerSession(
    val id: String = UUID.randomUUID().toString(),
    val taskId: String,
    val durationSeconds: Int,
    val startTime: String,
    val endTime: String
)

data class PlannerData(
    var topics: MutableList<Topic> = mutableListOf(),
    var tasks: MutableList<Task> = mutableListOf(),
    var timerSessions: MutableList<TimerSession> = mutableListOf()
)

object PlannerRepository {
    var data = PlannerData()

    fun addTask(task: Task) {
        data.tasks.add(task)
    }

    fun addTopic(topic: Topic) {
        data.topics.add(topic)
    }
    
    fun getTasksForDate(date: String): List<Task> {
        return data.tasks.filter { it.dueDate == date }
    }
    
    fun toggleTaskCompletion(taskId: String) {
        val task = data.tasks.find { it.id == taskId }
        task?.let {
            it.completed = !it.completed
            it.completedAt = if (it.completed) System.currentTimeMillis().toString() else null
        }
    }
}
