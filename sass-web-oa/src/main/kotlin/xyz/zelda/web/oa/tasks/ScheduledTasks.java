package xyz.zelda.web.oa.tasks;

import org.springframework.scheduling.annotation.Scheduled;

import java.text.SimpleDateFormat;

public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(fixedRate = 5000)
    public void reportCurrentTime() {
//        print("The time is now {}", dateFormat.format(new Date()));
    }
}