package com.example.studyplanner.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.studyplanner.data.PlannerRepository
import com.example.studyplanner.data.Task
import com.example.studyplanner.ui.theme.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen() {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val today = dateFormat.format(Date())
    
    // Some dummy data initialization if empty
    LaunchedEffect(Unit) {
        if (PlannerRepository.data.tasks.isEmpty()) {
            PlannerRepository.addTask(Task(
                topicId = "1",
                topicName = "Economics Chapter 1",
                subject = "ESI",
                stage = "Study",
                stageLabel = "Initial Study",
                dueDate = today,
                priority = "High",
                allottedMinutes = 120
            ))
            PlannerRepository.addTask(Task(
                topicId = "2",
                topicName = "Maths Ratio & Proportion",
                subject = "Quant",
                stage = "Revision",
                stageLabel = "Revision Session",
                dueDate = today,
                priority = "Medium",
                allottedMinutes = 45
            ))
        }
    }

    // Force recomposition when needed (simple approach for mock)
    var refreshTrigger by remember { mutableStateOf(0) }
    
    val tasksToday = PlannerRepository.getTasksForDate(today)

    Scaffold(
        containerColor = SurfaceBase,
        bottomBar = { BottomNavigationBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* Open Add Task */ },
                containerColor = PrimaryDefault,
                contentColor = OnSurfaceHigh,
                shape = RoundedCornerShape(16.dp)
            ) {
                Text("+", fontSize = 24.sp)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            HeaderSection()
            Spacer(modifier = Modifier.height(24.dp))
            StatsSection()
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "Today's Agenda",
                style = MaterialTheme.typography.titleLarge,
                color = OnSurfaceHigh,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(tasksToday) { task ->
                    TaskCard(task = task, onToggle = {
                        PlannerRepository.toggleTaskCompletion(task.id)
                        refreshTrigger++ // trigger recomposition
                    })
                }
            }
        }
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("Good Morning,", color = OnSurfaceMedium, style = MaterialTheme.typography.bodyMedium)
            Text("Focus Time", color = OnSurfaceHigh, style = MaterialTheme.typography.headlineMedium)
        }
        Surface(
            shape = CircleShape,
            color = SurfaceContainerHigh,
            modifier = Modifier.size(48.dp)
        ) {
            // Profile or settings icon
        }
    }
}

@Composable
fun StatsSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            modifier = Modifier.weight(1f),
            title = "STUDY",
            value = "2h 30m",
            accentColor = AccentStudy
        )
        StatCard(
            modifier = Modifier.weight(1f),
            title = "REVISION",
            value = "45m",
            accentColor = AccentRevision
        )
    }
}

@Composable
fun StatCard(modifier: Modifier = Modifier, title: String, value: String, accentColor: Color) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = SurfaceContainerLow,
        border = androidx.compose.foundation.BorderStroke(1.dp, SurfaceContainerHigh)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .drawAccentBorder(accentColor)
        ) {
            Text(title, color = OnSurfaceMedium, style = MaterialTheme.typography.labelSmall)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = OnSurfaceHigh, style = MaterialTheme.typography.titleLarge)
        }
    }
}

// Custom modifier to draw the left accent border in the stat card
fun Modifier.drawAccentBorder(color: Color) = this.run {
    Modifier.padding(start = 4.dp)
} // Simplified for this example

@Composable
fun TaskCard(task: Task, onToggle: () -> Unit) {
    Surface(
        shape = RoundedCornerShape(24.dp),
        color = SurfaceContainerLow,
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(24.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Surface(
                shape = CircleShape,
                color = if (task.completed) AccentStudy else Color.Transparent,
                border = if (!task.completed) androidx.compose.foundation.BorderStroke(1.5.dp, OnSurfaceMedium) else null,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onToggle() }
            ) {
                // inner check icon if completed
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    task.topicName,
                    color = if (task.completed) OnSurfaceMedium else OnSurfaceHigh,
                    style = MaterialTheme.typography.titleMedium,
                    textDecoration = if (task.completed) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TaskTag(task.subject, PrimaryDefault.copy(alpha = 0.2f), PrimaryLuminous)
                    TaskTag(task.stageLabel, AccentRevision.copy(alpha = 0.2f), AccentRevision)
                }
            }
            
            Text(
                "${task.allottedMinutes}m",
                color = OnSurfaceHigh,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun TaskTag(text: String, bgColor: Color, textColor: Color) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = bgColor,
    ) {
        Text(
            text,
            color = textColor,
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
        )
    }
}

@Composable
fun BottomNavigationBar() {
    Surface(
        color = SurfaceContainerLow,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp, horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Home", color = PrimaryDefault, fontWeight = FontWeight.Bold)
            Text("Schedule", color = OnSurfaceMedium)
            Text("Focus", color = OnSurfaceMedium)
            Text("Stats", color = OnSurfaceMedium)
        }
    }
}
