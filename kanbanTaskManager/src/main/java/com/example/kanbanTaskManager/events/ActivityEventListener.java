package com.example.kanbanTaskManager.events;

import com.example.kanbanTaskManager.enitiy.ActivityLog;
import com.example.kanbanTaskManager.repository.ActivityLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ActivityEventListener {

    private final ActivityLogRepository activityLogRepository;

    @EventListener
    @Transactional
    public void handleActivityEvent(ActivityEvent event) {
        ActivityLog log = ActivityLog.builder()
                .action(event.getAction())
                .details(event.getDetails())
                .actor(event.getActor())
                .workspace(event.getWorkspace())
                .task(event.getTask())
                .build();

        activityLogRepository.save(log);
    }
}
