package com.example.kanbanTaskManager.events;

import com.example.kanbanTaskManager.enitiy.Task;
import com.example.kanbanTaskManager.enitiy.User;
import com.example.kanbanTaskManager.enitiy.Workspace;
import lombok.Getter;

@Getter
public class ActivityEvent {
    private final String action;
    private final String details;
    private final User actor;
    private final Workspace workspace;
    private final Task task;

    public ActivityEvent(String action, String details, User actor, Workspace workspace, Task task) {
        this.action = action;
        this.details = details;
        this.actor = actor;
        this.workspace = workspace;
        this.task = task;
    }
}
